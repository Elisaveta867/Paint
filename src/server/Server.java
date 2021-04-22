package org.suai.java.server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.image.*;
import org.suai.java.client.*;

public class Server {
    private ServerSocket serverSocket = null;
    public static HashMap<String, BufferedImage> boards = null;
    public static ArrayList<ClientThread> clients = null;

    public Server(int portNum) {
        boards = new HashMap<String, BufferedImage>();
        clients = new ArrayList<ClientThread>();
        try {
            serverSocket = new ServerSocket(portNum);
            while (true) {
                ClientThread newClient = new ClientThread(serverSocket.accept());
                synchronized (clients) {
                    clients.add(newClient);
                    clients.get(clients.size() - 1).start();
                }
            }
        } catch (IOException err) {
            System.out.println(err.getMessage());
        }
    }
}