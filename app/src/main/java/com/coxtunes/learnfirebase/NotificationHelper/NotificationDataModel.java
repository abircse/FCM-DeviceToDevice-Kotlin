package com.coxtunes.learnfirebase.NotificationHelper;

public class NotificationDataModel {
    private String Title;
    private String Message;

    // Empty constructor mendatory in firebase
    public NotificationDataModel() {
    }

    public NotificationDataModel(String title, String message) {
        Title = title;
        Message = message;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

}
