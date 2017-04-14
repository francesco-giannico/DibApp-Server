package it.uniba.di.ivu.sms16.gruppo2.backend.notifications;

import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.gson.Gson;
import it.uniba.di.ivu.sms16.gruppo2.backend.models.Message;
import it.uniba.di.ivu.sms16.gruppo2.backend.models.Notification;
import it.uniba.di.ivu.sms16.gruppo2.backend.models.NotificationContent;
import it.uniba.di.ivu.sms16.gruppo2.backend.models.NotificationData;

import java.io.IOException;
import java.net.MalformedURLException;


public class SendNotification extends Thread {

    private String FCM_URL = "https://fcm.googleapis.com/fcm/send";
    private String SERVER_KEY = "AIzaSyB5KizObfKVaXSP2ErAaIFeeCWZbe2lovM";
    private Message message;
    private String token;

    public SendNotification(String token, Message message) {
        this.token = token;
        this.message = message;
    }

    public void run() {
        final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
        final JsonFactory JSON_FACTORY = new JacksonFactory();

        Gson gson = new Gson();

        NotificationContent notificationContent;
        if (message.message.startsWith("https://firebasestorage.googleapis.com/") && message.senderPhoto == null) {
            notificationContent = new NotificationContent("Image", message.senderName, null);

        } else if (message.message.startsWith("https://firebasestorage.googleapis.com/")) {
            notificationContent = new NotificationContent("Image", message.senderName, message.senderPhoto);

        } else {
            notificationContent = new NotificationContent(message.message, message.senderName, message.senderPhoto);

        }

        //TODO da inviare id Sessione per aprire l'activity del canale associato al click sulla notifica
        final String jsonInString = gson.toJson(new Notification(token,
                notificationContent, new NotificationData("idSessione")));

        try {

            GenericUrl url = new GenericUrl(FCM_URL.replaceAll(" ", "%20"));

            HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {

                public void initialize(HttpRequest request) throws IOException {
                    request.setParser(new JsonObjectParser(JSON_FACTORY));
                }
            });

            ExponentialBackOff backoff = new ExponentialBackOff.Builder()
                    .setInitialIntervalMillis(500)
                    .setMaxElapsedTimeMillis(900000)
                    .setMaxIntervalMillis(6000)
                    .setMultiplier(1.5)
                    .setRandomizationFactor(0.5)
                    .build();


            System.out.println(jsonInString);

            HttpRequest request = requestFactory.buildPostRequest(url, ByteArrayContent.fromString(null, jsonInString));

            request.setUnsuccessfulResponseHandler(new HttpBackOffUnsuccessfulResponseHandler(backoff));
            request.getHeaders().setContentType("application/json");
            request.getHeaders().setAuthorization("key=" + SERVER_KEY);
            HttpResponse response = request.execute();

            if (response.getStatusCode() != 200) {
                //TODO gestire errore
                System.out.println("ERROR EXECUTE POST");
            }
            System.out.println(response.getStatusCode() + response.getStatusMessage());


        } catch (MalformedURLException e) {
            //TODO gestire eccezione
            e.printStackTrace();
        } catch (IOException e) {
            //TODO gestire eccezione
            e.printStackTrace();
        }
    }


}
