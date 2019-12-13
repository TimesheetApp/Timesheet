package jbc.timesheet.controller.util;

import jbc.timesheet.configuration.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Objects;


public class JediModelAttributes<ENTITY> {

    private int code;
    private String error;
    private String success;
    private String info;
    private ENTITY entity;
    private Object entityCollection;
    private ActionType actionType;
    private HttpMethod requestedMethod;
    private String action;
    private HttpMethod method;


    public JediModelAttributes(int status, ActionType actionType, HttpMethod requestedMethod) {
        this(HttpURLConnection.HTTP_OK, null, actionType, requestedMethod);
    }

    public JediModelAttributes(int code, ENTITY entity, ActionType actionType, HttpMethod requestedMethod) {
        this.code = code;
        this.entity = entity;
        this.actionType = actionType;
        this.requestedMethod = requestedMethod;
    }

    public JediModelAttributes(int code, String error, String success, String info, ENTITY entity, ActionType actionType, HttpMethod requestedMethod) {
        this.code = code;
        this.error = error;
        this.success = success;
        this.info = info;
        this.entity = entity;
        this.actionType = actionType;
        this.requestedMethod = requestedMethod;
    }

    public JediModelAttributes(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public ENTITY getEntity() {
        return entity;
    }

    public void setEntity(ENTITY entity) {
        this.entity = entity;
    }

    public Object getEntityCollection() {
        return entityCollection;
    }

    public void setEntityCollection(Object entityCollection) {
        this.entityCollection = entityCollection;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public HttpMethod getRequestedMethod() {
        return requestedMethod;
    }

    public void setRequestedMethod(HttpMethod requestedMethod) {
        this.requestedMethod = requestedMethod;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public String view(Model model) {
        return view(model, "jedi/main");
    }

    public String view(Model model, String customTemplate) {


        HashMap<String, Object> modelAttributes = new HashMap<>();
        ENTITY myEntity = Objects.requireNonNull(getEntity());

        modelAttributes.put("jediCode", getCode());
        modelAttributes.put("jediEntityClassName", getClassName());
        modelAttributes.put("jediEntity", myEntity);
        modelAttributes.put("jediEntityCollection", entityCollection);
        modelAttributes.put("jediActionType", actionType);
        modelAttributes.put("jediRequestedMethod", requestedMethod);
        modelAttributes.put("jediActionNext", action);
        modelAttributes.put("jediMethodNext", method);
        modelAttributes.put("jediAutoTemplate", getAutoTemplate());
        modelAttributes.put("jediError", getError());
        modelAttributes.put("jediSuccess", getSuccess());
        modelAttributes.put("jediInfo", getInfo());

        model.addAllAttributes(modelAttributes);
        return customTemplate;
    }

    public String redirect(String url) {
        HashMap<String, Object> modelAttributes = new HashMap<>();
        ENTITY myEntity = Objects.requireNonNull(getEntity());

        modelAttributes.put("jediCode", getCode());
        modelAttributes.put("jediEntityClassName", getClassName());
        modelAttributes.put("jediEntity", myEntity);
        modelAttributes.put("jediEntityCollection", entityCollection);
        modelAttributes.put("jediActionType",  this.actionType);
        modelAttributes.put("jediRequestedMethod", requestedMethod);
        modelAttributes.put("jediActionNext", action);
        modelAttributes.put("jediMethodNext", method);
        modelAttributes.put("jediAutoTemplate", getAutoTemplate());
        modelAttributes.put("jediError", getError());
        modelAttributes.put("jediSuccess", getSuccess());
        modelAttributes.put("jediInfo", getInfo());
        StringBuilder queryString = new StringBuilder();
        modelAttributes.forEach(
                (k,v) -> {
                    if (v == null) return;

                    if (queryString.length() > 0)
                        queryString.append("&");

                    queryString.append(k);
                    queryString.append("=");
                    try {
                        queryString.append(URLEncoder.encode((String) v.toString(),"UTF-8"));
                    } catch (UnsupportedEncodingException ignored) {

                    }
                }
        );
        if (queryString.length() == 0) {
            return String.format("redirect:%s", url);
        }
        else if (url.contains("?")) {
            return String.format("redirect:%s&%s",url, queryString.toString());
        } else {
            return String.format("redirect:%s?%s",url, queryString.toString());
        }

    }

    private String getClassName() {
        ENTITY myEntity = Objects.requireNonNull(getEntity());

        String name = myEntity.getClass().getName();

        name = name.replaceAll("^jbc\\.timesheet\\.model\\.","");
        name = name.replaceAll("^java\\.util\\.","");
        name = name.replaceAll("^java\\.lang\\.","");
        name = name.replaceAll("^java\\.time\\.","");
        return name;
    }

    private String getAutoTemplate() {

        ENTITY myEntity = Objects.requireNonNull(getEntity());

        String name = myEntity.getClass().getName();
        String simpleName = myEntity.getClass().getSimpleName();

        if ((entityCollection != null) && (entityCollection.getClass().getName().equals("java.util.ArrayList"))) {


            if (!name.startsWith("jbc.timesheet.model."))
                name = name+"-arraylist";
        }

        if (name.startsWith("jbc.timesheet.model."))
            return "auto/entity-"+simpleName.toLowerCase()+" :: action-"+requestedMethod.toString().toLowerCase()+"-"+actionType.toString().toLowerCase();

        if (name.startsWith("java.util.")
            || name.startsWith("java.lang.")
            || name.startsWith("java.time.") )
        return "auto/java :: "+simpleName.toLowerCase();

        return name.replaceAll("\\.","-") + " :: action-"+requestedMethod.toString().toLowerCase()+"-"+actionType.toString().toLowerCase();
    }
}
