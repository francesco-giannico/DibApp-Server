package it.uniba.di.ivu.sms16.gruppo2.backend;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import it.uniba.di.ivu.sms16.gruppo2.backend.dataprocessing.ListenForMessages;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by Salvatore on 10/07/16.
 */
public class Application extends Thread {

    public static void main(String[] args) throws InterruptedException {
        new Application().start();
    }

    public void run() {
        System.out.println("Started");

        try {

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setServiceAccount(new FileInputStream("resources/serviceAccountCredentials.json"))
                    .setDatabaseUrl("https://dibapp-dev.firebaseio.com/")
                    .build();

            FirebaseApp.initializeApp(options);

            new ListenForMessages().start();

        } catch (FileNotFoundException e) {
            //TODO gestire eccezione
            e.printStackTrace();
        }

    }
}
