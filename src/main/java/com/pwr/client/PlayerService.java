package com.pwr.client;

import com.pwr.common.PlayerFeaturesInterface;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Scanner;

public class PlayerService {
    private final Scanner scanner;
    private final String userToken;
    private String connectedRoomToken;

    public PlayerService(String userToken)
    {
        this.scanner = new Scanner(System.in);
        this.userToken = userToken;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setConnectedRoomToken(String connectedRoomToken) {
        this.connectedRoomToken = connectedRoomToken;
    }

    public void createRoom(PlayerFeaturesInterface player) throws RemoteException {
        System.out.println();
        System.out.println("------------------------------------------------------");
        System.out.println("                   CREATING A GAME ROOM");
        System.out.println("Provide a room name: ");
        String roomName = scanner.nextLine();
        System.out.println(player.createGameRoom(roomName));
    }

    public void checkListOfRooms(PlayerFeaturesInterface player) throws RemoteException {
        System.out.println();
        System.out.println("------------------------------------------------------");
        System.out.println("                    LIST OF GAME ROOMS");
        List<String> gameRooms = player.showRooms();
        for(String room : gameRooms)
        {
            System.out.println(room);
        }
    }

    public void joinGameRoom(PlayerFeaturesInterface player) throws RemoteException {
        System.out.println("Provide a token of game room, in which u want to connect: ");
        String roomToken = scanner.nextLine();
        int resultOfJoining = player.joinGameRoom(getUserToken(),roomToken);
        if(resultOfJoining == -1)
        {
            System.out.println("ERROR!The room with provided token doesn't exit. Try again!");
        }
        else if(resultOfJoining == 0)
        {
            System.out.println("You can't connect to this room, because lobby is full. Try again later or connect to other room.");
        }
        else if(resultOfJoining == 1)
        {
            setConnectedRoomToken(roomToken);
            System.out.println("You have successfully connected to room " + roomToken.substring(roomToken.indexOf("@")+1) + "!");
            GameSession gameSession = new GameSession(roomToken.substring(roomToken.indexOf("@")+1),player.getNumberOfPlayersInRoom(roomToken),connectedRoomToken,userToken,player);
            gameSession.showGameSession();
        }
    }

    public void leaveGameRoom(PlayerFeaturesInterface player) throws RemoteException {
        System.out.println("Provide a token of game room that u want to leave");
        String roomToken = scanner.nextLine();
        int resultOfLeaving = player.leaveGameRoom(getUserToken(),roomToken);
        if(resultOfLeaving == -1) {
            System.out.println("ERROR! There is no room with this token. Please check the token of the room that u want to leave and try again!");
        } else if (resultOfLeaving == 0) {
            System.out.println("ERROR! You can't leave this room, because you are not connected to this room!");
        } else if (resultOfLeaving == 1) {
            System.out.println("You have successfully leave chosen room!");
        }
    }

    public void deleteGameRoom(PlayerFeaturesInterface player) throws RemoteException {
        System.out.println("Provide a token of game room that u want do delete: ");
        String roomToken = scanner.nextLine();
        int numberOfPlayers = player.getNumberOfPlayersInRoom(roomToken);
        if(numberOfPlayers == 0)
        {
            System.out.println(player.deleteRoom(roomToken));
        }
        else {
            System.out.println("You can't delete this room, cause amount of people in room is not 0!");
        }

    }

}
