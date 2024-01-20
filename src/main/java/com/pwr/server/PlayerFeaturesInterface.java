package com.pwr.server;

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
    int makeMove(String token,String roomToken, String playerType) throws RemoteException;
    boolean checkCombination(String roomToken) throws RemoteException;
    void showMap(String roomToken) throws RemoteException;

}
