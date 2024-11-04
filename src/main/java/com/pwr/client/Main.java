package com.pwr.client;

import com.pwr.common.PlayerFeaturesInterface;

import java.io.IOException;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.UUID;

public class Main {
    public static void main(String[] args) throws IOException, NotBoundException {
        // Creating a client
        ClientInterface clientInterface = new ClientInterface();
        String userName = clientInterface.promptUserName();
        String userToken = UUID.randomUUID() + "@" + userName;

        // Getting access to Remote object PlayerFeaturesInterface
        Registry registry = LocateRegistry.getRegistry("localhost",9090);
        PlayerFeaturesInterface player = (PlayerFeaturesInterface) registry.lookup("Player");

        // Creating connection with server
        Socket socket = new Socket("localhost",9091);
        ClientCommunicator communicator = new ClientCommunicator(socket);
        communicator.sendMessage(userToken);

        // Getting message that connection with server is success
        communicator.listenMessage();

        // Choosing role (player or watcher)
        ClientController controller = new ClientController(clientInterface, communicator, userToken, player);
        controller.handleUserRole();

        // sent next requests
        while(true)
        {
            System.out.println("Type 'exit' to close program:");
            String request = clientInterface.getUserOption();
            if(request.equals("exit") || request.equals("quit"))
            {
                communicator.sendMessage(request);
                break;
            }

        }
        communicator.closeEverything(socket,communicator.getBufferedReader(),communicator.getBufferedWriter());
    }
}
