package it.uniba.di.ivu.sms16.gruppo2.backend.models;

/**
 * Created by Salvatore on 10/07/16.
 */

import com.google.api.client.util.Key;

public class Notification {

    @Key
    private String to;
    @Key("object")
    private NotificationContent notification;
    @Key("object")
    private NotificationData data;

    public Notification(String to, NotificationContent notification, NotificationData data) {
        this.to = to;
        this.notification = notification;
        this.data = data;
    }

}