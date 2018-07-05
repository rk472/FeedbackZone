package com.smarttersstudio.feedbackzone.POJO;

public class Posts {
    String date,name,text,uid;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }



    public Posts() {

    }

    public Posts(String date, String name, String text, String uid) {

        this.date = date;
        this.name = name;
        this.text = text;
        this.uid = uid;
    }
}
