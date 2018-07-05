package com.smarttersstudio.feedbackzone.POJO;

public class Users {
    String name,image,manager,level;

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

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Users(String name, String image, String manager, String level) {

        this.name = name;
        this.image = image;
        this.manager = manager;
        this.level = level;
    }
}
