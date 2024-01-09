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

        cellGrid = scanForNearbyMineCount(layMines(generateNewGrid()));
    }

    public void testPrint(int a){
        for(int x = 0; x < xSize; x++){
            for(int y = 0; y < ySize; y++){
                if(a == 0){
                    System.out.print(cellGrid[x][y].getNearbyMines());
                }else if(a == 1){
                    System.out.print(getRenderGrid()[x][y]);
                }
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

        return newGrid;
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
        int[] scanDirs = {-1, 0, 1};
        for(int x = 0; x < xSize; x++){
            for(int y = 0; y < ySize; y++){
                int minesFound = 0;

                //Work through all modified combinations of the X, Y indices
                for(int scanX = 0; scanX < scanDirs.length; scanX++){
                    for(int scanY = 0; scanY < scanDirs.length; scanY++){
                        int modifiedIndexX = x + scanDirs[scanX];
                        int modifiedIndexY = y + scanDirs[scanY];
                        if((modifiedIndexX >= 0 && modifiedIndexX < xSize - 1) && (modifiedIndexY >= 0 && modifiedIndexY < ySize - 1)){
                            if(newGrid[modifiedIndexX][modifiedIndexY].getMineStatus() == true){
                                if(modifiedIndexX != 0 && modifiedIndexY !=  0){
                                    //Skips the count if both modifiers are 0, mine tiles shouldn't count themselves
                                    minesFound++;
                                }
                            }
                        }
                    }
                }
                //Set the amount of discovered mines
                newGrid[x][y].setNearbyMines(minesFound);
            }
        }

        return newGrid;
    }

    public int[][] getRenderGrid(){
        int[][] renderGrid = new int[xSize][ySize];
        for(int x = 0; x < xSize; x++){
            for(int y = 0; y < ySize; y++){
                renderGrid[x][y] = cellGrid[x][y].getRenderValue();
            }
        }
        return renderGrid;
    }
}
