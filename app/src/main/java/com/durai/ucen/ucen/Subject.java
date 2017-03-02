package com.durai.ucen.ucen;

/**
 * Created by Steephan Selvaraj on 3/2/2017.
 */

import java.util.HashMap;
import java.util.Map;

public class Subject {

    private String status;
    private String created;
    private String modified;
    private String score;
    private String code;
    private String name;
    private String attempt_count;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getAttemptCount() {
        return attempt_count;
    }

    public void setAttemptCount(String attempt_count) {
        this.attempt_count = attempt_count;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
