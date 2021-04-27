package ChitChat.webSocketServer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ChitChat.model.Message;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import ChitChat.repository.MessagesFirestoreIO;

import java.net.InetSocketAddress;
import java.util.HashSet;

/**
 * Opens and manages the web-socket connection
 */
public class WebSocketConnection extends WebSocketServer {

    // HashSet to add all the connections.
    private HashSet<WebSocket> connections;

    public WebSocketConnection(InetSocketAddress address) {
        super(address);
        this.connections = new HashSet<>();
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        connections.add(webSocket);
        System.out.println("New user connected from :" + webSocket.getRemoteSocketAddress().getAddress().getHostAddress());
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
        connections.remove(webSocket);
        System.out.println(webSocket.getRemoteSocketAddress().getAddress().getHostAddress() + "user disconnected");
    }

    @Override
    public void onMessage(WebSocket webSocket, String messageJSON) {
        System.out.println(messageJSON);
        try {
            MessagesFirestoreIO.saveToFirestore(messageJSON);

            ObjectMapper JsonToMessage = new ObjectMapper();
            Message messagePayload = JsonToMessage.readValue(messageJSON, Message.class);

            String roomId = messagePayload.getRoomId();

            Message lastMessage = MessagesFirestoreIO.retrieveFromFirestore(roomId);
            broadcastMessage(lastMessage);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {
        System.out.println("something went wrong " + e);

    }

    @Override
    public void onStart() {
        System.out.println("server started :O");
    }

    /**
     * Receives a Message objects and sends it to all connections.
     * @param message Message object to be sent to all the connected users.
     */
    private void broadcastMessage(Message message){
        ObjectMapper messageToJson = new ObjectMapper();
        try {
            String messagePayload = messageToJson.writeValueAsString(message);
            System.out.println(connections.size());
            System.out.println(connections);
            for (WebSocket socket: connections) {
                socket.send(messagePayload);
                System.out.println("message sent to: " + socket.getRemoteSocketAddress().getAddress().getHostAddress());
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
