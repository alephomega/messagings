package com.xxx.messaging.sending.sender;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.xxx.messaging.Notification;
import com.xxx.messaging.sending.Sender;

public class FCM extends Sender {
    private final FirebaseMessaging firebaseMessaging;
    private final TokenManager tokenManager;


    private FCM(FirebaseMessaging firebaseMessaging, TokenManager tokenManager) {
        this.firebaseMessaging = firebaseMessaging;
        this.tokenManager = tokenManager;
    }

    public Notification send(Notification notification) {
        String to = notification.getTo();
        String token = tokenManager.getToken(to);

        Message message = Message.builder()
                .setToken(token)
                .build();

        try {
            firebaseMessaging.send(message);
        } catch (FirebaseMessagingException e) {

        }

        return notification;
    }
}