package it.uniba.di.ivu.sms16.gruppo2.backend.models;

import com.google.api.client.util.Key;

/**
 * Created by Salvatore on 10/07/16.
 */
public class NotificationData {

    @Key
    public String sessionId;

    public NotificationData(String sessionId) {
        this.sessionId = sessionId;
    }
}