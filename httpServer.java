package ceit.aut.ac.ir;

import java.io.*;
import java.net.*;

public class httpServer {

    ServerSocket server;
    int port = 8080;


    public httpServer() {
        try {
            server = new ServerSocket(port);
            System.out.println("Server is listening to the port for a request");
            while (true) {
                // make a socket for each client
                Socket mySocket = server.accept();
                // start a thread for each client
                new httpThread(mySocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

