package it.unibo.mvc;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

/**
 * This class is a simple application that clicks a random number on a file.
 *
 * <p>
 * This application does not exploit the model-view-controller pattern, and as
 * such is just to be used to learn the basics, not as a template for your
 * applications.
 */
public class MiniGUI {

    private static final String TITLE = "A very simple GUI application";
    private final Random randomGenerator = new Random();
    private final JFrame frame = new JFrame(TITLE);

    /**
     * Creates a new {@link MiniGUI}.
     */
    public MiniGUI() {

        final JPanel canvas = new JPanel();
        canvas.setLayout(new BorderLayout());
        final JPanel myJPanel = new JPanel();
        myJPanel.setLayout(new BoxLayout(myJPanel, BoxLayout.X_AXIS));
        frame.add(myJPanel);

        final JButton click = new JButton("Print a random number on standard output");
        myJPanel.add(click, BorderLayout.CENTER); //here we add the button to the panel
        frame.setContentPane(canvas);
        frame.setContentPane(myJPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final JTextField result = new JTextField();
        canvas.add(result, BorderLayout.NORTH);
        frame.add(canvas);
        /*
         * Handlers
         */
        click.addActionListener(new ActionListener() {
            public int action() {
                return randomGenerator.nextInt();
            }

            @Override
            public void actionPerformed(final ActionEvent e) {
                final int value = action(); 
                result.setText(Integer.toString(value));
                System.out.println(value); // NOPMD
            }
        });
    }

    private void display() {
        /*
         * Instead of appearing at (0,0), upper left corner of the screen, this
         * flag makes the OS window manager take care of the default positioning
         * on screen. Results may vary, but it is generally the best choice.
         */
        frame.setLocationByPlatform(true);
        /*
         * Resize the frame to minimum size
         */
        frame.pack(); //WAS ALREADY USED
        /*
         * OK, ready to pull the frame onscreen
         */
        frame.setVisible(true);
    }

    /**
     * Launches the application.
     *
     * @param args
     *            ignored
     */
    public static void main(final String... args) {
        new MiniGUI().display();
    }

}
