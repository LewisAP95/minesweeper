public class gameController {
    
    public gameController(){
        guiManager gameUI = new guiManager(600, 600);
        gameBoard board = new gameBoard(10, 10, 25);
        board.register(this);
    }

    public void update(int[][] renderGrid){

    }

    public void update(int errorCode){

    }
}
