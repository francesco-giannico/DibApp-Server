package it.uniba.di.ivu.sms16.gruppo2.backend.models;

/**
 * Created by Salvatore on 10/07/16.
 */

import com.google.api.client.util.Key;

public class NotificationContent {

    @Key
    public String body;
    @Key
    public String title;
    @Key
    public String icon;

    public NotificationContent(String body, String title, String icon) {
        this.body = body;
        this.title = title;
        this.icon = icon;
    }
}