import java.util.concurrent.ThreadLocalRandom;

public class gameBoard {
    private int xSize;
    private int ySize;
    private int mineCount;
    private cell[][] cellGrid;

    public gameBoard(int xSize, int ySize, int mineCount){
        this.xSize = xSize;
        this.ySize = ySize;
        this.mineCount = mineCount;

        cellGrid = generateNewGrid();
    }

    private cell[][] generateNewGrid(){
        cell[][] newGrid = new cell[xSize][ySize];

        for(int x = 0; x < xSize; x++){
            for(int y = 0; y < ySize; y++){
                newGrid[x][y] = new cell();
            }
        }

        return layMines(newGrid);
    }

    private cell[][] layMines(cell[][] newGrid){
        for(int minesPlaced = 0; minesPlaced < mineCount; minesPlaced++){
            int randX = ThreadLocalRandom.current().nextInt(0, xSize);
            int randY = ThreadLocalRandom.current().nextInt(0, ySize);
            newGrid[randX][randY].setMineStatus(true);
        }

        return newGrid;
    }

    private cell[][] scanForNearbyMineCount(cell[][] newGrid){
        for(int x = 0; x < xSize; x++){
            for(int y = 0; y < ySize; y++){
                int minesFound = 0;
                int left = x - 1;
                int right = x + 1;
                int up = y + 1;
                int down = y - 1;
                //upper left: x-1 & y+1
                //upper right: x+1 & y+1
                //lower left: x-1 & y-1
                //lower right: x+1 & y-1
            }
        }

        return newGrid;
    }
}
