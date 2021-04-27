package ChitChat;

import ChitChat.config.FirebaseConnection;
import ChitChat.util.WebSocketThread;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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

