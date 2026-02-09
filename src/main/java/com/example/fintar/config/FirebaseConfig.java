package com.example.fintar.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class FirebaseConfig {

  @org.springframework.beans.factory.annotation.Value("${app.firebase.config-file:classpath:firebase-service-account.json}")
  private String firebaseConfigPath;

  @PostConstruct
  public void initialize() {
    try {
      if (FirebaseApp.getApps().isEmpty()) {
        try (InputStream serviceAccount = getServiceAccountStream()) {
          FirebaseOptions options = FirebaseOptions.builder()
              .setCredentials(GoogleCredentials.fromStream(serviceAccount))
              .build();
          FirebaseApp.initializeApp(options);
        }
      }
    } catch (java.io.FileNotFoundException e) {
      System.err.println("WARNING: Firebase config file not found: " + firebaseConfigPath);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private InputStream getServiceAccountStream() throws IOException {
    if (firebaseConfigPath.startsWith("classpath:")) {
      String path = firebaseConfigPath.substring("classpath:".length());
      return new ClassPathResource(path).getInputStream();
    } else {
      return new java.io.FileInputStream(firebaseConfigPath);
    }
  }
}
