package com.durai.ucen.ucen;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Steephan Selvaraj on 2/27/2017.
 */

public class CircularDetail {
    private Integer id;
    private String created;
    private String modified;
    private String title;
    private String summary;
    private String content_html;
    private Boolean is_active;
    private String published_date;
    private Integer created_by;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getContentHtml() {
        return content_html;
    }

    public void setContentHtml(String content_html) {
        this.content_html = content_html;
    }

    public Boolean getIsActive() {
        return is_active;
    }

    public void setIsActive(Boolean is_active) {
        this.is_active = is_active;
    }

    public String getPublishedDate() {
        return published_date;
    }

    public void setPublishedDate(String published_date) {
        this.published_date = published_date;
    }

    public Integer getCreatedBy() {
        return created_by;
    }

    public void setCreatedBy(Integer created_by) {
        this.created_by = created_by;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
