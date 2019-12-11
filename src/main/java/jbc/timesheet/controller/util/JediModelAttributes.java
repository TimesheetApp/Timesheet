package jbc.timesheet.controller.util;

import jbc.timesheet.configuration.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Optional;


public class JediModelAttributes {

    @Autowired
    private ApplicationProperties applicationProperties;

    private int code;
    private String error;
    private String success;
    private String info;
    private Object entity;
    private Object meta;
    private ActionType action;

    public JediModelAttributes() {
        this(HttpURLConnection.HTTP_OK, ActionType.DEFAULT);
    }

    public JediModelAttributes(int status, ActionType action) {
        this(HttpURLConnection.HTTP_OK, null, ActionType.DEFAULT);
    }

    public JediModelAttributes(int code, Object entity, ActionType action) {
        this.code = code;
        this.entity = entity;
        this.action = action;
    }

    public JediModelAttributes(int code, String error, String success, String info, Object entity, ActionType action) {
        this.code = code;
        this.error = error;
        this.success = success;
        this.info = info;
        this.entity = entity;
        this.action = action;
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

    public Object getEntity() {
        return entity;
    }

    public void setEntity(Object entity) {
        this.entity = entity;
    }

    public Object getMeta() {
        return meta;
    }

    public void setMeta(Object meta) {
        this.meta = meta;
    }

    public ActionType getAction() {
        return action;
    }

    public void setAction(ActionType action) {
        this.action = action;
    }

    public String view(Model model) {
        return view(model, applicationProperties.getDefaultTemplate());
    }

    public String view(Model model, String template) {


        HashMap<String, Object> modelAttributes = new HashMap<>();
        Object myEntity = Optional.ofNullable(getEntity()).orElse(new Object());

        modelAttributes.put("jediCode", getCode());
        modelAttributes.put("jediEntityClassName", myEntity.getClass().getName());
        modelAttributes.put("jediEntity", myEntity);
        modelAttributes.put("jediMeta", meta);
        modelAttributes.put("jediActionType",  action);
        modelAttributes.put("jediError", getError());
        modelAttributes.put("jediSuccess", getSuccess());
        modelAttributes.put("jediInfo", getInfo());
        model.addAllAttributes(modelAttributes);
        return template;
    }

    public String redirect(String url) {
        HashMap<String, Object> modelAttributes = new HashMap<>();
        Object myEntity = Optional.ofNullable(getEntity()).orElse(new Object());

        modelAttributes.put("jediCode", getCode());
        modelAttributes.put("jediEntityClassName", myEntity.getClass().getName());
        modelAttributes.put("jediEntity", myEntity);
        modelAttributes.put("jediMeta", meta);
        modelAttributes.put("jediActionType",  this.action);
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
}
