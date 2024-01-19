import java.awt.*;
import java.util.Arrays;

import javax.swing.*;

public class guiManager {
    private int windowWidth;
    private int windowHeight;
    private JFrame gameWindow;
    private gameController controller;

    public guiManager(int desiredWindowHeight, int desiredWindowWidth, gameController controller){
        windowWidth = desiredWindowWidth;
        windowHeight = desiredWindowHeight;
        this.controller = controller;
        javax.swing.SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                setupMainWindow();

                //Inserts a fake grid at first run so the player can click a cell and immediately begin playing
                int[][] fakeStartingGrid = new int[10][10];
                for(int[] innerArray : fakeStartingGrid){
                    Arrays.fill(innerArray, -1);
                }
                visualUpdate(fakeStartingGrid);
                }
        });
    }

    public void visualUpdate(int[][] renderGrid){
        gameWindow.getContentPane().removeAll();
        gameWindow.getContentPane().setLayout(new GridLayout(renderGrid.length, renderGrid[0].length));
        //renderGrid status list:
        // -1 for cells not yet revealed
        // -2 for flagged cells
        // All else is just the number of surrounding mines
        for(int x = 0; x < renderGrid.length; x++){
            for(int y = 0; y < renderGrid[0].length; y++){
                String buttonContent = "";
                switch (renderGrid[x][y]) {
                    case -1:
                        buttonContent = "-";
                        break;
                    
                    case -2:
                        buttonContent = "F";
                        break;

                    default:
                        buttonContent = "" + renderGrid[x][y];
                        break;
                }
                JButton cellButton = new JButton(buttonContent);
                cellButton.setActionCommand(x + " " + y);
                cellButton.addActionListener(controller);
                gameWindow.getContentPane().add(cellButton);
            }
        }

        //Set the new preferred size to whatever the current size is, to keep any user resizing active
        gameWindow.setPreferredSize(new Dimension(gameWindow.getWidth(), gameWindow.getHeight()));
        gameWindow.pack();
    }

    private void setupMainWindow(){
        //Setting up window frame
        JFrame baseFrame = new JFrame("Minesweeper");
        baseFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Setting the window icon
        ImageIcon mineIcon = new ImageIcon(getClass().getResource("/mine.png"));

        baseFrame.setIconImage(mineIcon.getImage());

        //Setting up a content pane inside the frame
        JPanel frameContents = new JPanel(new GridLayout());
        int frameContentsHeight = (int)Math.round(windowHeight*0.9);
        frameContents.setPreferredSize(new Dimension(frameContentsHeight, windowWidth));

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
        newgameButton.addActionListener(controller);

        JButton customgameButton = new JButton("Custom Game");
        customgameButton.setActionCommand("custom game");
        customgameButton.addActionListener(controller);

        JLabel timeLabel = new JLabel("Time: ");
        timeLabel.setOpaque(true);

        JLabel difficultyLabel = new JLabel("Difficulty: Normal");
        difficultyLabel.setOpaque(true);

        JLabel minesLabel = new JLabel("Mines: 25");
        minesLabel.setOpaque(true);

        optionsAndInfoBar.add(newgameButton);
        optionsAndInfoBar.add(customgameButton);
        optionsAndInfoBar.add(timeLabel);
        optionsAndInfoBar.add(difficultyLabel);
        optionsAndInfoBar.add(minesLabel);

        //Adding a label to act as the background colour
        //JLabel blankBackground = new JLabel();
        //blankBackground.setOpaque(true);
        //blankBackground.setBackground(new Color(100, 100, 150));
        //int blankBackgroundHeight = (int)Math.round(windowHeight*0.9);
        //blankBackground.setPreferredSize(new Dimension(windowWidth, blankBackgroundHeight));

        //baseFrame.getContentPane().add(blankBackground);

        //Making the window display itself
        gameWindow = baseFrame;
        baseFrame.pack();
        baseFrame.setVisible(true);
    }

}
