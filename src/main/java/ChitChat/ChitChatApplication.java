package ChitChat;

import ChitChat.config.FirebaseConnection;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import util.WebSocketThread;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@SpringBootApplication
public class ChitChatApplication {

	public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {

		SpringApplication.run(ChitChatApplication.class, args);
		FirebaseConnection connection = new FirebaseConnection();
		connection.initializeFirebaseConnection();

//		WebSocketThread webSocketServer = new WebSocketThread();
//		Thread thread1 = new Thread(webSocketServer);
//		thread1.start();

//		List<String> rooms = new ArrayList<>();
//		rooms.add("");
//		User peti = new User(1, "Peti7", "123", "true", rooms);
//		ObjectMapper userToJson = new ObjectMapper();
//		String userPayload = userToJson.writeValueAsString(peti);
//
//		UserFirestoreIO.saveToFirestore(userPayload);

//		User asd = UserFirestoreIO.getUser("3");
//		System.out.println(asd.toString());


	}

}

