package com.pwr.client;

import com.pwr.common.PlayerFeaturesInterface;

import java.io.IOException;
import java.rmi.RemoteException;

public class ClientController {
    private final ClientInterface clientInterface;
    private final ClientCommunicator communicator;
    private final String clientToken;
    private final PlayerFeaturesInterface player;


    public ClientController(ClientInterface clientInterface, ClientCommunicator communicator, String userToken, PlayerFeaturesInterface player) {
        this.clientInterface = clientInterface;
        this.communicator = communicator;
        this.clientToken = userToken;
        this.player = player;
    }

    public void handleUserRole() throws IOException {
        String role = clientInterface.promptUserRole();
        communicator.sendMessage(role);

        if (role.equals("p")) {
            System.out.println("You have chosen player mode");
            handlePlayerMenu();
        } else if (role.equals("w")) {
            System.out.println("You have chosen watcher mode");
            handleWatcherMenu();
        } else System.out.println("Invalid role selected.");
    }

    private void handlePlayerMenu() throws RemoteException {
        PlayerService playerService = new PlayerService(clientToken);
        clientInterface.showPlayerMenu();
        String option = clientInterface.getUserOption();
        while (!option.equals("0")) {
            switch (option) {
                case "1" -> playerService.createRoom(player);
                case "2" -> playerService.checkListOfRooms(player);
                case "3" -> playerService.joinGameRoom(player);
                case "4" -> playerService.leaveGameRoom(player);
                case "5" -> playerService.deleteGameRoom(player);
                default ->
                        System.out.println("Invalid number.Please enter a number of option from '1' to '5' to continue the program!");
            }
            System.out.println();
            System.out.println();
            clientInterface.showPlayerMenu();
            option = clientInterface.getUserOption();
        }
    }

    private void handleWatcherMenu() throws IOException {
        // create watcher service
        clientInterface.showWatcherMenu();
        String option = clientInterface.getUserOption();
        while(!option.equals("0")){
            if(option.equals("1")){
                WatcherService watcherService = new WatcherService(player, communicator);
                watcherService.showListOfGameRooms();
                System.out.println();
                clientInterface.showWatcherMenu();
                option = clientInterface.getUserOption();
            }else System.out.println("Invalid number.Try again.");
        }
        // Closing program
    }
}

