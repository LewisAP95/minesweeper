import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class gameController implements ActionListener{
    private guiManager gameUI;
    private gameBoard board;
    
    public gameController(){
        gameUI = new guiManager(600, 600, this);
        board = new gameBoard(10, 10, 25, this);
    }

    public void update(int[][] renderGrid){
        gameUI.visualUpdate(renderGrid);
    }

    public void update(int errorCode){
        //1 = out of grid bounds
        //2 = game over
        if(errorCode == 1){
            //Insert exception for out of bounds guess
        }else{
            //Game over code will go here
            System.out.println("X| MINE CELL CLICKED |X");
        }
    }

    public void actionPerformed(ActionEvent e){
        if(e.getActionCommand() == "new game"){
            //Make new game with default settings
            board = new gameBoard(10, 10, 25, this);
            
            //Passes a fake starting grid to the GUI so there-
            //- will be something for the player to click on to begin the game
            int[][] fakeStartingGrid = new int[10][10];
                for(int[] innerArray : fakeStartingGrid){
                    Arrays.fill(innerArray, -1);
                }
            update(fakeStartingGrid);
        }else if(e.getActionCommand() == "custom game"){
            //Custom game menu popup
        }else{
            System.out.println(e.getActionCommand());
            String[] guessCoords = e.getActionCommand().split(" ");
            board.makeGuess(Integer.parseInt(guessCoords[0]), Integer.parseInt(guessCoords[1]));
        }
    }

    public void flagCell(int x, int y){
        board.doFlag(x, y);
    }
}
