package com.coxtunes.learnfirebase.NotificationHelper;

public class NotificationSenderModel {
    private NotificationDataModel data;
    private String to;

    // Empty constructor mendatory in firebase
    public NotificationSenderModel() {
    }

    public NotificationSenderModel(NotificationDataModel data, String to) {
        this.data = data;
        this.to = to;
    }
}
