package it.uniba.di.ivu.sms16.gruppo2.backend.dataprocessing;

import com.google.firebase.database.*;
import it.uniba.di.ivu.sms16.gruppo2.backend.models.Message;
import it.uniba.di.ivu.sms16.gruppo2.backend.notifications.SendNotification;

import java.util.ArrayList;

/**
 * Created by Salvatore on 10/07/16.
 */
public class ListenForMessages extends Thread {

    private FirebaseDatabase database;
    private DatabaseReference messagesRef;
    private DatabaseReference participantsRef;
    private DatabaseReference tokensRef;

    public ListenForMessages() {
        database = FirebaseDatabase.getInstance();
        messagesRef = database.getReference("messages");
        participantsRef = database.getReference("sessionParticipants");
        tokensRef = database.getReference("backend/devices");
    }

    public void run() {


        messagesRef.addChildEventListener(new ChildEventListener() {
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                System.out.println(dataSnapshot.getKey());

                DatabaseReference sessionMessagesRef = messagesRef.child(dataSnapshot.getKey());
                addSessionMessagesListener(sessionMessagesRef);
            }

            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                //DO NOTHING
            }

            public void onChildRemoved(DataSnapshot dataSnapshot) {
                //DO NOTHING
            }

            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                //DO NOTHING
            }

            public void onCancelled(DatabaseError databaseError) {
                //DO NOTHING
            }
        });

        while (true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                //TODO gestire eccezione
                e.printStackTrace();
            }
        }
    }


    private void addSessionMessagesListener(final DatabaseReference sessionMessagesRef) {
        sessionMessagesRef.addChildEventListener(new ChildEventListener() {
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                final Message message = dataSnapshot.getValue(Message.class);
                DatabaseReference sessionParticipantsRef = participantsRef.child(sessionMessagesRef.getKey());

                sessionParticipantsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<String> participantList = new ArrayList<String>();
                        for (DataSnapshot d : dataSnapshot.getChildren()) {

                            participantList.add(d.getKey());
                        }

                        for (final String uid : participantList) {
                            System.out.println(uid);

                            tokensRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    sendMessageNotification(dataSnapshot.getValue(String.class), message);

                                }

                                public void onCancelled(DatabaseError databaseError) {
                                    //DO NOTHING
                                }
                            });
                        }
                    }

                    public void onCancelled(DatabaseError databaseError) {
                        //DO NOTHING
                    }
                });


            }

            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                //DO NOTHING
            }

            public void onChildRemoved(DataSnapshot dataSnapshot) {
                //DO NOTHING
            }

            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                //DO NOTHING
            }

            public void onCancelled(DatabaseError databaseError) {
                //DO NOTHING
            }
        });
    }

    private void sendMessageNotification(String token, Message message) {
        new SendNotification(token, message).start();
    }

}
