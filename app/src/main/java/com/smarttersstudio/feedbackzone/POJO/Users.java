package com.smarttersstudio.feedbackzone.POJO;

public class Users {
    String name,image,manager;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public Users() {

    }

    public Users(String name, String image, String manager) {

        this.name = name;
        this.image = image;
        this.manager = manager;
    }
}
