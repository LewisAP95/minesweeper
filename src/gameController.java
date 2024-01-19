public class gameController {
    private guiManager gameUI;
    private gameBoard board;
    
    public gameController(){
        gameUI = new guiManager(600, 600);
        board = new gameBoard();
        board.registerController(this);
    }

    public void update(int[][] renderGrid){
        gameUI.visualUpdate(renderGrid);
    }

    public void update(int errorCode){
        //1 = out of grid bounds
        //2 = game over
    }
}
