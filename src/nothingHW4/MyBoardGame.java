    package nothingHW4;
    import HW2.Board;
    import HW2.cpuP;

    import javax.swing.*;
    import java.awt.*;
    import java.awt.event.*;
    import java.util.Random;

    public class MyBoardGame extends boardDraw {
        final int sizeOfBoard = 15;
        public boolean isWhiteTurn = false;
        public int gameMode = 0;
        private boolean inGame = false;
        private boolean restartConfirmation = false;


        /**
         * Constructor. It sets the size of the board.
         * It creates a new "board" which is what handles the logic behind the scenes.
         * Starts the MouseListener. It is here after IA recommendation. Thank you.
         */
        public MyBoardGame() {
            setPreferredSize(new Dimension(592, 592));
            board = new Board();
            board.populateBoard();
            addMouseListener(new MyMouseListener(this, board)); // Add the MouseListener
            start = new JButton();
            showStartScreen();

        }

        /**
         * This is what happens when you click the "go home" button.
         */
        @Override
        public void start(){
            Object[] options = {"Yes, let me go", "Keep playing"};
            int choice = JOptionPane.showOptionDialog(MyBoardGame.this, "You are about to leave the game.", "Leaving?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (choice == JOptionPane.YES_OPTION){
                inGame = false;
                gameMode = 0;
                showStartScreen();
            }
        }


        /**
         * First things the player sees.
         * You can also get here from the menu bar.
         */
        private void showStartScreen() {
            //Resets everything .
            removeAll();
            resetGame();
            inGame = false;
            //Sets the button to click.
            JButton startButton = new JButton("Start Game");
            //Shows options if clicked.
            startButton.addActionListener(e -> showGameModeOptions());
            add(startButton);
            start.setEnabled(inGame);
            revalidate();
        }
        /**
        Code gets here if the button to restart is clicked and confirmed.
         **/
        private void resetGame() {
            isWhiteTurn = false;
            board.populateBoard();
            removeAll();
            repaint();
        }
        /**
        Method that shows game mode options and the initial "play" button. 
        It holds 3 Radio buttons that are shown after press play.
        Once you choose something it sets the field gameMode. 
        That's how we know what are we playing.  
         **/
        private void showGameModeOptions() {
            removeAll();
            start.setEnabled(false);

            //Initializes options.

            if (!inGame) {
                JRadioButton mode1Button = new JRadioButton("Player vs Opponent");
                JRadioButton mode2Button = new JRadioButton("Player vs CPU");
                JRadioButton mode3Button = new JRadioButton("Online");

                ButtonGroup group = new ButtonGroup();
                group.add(mode1Button);
                group.add(mode2Button);
                group.add(mode3Button);

                JButton playButton = new JButton("Play");

                //We say what happens after we choose.
                playButton.addActionListener(e -> {
                    if (mode1Button.isSelected()) {
                        gameMode = 1;
                    } else if (mode2Button.isSelected()) {
                        gameMode = 2;
                    } else if (mode3Button.isSelected()) {
                        gameMode = 3;
                    }
                    //You have to choose something.
                    else{
                        showGameModeOptions();
                    }

                    inGame = true;
                    removeAll();
                    repaint();
                });

                add(mode1Button);
                add(mode2Button);
                add(mode3Button);
                add(playButton);
            }
            revalidate();
        }

        /**
        Confirmation method that is called after the restart button is pressed or actuated with accelerator.
         **/
        public void restart(){
            int choice = JOptionPane.showConfirmDialog(MyBoardGame.this, "Are you sure you want to restart the game?", "Confirm Restart", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                resetGame();  // Reset the game state
                restartConfirmation = false;
            }
        }

        /**
         * Gets the line that won the game and displays it later.
         * @return Beginning and ending point of winning line.
         */
        public Point [] showWinning (boolean whoWin){
            int [][] highlightArray = board.modifyWinCheck();
            winingLine(getGraphics(), highlightArray, whoWin);
            Point start = new Point(highlightArray[0][0], highlightArray[1][0]);
            Point end = new Point(highlightArray[0][highlightArray[0].length-1], highlightArray[1][highlightArray[0].length-1]);
            System.out.println("("+start.x+"," +start.y+")" + " , " +"("+end.x+"," +end.y+")");

            return new Point[]{start, end};
        }

        /**
         * Graphics.
         * @param g the <code>Graphics</code> object to protect
         */

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (inGame) {
                if (gameMode == 1 || gameMode == 2) {
                    //If here, we know we are playing.
                    //What happens after we start playing is not handle here as it may cause bugs.
                    if (!restartConfirmation) {
                        restartConfirmation = true;
                        board.populateBoard();

                    }
                    drawBoard(g);
                } else if (gameMode == 3) {
                    //Online mode is not yer available.
                    if (!restartConfirmation) {
                        restartConfirmation = true;
                    }
                    g.setColor(Color.BLACK);
                    g.setFont(new Font("Arial Black", Font.BOLD, 20));
                    String message = "Online mode is not available yet :)";
                    int messageWidth = g.getFontMetrics().stringWidth(message);
                    g.drawString(message, getWidth() / 2 - messageWidth / 2, getHeight() / 2);
                }
            }
            else{
                //We draw the beginning screen.
                g.setColor(Color.BLACK);
                g.setFont(new Font("Malgun Gothic", Font.BOLD, 50));
                String welcomeMessage = "오목 놀자"; //"Let's play omok"
                int messageWidth = g.getFontMetrics().stringWidth(welcomeMessage);
                g.drawString(welcomeMessage, getWidth() / 2 - messageWidth / 2, getHeight() / 2);
            }
        }

        /**
         * Class that handles when there is a mouse press and we are playing.
         */

        class MyMouseListener implements MouseListener {
            private MyBoardGame outer; // Reference to the outer class
            private String [][] tablero;
            //Random r = new Random();

            MyMouseListener(MyBoardGame outer, Board board) {
                this.outer = outer;
                tablero = board.getBoard();
            }

            /**
             * We process a mouse click.
             * @param e the event to be processed
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!inGame) return;
                whoIsPlaying(getGraphics(), isWhiteTurn);
                if (Board.isThereMoves(tablero)){
                    int constant = 592 / 16;
                    if (outer.gameMode == 1){
                        //We get here if are playing against another human.

                        //We get their mouse click position.
                        int mcX = (int) findClosestIntersection(e.getX(), e.getY()).getY();
                        int mcY = (int) findClosestIntersection(e.getX(), e.getY()).getX();
                        int [] playerMove = {mcY - 1,mcX - 1 };

                        //We check if it was a valid move.
                        if (board.isMovePossible(playerMove)){
                            if (e.getX() > 37 && e.getX() < 592 && e.getY() > 37 && e.getY() < 592){
                                if (isWhiteTurn) {
                                    board.modifyPlaceStone(playerMove, "X");
                                    whitePiece(getGraphics(), constant * mcX, constant * mcY);
                                    if (Board.hasSomeoneWon(board.getBoard())) win(isWhiteTurn);
                                } else {
                                    board.modifyPlaceStone(playerMove, "O");
                                    blackPiece(getGraphics(), constant * mcX, constant * mcY);
                                    if (Board.hasSomeoneWon(board.getBoard())) win(isWhiteTurn);
                                }
                                isWhiteTurn = !isWhiteTurn;
                            }
                        }
                    }
                    else if (gameMode == 2){
                        //Here we are playing against machine.
                        int mcX = (int) findClosestIntersection(e.getX(), e.getY()).getY();
                        int mcY = (int) findClosestIntersection(e.getX(), e.getY()).getX();
                        int [] playerMove = {mcY - 1,mcX - 1 };

                        //Check if move was valid.
                        if (e.getX() > 37 && e.getX() < 592 && e.getY() > 37 && e.getY() < 592) {
                            if (board.isMovePossible(playerMove)){
                                board.modifyPlaceStone(playerMove, "X");
                                whitePiece(getGraphics(), constant * mcX, constant * mcY);
                                if (Board.hasSomeoneWon(board.getBoard())){
                                    win(true);
                                }

                                //Right now the computer is set to the easiest difficulty.
                                //Easiest = random.
                                //Hard = logic from assigment 2
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
                    board = new Board();
                    board.populateBoard();
                    tablero = board.getBoard();
                    repaint();
                    resetGame();
                    //inGame = false;
                    restartConfirmation = false;
                    //showStartScreen();
                }

            }

            /**
             * Modify the difficulty of the machine.
             * @return move of the computer.
             */
            public int [] computerMove (){
                cpuP CPU = new cpuP(1, "O");
                Random rand = new Random();
                int [] ranAns = new int[]{rand.nextInt(14), rand.nextInt(14)};
                while(!board.isMovePossible(ranAns)){
                    ranAns = new int[]{rand.nextInt(14), rand.nextInt(14)};
                }

                //If you want to play against ca higher difficulty
                int [] cpuCoords = CPU.cheat(board.getBoard());
                return ranAns;
            }

            /**
             * What happens if we win.
             * @param w, flag to check who wins.
             */
            public void win(boolean w){
                Point [] highlightWin = showWinning(w);
                String winner = w ? "Blue" : "Red";
                JOptionPane.showMessageDialog(outer, winner +" player wins!\n Winning line: " + "("+getCharForNumber(highlightWin[0].y + 1)+"," +highlightWin[0].x+")" + " , " +"("+getCharForNumber(highlightWin[1].y + 1)+"," +highlightWin[1].x+")", "Game Over", JOptionPane.INFORMATION_MESSAGE);

                // Reset the board and play again.
                // This could take you back to the startScreen but that was more of a design thing.
                board = new Board();
                board.populateBoard();
                tablero = board.getBoard();
                repaint();
                resetGame();
                //inGame = false;
                restartConfirmation = false;
                //showStartScreen();
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

        /**
         * Shows what is the best move in a specific moment.
         */
        @Override
        public void cheats() {
            cpuP CPU = new cpuP(1, "O");
            int [] cheat = CPU.cheat(board.getBoard());
            JOptionPane.showMessageDialog(this, "Best move: \n"+"("+getCharForNumber(cheat[1]+1)+", "+cheat[0]+")", "Cheat", JOptionPane.INFORMATION_MESSAGE);

        }

        /**
         * main method.
         * @param args could have args to change screen size, but I decided to keep it fixed.
         */

        public static void main(String[] args) {
            new MyBoardGame().run();
        }
    }
