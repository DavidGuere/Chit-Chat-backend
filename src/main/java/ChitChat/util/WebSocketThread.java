package ChitChat.util;

import ChitChat.webSocketServer.WebSocketConnection;

import java.net.InetSocketAddress;

public class WebSocketThread implements Runnable{
    @Override
    public void run() {
//        String host = "localhost";
        String host = "chit-chatdegg.herokuapp.com/";
//        int port = process.env.PORT || 9000;
        int port = 9000;
        WebSocketConnection server = new WebSocketConnection(new InetSocketAddress(host, port));
        server.run();
    }
}
