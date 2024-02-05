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
        //Create the GUI, game board and the icon for use in popup menus
        gameUI = new guiManager(600, 600, this);
        board = new gameBoard(10, 10, 25, this);
        gameIcon =  new ImageIcon(getClass().getResource("/mine.png"));
    }

    public void update(int[][] renderGrid){
        //Passes a given render grid through to the GUI
        gameUI.visualUpdate(renderGrid);
    }

    public void endGame(int endCode){
        //1 = player won the game
        //2 = game over
        String endTitle = "";
        String endMessage = "";
        //Changes the title and message based on the state
        if(endCode == 1){
            endTitle = "Minesweeper: Game complete.";
            endMessage = "You have flagged all the mines! You win!";
        }else{
            endTitle = "Minesweeper: Game over.";
            endMessage = "You hit a mine! Game over.";
        }
        //Displays the choice menu when the game is lost or won
        String[] options = new String[]{"New game", "Custom game", "Quit"};
        int userChoice = JOptionPane.showOptionDialog(
            null,
                endMessage,
                endTitle,
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

    public void actionPerformed(ActionEvent e){
        //The event listener for cell and button clicks from the GUI
        //Calls the appropriate method in either menu bar button, or-
        //-splits the action command up to get the coordinates to call the guess method of the board
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
        String[] options = new String[]{"Easy", "Normal", "Hard", "Very Hard", "Custom settings"};
        int userChoice = JOptionPane.showOptionDialog(
            null,
                "Choose a difficulty for the new game.",
                "Minesweeper: Custom game",
                JOptionPane.DEFAULT_OPTION, 
                JOptionPane.WARNING_MESSAGE,
                gameIcon,
                options,
                options[0]
            ); 

        int desiredMines = 20;
        int desiredX = 10;
        int desiredY = 10;
        switch (userChoice + 1) {
            case 1:
            case 2:
            case 3:
            case 4:
                desiredMines = desiredMines*(userChoice+1);
                createNewGame(desiredX, desiredY, desiredMines);
                break;
            case 5: 
                try {
                    do{desiredX = Integer.parseInt(JOptionPane.showInputDialog("How many tiles wide do you want the grid to be? (Between 2 and 20.)"));}
                    while(!validateCustomGameDimension(desiredX));

                    do{desiredY = Integer.parseInt(JOptionPane.showInputDialog("How many tiles high do you want the grid to be? (Between 2 and 20.)"));}
                    while(!validateCustomGameDimension(desiredY));

                    do{desiredMines = Integer.parseInt(JOptionPane.showInputDialog("How many mines do you want to be placed throughout the grid? (Between 1 and grid width * height)"));}
                    while(!validateCustomGameMines(desiredX, desiredY, desiredMines));

                    createNewGame(desiredX, desiredY, desiredMines);

                } catch (NumberFormatException e) {
                    //Shows a message and drops back to the custom game dialog if incompatible data was entered
                    JOptionPane.showMessageDialog(null, "Custom settings values must be a number.");
                    doCustomGame();
                }
                break;
            default:
                //If user closes out the custom game popup the game over one will be displayed again
                endGame(2);
                break;
        }
    }

    private boolean validateCustomGameDimension(int value){
        //Checks that user input dimensions are within acceptable values
        if(value > 1 && value <= 20){
            return true;
        }else{
            return false;
        }
    }

    private boolean validateCustomGameMines(int x, int y, int mines){
        //Checks that user input mine count is within acceptable values
        int totalCells = x * y;
        if(mines > 0 && mines < totalCells){
            return true;
        }else{
            return false;
        }
    }

    private void doNewGame(){
        //Starts a new game with the default values
        createNewGame(10, 10, 25);
    }

    private void createNewGame(int x, int y, int mines){
        //Make new game with the given settings
        board = new gameBoard(x, y, mines, this);
            
        //Passes a fake starting grid to the GUI so there-
        //- will be something for the player to click on to begin the game
        int[][] fakeStartingGrid = new int[x][y];
            for(int[] innerArray : fakeStartingGrid){
                Arrays.fill(innerArray, -1);
            }
        update(fakeStartingGrid);
        gameUI.resetGuiTimer();
    }

    public void flagCell(int x, int y){
        //Passes the flagged cell coords along to the method in the game board
        board.doFlag(x, y);
    }
}
