package jbc.timesheet.controller;

import jbc.timesheet.controller.util.ActionType;
import jbc.timesheet.controller.util.JediModelAttributes;
import jbc.timesheet.model.Timesheet;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpMethod;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.net.ssl.HttpsURLConnection;
import javax.validation.Valid;

public interface JediController<REPOSITORY extends CrudRepository, ENTITY, ID> {

    ENTITY newEntity();

    REPOSITORY getRepository();

    String getTemplatePrefix();

    @GetMapping("/create")
    default String getCreate(Model model) {

        JediModelAttributes<ENTITY> jediModelAttributes = new JediModelAttributes<ENTITY>(HttpsURLConnection.HTTP_OK,newEntity(), ActionType.CREATE);
        jediModelAttributes.setAction(getTemplatePrefix()+"/process");
        jediModelAttributes.setMethod(HttpMethod.POST);
        return jediModelAttributes.view(model,getTemplatePrefix()+"/"+getTemplatePrefix()+"_form");
    }

    @GetMapping("/update/{id}")
    default String getUpdateId(@PathVariable("id") ID id, Model model){
        @SuppressWarnings("unchecked")
        JediModelAttributes<ENTITY> jediModelAttributes = new JediModelAttributes<ENTITY>(HttpsURLConnection.HTTP_OK, (ENTITY) getRepository().findById(id), ActionType.UPDATE);
        jediModelAttributes.setAction(getTemplatePrefix()+"/process");
        jediModelAttributes.setMethod(HttpMethod.POST);
        return jediModelAttributes.view(model, getTemplatePrefix()+"/"+getTemplatePrefix()+"_form");
    }

    @GetMapping(value = {"/view/{id}", "/retrieve/{id}"})
    default String getViewId(@PathVariable("id") ID id, Model model){
        @SuppressWarnings("unchecked")
        JediModelAttributes<ENTITY> jediModelAttributes = new JediModelAttributes<ENTITY>(HttpsURLConnection.HTTP_OK,(ENTITY) getRepository().findById(id), ActionType.VIEW);
        return jediModelAttributes.view(model, getTemplatePrefix()+"/"+getTemplatePrefix()+"_detail");
    }

    @RequestMapping("/delete/{id}")
    default String getDeleteId(@PathVariable("id") ID id){
        JediModelAttributes<ENTITY> jediModelAttributes = new JediModelAttributes<ENTITY>(HttpsURLConnection.HTTP_OK,newEntity(), ActionType.DELETE);

        if (getRepository().existsById(id)) {

            getRepository().deleteById(id);

            jediModelAttributes.setSuccess("Object deleted");
            jediModelAttributes.setCode(HttpsURLConnection.HTTP_OK);
        } else {
            jediModelAttributes.setError("Object not found");
            jediModelAttributes.setCode(HttpsURLConnection.HTTP_NOT_FOUND);
        }


        return jediModelAttributes.redirect(getTemplatePrefix()+"/"+getTemplatePrefix()+"_detail");
    }

    @GetMapping("/show")
    default String getShow(Model model){
        @SuppressWarnings("unchecked")
        JediModelAttributes<ENTITY> jediModelAttributes = new JediModelAttributes<ENTITY>(HttpsURLConnection.HTTP_OK,(ENTITY) getRepository().findAll(), ActionType.LIST);
        return jediModelAttributes.view(model, getTemplatePrefix()+"/"+getTemplatePrefix()+"_list");
    }


    @PostMapping("/process")
    default String postProcess(Model model, @Valid @ModelAttribute ENTITY entity, BindingResult result){

        if(result.hasErrors()){
            @SuppressWarnings("unchecked")
            JediModelAttributes<ENTITY> jediModelAttributes = new JediModelAttributes<ENTITY>(HttpsURLConnection.HTTP_OK, entity, ActionType.UPDATE);
            jediModelAttributes.setAction(getTemplatePrefix()+"/process");
            jediModelAttributes.setMethod(HttpMethod.POST);
            return jediModelAttributes.view(model);
        }

        getRepository().save(entity);

        @SuppressWarnings("unchecked")
        JediModelAttributes<ENTITY> jediModelAttributes = new JediModelAttributes<ENTITY>(HttpsURLConnection.HTTP_OK,null, ActionType.DELETE);
        return jediModelAttributes.redirect(getTemplatePrefix()+"/"+getTemplatePrefix()+"_list");
    }
}
