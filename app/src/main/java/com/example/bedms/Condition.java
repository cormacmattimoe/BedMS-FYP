package com.example.bedms;

public class Condition {

    String id;
    String name;
    String commonName;
    String sex_filter;
    String [] categories;
    String prevalence;
    String acuteness;
    String severity;
    Extras extra;
    String triage_level;

    public Condition() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getSex_filter() {
        return sex_filter;
    }

    public void setSex_filter(String sex_filter) {
        this.sex_filter = sex_filter;
    }

    public String[] getCategories() {
        return categories;
    }

    public void setCategories(String[] categories) {
        this.categories = categories;
    }

    public String getPrevalence() {
        return prevalence;
    }

    public void setPrevalence(String prevalence) {
        this.prevalence = prevalence;
    }

    public String getAcuteness() {
        return acuteness;
    }

    public void setAcuteness(String acuteness) {
        this.acuteness = acuteness;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public Extras getExtra() {
        return extra;
    }

    public void setExtra(Extras extra) {
        this.extra = extra;
    }

    public String getTriage_level() {
        return triage_level;
    }

    public void setTriage_level(String triage_level) {
        this.triage_level = triage_level;
    }
}