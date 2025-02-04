package com.pwr.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.rmi.registry.LocateRegistry;

public class Main {
    public static void main(String[] args)
    {
        //Create a server
        try {
            ServerSocket serverSocket = new ServerSocket(Server.port);
            Server server = new Server(serverSocket);
            System.out.println("Server is running");

            Player player = new Player();
            server.registry = LocateRegistry.createRegistry(Server.rmiPort);
            server.registry.rebind("Player",player);

            server.startServer(player);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
