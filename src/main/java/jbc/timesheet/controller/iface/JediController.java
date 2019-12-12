package jbc.timesheet.controller.iface;

import jbc.timesheet.controller.util.ActionType;
import jbc.timesheet.controller.util.JediModelAttributes;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpMethod;
import org.springframework.ui.Model;
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

        jediModelAttributes.setAction("create");
        jediModelAttributes.setMethod(HttpMethod.POST);

        return jediModelAttributes.view(model);
    }

    @GetMapping("/update/{id}")
    default String getUpdateId(@PathVariable("id") ID id, Model model){
        @SuppressWarnings("unchecked")
        JediModelAttributes<ENTITY> jediModelAttributes =
                new JediModelAttributes<ENTITY>(HttpsURLConnection.HTTP_OK, (ENTITY) getRepository().findById(id), ActionType.UPDATE, HttpMethod.GET);
        jediModelAttributes.setAction("update");
        jediModelAttributes.setMethod(HttpMethod.POST);
        return jediModelAttributes.view(model);
    }

    @GetMapping(value = {"/view/{id}", "/retrieve/{id}"})
    default String getViewId(@PathVariable("id") ID id, Model model){
        @SuppressWarnings("unchecked")
        Optional<ENTITY> optionalEntity = getRepository().findById(id);
        JediModelAttributes<ENTITY> jediModelAttributes =
                new JediModelAttributes<ENTITY>(HttpsURLConnection.HTTP_OK,(ENTITY) optionalEntity.orElse(null), ActionType.VIEW, HttpMethod.GET);

        if (!optionalEntity.isPresent()) {
            jediModelAttributes.setError("Object not found");
            return jediModelAttributes.redirect("/view/error");
        }

         return jediModelAttributes.view(model);
    }

    @GetMapping("/delete/{id}")
    default String getDeleteId(Model model, @PathVariable("id") ID id){
        JediModelAttributes<ENTITY> jediModelAttributes =
                new JediModelAttributes<ENTITY>(HttpsURLConnection.HTTP_OK,newEntity(), ActionType.DELETE, HttpMethod.GET);

        if (getRepository().existsById(id)) {

            getRepository().deleteById(id);

            jediModelAttributes.setSuccess("Object deleted");
            jediModelAttributes.setCode(HttpsURLConnection.HTTP_OK);
        } else {
            jediModelAttributes.setError("Object id='"+id.toString()+"' not found");
            jediModelAttributes.setCode(HttpsURLConnection.HTTP_NOT_FOUND);
        }


        return jediModelAttributes.view(model);
    }

    @GetMapping(value={"/search"})
    default String getShow(Model model){
        @SuppressWarnings("unchecked")
        JediModelAttributes<ENTITY> jediModelAttributes =
                new JediModelAttributes<ENTITY>(HttpsURLConnection.HTTP_OK,(ENTITY) getRepository().findAll(), ActionType.LIST, HttpMethod.GET);
        jediModelAttributes.setMeta(newEntity());
        return jediModelAttributes.view(model);
    }


    @PostMapping(value={"/process","/update", "/create"})
    default String postProcess(Model model, @Valid @ModelAttribute ENTITY entity, BindingResult result){

        if(result.hasErrors()){
            JediModelAttributes<ENTITY> jediModelAttributes =
                    new JediModelAttributes<ENTITY>(HttpsURLConnection.HTTP_OK, entity, getId(entity).equals(0)?ActionType.CREATE:ActionType.UPDATE, HttpMethod.POST);
            jediModelAttributes.setAction(getId(entity).equals(0)?"create":"update");
            jediModelAttributes.setError("Jedi reject your form because your form could not pass the validations. \n\n"+result.toString());
            jediModelAttributes.setMethod(HttpMethod.POST);
            return jediModelAttributes.view(model);
        }

        getRepository().save(entity);

        JediModelAttributes<ENTITY> jediModelAttributes =
                new JediModelAttributes<ENTITY>(HttpsURLConnection.HTTP_OK,entity, getId(entity).equals(0)?ActionType.CREATE:ActionType.UPDATE, HttpMethod.POST);
        return jediModelAttributes.redirect("view/"+getId(entity));
    }
}
