package jbc.timesheet.controller.iface;

import jbc.timesheet.controller.util.ActionType;
import jbc.timesheet.controller.util.JediModelAttributes;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpMethod;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.net.ssl.HttpsURLConnection;
import javax.validation.Valid;
import java.util.Optional;

public interface JediController<REPOSITORY extends CrudRepository, ENTITY, ID> {

    ENTITY newEntity();

    REPOSITORY getRepository();

    String getTemplatePrefix();

    ID getId(ENTITY entity);

    default Iterable<ENTITY> searchEntity(MultiValueMap<String, String> parameters) {
        return getRepository().findAll();
    };


    @GetMapping(value = {"/","{id}"})
    default String index(@PathVariable("id") ID id, Model model) {
        JediModelAttributes<ENTITY> jediModelAttributes =
            new JediModelAttributes<ENTITY>(HttpsURLConnection.HTTP_OK,newEntity(), ActionType.DEFAULT, HttpMethod.GET);
        return jediModelAttributes.view(model);
    }

    @GetMapping("/create")
    default String getCreate(Model model) {

        JediModelAttributes<ENTITY> jediModelAttributes =
//                the first argument is status code, which will be assigned to ${jediCode}
//                the second argument is the entity that pass through ${jediEntity}
//                the third argument is the action that will define the fragment within a template
//                In this case Jedi will call <th:block th:fragment="action-post-create"

                new JediModelAttributes<ENTITY>(HttpsURLConnection.HTTP_OK,newEntity(), ActionType.CREATE, HttpMethod.GET);


//         the callback action URL that form shuld set to
//
//        <form th:action="@{${jediActionNext}}" th:method="${jediMethodNext}"... > ... </form>
//
//        In this case
//          ${jediActionNext} will be assigned to 'process'
//          ${jediMethodNext} will be assigned to 'POST'
//          (see below for matching value)

        jediModelAttributes.setAction("/"+getTemplatePrefix()+"/create");
        jediModelAttributes.setMethod(HttpMethod.POST);

        return jediModelAttributes.view(model);
    }

    @GetMapping("/update/{id}")
    default String getUpdateId(@PathVariable("id") ID id, Model model){
        Optional<ENTITY> optionalEntity = getRepository().findById(id);

        @SuppressWarnings("unchecked")
        JediModelAttributes<ENTITY> jediModelAttributes =
                new JediModelAttributes<ENTITY>(HttpsURLConnection.HTTP_OK, (ENTITY) getRepository().findById(id).orElse(newEntity()), ActionType.UPDATE, HttpMethod.GET);

        if (!optionalEntity.isPresent()) {
            jediModelAttributes.setError("Object not found");
            return jediModelAttributes.redirect("/"+getTemplatePrefix()+"/retrieve/"+id);
        }
        jediModelAttributes.setAction("/"+getTemplatePrefix()+"/update");
        jediModelAttributes.setMethod(HttpMethod.POST);
        return jediModelAttributes.view(model);
    }

    @GetMapping("/view/{id}")
    default String getViewId(@PathVariable("id") ID id, Model model) {
        return "redirect:/"+getTemplatePrefix()+"/retrieve/"+id+"?info=URL+/view+is+depreciated.+Please+use+/retrieve";
    }

    default void preRetrieve(ENTITY entity) {

    }

    @GetMapping("/retrieve/{id}")
    default String getRetrieveId(@PathVariable("id") ID id, Model model){
        @SuppressWarnings("unchecked")
        Optional<ENTITY> optionalEntity = getRepository().findById(id);
        JediModelAttributes<ENTITY> jediModelAttributes =
                new JediModelAttributes<ENTITY>(HttpsURLConnection.HTTP_OK,(ENTITY) optionalEntity.orElse(newEntity()), ActionType.VIEW, HttpMethod.GET);

        if (!optionalEntity.isPresent()) {
            jediModelAttributes.setCode(HttpsURLConnection.HTTP_NOT_FOUND);
            jediModelAttributes.setError("Object not found");
            return jediModelAttributes.redirect("/"+getTemplatePrefix()+"/"+id);
        }

        preRetrieve(optionalEntity.get());

         return jediModelAttributes.view(model);
    }
    default void preDelete(ID id) {

    }

    @GetMapping("/delete/{id}")
    default String getDeleteId(Model model, @PathVariable("id") ID id){
        JediModelAttributes<ENTITY> jediModelAttributes =
                new JediModelAttributes<ENTITY>(HttpsURLConnection.HTTP_OK,newEntity(), ActionType.DELETE, HttpMethod.GET);

        if (getRepository().existsById(id)) {

            preDelete(id);

            getRepository().deleteById(id);

            jediModelAttributes.setSuccess("Object '"+id.toString()+"' deleted");
            jediModelAttributes.setCode(HttpsURLConnection.HTTP_OK);
        } else {
            jediModelAttributes.setError("Object id='"+id.toString()+"' not found");
            jediModelAttributes.setCode(HttpsURLConnection.HTTP_NOT_FOUND);
        }


        return jediModelAttributes.redirect("/"+getTemplatePrefix()+"/"+id);
    }

    @GetMapping("/search")
    default String getSearch(Model model, @RequestParam MultiValueMap<String, String> parameters){

        Iterable<ENTITY> jediEntityCollection;
        if (parameters.isEmpty()) {
            jediEntityCollection = getRepository().findAll();
        } else {
            jediEntityCollection = searchEntity(parameters);
        }



        JediModelAttributes<ENTITY> jediModelAttributes =
                new JediModelAttributes<ENTITY>(HttpsURLConnection.HTTP_OK,newEntity(), ActionType.LIST, HttpMethod.GET);
        jediModelAttributes.setEntityCollection(jediEntityCollection);
        return jediModelAttributes.view(model);
    }

    default void preProcess(ENTITY entity, BindingResult result) {

    }

    @PostMapping(value={"/process","/update", "/create"})
    default String postProcess(Model model, @Valid @ModelAttribute("jediEntity") ENTITY entity, BindingResult result){
        preProcess(entity, result);
        if(result.hasErrors()){
            System.out.println("Form Error:\n"+result.toString());
            JediModelAttributes<ENTITY> jediModelAttributes =
                    new JediModelAttributes<ENTITY>(HttpsURLConnection.HTTP_OK, entity, getId(entity).equals(0)?ActionType.CREATE:ActionType.UPDATE, HttpMethod.POST);
            jediModelAttributes.setAction("/"+getTemplatePrefix()+(getId(entity).equals(0)?"/create":"/update"));
            jediModelAttributes.setError("Jedi reject your form because your form could not pass the validations. \n\n"+result.toString());
            jediModelAttributes.setMethod(HttpMethod.POST);
            return jediModelAttributes.view(model);
        }

        getRepository().save(entity);

        JediModelAttributes<ENTITY> jediModelAttributes =
                new JediModelAttributes<ENTITY>(HttpsURLConnection.HTTP_OK,entity, getId(entity).equals(0)?ActionType.CREATE:ActionType.UPDATE, HttpMethod.POST);
        return jediModelAttributes.redirect("/"+getTemplatePrefix()+"/retrieve/"+getId(entity));
    }
}
