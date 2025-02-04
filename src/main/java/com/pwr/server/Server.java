package com.pwr.server;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.registry.Registry;
import java.util.*;


public class Server {
    private String[][] map;
    public Registry registry;
    public static int rmiPort = 9090;
    public static int port = 9091;
    private final ServerSocket serverSocket;
    private ArrayList<ClientHandler> clients = new ArrayList<>();
    public Server(ServerSocket serverSocket)
    {
        this.serverSocket = serverSocket;
    }

    public void startServer(Player player)
    {
        try{
            while(!serverSocket.isClosed())
            {
                Socket socket = serverSocket.accept();
                System.out.println("A new client has connected to server");
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String userToken = reader.readLine();
                System.out.println("A token has been generated for the new client: " + userToken);
                ClientHandler clientThread = new ClientHandler(socket,userToken,player);
                clients.add(clientThread);
                Thread thread = new Thread(clientThread);
                thread.start();
            }
        }catch(IOException e)
        {
            closeServerSocket();
        }
    }


    public void closeServerSocket()
    {
        try {
            if (serverSocket != null) {
                System.out.println("Server closed");
                serverSocket.close();
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
