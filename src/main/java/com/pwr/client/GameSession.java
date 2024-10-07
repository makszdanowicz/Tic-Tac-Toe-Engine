package com.pwr.client;

import com.pwr.common.PlayerFeaturesInterface;

import java.rmi.RemoteException;
import java.util.Scanner;

public class GameSession {
    private String roomName;
    private int numberOfPlayers;
    private String connectedRoomToken;
    private String userToken;
    private PlayerFeaturesInterface player;

    public GameSession(String roomName, int numberOfPlayers, String roomToken,String userToken,PlayerFeaturesInterface player) {
        this.roomName = roomName;
        this.numberOfPlayers = numberOfPlayers;
        this.connectedRoomToken = roomToken;
        this.userToken = userToken;
        this.player = player;
    }

    protected void showGameSession() throws RemoteException {
        showGameDetails();
        if(numberOfPlayers == 1)
        {
            String answer = waitingAction();
            if(answer.equals("w"))
            {
                while (answer.equals("w"))
                {
                    if(player.getNumberOfPlayersInRoom(connectedRoomToken) == 1)
                    {
                        answer = waitingAction();
                    } else if (player.getNumberOfPlayersInRoom(connectedRoomToken) == 2) {
                        break;
                    }
                }
                if(answer.equals("l"))
                {
                    System.out.println("Leaving game session...");
                    player.leaveGameRoom(userToken,connectedRoomToken);
                }
                else {
                    startGame();
                }
            } else if (answer.equals("l")) {
                System.out.println("Leaving game session...");
                player.leaveGameRoom(userToken,connectedRoomToken);
            }

        }
        else if(numberOfPlayers == 2)
        {
            startGame();
        }
    }

    public void showGameDetails() {
        System.out.println();
        System.out.println("{---------------------------------------------------------}");
        System.out.println("                    " + roomName.toUpperCase() +" ROOM");
        System.out.println("Room token: " + connectedRoomToken);
        System.out.println("Number of players in room: " + numberOfPlayers);
    }

    public String waitingAction() {
        System.out.println("Can't start a game.You need to wait for other player to join");
        System.out.println("Would you like to wait(press 'w') or u want to leave this room(press 'l'):");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    private void startGame() throws RemoteException {
        String figure = showStartGameInfo();
        boolean isYourTurn = player.turnStatus(connectedRoomToken,userToken);
        //X - first Turn
        int resultOfCombination = player.checkCombination(connectedRoomToken,figure);
        //5 - game over X won
        //1 - game over O won
        //0 - draw
        //2 - nextMove
        while(resultOfCombination == 2) // playing until don't have a draw or someone won
        {
            if(isYourTurn)
            {
                makeMoveInGame(figure);
                resultOfCombination = player.checkCombination(connectedRoomToken,figure);
                isYourTurn = player.turnStatus(connectedRoomToken,userToken);
            }
            else {
                System.out.println("Waiting for the opponent to make a move...");
                System.out.println();
                while(!isYourTurn)
                {
                    isYourTurn = player.turnStatus(connectedRoomToken,userToken);
                }
                resultOfCombination = player.checkCombination(connectedRoomToken,figure);
            }
        }
        endGame(resultOfCombination);

    }

    public String showStartGameInfo() throws RemoteException {
        System.out.println();
        System.out.println("-------------|[START OF GAME]|---------------");
        String figure = player.getFigureOfPlayer(connectedRoomToken,userToken);
        String opponentToken = player.getTokenOfOpponent(connectedRoomToken,userToken);
        System.out.println("Your figure is: " + figure);
        System.out.println("Your opponent is: " + opponentToken.substring(opponentToken.indexOf("@")+1));
        System.out.println(".____________________________________________.");
        return figure;
    }

    private void makeMoveInGame(String figure) throws RemoteException {
        printMap();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Type the number in which you want enter a move:");
        int number = scanner.nextInt();
        int resultOfMove = player.makeMove(connectedRoomToken,userToken,figure,number);
            /* RESULT OF MOVE
              1 - EVERYTHING IS OKAY
              0 - ON THIS POSITION FIGURE IS ALREADY
             -1 - ERROR There is no slot with this number
             */
        if(resultOfMove == -1 || resultOfMove == 0)
        {
            while(resultOfMove ==-1 || resultOfMove == 0)
            {
                System.out.println("ERROR! Map don't have any slot with this number");
                System.out.println("Please try again");
                System.out.println("Type the number in which you want enter a move:");
                number = scanner.nextInt();
                resultOfMove = player.makeMove(connectedRoomToken,userToken,figure,number);
            }
        }
        if(resultOfMove == 1)
        {
            System.out.println("The position № " + number + " now is " + figure);
            System.out.println();
        }
    }

    public void endGame(int resultOfCombination) throws RemoteException {
        if(resultOfCombination == 1){
            printMap();
            System.out.println("End of the game. O won!");
        } else if (resultOfCombination == 5) {
            printMap();
            System.out.println("End of the game. X won!");
        } else if (resultOfCombination ==0) {
            printMap();
            System.out.println("End of the game. It's Draw!");
        }
        player.restartGame(connectedRoomToken,userToken);
        System.out.println("You have been kicked from game room, because game session is over!");
    }

    public void printMap() throws RemoteException {
        String[][] map = player.getMap(connectedRoomToken);
        System.out.println("-------------");
        for (String[] strings : map) {
            for (int j = 0; j < map.length; j++) {
                System.out.print("| " + strings[j] + " ");
            }
            System.out.println("|");
            System.out.println("|-----------|");
        }
        System.out.println();
    }

    /*
    // Printing map at the moment of the game
    public void printMap() throws RemoteException {
        String[][] map = player.getMap(connectedRoomToken);
        showMap(map);
        System.out.println();
    }
     */

    private void showMap(String[][] map) {
        System.out.println("-------------");
        for (String[] strings : map) {
            for (int j = 0; j < map.length; j++) {
                System.out.print("| " + strings[j] + " ");
            }
            System.out.println("|");
            System.out.println("|-----------|");
        }
    }
}
