import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class guiController {
    private int windowWidth;
    private int windowHeight;
    private JFrame gameWindow;

    public guiController(int desiredWindowHeight, int desiredWindowWidth){
        windowWidth = desiredWindowWidth;
        windowHeight = desiredWindowHeight;
        javax.swing.SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                setupMainWindow();
            }
        });
    }

    public void visualUpdate(){
        //gameWindow.getContentPane();
    }

    private void setupMainWindow(){
        //Settin up window frame
        JFrame baseFrame = new JFrame("Minesweeper");
        baseFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Setting the window icon
        ImageIcon mineIcon = new ImageIcon(getClass().getResource("/mine.png"));

        baseFrame.setIconImage(mineIcon.getImage());

        //Setting up a content pane inside the frame
        JPanel frameContents = new JPanel(new BorderLayout());

        baseFrame.setContentPane(frameContents);

        //Setting up menu bar for info display and options buttons
        JMenuBar optionsAndInfoBar =  new JMenuBar();
        optionsAndInfoBar.setOpaque(true);
        optionsAndInfoBar.setBackground(new Color(100, 100, 125));
        int optionsAndInfoBarHeight = (int)Math.round(windowHeight*0.1);
        optionsAndInfoBar.setPreferredSize(new Dimension(windowWidth, optionsAndInfoBarHeight));

        baseFrame.setJMenuBar(optionsAndInfoBar);

        //Setting up the options buttons and info labels
        JButton newgameButton = new JButton("New Game");
        newgameButton.setActionCommand("new game");

        JButton customgameButton = new JButton("Custom Game");
        customgameButton.setActionCommand("custom game");

        JLabel timeLabel = new JLabel("Time: ");
        timeLabel.setOpaque(true);

        optionsAndInfoBar.add(newgameButton);
        optionsAndInfoBar.add(customgameButton);
        optionsAndInfoBar.add(timeLabel);

        //Adding a label to act as the background colour
        //JLabel blankBackground = new JLabel();
        //blankBackground.setOpaque(true);
        //blankBackground.setBackground(new Color(100, 100, 150));
        //int blankBackgroundHeight = (int)Math.round(windowHeight*0.9);
        //blankBackground.setPreferredSize(new Dimension(windowWidth, blankBackgroundHeight));

        //baseFrame.getContentPane().add(blankBackground);

        //Making the window display
        gameWindow = baseFrame;
        baseFrame.pack();
        baseFrame.setVisible(true);
    }

}
