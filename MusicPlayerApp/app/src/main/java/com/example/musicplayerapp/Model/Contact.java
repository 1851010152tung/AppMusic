package com.example.musicplayerapp.Model;

public class Contact {
    String id;
    String name;
    String number;
    private Boolean deleted;

    public Contact() {
    }
    private static Contact instance = new Contact();

    public static  Contact getInstance(){
        return instance;
    }
    public Contact(String id, String name, String number, Boolean deleted) {
        super();
        this.id = id;
        this.name = name;
        this.number = number;
        this.deleted = deleted;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

}
