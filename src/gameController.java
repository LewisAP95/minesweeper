import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class gameController implements ActionListener{
    private guiManager gameUI;
    private gameBoard board;
    private ImageIcon gameIcon;
    
    public gameController(){
        gameUI = new guiManager(600, 600, this);
        board = new gameBoard(10, 10, 25, this);
        gameIcon =  new ImageIcon(getClass().getResource("/mine.png"));
    }

    public void update(int[][] renderGrid){
        gameUI.visualUpdate(renderGrid);
    }

    public void update(int errorCode){
        //1 = out of grid bounds
        //2 = game over
        if(errorCode == 1){
            //Throw exception for out of bounds guess
        }else{
            //Game over code will go here
            String[] options = new String[]{"New game", "Custom game", "Quit"};
            int userChoice = JOptionPane.showOptionDialog(
                null,
                 "You hit a mine! Game over!",
                  "Minesweeper: Game over",
                  JOptionPane.DEFAULT_OPTION, 
                  JOptionPane.WARNING_MESSAGE,
                  gameIcon,
                  options,
                  options[0]
                ); 

            if(userChoice == 0){
                //First button is the new game button
                doNewGame();
            }else if(userChoice == 1){
                //Second button is the custom game button
                doCustomGame();
            }else if(userChoice == 2){
                //Quit game
                System.exit(0);
            }else if(userChoice == JOptionPane.CLOSED_OPTION){
                //Also quit if user tries to close the window, to prevent cheating
                System.exit(0);
            }
        }
    }

    public void actionPerformed(ActionEvent e){
        if(e.getActionCommand() == "new game"){
            doNewGame();
        }else if(e.getActionCommand() == "custom game"){
            doCustomGame();
        }else{
            String[] guessCoords = e.getActionCommand().split(" ");
            board.makeGuess(Integer.parseInt(guessCoords[0]), Integer.parseInt(guessCoords[1]));
        }
    }

    private void doCustomGame(){
        //Custom game menu popup
    }

    private void doNewGame(){
        //Make new game with default settings
        board = new gameBoard(10, 10, 25, this);
            
        //Passes a fake starting grid to the GUI so there-
        //- will be something for the player to click on to begin the game
        int[][] fakeStartingGrid = new int[10][10];
            for(int[] innerArray : fakeStartingGrid){
                Arrays.fill(innerArray, -1);
            }
        update(fakeStartingGrid);
    }

    public void flagCell(int x, int y){
        board.doFlag(x, y);
    }
}
