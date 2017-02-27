package com.durai.ucen.ucen;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Steephan Selvaraj on 2/27/2017.
 */

public class Attendance {
    private String semester;
    private String percentage;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
