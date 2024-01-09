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

    public void testPrint(){
        for(int x = 0; x < xSize; x++){
            for(int y = 0; y < ySize; y++){
                System.out.print(cellGrid[x][y].getNearbyMines());
            }
            System.out.println();
        }
    }

    private cell[][] generateNewGrid(){
        cell[][] newGrid = new cell[xSize][ySize];

        for(int x = 0; x < xSize; x++){
            for(int y = 0; y < ySize; y++){
                newGrid[x][y] = new cell();
            }
        }

        return scanForNearbyMineCount(layMines(newGrid));
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

                //Left, upper left and lower left
                if(left >= 0){
                    if(newGrid[left][y].getMineStatus() == true){
                        minesFound++;
                    }
                    if(up < ySize - 1){
                        if(newGrid[left][up].getMineStatus() == true){
                            minesFound++;
                        }
                    }
                    if(down >= 0){
                        if(newGrid[left][down].getMineStatus() == true){
                            minesFound++;
                        }
                    }
                }

                //Right, upper right and lower right
                if(right < xSize - 1){
                    if(newGrid[right][y].getMineStatus() == true){
                        minesFound++;
                    }
                    if(up < ySize - 1){
                        if(newGrid[right][up].getMineStatus() == true){
                            minesFound++;
                        }
                    }
                    if(down >= 0){
                        if(newGrid[right][down].getMineStatus() == true){
                            minesFound++;
                        }
                    }
                }

                //Up
                if(up < ySize - 1){
                    if(newGrid[x][up].getMineStatus() == true){
                        minesFound++;
                    }
                }

                //Down
                if(down >= 0){
                    if(newGrid[x][down].getMineStatus() == true){
                        minesFound++;
                    }
                }

                //Set the amount of discovered mines
                newGrid[x][y].setNearbyMines(minesFound);
            }
        }

        return newGrid;
    }
}
