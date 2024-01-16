public class App {
    public static void main(String[] args) throws Exception{
        gameBoard testBoard = new gameBoard(10, 10, 10);
        testBoard.testPrint(0);
        testBoard.testPrint(1);

        guiManager testGui = new guiManager(600, 600);
    }
}
