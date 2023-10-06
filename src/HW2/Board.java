package HW2;

import java.util.*;

public class Board {
    private  int tableroSize;

    //Pieces will be used later when asked to animate this.
    private List<Piece> pieces = new ArrayList<>();
    private static String [][] tablero;
   public Board(){

   }

    /**
     *
     * @param tableroSize gets the size of the board.
     */
    public Board (int tableroSize){
        this.tableroSize = tableroSize;
//        this.tablero = new String[][]
        //ask how to set a String array where I can have it initialized here
    }

    /**
     * Creates the main board of the game.
     * Board in spanish is "tablero".
     *
     * @return main board of the game is crated and returned.
     */
    public static String [][] populateBoard(){
        //Here, I changed the variables to statics, so it wouldn't give me an error
        //ask about it

        //had to change it back, don't know how to fix, pls help

        tablero = new String[15 ][15];
        for (int i = 0; i < tablero.length; i++){
            for (int j = 0; j < tablero.length; j++){
                tablero[i][j] = ".";
            }
        }
        return tablero;
    }

    /**
     * Checks if someone has won the given game board.
     *
     * @param tablero the game board.
     *                even though it is not necessary to pass a parameter for this method
     *                it helps to check hypothetical boards for cheat.
     * @return `true` if someone has won, `false` otherwise
     */
    public static boolean hasSomeoneWon (String [][] tablero){
        for (int i = 0; i < tablero.length; i++) {
            for (int j = 0; j < tablero[i].length; j++) {
                if (tablero[i][j].equals(".")){
                    continue;
                }
                // Check horizontally
                if (j + 4 < tablero[i].length && tablero[i][j].equals(tablero[i][j + 1]) && tablero[i][j + 1].equals(tablero[i][j + 2]) && tablero[i][j + 2].equals(tablero[i][j + 3]) && tablero[i][j + 3].equals(tablero[i][j + 4])) {
                    return true;
                }

                // Check vertically
                if (i + 4 < tablero.length && tablero[i][j].equals(tablero[i + 1][j]) && tablero[i + 1][j].equals(tablero[i + 2][j]) && tablero[i + 2][j].equals(tablero[i + 3][j]) && tablero[i + 3][j].equals(tablero[i + 4][j])) {
                    return true;
                }

                // Check diagonally (northeast to southwest)
                if (i + 4 < tablero.length && j + 4 < tablero[i].length && tablero[i][j].equals(tablero[i + 1][j + 1]) && tablero[i + 1][j + 1].equals(tablero[i + 2][j + 2]) && tablero[i + 2][j + 2].equals(tablero[i + 3][j + 3]) && tablero[i + 3][j + 3].equals(tablero[i + 4][j + 4])) {
                    return true;
                }

                // Check diagonally (southwest to northeast)
                if (i - 4 >= 0 && j + 4 < tablero[i].length && tablero[i][j].equals(tablero[i - 1][j + 1]) && tablero[i - 1][j + 1].equals(tablero[i - 2][j + 2]) && tablero[i - 2][j + 2].equals(tablero[i - 3][j + 3]) && tablero[i - 3][j + 3].equals(tablero[i - 4][j + 4])) {
                    return true;
                }
            }
        }

        // If we reach here, there are no 5 in a row of the same type of string.
        return false;
    }

    /**
     * Prints given board.
     *
     *
     * @param tablero the game board.
     */
    public static void printBoard(String[][] tablero){
        System.out.println("    a   b   c   d   e   f   g   h   i   j   k   l   m   n   o");
        for (int i = 0; i < tablero.length; i++){
            if (i < 10){System.out.print(i+"  ");}
            else {System.out.print(i+" "); }
            for (int j = 0; j < tablero.length; j++){
                System.out.print("["+tablero[i][j]+"] ");
            }
            System.out.println();
        }
        System.out.println("    a   b   c   d   e   f   g   h   i   j   k   l   m   n   o");
    }

    /**
     * Places a stone and checks if someone has won.
     * This method is overloaded to check Human vs Human.
     *
     * @param player first player.
     * @param opponent second player.
     * @return 'true' if someone has won, 'false' otherwise.
     */
    public static boolean placeStone (humanP player, humanP opponent){
        int [] temp = isMovePossible(player);
        if (temp [0] == 1233457754){
            System.out.println("You have decided to end the game");
            return true;
        }
        tablero[temp[0]][temp[1]] = player.getPieceType();
        if (player.haveYouWon(tablero)){
            printBoard(tablero);
            return true;
        }
        int [] opps = isMovePossible(opponent);
        if (opps [0] == 1233457754){
            System.out.println("You have decided to end the game");
            return true;
        }
        tablero[opps[0]][opps[1]] = opponent.getPieceType();
        if (opponent.haveYouWon(tablero)){
            printBoard(tablero);
            return true;
        }
        return false;

    }

    /**
     * Places a stone and checks if someone has won.
     * This method is overloaded to check Human vs Human.
     *
     * @param player first and only human player.
     * @param computer computer based player
     * @return  'true' if someone has won, 'false' otherwise.
     */
    public static boolean placeStone (humanP player, cpuP computer){
        int [] temp = isMovePossible(player);
        if (temp [0] == 1233457754){
            System.out.println("You have decided to end the game");
            return true;
        }
        tablero[temp[0]][temp[1]] = player.getPieceType();
        if (player.haveYouWon(tablero)){
            printBoard(tablero);
            return true;
        }


        int [] opsMove = isMovePossible(computer, player);
        tablero[opsMove[0]][opsMove[1]] = computer.getPieceType();
        if(computer.haveYouWon(tablero)){
            printBoard(tablero);
            return true;
        }

        return false;

    }

    /**
     *
     * @return size of the Board.
     */
    public int sizeBoard(){
        return tableroSize;
    }

    /**
     * Checks to see if current board still has available spots.
     * This help terminate the program in main.
     *
     * @return 'true' if there are still moves possible and 'false' otherwise.
     */
    public static boolean isThereMoves (){
        boolean flag = false;
        for (int i = 0; i < tablero.length; i++){
            for (int j = 0; j < tablero.length; j++){
                if(tablero[i][j].equals(".")){
                    flag = true;
                }
            }
        }
        return flag;
    }

    /**
     * Check if the move from method "coord" is valid.
     *
     * @param human prompts player for a move.
     * @return move if it was possible otherwise prompts again.
     */
    public static int [] isMovePossible(humanP human){
        int [] ans;
        while(true){
            ans = human.coord(tablero);
            if (ans[0] == 1233457754){
                return ans;
            }
            if (ans[0] > 15 || ans[1] > 15){
                System.out.println("Index out of order");
            }
            else if (tablero[ans[0]][ans[1]].equals("X")|| tablero[ans[0]][ans[1]].equals("O")){
                System.out.println("Position is occupied");
            }
            else{
                break;
            }
        }

        return ans;

    }

    /**
     *
     * Checks if ithe best move possible can work on current board.
     * This is made for the computer so no printing is done.
     * Overloaded Method.
     *
     * @param computer gets computer opponent.
     * @param player compares against player.
     * @return array of coordinates of best move.
     */
    public static int [] isMovePossible(cpuP computer, humanP player){
        ArrayList<int []> moves = computer.computerVision(tablero);
        int [] just2Return = new int[3];
        do{
            for (int[] ans : moves) {
                just2Return  = ans;
                moves = computer.computerVision(tablero);
                System.out.println(Arrays.toString(ans));
                if ((tablero[ans[0]][ans[1]].equals("X") || tablero[ans[0]][ans[1]].equals("O"))) {
                    int[] temp = player.cheat(tablero);
                    return computer.coord(tablero);
//                    if (temp[0] == 0 && temp[1] == 0) {
//
//                    } else {
//                        return player.cheat(tablero);
//                    }
                }
            }
        }
        while(just2Return[0] > 15 || just2Return[1] > 15 || (tablero[just2Return[0]][just2Return[1]].equals("X") || tablero[just2Return[0]][just2Return[1]].equals("O")));
        return just2Return;

    }
}

