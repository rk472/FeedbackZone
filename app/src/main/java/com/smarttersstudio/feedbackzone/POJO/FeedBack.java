package com.smarttersstudio.feedbackzone.POJO;

public class FeedBack {
    String uid,feedback,date;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public FeedBack() {

    }

    public FeedBack(String uid, String feedback, String date) {

        this.uid = uid;
        this.feedback = feedback;
        this.date = date;
    }
}
