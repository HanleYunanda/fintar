package com.example.fintar.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

  // Firebase intialized in FirebaseConfig

  public void sendNotification(String token, String title, String body) {
    if (token == null || token.isEmpty()) {
      return;
    }
    try {
      Message message =
          Message.builder()
              .setToken(token)
              .setNotification(Notification.builder().setTitle(title).setBody(body).build())
              .build();

      String response = FirebaseMessaging.getInstance().send(message);
      System.out.println("Successfully sent message: " + response);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
