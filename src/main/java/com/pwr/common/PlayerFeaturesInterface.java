package com.pwr.common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface PlayerFeaturesInterface extends Remote {
    String createGameRoom(String roomName) throws RemoteException;
    int joinGameRoom(String playerToken,String roomToken) throws RemoteException;
    int leaveGameRoom(String playerToken,String roomToken) throws RemoteException;
    List<String> showRooms() throws RemoteException;
    String deleteRoom(String roomToken) throws RemoteException;
    int getNumberOfPlayersInRoom(String roomToken) throws RemoteException;
    String getTokenOfPlayerWhoTurn(String roomToken) throws RemoteException;
    String getTokenOfOpponent(String roomToken,String playerToken) throws RemoteException;
    String getFigureOfPlayer(String roomToken,String playerToken) throws RemoteException;
    String[][] getMap(String roomToken) throws RemoteException;
    boolean turnStatus(String roomToken,String playerToken) throws RemoteException;
    int makeMove(String roomToken,String playerToken, String playerFigure, int moveNumber) throws RemoteException;
    int checkCombination(String roomToken,String playerFigure) throws RemoteException;
    //int restartGame(String roomToken,String playerToken) throws RemoteException;
    boolean hasRoomWithToken(String roomToken) throws RemoteException;



}
