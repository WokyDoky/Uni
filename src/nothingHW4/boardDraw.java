package nothingHW4;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class boardDraw extends JPanel {
    final int sizeOfBoard = 15; //This can be added else where if the option to create different size boards is given.

    JButton start;
    JButton restart;
    Point lastStonePosition;
    public void drawBoard(Graphics g){
        g.setColor(Color.orange);
        g.fillRect(0,0,getWidth(),getHeight());
        g.setColor(Color.BLACK);
        for (int i = 0; i < 16; i++){
            g.drawLine(i*(getHeight()/16),0,i*(getHeight()/16),getHeight());
        }
        for (int j = 0; j < 16; j++){
            g.drawLine(0,j*(getWidth()/(sizeOfBoard+1)),getWidth(),j*(getWidth()/(sizeOfBoard+1)));
        }
    }
    void whitePiece(Graphics g, int x, int y) {
        g.setColor(new Color(255, 255, 255));
        g.fillOval(x - 16, y - 16, 32, 32);
        g.setColor(new Color(47, 127, 141));
        g.fillOval(x - 8, y - 8, 16, 16);
    }
    void blackPiece(Graphics g, int x, int y){
        g.setColor(new Color(0, 0, 0));
        g.fillOval(x - 16, y - 16, 32, 32);
        g.setColor(new Color(108, 0, 0));
        g.fillOval(x - 8, y - 8, 16, 16);
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
    public void run(){
        JFrame frame = new JFrame("My Board Game");
        MyBoardGame gamePanel = new MyBoardGame();
        frame.setResizable(false);
        frame.add(gamePanel);
        //frame.setContentPane(createUI());
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    private JButton createButton(String label, boolean enabled) {
        JButton button = new JButton(label);
        button.setFocusable(false);
        button.setEnabled(enabled);
        return button;
    }
    public void start(){
        //override
    }
    public void restart(){
        //override
    }

    /** Create a UI consisting of start and stop buttons. */
    private JPanel createUI() {

        start = createButton("Start Game", false);
        start.addActionListener(e -> {
            start();
        });
        JPanel control = new JPanel();
        control.setLayout(new FlowLayout());
        control.add(start);

        JPanel root = new JPanel();
        root.setLayout(new BorderLayout());
        root.add(control, BorderLayout.NORTH);
        root.add(this, BorderLayout.CENTER);
        return root;
    }
}
