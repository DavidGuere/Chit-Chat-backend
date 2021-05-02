package ChitChat.util;

import ChitChat.webSocketServer.WebSocketConnection;
import org.java_websocket.server.DefaultSSLWebSocketServerFactory;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.CertificateException;

public class WebSocketThread implements Runnable{

    @Override
    public void run() {

//        String host = "localhost";
        String host = "https://chit-chatdegg.herokuapp.com/";
//        int port = process.env.PORT || 9000;
        int port = 9000;
        WebSocketConnection server = new WebSocketConnection(new InetSocketAddress(host, port));

        server.run();



    }
}
