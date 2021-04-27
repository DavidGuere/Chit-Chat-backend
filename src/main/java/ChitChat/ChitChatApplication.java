package ChitChat;

import ChitChat.config.FirebaseConnection;
import ChitChat.util.WebSocketThread;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;

@SpringBootApplication
public class ChitChatApplication {

	public static void main(String[] args) throws IOException{

		SpringApplication.run(ChitChatApplication.class, args);
		FirebaseConnection.initializeFirebaseConnection();

		WebSocketThread webSocketServer = new WebSocketThread();
		Thread thread1 = new Thread(webSocketServer);
		thread1.start();
	}


}

