public class gameController {
    private guiManager gameUI;
    private gameBoard board;
    
    public gameController(){
        guiManager gameUI = new guiManager(600, 600);
        gameBoard board = new gameBoard(10, 10, 25);
        board.register(this);
    }

    public void update(int[][] renderGrid){
        gameUI.visualUpdate(renderGrid);
    }

    public void update(int errorCode){
        //1 = out of grid bounds
        //2 = game over
    }
}
