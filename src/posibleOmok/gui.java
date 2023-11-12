//package posibleOmok;
//
//import connect4.Grid;
//
//import javax.swing.*;
//import javax.swing.border.LineBorder;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//
//public class gui {
//    private JFrame frame;
//    private JLabel[][] slots;
//    private JButton[] buttons;
//    //variables used in grid
//    private int xsize = 7;
//    private int ysize = 6;
//    private boolean hasWon = false;
//    private boolean hasDraw = false;
//    private boolean quit = false;
//    private boolean newGame = false;
//    public gui() {
//
//        frame = new JFrame("connect four");
//
//        JPanel panel = (JPanel) frame.getContentPane();
//        panel.setLayout(new GridLayout(xsize, ysize + 1));
//
//        slots = new JLabel[xsize][ysize];
//        buttons = new JButton[xsize];
//
//        for (int i = 0; i < xsize; i++) {
//            buttons[i] = new JButton("" + (i + 1));
//            buttons[i].setActionCommand("" + i);
//            buttons[i].addActionListener(
//                    new ActionListener() {
//
//                        public void actionPerformed(ActionEvent e) {
//                            int a = Integer.parseInt(e.getActionCommand());
//                            int y = my_grid.find_y(a);//check for space in collumn
//                            if (y != -1) {
//                                //sets a place to current player
//                                if (my_logic.set_and_check(a, y, currentPlayer)) {
//                                    hasWon = true;
//                                } else if (my_logic.draw_game()) {//checks for drawgame
//                                    hasDraw = true;
//                                } else {
//                                    //change player
//                                    currentPlayer = my_grid.changeplayer(currentPlayer, 2);
//                                    frame.setTitle("connect four - player " + currentPlayer + "'s turn");
//                                }
//                            } else {
//                                JOptionPane.showMessageDialog(null, "choose another one", "column is filled", JOptionPane.INFORMATION_MESSAGE);
//                            }
//                        }
//                    });
//            panel.add(buttons[i]);
//        }
//        for (int column = 0; column < ysize; column++) {
//            for (int row = 0; row < xsize; row++) {
//                slots[row][column] = new JLabel();
//                slots[row][column].setHorizontalAlignment(SwingConstants.CENTER);
//                slots[row][column].setBorder(new LineBorder(Color.black));
//                panel.add(slots[row][column]);
//            }
//        }
//
//        //jframe stuff
//        frame.setContentPane(panel);
//        frame.setSize(
//                700, 600);
//        frame.setVisible(
//                true);
//        frame.setLocationRelativeTo(null);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//    }
//}
