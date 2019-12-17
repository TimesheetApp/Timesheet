package jbc.timesheet.controller.iface;

import jbc.timesheet.configuration.Jedi;
import jbc.timesheet.controller.util.ActionType;
import jbc.timesheet.controller.util.JediModelAttributes;
import jbc.timesheet.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.net.ssl.HttpsURLConnection;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

public interface JediController<REPOSITORY extends CrudRepository, ENTITY, ID> {

    ENTITY newEntity();

    REPOSITORY getRepository();

    String getTemplatePrefix();

    ID getId(ENTITY entity);


    Jedi jedi = new Jedi();

    default String getCurrentUsername() {

       return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    default Iterable<ENTITY> searchEntity(
            Model model,
            Principal user,
            SecurityContextHolderAwareRequestWrapper requestWrapper,
            MultiValueMap<String, String> parameters) {
        return getRepository().findAll();
    };

    @ModelAttribute
    default void init (Model model, Principal user, SecurityContextHolderAwareRequestWrapper requestWrapper) {

        model.addAttribute("jediIsAuthenticated", user != null);
        model.addAttribute("jediPrincipal", user);
    }

    @GetMapping(value = {"/","{id}"})
    default String index(@PathVariable("id") ID id, Model model) {
        JediModelAttributes<ENTITY> jediModelAttributes =
            new JediModelAttributes<ENTITY>(HttpsURLConnection.HTTP_OK,newEntity(), ActionType.DEFAULT, HttpMethod.GET);
        return jediModelAttributes.view(model);
    }

    @GetMapping("/create")
    default String doCreate(Model model) {

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
    default String doUpdateById(@PathVariable("id") ID id, Model model){
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

    default void postRetrieve(Model model, ENTITY entity) {

    }

    @GetMapping("/retrieve/{id}")
    default String doRetrieveById(@PathVariable("id") ID id, Model model){
        @SuppressWarnings("unchecked")
        Optional<ENTITY> optionalEntity = getRepository().findById(id);
        JediModelAttributes<ENTITY> jediModelAttributes =
                new JediModelAttributes<ENTITY>(HttpsURLConnection.HTTP_OK,(ENTITY) optionalEntity.orElse(newEntity()), ActionType.VIEW, HttpMethod.GET);

        if (!optionalEntity.isPresent()) {
            jediModelAttributes.setCode(HttpsURLConnection.HTTP_NOT_FOUND);
            jediModelAttributes.setError("Object not found");
            return jediModelAttributes.redirect("/"+getTemplatePrefix()+"/"+id);
        }
        //TODO: Security
        preRetrieve(optionalEntity.get());

        postRetrieve(model, optionalEntity.get());

         return jediModelAttributes.view(model);
    }
    default void preDelete(ID id) {

    }

    default String postDelete(ID id, JediModelAttributes jediModelAttributes) {
        return "/"+getTemplatePrefix()+"/"+id;
    }

    @GetMapping("/delete/{id}")
    default String doDeleteById(Model model, @PathVariable("id") ID id){
        JediModelAttributes<ENTITY> jediModelAttributes =
                new JediModelAttributes<ENTITY>(HttpsURLConnection.HTTP_OK,newEntity(), ActionType.DELETE, HttpMethod.GET);

        if (getRepository().existsById(id)) {

            //TODO: Security
            preDelete(id);

            System.out.printf("Deleting %s(%s)\n", getTemplatePrefix(), id.toString());
            getRepository().deleteById(id);

            jediModelAttributes.setSuccess("Object '"+id.toString()+"' deleted");
            jediModelAttributes.setCode(HttpsURLConnection.HTTP_OK);
        } else {
            jediModelAttributes.setError("Object id='"+id.toString()+"' not found");
            jediModelAttributes.setCode(HttpsURLConnection.HTTP_NOT_FOUND);
        }


        return jediModelAttributes.redirect(postDelete(id, jediModelAttributes));
    }

    @GetMapping("/search")
    default String doSearch(Model model,
                            Principal user,
                            SecurityContextHolderAwareRequestWrapper requestWrapper,
                            @RequestParam MultiValueMap<String, String> parameters){

        Iterable<ENTITY> jediEntityCollection;

        jediEntityCollection = searchEntity(model, user, requestWrapper,parameters);


        JediModelAttributes<ENTITY> jediModelAttributes =
                new JediModelAttributes<ENTITY>(HttpsURLConnection.HTTP_OK,newEntity(), ActionType.LIST, HttpMethod.GET);
        jediModelAttributes.setEntityCollection(jediEntityCollection);
        return jediModelAttributes.view(model);
    }

    default void preProcess(ENTITY entity, BindingResult result) {

    }

    default void preSave(ENTITY entity) {

    }
    default void postCreate(ENTITY entity) {

    }
    default void postUpdate(ENTITY entity) {

    }
    default String postProcess(ENTITY entity, JediModelAttributes jediModelAttributes) {
        return "/"+getTemplatePrefix()+"/retrieve/"+getId(entity);
    }

    @PostMapping(value={"/process","/update", "/create"})
    default String doProcess(Model model, @Valid @ModelAttribute("jediEntity") ENTITY entity, BindingResult result){
        preProcess(entity, result);
        ActionType action = getId(entity).toString().equals("0")?ActionType.CREATE:ActionType.UPDATE;


        if(result.hasErrors()){
            System.out.println("Form Error:\n"+result.toString());
            JediModelAttributes<ENTITY> jediModelAttributes =
                    new JediModelAttributes<ENTITY>(HttpsURLConnection.HTTP_OK, entity, action, HttpMethod.POST);
            jediModelAttributes.setAction("/"+getTemplatePrefix()+(getId(entity).equals(0)?"/create":"/update"));
            jediModelAttributes.setError("Jedi reject your form because your form could not pass the validations. \n\n"+result.toString());
            jediModelAttributes.setMethod(HttpMethod.POST);
            return jediModelAttributes.view(model);
        }


        preSave(entity);
        getRepository().save(entity);

        switch (action) {
            case CREATE: postCreate(entity);
            break;
            case UPDATE: postUpdate(entity);
            break;
        }


        JediModelAttributes<ENTITY> jediModelAttributes =
                new JediModelAttributes<ENTITY>(HttpsURLConnection.HTTP_OK,entity, getId(entity).equals(0)?ActionType.CREATE:ActionType.UPDATE, HttpMethod.POST);
        return jediModelAttributes.redirect(postProcess(entity,jediModelAttributes));
    }
}
