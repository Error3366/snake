package org.cis1200.snake;

// imports necessary libraries for Java swing

import javax.swing.*;
import java.awt.*;

/**
 * Game Main class that specifies the frame and widgets of the GUI
 */
public class RunSnake implements Runnable {
    public void run() {
        // NOTE : recall that the 'final' keyword notes immutability even for
        // local variables.

        final JFrame intructions = new JFrame();
        JOptionPane.showMessageDialog(
                intructions,
                """
                        Today, you are going to be an official SNEK BOI!\s
                        
                        You can move using the arrow keys. Your head is represented by the
                        green square, and you will want to be eating the cyan/blue/yellow
                        squares to get bigger.\s
                        
                        The cyan squares can only spawn on the center.\s
                        The blue squares can only spawn on the sides.\s
                        The yellow squares can only spawn on the corners.\s
                        
                        There is a 2/5 chance to get a cyan and blue, and a
                        1/5 chance to get a yellow.\s
                        
                        You can also save/load the game using the provided button!\s
                        Although, remember that you cannot save a snek that is already ded T_T"""
        );

        // Top-level frame in which game components live.
        // Be sure to change "TOP LEVEL FRAME" to the name of your game
        final JFrame frame = new JFrame("SNEK BOI");
        frame.setLocation(400, 50);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Running...");
        status_panel.add(status);

        // Main playing area
        final GameCourt court = new GameCourt(status);
        frame.add(court, BorderLayout.CENTER);

        // Reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        // Note here that when we add an action listener to the reset button, we
        // define it as an anonymous inner class that is an instance of
        // ActionListener with its actionPerformed() method overridden. When the
        // button is pressed, actionPerformed() will be called.
        final JButton reset = new JButton("Reset");
        reset.addActionListener(e -> court.reset());
        control_panel.add(reset);

        final JButton save = new JButton("Save");
        save.addActionListener(e -> court.setPlaying(true));
        save.addActionListener(e -> court.save());
        control_panel.add(save);

        final JButton load = new JButton("Load");
        load.addActionListener(e -> court.load());
        control_panel.add(load);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start game
        court.reset();
    }
}