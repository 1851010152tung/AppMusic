package com.example.musicplayerapp.Model;

public class Report {
    String id;
    String name;
    String email;
    String vdreport;
    String ndreport;
    private Boolean deleted;

    public Report(String id, String name, String email, String vdreport, String ndreport, Boolean deleted) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.vdreport = vdreport;
        this.ndreport = ndreport;
        this.deleted = deleted;
    }
    public Report() {
    }
    private static Report instance = new Report();

    public static  Report getInstance(){
        return instance;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVdreport() {
        return vdreport;
    }

    public void setVdreport(String vdreport) {
        this.vdreport = vdreport;
    }

    public String getNdreport() {
        return ndreport;
    }

    public void setNdreport(String ndreport) {
        this.ndreport = ndreport;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
