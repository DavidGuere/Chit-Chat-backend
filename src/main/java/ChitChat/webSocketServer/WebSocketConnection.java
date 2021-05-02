package ChitChat.webSocketServer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ChitChat.model.Message;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import ChitChat.repository.MessagesFirestoreIO;

import java.net.InetSocketAddress;
import java.net.URI;
import java.util.HashSet;

/**
 * Opens and manages the web-socket connection
 */
public class WebSocketConnection extends WebSocketServer {

    // HashSet to add all the connections.
    private HashSet<String> connections;

    public WebSocketConnection(InetSocketAddress address) {
        super(address);
        this.connections = new HashSet<>();
    }

    public WebSocketConnection(int port){
        super(new InetSocketAddress(port));
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        connections.add(webSocket.getRemoteSocketAddress().getAddress().getHostAddress());
        System.out.println("New user connected from :" + webSocket.getRemoteSocketAddress().getAddress().getHostAddress());

    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
        connections.remove(webSocket);
        System.out.println(webSocket.getRemoteSocketAddress().getAddress().getHostAddress() + "user disconnected");
    }

    @Override
    public void onMessage(WebSocket webSocket, String messageJSON) {
        Message messageToBroadcast = MessagesFirestoreIO.saveToDatabaseAndUpdateID(messageJSON);
        broadcastMessage(messageToBroadcast);
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
        try {
            ObjectMapper messageToJson = new ObjectMapper();
            String messagePayload = messageToJson.writeValueAsString(message);
            broadcast(messagePayload);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
