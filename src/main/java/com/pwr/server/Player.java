package com.pwr.server;

import com.pwr.common.PlayerFeaturesInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class Player extends UnicastRemoteObject implements PlayerFeaturesInterface {

    private static final HashMap<String, GameRoom> gameRooms= new HashMap<>();
    // UnicastRemoteObject is used, because objects from this class could be transfer to client using RMI server
    public Player () throws RemoteException
    {
        super();
    }
    @Override
    public String createGameRoom(String roomName) throws RemoteException {
        String roomToken = UUID.randomUUID().toString() + "@" + roomName;
        if(gameRooms.containsKey(roomToken))
        {
            return "ERROR!Can't create a new game room using this token, cause room with this token already exists!";
        }
        else {
            gameRooms.put(roomToken, new GameRoom(roomName,roomToken));
            return "The room named " + roomName + " was created!\n" + "If u want to connect to this room use this token: " + roomToken;
        }

    }

    @Override
    public int joinGameRoom(String playerToken, String roomToken) throws RemoteException {
        /*
           resultOfJoiningToGameRoom -1 - ERROR! The room with provided token doesn't exit.Try again!
           resultOfJoiningToGameRoom 0 - You can't connect to this room, because lobby is full. Try again later or connect to other room.
           resultOfJoiningToGameRoom 1 - You have successfully connected to room!
         */
        int resultOfJoiningToGameRoom = -1;
        for(String key : gameRooms.keySet())
        {
            if(key.equals(roomToken))
            {
                GameRoom room = gameRooms.get(roomToken);
                int statusOfJoining = room.addNewPlayer(playerToken);
                if(statusOfJoining == 0)
                {
                    return 0;
                } else if (statusOfJoining == 1) {
                    return 1;
                }
            }
        }
        return resultOfJoiningToGameRoom;
    }

    @Override
    public int leaveGameRoom(String playerToken, String roomToken) throws RemoteException {
        if(!gameRooms.containsKey(roomToken))
        {
            return -1; // There is no room with this token, so it's impossible to leave
        }
        GameRoom room = gameRooms.get(roomToken);
        if(!room.hasPlayer(playerToken))
        {
            return 0; // There is no player with this token in this room

        }
        room.removePlayer(playerToken);
        // Player is successfully was kicked from this room
        return 1;

    }

    @Override
    public List<String> showRooms() throws RemoteException {
        List<String> gameRoomsList = new ArrayList<>();
        if(gameRooms.isEmpty())
        {
            String string = "No rooms have created yet";
            gameRoomsList.add(string);
            return gameRoomsList;
        }
        for(Map.Entry<String,GameRoom> entry: gameRooms.entrySet())
        {
            String string = "name: " + entry.getValue().getName() + " | number of player: " + entry.getValue().getPlayersNumber()+ " | token: " + entry.getKey();
            gameRoomsList.add(string);
        }
        return gameRoomsList;
    }

    @Override
    public String deleteRoom(String roomToken) throws RemoteException {
        for(String key : gameRooms.keySet())
        {
            if(key.equals(roomToken))
            {
                gameRooms.remove(key);
                return "Room with token: " + roomToken +" was successfully deleted!";
            }
        }
        return "Server don't have any room with provided token: " + roomToken + ".Try again.";
    }

    @Override
    public int getNumberOfPlayersInRoom(String roomToken) throws RemoteException {
        GameRoom room = gameRooms.get(roomToken);
        return room.getPlayersNumber();
    }

    @Override
    public String getTokenOfPlayerWhoTurn(String roomToken) throws RemoteException {
        return gameRooms.get(roomToken).getTokenOfTurnPlayer();
    }


    @Override
    public String getTokenOfOpponent(String roomToken, String playerToken) throws RemoteException {
        GameRoom room = gameRooms.get(roomToken);
        return room.getOpponentToken(playerToken);
    }

    @Override
    public String getFigureOfPlayer(String roomToken,String playerToken) throws RemoteException {
        GameRoom room = gameRooms.get(roomToken);
        return room.getPlayerFigure(playerToken);
    }

    @Override
    public String[][] getMap(String roomToken) throws RemoteException {
        GameRoom room = gameRooms.get(roomToken);
        return room.getMap();
    }

    @Override
    public boolean turnStatus(String roomToken,String playerToken) throws RemoteException {
        GameRoom room = gameRooms.get(roomToken);
        return room.getTokenOfTurnPlayer().equals(playerToken);
    }

    @Override
    public int makeMove(String roomToken, String playerToken, String playerFigure, int moveNumber) throws RemoteException {
        GameRoom room = gameRooms.get(roomToken);
        /*
        1 - EVERYTHING IS OKAY
        0 - ON THIS POSITION FIGURE IS ALREADY
        -1 - ERROR There is no slot with this number
         */
        return room.makeMove(playerToken,playerFigure,moveNumber);
    }


    @Override
    public int checkCombination(String roomToken, String playerFigure) throws RemoteException {
        GameRoom room = gameRooms.get(roomToken);
        //5 - game over X won
        //1 - game over O won
        //0 - draw
        //2 - nextMove
        return room.checkCombination(playerFigure);
    }

//    @Override
//    public int restartGame(String roomToken, String playerToken) throws RemoteException {
//        GameRoom room = gameRooms.get(roomToken);
//        return room.restart(playerToken);
//    }

    @Override
    public boolean hasRoomWithToken(String roomToken) throws RemoteException {
        return gameRooms.containsKey(roomToken);
    }

}
