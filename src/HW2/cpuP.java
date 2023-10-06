package HW2;
import java.util.ArrayList;
import java.util.Random;

//use abstract or interface for this, you can also ask TA if there is a way to have the abstract class do the return methods instead of every class
//Mainly I just want to have something here for the points
public class cpuP extends Player {

    public cpuP(int turn, String pieceType){
        super(turn, pieceType);
    }

    /**
     * computer vision check for the cheat method to get the best move and use it to it's advantage.
     *
     * @param tablero get board from the game
     * @return a list of possible options where to put a piece. They are in order from best option to worst.
     */
    public ArrayList<int[]> computerVision (String [][] tablero){
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


        return ans;

    }


    /**
     * Checks if you have won, but this time in prints a different message since it's the computer who won.
     *
     * @param tablero reads the board to check if there are any winners.
     *                tablero is sent here even-when it is not necessary to help other methods such as cheat.
     * @return 'true' if computer has won, 'false' otherwise.
     */
    @Override
    public  boolean haveYouWon(String[][] tablero){
        if (Board.hasSomeoneWon(tablero)){
            System.out.println("Computer has won!");
            return true;
        }
        return false;
    }
}
