import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Timer;

import javax.swing.*;

public class guiManager {
    private int windowWidth;
    private int windowHeight;
    private long gameTimerValue;
    private JFrame gameWindow;
    private gameController controller;

    private ImageIcon mineIcon;
    private ImageIcon flagIcon;
    private Color[] cellColourStages;

    public guiManager(int desiredWindowHeight, int desiredWindowWidth, gameController controller){
        windowWidth = desiredWindowWidth;
        windowHeight = desiredWindowHeight;
        this.controller = controller;

        mineIcon = new ImageIcon(getClass().getResource("/mine.png"));
        flagIcon = new ImageIcon(getClass().getResource("/flag.png"));
        cellColourStages = new Color[]{
            new Color(55, 138, 28), //First surrounding mines colour stage
            new Color(173, 86, 19), //Second surrounding mines colour stage
            new Color(128, 1, 1), //Third surrounding mines colour stage
            new Color(34, 0, 79) //Fourth surrounding mines colour stage
        };

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
                JButton cellButton = new JButton("");
                cellButton.setRolloverEnabled(false);
                switch (renderGrid[x][y]) {
                    case -1:
                        //A hidden cell should look blank so button content is left alone
                        cellButton.setRolloverEnabled(true);
                        break;
                    
                    case -2:
                        //Makes cell button contain an image of a flag
                        cellButton.setIcon(flagIcon);
                        cellButton.setRolloverEnabled(true);
                        break;

                    case -3:
                        //Makes cell button contain image of a mine
                        cellButton.setIcon(mineIcon);
                        break;

                    case 0:
                        //Cells with 0 surrounding mines are empty
                        cellButton.setContentAreaFilled(false);
                        break;

                    default:
                        //Sets the number on the cell button equal to the amount of mines surrounding it
                        int cellNumber = renderGrid[x][y];

                        cellButton.setText("" + cellNumber);
                        cellButton.setContentAreaFilled(false);

                        //Changes the text colour based on how many surrounding mines there are, with four tiers
                        if(cellNumber <= renderGrid[0].length/4){
                            cellButton.setForeground(cellColourStages[0]);
                        }else if(cellNumber <= renderGrid[0].length/2){
                            cellButton.setForeground(cellColourStages[1]);
                        }else if(cellNumber <= (renderGrid[0].length/2)+(renderGrid[0].length/4)){
                            cellButton.setForeground(cellColourStages[2]);
                        }else{
                            cellButton.setForeground(cellColourStages[3]);
                        }
                        break;
                }

                //Cells which cannot be interacted with (discovered 0 cells and revealed other cells)-
                //- will be made so via skipping adding their listeners if this is the case
                if(cellButton.isContentAreaFilled() == true){
                    cellButton.setActionCommand(x + " " + y);
                    cellButton.addActionListener(controller);

                    //Attaches a mouse listener to each button that only responds to right clicks
                    //Duplicating the coordinates variables is needed because they cannot be-
                    //-used in this inner class without being final
                    final int xForFlag = x;
                    final int yForFlag = y;
                    cellButton.addMouseListener(new MouseAdapter(){
                        public void mouseClicked(MouseEvent e){
                            if(SwingUtilities.isRightMouseButton(e)){
                                controller.flagCell(xForFlag, yForFlag);
                            }
                        }
                    });
                };


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

        //Sets up the label, timer object and listener for the game timer
        JLabel timeLabel = new JLabel("Time: ");
        gameTimerValue = java.lang.System.currentTimeMillis(); 
        timeLabel.setBackground(new Color(100, 100, 125));
        timeLabel.setOpaque(true);
        ActionListener timerDisplay = new ActionListener() {
            public void actionPerformed(ActionEvent e){
                long timeSeconds = (java.lang.System.currentTimeMillis() - gameTimerValue)/1000;
                timeLabel.setText("Time: " + timeSeconds / 60 + "m " + timeSeconds % 60 + "s");
            }
        };
        javax.swing.Timer gameTimer = new javax.swing.Timer(1000, timerDisplay);
        gameTimer.start();

        optionsAndInfoBar.add(newgameButton);
        optionsAndInfoBar.add(customgameButton);
        optionsAndInfoBar.add(timeLabel);

        //Making the window display itself
        gameWindow = baseFrame;
        baseFrame.pack();
        baseFrame.setVisible(true);
    }

    public void resetGuiTimer(){
        gameTimerValue = java.lang.System.currentTimeMillis();
    }

}
