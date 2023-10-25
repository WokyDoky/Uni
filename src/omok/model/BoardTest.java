package omok.model;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    Player player = new Player("p1");
    Player opponent = new Player("p2");

    @Test
    void size() {
        var board = new Board();
        var biggerBoard = new Board(19);
        var impossibleBoard = new Board(-1);
        assertEquals(15, board.size());
        assertEquals(19, biggerBoard.size());
        assertEquals(15, impossibleBoard.size());
    }

    @Test
    void clear() {
        var board = new Board();
        // I will place a stone then clean it.
        board.placeStone(1,1,new Player("p1"));
        assertTrue(board.isOccupied(1,1));

        board.clear();
        assertFalse(board.isOccupied(1,1));
    }

    @Test
    void isFull() {
        var board = new Board(6);
        assertFalse(board.isFull());
        for (int i = 0; i < board.size(); i++){
            for (int j = 0; j < board.size(); j++){
                board.placeStone(i , j, new Player("p1"));
            }
        }
        assertTrue(board.isFull());
    }

    @Test
    void placeStone() {
        var board = new Board();
        board.placeStone(1,1,new Player("p1"));
        assertTrue(board.isOccupied(1,1));
        assertFalse(board.isFull());
    }

    @Test
    void isEmpty() {
        var board = new Board();
        for (int i = 0; i < board.size(); i++){
            for (int j = 0; j < board.size(); j++){
                assertTrue(board.isEmpty(i,j));
            }
        }
        board.placeStone(1,1, new Player("p1"));
        assertFalse(board.isEmpty(1,1));
    }

    @Test
    void isOccupied() {
        var board = new Board();
        for (int i = 0; i < board.size(); i++){
            for (int j = 0; j < board.size(); j++){
                assertFalse(board.isOccupied(i,j));
            }
        }
        board.placeStone(1,1, new Player("p1"));
        assertTrue(board.isOccupied(1,1));
    }

    @Test
    void isOccupiedBy() {
        var board = new Board();
        board.placeStone(1,1, new Player("p1"));
        assertTrue(board.isOccupiedBy(1,1, new Player("p1")));
        assertFalse(board.isOccupiedBy(1,1, new Player("p2")));
    }

    @Test
    void playerAt() {
        var board = new Board();
        board.placeStone(1,1, new Player("p1"));
        assertEquals("p1", board.playerAt(1, 1).name());
        assertNotEquals("p2", board.playerAt(1,1).name());
    }

    @Test
    void isWonBy() {


        //I could have cleared the board, but I wanted to show the different type of winning possibilities.

        var horizontallWinningBoard = new Board (6);
        for (int i = 0; i < 5; i++) horizontallWinningBoard.placeStone(0,i, player);
        horizontallWinningBoard.printer();
        assertTrue(horizontallWinningBoard.isWonBy(player));
        assertFalse(horizontallWinningBoard.isWonBy(opponent));

        System.out.println();
        var verticalWinningBoard = new Board (6);
        for (int i = 0; i < 5; i++) verticalWinningBoard.placeStone(i,0, player);
        verticalWinningBoard.printer();
        assertTrue(verticalWinningBoard.isWonBy(player));
        assertFalse(verticalWinningBoard.isWonBy(opponent));

    }

    @Test
    void winningRow() {

        //I could have cleaned the board, but I wanted to show the different type of winning possibilities.
        var horizontalBoard = new Board();
        for (int i = 5; i < 10; i++) horizontalBoard.placeStone(i, 5, player);
        int count = 5;
        for (Board.Place coordinate : horizontalBoard.winningRow()){
            assertEquals(5, coordinate.getCoords()[1]);
            assertEquals(count, coordinate.getCoords()[0]);
            count++;
        }

        var verticalBoard = new Board();
        for (int i = 2; i < 7; i++) verticalBoard.placeStone(i, 10, player);
        count = 2;
        for (Board.Place coordinate : verticalBoard.winningRow()){
            assertEquals(10, coordinate.getCoords()[1]);
            assertEquals(count, coordinate.getCoords()[0]);
            count++;
        }

        //Different size boards and directions.
        var diagonalBoard = new Board (6);
        for (int i = 0; i < 5; i++) diagonalBoard.placeStone(i,i, player);
        for (Board.Place coordinate : diagonalBoard.winningRow()){
            assertEquals(coordinate.getCoords()[0], coordinate.getCoords()[1]);
        }

        var otherDiagonalBoard = new Board(19);
        for (int i = otherDiagonalBoard.size() - 1; i > otherDiagonalBoard.size() - 6; i--) otherDiagonalBoard.placeStone(i,i, player);
        for (Board.Place coordinate : otherDiagonalBoard.winningRow()){
            assertEquals(coordinate.getCoords()[0], coordinate.getCoords()[1]);
        }

        //Weird cases:
        var nonWinningBoard = new Board (6);
        for (int i = 0; i < 3; i++) nonWinningBoard.placeStone(i,i, player);
        assertFalse(nonWinningBoard.winningRow().iterator().hasNext());

        var wonByMoreThan5 = new Board ();
        for (int i = 0; i < 7; i++) wonByMoreThan5.placeStone(i,i, player);
        //Even thought it has 7 in a row, it only returns the first 5.
        //This could be changed.
        for (Board.Place coordinate : wonByMoreThan5.winningRow()){
            assertEquals(coordinate.getCoords()[0], coordinate.getCoords()[1]);
        }
    }
}