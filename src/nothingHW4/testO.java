package nothingHW4;

import java.awt.*;

public class testO extends modifyApplet{
    public void paintComponent (Graphics g){
        g.setColor(new Color(1,1,1));
        g.fillRect(0,0,592,592);
        if (screen == 2){
            g.setColor(new Color(255, 255, 255));
            g.fillRect(0,0,592,592);
        }
    }
    public static void main (String [] args){
        new testO().run();
    }
}
