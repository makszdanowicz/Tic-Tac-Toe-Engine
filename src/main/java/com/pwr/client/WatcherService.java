package com.pwr.client;

import com.pwr.common.PlayerFeaturesInterface;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Scanner;

public class WatcherService {
    private final PlayerFeaturesInterface player;
    private final ClientCommunicator communicator;
    private String roomToken;

    public WatcherService(PlayerFeaturesInterface player, ClientCommunicator communicator) {
        this.player = player;
        this.communicator = communicator;
    }

    public void showListOfGameRooms() throws IOException {
        System.out.println("------------------------------------------------------");
        System.out.println("                    LIST OF GAME ROOMS");
        List<String> gameRoomsList = player.showRooms();
        for(String room : gameRoomsList)
        {
            System.out.println(room);
        }
        if(gameRoomsList.size() == 0){
            System.out.println("No rooms have created yet.Check later list of rooms.");
        }
        else{
            joinGameRoom();
        }
    }

    private void joinGameRoom() throws IOException {
        System.out.println("Type a token of Game room that u want to view( or if no rooms created yet type anything else to update info):");
        Scanner scanner = new Scanner(System.in);
        roomToken = scanner.nextLine();
        boolean roomExists = player.hasRoomWithToken(roomToken);
        if(!roomExists)
        {
            printNoRoomCommunicat();
        }else{
            int playerNumber = player.getNumberOfPlayersInRoom(roomToken);
            if(playerNumber != 2) {
                printNotEnoughPlayersCommunicat();
            }else {
                showGame();
            }
        }
    }

    private void showGame() throws IOException {
        String[] players = communicator.sendWatcherMessage("getPlayersInfo",roomToken).split(",");
        String currentPlayerTurn = communicator.sendWatcherMessage("getCurrentPlayerTurn",roomToken);
        String map = communicator.sendWatcherMessage("getMap", roomToken);

        System.out.println("-----------------------------------------------");
        System.out.println("                ROOM " + roomToken.substring(roomToken.indexOf("@")+1).toUpperCase());
        System.out.println("Player 1: " + players[0]);
        System.out.println("Player 2: " + players[1]);
        System.out.println("Current turn: " + currentPlayerTurn);
        System.out.println("-----------------------------------------------");
        System.out.println(map.replaceAll("\\*","\n"));
        System.out.println();

        String checkCombinationX = communicator.sendWatcherMessage("checkCombinationX",roomToken);
        System.out.println("Status of game : " + checkCombinationX);
        System.out.println();
        System.out.println();
        System.out.println();
        if(!checkCombinationX.equals("Players have next moves!")) {
            System.out.println("You have been kicked to menu, because game is over!");
        }
        String turn = currentPlayerTurn;
        while(turn.equals(currentPlayerTurn))
        {
            turn = communicator.sendWatcherMessage("getCurrentPlayerTurn",roomToken);
            String checkCombination = communicator.sendWatcherMessage("checkCombinationX",roomToken);
            if(!checkCombination.equals("Players have next moves!"))
            {
                String endMap = communicator.sendWatcherMessage("getMap",roomToken);
                System.out.println(endMap.replaceAll("\\*","\n"));
                System.out.println("Status of game : " + checkCombination);
                System.out.println("You have been kicked to menu, because game is over!");
            }
        }


    }

    public void printNoRoomCommunicat(){
        System.out.println();
        System.out.println("There is no room with that token");
        System.out.println("Check again list of game rooms and try again");
        System.out.println();
    }

    public void printNotEnoughPlayersCommunicat() {
        System.out.println("Error, room doesn't have 2 player in lobby. You must to connect to room with 2 players to watch game!");
        System.out.println("Check again list of game rooms and try again");
        System.out.println();
    }


}
