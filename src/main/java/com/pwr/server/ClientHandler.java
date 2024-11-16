package com.pwr.server;

import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;

public class ClientHandler implements Runnable{

    //public static ArrayList<com.pwr.client.ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientToken;
    private Player player;


    public ClientHandler(Socket clientSocket,String clientToken,Player player)
    {
        try{
            this.socket = clientSocket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));//charecter stream, not a byte stream
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientToken = clientToken;
            this.player = player;
        } catch (IOException e)
        {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }

    }
    @Override
    public void run() {
        System.out.println("Thread started for the new client with token: " + clientToken);
        String clientUserName = clientToken.substring(clientToken.indexOf("@")+1);
        try {
            //send a message that server got a name and return it with information that connection is good
            sendMessage(clientUserName + " you have connected successfully to server!");

            //reading a role from client
            handleRoleSelection();

            handleClientRequests();

            // When client leaving app - closing thread for this client
            closeEverything(socket,bufferedReader,bufferedWriter);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void sendMessage(String message) throws IOException {
        bufferedWriter.write(message);
        bufferedWriter.newLine();
        bufferedWriter.flush();
    }

    private void handleRoleSelection() throws IOException {
        String roleLetter = bufferedReader.readLine();
        if(roleLetter.equals("p") || roleLetter.equals("P")) {
            String role = "player";
            System.out.println(clientToken + " have chosen " + role + " mode" );
        }
        else if (roleLetter.equals("w") || roleLetter.equals("W")) {
            String role = "watcher";
            System.out.println(clientToken + " have chosen " + role + " mode" );
        }
    }

    private void handleClientRequests() throws IOException {
        while (true) {
            String request = bufferedReader.readLine();
            //System.out.println(clientUserName + " request was: " + request);  !!!!!!!!!!!!!!!!!!!
            if (request.equals("exit") || request.equals("quit")) {
                System.out.println(clientToken + " request was: " + request);
                System.out.println(clientToken + " disconnected from server");
                break;
            } else if (request.startsWith("getPlayersInfo")) {
                getPlayersInfo(request);
            } else if (request.startsWith("getMap")) {
                getMap(request);
            } else if (request.startsWith("getCurrentPlayerTurn")) {
                getCurrentPlayerTurn(request);
            } else if (request.startsWith("checkCombinationX")) {
                checkCombination(request);
            }
        }
    }

    private void getPlayersInfo(String request) throws IOException {
        String roomToken = request.split(":")[1];
        String player1 = player.getTokenOfPlayerWhoTurn(roomToken);
        String player2 = player.getTokenOfOpponent(roomToken,player1);
        sendMessage(player1 + "," + player2);
    }

    private void getMap(String request) throws IOException {
        String roomToken = request.split(":")[1];
        String[][] map = player.getMap(roomToken);
        StringBuilder stringBuilder = new StringBuilder();
        for(String[] row : map)
        {
            for(String element : row)
            {
                stringBuilder.append(element).append(" ");
            }
            stringBuilder.append("*");
        }
        sendMessage(stringBuilder.toString());
    }

    private void getCurrentPlayerTurn(String request) throws IOException {
        String roomToken = request.split(":")[1];
        String currentPlayerToken = player.getTokenOfPlayerWhoTurn(roomToken);
        sendMessage(currentPlayerToken);
    }

    private void checkCombination(String request) throws IOException {
        String roomToken = request.split(":")[1];
        int combinationX = player.checkCombination(roomToken,"X");
        String message;
        switch (combinationX){
            case 1 -> message = "Game over. O wins!";
            case 5 -> message = "Game over. X wins!";
            case 0 -> message = "It's draw!";
            default -> message = "Players have next moves!";
        }
        sendMessage(message);
    }


    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter)
    {
        try{
            if(bufferedReader != null)
            {
                bufferedReader.close();
            }
            if(bufferedWriter != null)
            {
                bufferedWriter.close();
            }
            if(socket != null)
            {
                socket.close();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


}

