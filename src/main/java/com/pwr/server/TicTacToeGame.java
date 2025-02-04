package com.pwr.server;

import java.util.Scanner;
import java.util.SortedMap;

public class TicTacToeGame {
    private String[][] map;
    public TicTacToeGame()
    {
        createMap();
    }

    // To print out the board.
    /*

       -------------
       | 0 | 1 | 2 |
       |-----------|
       | 3 | 4 | 5 |
       |-----------|
       | 6 | 7 | 8 |
       -------------
       */
    public void createMap()
    {
       map = new String[][] { {"0", "1", "2"},
                              {"3", "4", "5"},
                              {"6", "7", "8"}
       };
    }

    public String[][] getMap() {
        return map;
    }

    public void showMap()
    {
        System.out.println("-------------");
        for (String[] strings : map) {
            for (int j = 0; j < map.length; j++) {
                System.out.print("| " + strings[j] + " ");
            }
            System.out.println("|");
            System.out.println("|-----------|");
        }
    }


    public int makeMove(String figure, int move)
    {
        // Validate move
        if(move > -1 && move < 9)
        {
            if(move < 3)
            {
                map[0][move] = figure;
                return 1;// move is correct
            }
            else
            {
                int i = move / 3;
                int j = move % 3;
                if(map[i][j].equals("X") || map[i][j].equals("O"))
                {
                    return 0;// position is taken by other figure
                }
                else {
                    map[i][j] = figure;
                }
            }
            return 1;
        }
        return -1;// There is no slot with this number
    }

    public int showResultOfGame(String type)
    {
        //Checking is some player win the game(X or O)
        if(checkCombination("X"))
        {
            System.out.println("Game over! X wins");
            return 5;
        }
        else if(checkCombination("O"))
        {
            System.out.println("Game over! O wins");
            return 1;
        }
        //Checking is draw or not completed game
        else {
            //Checking does map have any numbers
            int numberCounter = 0;
            for (String[] strings : map) {
                for (String string : strings) {
                    if (!string.equals("X") && !string.equals("O")) {
                        numberCounter++;
                    }
                }
            }
            if(numberCounter == 0)
            {
                System.out.println("Draw!The map don't have free slots for move");
                return 0; // game is over because of draw
            }
            else {
                return 2; // game is continued
            }
        }
    }

    private boolean checkCombination(String type)
    {
        // All number of possible combinations = 3+3+2 = 8
        // checking is any raw combination has success
        for (String[] strings : map) {
            int j = 0;
            if (strings[j].equals(type) && strings[j + 1].equals(type) && strings[j + 2].equals(type)) {
                return true;
            }
        }

        // checking is any column combination has success
        for(int j = 0; j < map.length; j++)
        {
            int i = 0;
            if(map[i][j].equals(type) && map[i+1][j].equals(type) && map[i+2][j].equals(type))
            {
                return true;
            }
        }

        // checking is any combination of diagonals has success
        return map[0][0].equals(type) && map[1][1].equals(type) && map[2][2].equals(type) || map[2][0].equals(type) && map[1][1].equals(type) && map[0][2].equals(type);
    }
}
