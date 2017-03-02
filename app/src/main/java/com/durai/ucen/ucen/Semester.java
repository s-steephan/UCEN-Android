package com.durai.ucen.ucen;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Steephan Selvaraj on 3/2/2017.
 */

public class Semester {
    private List<Subject> subjects = null;
    private String semester;
    private String created;
    private String modified;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
