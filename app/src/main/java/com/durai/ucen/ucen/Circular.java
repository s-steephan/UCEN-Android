package com.durai.ucen.ucen;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Steephan Selvaraj on 2/23/2017.
 */

public class Circular {
    private Integer id;
    private String title;
    private String summary;
    private String published_date;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getPublishedDate() {
        return published_date;
    }

    public void setPublishedDate(String published_date) {
        this.published_date = published_date;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
