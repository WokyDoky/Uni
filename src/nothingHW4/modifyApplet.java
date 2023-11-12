package nothingHW4;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;

public class modifyApplet extends JPanel {

    private static final int DEFAULT_WIDTH = 592;
    private static final int DEFAULT_HEIGHT = 592;
    private final Map<String, String> parameters = new HashMap<>();

    public int screen = 0;
    JFrame frame;


    /** Create a new instance and show it in a frame. */
    protected modifyApplet() {
        this(new String[0]);
    }

    /** Create a new instance and show it in a frame.
     * The <code>params</code> parameter behaves like Applet parameters
     * and can be accessed by calling the {@link #getParameter(String)} method.
     * The <code>params</code> parameter is strings of name-value pairs, e.g.,
     *
     * <pre>
     * width=300 height=400 color=red
     * </pre>
     */
    protected modifyApplet(String[] params) {
        parseParameters(params);
    }

    /** Called after creation of this NoApplet. */
    public void init() {
        if (screen == 1){
            menu();
        }
    }
    public void menu (){
        JButton option1 = createButton("human v human", true);
        frame.add(option1);
        option1.addActionListener(e -> {
            screen = 2;
        });
    }

    /** Called when the start button is clicked. */
    public void start() {
        screen = 1;
        init();
    }

    /** Called when this app is closed. */
    public void destroy() {
        System.out.println("Thank you for playing!");
    }

    /** Return the value of the named parameter or null if the such
     * parameter is defined. */
    public String getParameter(String name) {
        return parameters.get(name);
    }


    /** Display the given message in the status window. */

    /** Parse the given parameters, each of the form: <code>name=value</code>. */
    private void parseParameters(String[] params) {
        for (String param: params) {
            StringTokenizer tokens = new StringTokenizer(param, "=");
            if (tokens.hasMoreTokens()) {
                String name = tokens.nextToken().trim().toLowerCase();
                String value = tokens.hasMoreTokens() ? tokens.nextToken().trim() : null;
                parameters.put(name, value);
            }
        }
        int width = parseInt(getParameter("width"), DEFAULT_WIDTH);
        int height = parseInt(getParameter("height"), DEFAULT_HEIGHT);
        setPreferredSize(new Dimension(width, height));
    }

    /** Parse an int value and return it or the default value if the parsed value
     * is negative or a parsing error is encountered.
     */
    private static int parseInt(String value, int defaultValue) {
        try {
            int parsedValue = Integer.parseInt(value);
            return parsedValue <= 0 ? defaultValue : parsedValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /** Create a button with the given label. */
    private JButton createButton(String label, boolean enabled) {
        JButton button = new JButton(label);
        button.setFocusable(false);
        button.setEnabled(enabled);
        return button;
    }

    /** Create a UI consisting of start and stop buttons. */
    private JPanel createUI() {

        JButton start = createButton("Start Game", true);
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

    /** Show this NoApplet in a frame with control buttons. */
    public void run() {
        frame = new JFrame();
        frame.setContentPane(createUI());
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {

                destroy();
            }
        });
        frame.setTitle(getClass().getSimpleName());
        frame.setResizable(false);
        frame.setVisible(true);
        init();
        start();
    }

    public static void main(String[] args) {
        new nothingHW4.modifyApplet(args).run();
    }
}