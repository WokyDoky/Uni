    package nothingHW4;

    import HW2.Board;
    import HW2.cpuP;

    import javax.swing.*;
    import java.awt.*;
    import java.awt.event.*;
    import java.util.ArrayList;
    import java.util.Random;

    public class MyBoardGame extends JPanel {
        final int sizeOfBoard = 15;
        boolean isWhiteTurn = false;
        boolean won = false;
        Point lastStonePosition;
        public int gameMode = 0;
        private boolean inGame = false;
        private boolean restartConfirmation = false;
        private final int constant = 592 / 16;

        HW2.Board board;

        public MyBoardGame() {
            setPreferredSize(new Dimension(592, 592));
            board = new Board();
            board.populateBoard();
            addMouseListener(new MyMouseListener(this, board)); // Add the MouseListener
            showStartScreen();
        }

        private void showStartScreen() {
            removeAll();
            JButton startButton = new JButton("Start Game");
            startButton.addActionListener(e -> showGameModeOptions());

            add(startButton);
            revalidate();
        }
        private void resetGame() {
            isWhiteTurn = false;
            won = false;
            lastStonePosition = null;
            board.populateBoard();
            Board.printBoard(board.getBoard());
            removeAll();
            MyMouseListener mouseListener = new MyMouseListener(this, board);
            addMouseListener(mouseListener);  // Reattach the MouseListener
            repaint();
        }
        private void showGameModeOptions() {
            removeAll();

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
                    repaint();
                });

                add(mode1Button);
                add(mode2Button);
                add(mode3Button);
                add(playButton);
            }

            revalidate();
        }

        private void showRestartConfirmation() {
            removeAll();
            JButton menuButton = new JButton("Menu");

            menuButton.addActionListener(e -> {
                inGame = false;
                restartConfirmation = false;
                resetGame();  // Reset the game state
                showGameModeOptions();
            });

            if (restartConfirmation) {
                JButton restartButton = new JButton("Restart");

                restartButton.addActionListener(e -> {
                    int choice = JOptionPane.showConfirmDialog(MyBoardGame.this, "Are you sure you want to restart the game?", "Confirm Restart", JOptionPane.YES_NO_OPTION);
                    if (choice == JOptionPane.YES_OPTION) {
                        resetGame();  // Reset the game state
                        inGame = false;
                        restartConfirmation = false;
                        showStartScreen();
                    }
                });
                add(restartButton);
            }

            add(menuButton);
            revalidate();
        }

        private void init() {
            // Create a JPanel to hold the buttons
            JPanel buttonPanel = new JPanel();

            // Add the buttons to the JPanel
            buttonPanel.add(new JButton("Menu"));
            buttonPanel.add(new JButton("Restart"));

            // Add the button panel to the BorderLayout container using the `BorderLayout.NORTH` constant
            add(buttonPanel, BorderLayout.NORTH);

            // Add the board to the BorderLayout container using the `BorderLayout.CENTER` constant
            add(this, BorderLayout.CENTER);
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
                    g.setFont(new Font("Arial", Font.BOLD, 20));
                    String message = "Online mode is not available yet :)";
                    int messageWidth = g.getFontMetrics().stringWidth(message);
                    g.drawString(message, getWidth() / 2 - messageWidth / 2, getHeight() / 2);
                }
            }
        }

        public void drawBoard(Graphics g) {
            g.setColor(Color.orange);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(Color.BLACK);
            for (int i = 0; i < 16; i++) {
                g.drawLine(i * (getHeight() / 16), 0, i * (getHeight() / 16), getHeight());
            }
            for (int j = 0; j < 16; j++) {
                g.drawLine(0, j * (getWidth() / (sizeOfBoard + 1)), getWidth(), j * (getWidth() / (sizeOfBoard + 1)));
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
                if (!Board.hasSomeoneWon(tablero)){
                    if (outer.gameMode == 1){
                        int mcX = (int) findClosestIntersection(e.getX(), e.getY()).getY();
                        int mcY = (int) findClosestIntersection(e.getX(), e.getY()).getX();
                        int [] playerMove = {mcY - 1,mcX - 1 };

                        if (board.isMovePossible(playerMove)) {
                            if (board.isMovePossible(playerMove)) {
                                if (e.getX() > 37 && e.getX() < 592 && e.getY() > 37 && e.getY() < 592) {
                                    if (isWhiteTurn) {
                                        System.out.println("HERE " + board.isMovePossible(playerMove));
                                        board.modifyPlaceStone(playerMove, "X");
                                        whitePiece(getGraphics(), constant * mcX, constant * mcY);
                                    } else {
                                        board.modifyPlaceStone(playerMove, "O");
                                        blackPiece(getGraphics(), constant * mcX, constant * mcY);
                                    }
                                    isWhiteTurn = !isWhiteTurn;
                                    lastStonePosition = new Point(0, 0);
                                    Board.printBoard(tablero);
                                }
                            }
                        }
                    }
                    else if (gameMode == 2){
                        HW2.cpuP cpu = new cpuP(1,"O");
                        int mcX = (int) findClosestIntersection(e.getX(), e.getY()).getY();
                        int mcY = (int) findClosestIntersection(e.getX(), e.getY()).getX();
                        int [] playerMove = {mcY - 1,mcX - 1 };
                        if (board.isMovePossible(playerMove)){
                            if (e.getX() > 37 && e.getX() < 592 && e.getY() > 37 && e.getY() < 592) {
                                Board.printBoard(board.getBoard());
                                board.modifyPlaceStone(playerMove, "X");

                                int [] computerMove = {cpu.cheat(board.getBoard())[0], cpu.cheat(board.getBoard())[1]};
                                board.modifyPlaceStone(computerMove, "O");
                                whitePiece(getGraphics(), constant * mcX, constant * mcY);
                                blackPiece(getGraphics(), constant*(cpu.cheat(board.getBoard())[0] + 1), constant*(cpu.cheat(board.getBoard())[1]) + 1);
                                //why isn't it working if it is in the console part??
                                System.out.println(cpu.cheat(board.getBoard())[0] + " - " + cpu.cheat(board.getBoard())[1]);
                            }
                        }

                    }
                    else if (gameMode == 3){
                        System.out.println("Game mode is not available :)");
                    }
                }
                else{
                    board = new Board();
                    board.populateBoard();
                    tablero = board.getBoard();
                    repaint();
                    resetGame();

                    System.out.println("you have won!");
                    String winner = isWhiteTurn ? "one" : "two";
                    isWhiteTurn = !isWhiteTurn;
                    JOptionPane.showMessageDialog(outer, "Player " + winner + " wins!", "Game Over", JOptionPane.INFORMATION_MESSAGE);

                    // Reset the board and show the start screen
                    board.populateBoard();
                    tablero = board.getBoard();
                    repaint();
                    inGame = false;
                    restartConfirmation = false;
                    showStartScreen();
                }

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

        public Point findClosestIntersection(int x, int y) {
            // Calculate the distance from the user's click to each intersection.
            ArrayList<Double> distances = new ArrayList<>();
            for (int i = 0; i < 16; i++) {
                for (int j = 0; j < 16; j++) {
                    int intersectionX = i * 37;
                    int intersectionY = j * 37;
                    double distance = Math.sqrt((x - intersectionX) * (x - intersectionX) + (y - intersectionY) * (y - intersectionY));
                    distances.add(distance);
                }
            }

            // Find the intersection with the smallest distance.
            int minIndex = 0;
            double minDistance = distances.get(0);
            for (int i = 1; i < distances.size(); i++) {
                if (distances.get(i) < minDistance) {
                    minIndex = i;
                    minDistance = distances.get(i);
                }
            }

            // Return the coordinates of the nearest intersection.
            return new Point(minIndex % 16, minIndex / 16);
        }

        void whitePiece(Graphics g, int x, int y) {
            g.setColor(new Color(255, 255, 255));
            g.fillOval(x - 16, y - 16, 32, 32);
            g.setColor(new Color(47, 127, 141));
            g.fillOval(x - 8, y - 8, 16, 16);
        }

        void blackPiece(Graphics g, int x, int y) {
            g.setColor(new Color(0, 0, 0));
            g.fillOval(x - 16, y - 16, 32, 32);
            g.setColor(new Color(108, 0, 0));
            g.fillOval(x - 8, y - 8, 16, 16);
        }

        public static void main(String[] args) {
            JFrame frame = new JFrame("My Board Game");
            MyBoardGame gamePanel = new MyBoardGame();
            frame.add(gamePanel);
            frame.pack();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        }
    }
