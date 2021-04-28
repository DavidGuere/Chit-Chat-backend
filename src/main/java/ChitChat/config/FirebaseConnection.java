package ChitChat.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.FileInputStream;
import java.io.IOException;

public class FirebaseConnection {
    public static void initializeFirebaseConnection() throws IOException {
//        Dotenv dotenv = Dotenv.load();
        FileInputStream key = new FileInputStream("src/main/java/ChitChat/config/firestoreKey.json");
//        FileInputStream key = new FileInputStream(dotenv.get("GG_CRED"));
        GoogleCredentials credentials = GoogleCredentials.fromStream(key);
        FirebaseOptions options = new FirebaseOptions.Builder().setCredentials(credentials).build();
        FirebaseApp.initializeApp(options);
    }
}
