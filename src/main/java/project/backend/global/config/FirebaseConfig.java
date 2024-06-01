package project.backend.global.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Configuration
public class FirebaseConfig {

    @Value("${firebase.key}")
    private String fcmSecretKey;

    @Bean
    public FirebaseApp initializeFirebaseApp() throws IOException {
        if (fcmSecretKey == null) {
            throw new IOException("FCM_SECRET environment variable is not set.");
        }

        ByteArrayInputStream serviceAccount = new ByteArrayInputStream(fcmSecretKey.getBytes());

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        return FirebaseApp.initializeApp(options);
    }
}