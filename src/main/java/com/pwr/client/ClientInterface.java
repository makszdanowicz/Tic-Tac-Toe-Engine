package com.pwr.client;

import java.util.Scanner;

public class ClientInterface {
    private final Scanner scanner;

    public ClientInterface(){
        this.scanner = new Scanner(System.in);
    }

    public String promptUserName() {
        System.out.println("Enter your nick-name for game session: ");
        return scanner.nextLine();
    }

    public String promptUserRole(){
        System.out.println("If u want to be a player pls type and send 'p'");
        System.out.println("If u want to be a watcher, pls type and send 'w'");
        System.out.println("Enter the role,that u want to be: ");
        return scanner.nextLine();
    }

    public void showPlayerMenu(){
        System.out.println(".-----------------------------------------------------.");
        System.out.println("|In player mode you can:                              |");
        System.out.println("|Create a game room -- type [1]                       |");
        System.out.println("|See the list of created rooms -- type [2]            |");
        System.out.println("|Join a game room -- type [3]                         |");
        System.out.println("|Leave a game room -- type [4]                        |");
        System.out.println("|Delete a game room -- type [5]                       |");
        System.out.println("|Exit from program -- type [0]                        |");
        System.out.println("._____________________________________________________.");
        System.out.println("Type and send option that u want to do:");
    }

    public void showWatcherMenu(){
        System.out.println();
        System.out.println("{----------------------------------------------------------------}");
        System.out.println("You can connect and view a game between 2 players in chosen room");
        System.out.println("If you want to see the list of game rooms  to connect to one of them type '1':");
        System.out.println("If you want to close the program type '0' :");
    }

    public String getUserOption(){
        return scanner.nextLine();
    }

}
