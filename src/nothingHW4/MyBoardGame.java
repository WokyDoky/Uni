    package nothingHW4;

    import HW2.Board;
    import HW2.cpuP;
    import omok.model.Player;

    import javax.swing.*;
    import java.awt.*;
    import java.awt.event.*;
    import java.util.ArrayList;
    import java.util.Random;

    public class MyBoardGame extends boardDraw {
        final int sizeOfBoard = 15;
        public boolean isWhiteTurn = false;
        public int gameMode = 0;
        private boolean inGame = false;
        private boolean restartConfirmation = false;
        private final int constant = 592 / 16;

        HW2.Board board;

        /*
        Constructor. It sets the size of the board.
        It creates a new "board" which is what handles the logic behind the scenes.
        Starts the MouseListener. It is here after IA recommendation. Thank you.
         */
        public MyBoardGame() {
            setPreferredSize(new Dimension(592, 592));
            board = new Board();
            board.populateBoard();
            addMouseListener(new MyMouseListener(this, board)); // Add the MouseListener
            start = new JButton();
            showStartScreen();

        }
        @Override
        public void start(){
            showGameModeOptions();
        }


        /*
        First things the player sees.
        You get to see it again if the menu button is clicked.
         */
        private void showStartScreen() {
            removeAll();
            JButton startButton = new JButton("Start Game");
            startButton.addActionListener(e -> showGameModeOptions());
            add(startButton);
            start.setEnabled(inGame);
            revalidate();
        }
        /*
        Code gets here if the button to restart is clicked and confirmed.
         */
        private void resetGame() {
            isWhiteTurn = false;
            board.populateBoard();
            removeAll();
            repaint();
        }
        /*
        Method that shows game mode options and the initial "play" button. 
        It holds 3 Radio buttons that are shown after press play.
        Once you choose something it sets the field gameMode. 
        That's how we know what are we playing.  
         */
        private void showGameModeOptions() {
            removeAll();
            start.setEnabled(false);

            if (inGame) {
                showRestartConfirmation();
            } else {
                JRadioButton mode1Button = new JRadioButton("Player vs Opponent");
                JRadioButton mode2Button = new JRadioButton("Player vs CPU");
                JRadioButton mode3Button = new JRadioButton("Online");

                ButtonGroup group = new ButtonGroup();
                group.add(mode1Button);
                group.add(mode2Button);
                group.add(mode3Button);

                JButton playButton = new JButton("Play");

                playButton.addActionListener(e -> {
                    if (mode1Button.isSelected()) {
                        gameMode = 1;
                    } else if (mode2Button.isSelected()) {
                        gameMode = 2;
                    } else if (mode3Button.isSelected()) {
                        gameMode = 3;
                    }

                    inGame = true;
                    System.out.println(inGame);
                    repaint();
                });

                add(mode1Button);
                add(mode2Button);
                add(mode3Button);
                add(playButton);
            }

            revalidate();
        }

        /*
        This is what happens after the menu and restart button are clicked. 
        There are confirmation buttons for both of them.
        Buttons could be set as methods, but I don't really see the point.
         */
        private void showRestartConfirmation() {
            removeAll();
            start.setEnabled(true);
            JButton menuButton = new JButton("Menu");

            menuButton.addActionListener(e -> {
                Object[] options = {"Yes, let me go", "Keep playing"};
                int choice = JOptionPane.showOptionDialog(MyBoardGame.this, "You are about to leave the game.", "Leaving?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                if (choice == JOptionPane.YES_OPTION){
                    inGame = false;
                    gameMode = 0;
                    resetGame();  // Reset the game state
                    showStartScreen();
                }
            });

            if (restartConfirmation) {
                JButton restartButton = new JButton("Restart");

                restartButton.addActionListener(e -> {
                    int choice = JOptionPane.showConfirmDialog(MyBoardGame.this, "Are you sure you want to restart the game?", "Confirm Restart", JOptionPane.YES_NO_OPTION);
                    if (choice == JOptionPane.YES_OPTION) {
                        resetGame();  // Reset the game state
                        restartConfirmation = false;
                    }
                });
                add(restartButton);
            }

            add(menuButton);
            revalidate();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (inGame) {
                if (gameMode == 1 || gameMode == 2) {
                    if (!restartConfirmation) {
                        restartConfirmation = true;
                        board.populateBoard();
                        showRestartConfirmation();
                    }
                    drawBoard(g);
                } else if (gameMode == 3) {
                    if (!restartConfirmation) {
                        restartConfirmation = true;
                        showRestartConfirmation();
                    }
                    g.setColor(Color.BLACK);
                    g.setFont(new Font("Arial Black", Font.BOLD, 20));
                    String message = "Online mode is not available yet :)";
                    int messageWidth = g.getFontMetrics().stringWidth(message);
                    g.drawString(message, getWidth() / 2 - messageWidth / 2, getHeight() / 2);
                }
            }
        }

        class MyMouseListener implements MouseListener {
            private MyBoardGame outer; // Reference to the outer class
            private String [][] tablero;
            //Random r = new Random();

            MyMouseListener(MyBoardGame outer, Board board) {
                this.outer = outer;
                tablero = board.getBoard();
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                if (Board.isThereMoves(tablero)){
                    if (outer.gameMode == 1){
                        int mcX = (int) findClosestIntersection(e.getX(), e.getY()).getY();
                        int mcY = (int) findClosestIntersection(e.getX(), e.getY()).getX();
                        int [] playerMove = {mcY - 1,mcX - 1 };

                        if (!Board.hasSomeoneWon(board.getBoard())) {
                            if (board.isMovePossible(playerMove)) {
                                if (e.getX() > 37 && e.getX() < 592 && e.getY() > 37 && e.getY() < 592) {
                                    if (isWhiteTurn) {
                                        board.modifyPlaceStone(playerMove, "X");
                                        whitePiece(getGraphics(), constant * mcX, constant * mcY);
                                    } else {
                                        board.modifyPlaceStone(playerMove, "O");
                                        blackPiece(getGraphics(), constant * mcX, constant * mcY);
                                    }
                                    isWhiteTurn = !isWhiteTurn;
                                    Board.printBoard(board.getBoard());
                                }
                            }
                        }
                        else{
                            //wasn't reading the correct turn for some reason. The value was turning false every time after the call.
                            //Possible fix, remove the isWhite from restartScreen.
                            //Extra comment: This code was working perfectly fine before I pushed.
                            //               Then broken things started to work and this stop working.
                            win(isWhiteTurn);
                        }
                    }
                    else if (gameMode == 2){
                        int mcX = (int) findClosestIntersection(e.getX(), e.getY()).getY();
                        int mcY = (int) findClosestIntersection(e.getX(), e.getY()).getX();
                        int [] playerMove = {mcY - 1,mcX - 1 };


                        if (e.getX() > 37 && e.getX() < 592 && e.getY() > 37 && e.getY() < 592) {
                            if (board.isMovePossible(playerMove)){
                                board.modifyPlaceStone(playerMove, "X");
                                whitePiece(getGraphics(), constant * mcX, constant * mcY);
                                if (Board.hasSomeoneWon(board.getBoard())) win(true);

                                int [] cpuCoords = computerMove();
                                board.modifyPlaceStone(cpuCoords, "O");
                                cpuCoords[0] += 1; cpuCoords[1] += 1;
                                blackPiece(getGraphics(), constant * cpuCoords[1], constant * cpuCoords[0]);
                                if (Board.hasSomeoneWon(board.getBoard())) win(false);
                            }
                        }
                        Board.printBoard(board.getBoard());
                    }
                }
                else{
                    JOptionPane.showMessageDialog(outer, "TIE!", "tie", JOptionPane.INFORMATION_MESSAGE);
                }

            }
            public int [] computerMove (){
                cpuP CPU = new cpuP(1, "O");
                Random rand = new Random();
                int [] ranAns = new int[]{rand.nextInt(14), rand.nextInt(14)};
                while(!board.isMovePossible(ranAns)){
                    ranAns = new int[]{rand.nextInt(14), rand.nextInt(14)};
                }


                //If you want to play against cheat
                int [] cpuCoords = CPU.cheat(board.getBoard());
                return ranAns;
            }
            public void win(boolean w){
                board = new Board();
                board.populateBoard();
                tablero = board.getBoard();
                repaint();
                resetGame();

                String winner = w ? "one" : "two";
                JOptionPane.showMessageDialog(outer, "Player " + winner + " wins!", "Game Over", JOptionPane.INFORMATION_MESSAGE);

                // Reset the board and show the start screen
                repaint();
                inGame = false;
                restartConfirmation = false;
                showStartScreen();
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        }



        public static void main(String[] args) {
            new MyBoardGame().run();
        }
    }
