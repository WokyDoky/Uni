package HW2;

import java.util.*;

abstract class Player {
    private int turn;
    private String pieceType;
    public Player(){}

    /**
     *
     * @param turn determine who is playing. human / machine / opponent.
     * @param pieceType differentiates between the two types of pieces.
     */
    public Player(int turn, String pieceType){
        // Fist player = 1
        // Opponent = 2
        this.turn = turn;
        this.pieceType = pieceType;

    }

    /**
     * Returns 2 randomly given coordinates to functions as a "basic" AI to play against.
     * The "AI" can get better if used with the "cheat" method that outputs the best move.
     *
     * @param tablero reads the board to check if the random coordinates are not previously occupied
     * @return 2 numbers in the range of the board in an array.
     */
    public int [] coord(String[][] tablero){
        Random random = new Random();

        int [] ans = new int[2];
        do {
            ans[0] = random.nextInt(15);
            ans[1] = random.nextInt(15);
        } while (!tablero[ans[0]][ans[1]].equals("."));
        return ans;
    }

    /**
     *
     * @return the int that tells the game what player are we dealing with.
     */
    public int whatTypeOfPlayer(){
        return turn;
    }

    /**
     *
     * @return the type of piece to draw in the board.
     */
    public String getPieceType(){
        return pieceType;
    };


    /**
     *
     * @param tablero reads the board to check if there are any winners.
     *                tablero is sent here even-when it is not necessary to help other methods such as cheat.
     * @return 'true' if you have win, 'false' otherwise.
     */
    public boolean haveYouWon(String[][] tablero){
        if (Board.hasSomeoneWon(tablero)){
            System.out.println("Player "+whatTypeOfPlayer()+" has won!");
            return true;
        }
        return false;
    }

    /**
     * Returns the best next move.
     *
     * @param tablero reads a board to give the best next move
     * @return 2 coordinates where the game thinks it's the best position in a 1D array
     * @see "Go-Moku and Threat-Space Search"
     * @see <a href>https://en.wikipedia.org/wiki/Gomoku#Computers_and_gomoku</a>
     */
    public int [] cheat (String [][] tablero){
        String [][] temp = new String[tablero.length][tablero.length];
        ArrayList<int []> ans = new ArrayList<>();

        for (int i = 0; i < tablero.length; i++){
            for (int j = 0; j < tablero.length; j++){
                temp[i][j] = tablero[i][j];
            }
        }
        int [] resp;


        for (int i = 0; i < tablero.length; i++){
            for (int j = 0; j < tablero.length;j++){
                resp = isThereAWin(temp, i, j);
                ans.add(resp);
            }
        }
        int bestOption = Integer.MIN_VALUE;
        int [] possible = new int[2];
        for (int [] possibleAnswer: ans){
            if (possibleAnswer[2] > bestOption) {
                possible = Arrays.copyOfRange(possibleAnswer,0,2);
                bestOption = possibleAnswer[2];
            }
        }


        return possible;

    }

    /**
     *
     * @param number number to transform
     * @return letter that corresponds to the number.
     */

    protected static String numberToLetter(int number) {
        if (number < 0 || number > 25) {
            return "Invalid number";
        }
        char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        return String.valueOf(alphabet[number]);
    }

    /**
     * This method helps the "cheat" method to check if by putting a piece you can win.
     * If you can't then it goes to the next best thing, which is creating a 4 in line.
     * If you also can't then it goes to the next best thing to search for a 3 in line.
     *
     * This method can be used reactively or proactively to check whether the opponent has a winning line.
     *
     * @param temp reads a copy of the orginal board to check if a hypothetical move would work
     * @param i iterate over the intersections.
     * @param j iterate over the intersections.
     * @return a set of coordinates in an array.
     */
    int []isThereAWin(String[][] temp, int i, int j){
        int [] ans = new int[3];
        temp[i][j] = getPieceType();

        if(Board.hasSomeoneWon(temp)){
            ans[0] = i;
            ans[1] = j;
            ans[2] = 5;
            temp[i][j] = ".";
            return ans;
        }
        else if(find4(temp)){
            ans[0] = i;
            ans[1] = j;
            ans[2] = 4;
            temp[i][j] = ".";
            return ans;
        }
        else if (find3(temp)) {
            ans[0] = i;
            ans[1] = j;
            ans[2] = 3;
            temp[i][j] = ".";
            return ans;
        }
        else{
            temp[i][j] = ".";
        }
        return ans;
    }

    /**
     * Works very similar to the method that check if you have won but instead of 5 in a row, it's for 4 in row.
     * @param tablero reads the hypothetical board to check for best moves.
     * @return set of coordinates in an array.
     */

    private boolean find4(String [][] tablero){
        for (int i = 0; i < tablero.length; i++) {
            for (int j = 0; j < tablero[i].length; j++) {
                if (tablero[i][j].equals(".")){
                    continue;
                }
                // Check horizontally
                if (j + 3 < tablero[i].length && tablero[i][j].equals(tablero[i][j + 1]) && tablero[i][j + 1].equals(tablero[i][j + 2]) && tablero[i][j + 2].equals(tablero[i][j + 3])) {
                    return true;
                }

                // Check vertically
                if (i + 3 < tablero.length && tablero[i][j].equals(tablero[i + 1][j]) && tablero[i + 1][j].equals(tablero[i + 2][j]) && tablero[i + 2][j].equals(tablero[i + 3][j])) {
                    return true;
                }

                // Check diagonally (northeast to southwest)
                if (i + 3 < tablero.length && j + 3 < tablero[i].length && tablero[i][j].equals(tablero[i + 1][j + 1]) && tablero[i + 1][j + 1].equals(tablero[i + 2][j + 2]) && tablero[i + 2][j + 2].equals(tablero[i + 3][j + 3])) {
                    return true;
                }

                // Check diagonally (southwest to northeast)
                if (i - 3 >= 0 && j + 3 < tablero[i].length && tablero[i][j].equals(tablero[i - 1][j + 1]) && tablero[i - 1][j + 1].equals(tablero[i - 2][j + 2]) && tablero[i - 2][j + 2].equals(tablero[i - 3][j + 3])) {
                    return true;
                }
            }
        }

        // If we reach here, there are no 5 in a row of the same type of string.
        return false;
    }
    /**
     * Works very similar to the method that check if you have won but instead of 5 in a row, it's for 3 in row.
     * I believe searching for 2 in a row is not as strong of a strategy.
     * You can spend your time else where, like checking for the opponents best moves.
     * @param tablero reads the hypothetical board to check for best moves.
     * @return set of coordinates in an array.
     */
    private boolean find3(String [][] tablero){
        for (int i = 0; i < tablero.length; i++) {
            for (int j = 0; j < tablero[i].length; j++) {
                if (tablero[i][j].equals(".")){
                    continue;
                }
                // Check horizontally
                if (j + 2 < tablero[i].length && tablero[i][j].equals(tablero[i][j + 1]) && tablero[i][j + 1].equals(tablero[i][j + 2])) {
                    return true;
                }

                // Check vertically
                if (i + 2 < tablero.length && tablero[i][j].equals(tablero[i + 1][j]) && tablero[i + 1][j].equals(tablero[i + 2][j])) {
                    return true;
                }

                // Check diagonally (northeast to southwest)
                if (i + 2 < tablero.length && j + 2 < tablero[i].length && tablero[i][j].equals(tablero[i + 1][j + 1]) && tablero[i + 1][j + 1].equals(tablero[i + 2][j + 2])) {
                    return true;
                }

                // Check diagonally (southwest to northeast)
                if (i - 2 >= 0 && j + 2 < tablero[i].length && tablero[i][j].equals(tablero[i - 1][j + 1]) && tablero[i - 1][j + 1].equals(tablero[i - 2][j + 2])) {
                    return true;
                }
            }
        }

        // If we reach here, there are no 5 in a row of the same type of string.
        return false;
    }

}
