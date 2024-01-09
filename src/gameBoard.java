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
        //Makes a new grid (2d array) of the size given when the game board was instantiated
        //Fills this grid with cells of the default setup
        cell[][] newGrid = new cell[xSize][ySize];

        for(int x = 0; x < xSize; x++){
            for(int y = 0; y < ySize; y++){
                newGrid[x][y] = new cell();
            }
        }

        return newGrid;
    }

    private cell[][] layMines(cell[][] newGrid){
        //Takes in a grid and, for the desired number of mines, randomly generates X and Y to 'place' these mines at
        for(int minesPlaced = 0; minesPlaced < mineCount; minesPlaced++){
            int randX = ThreadLocalRandom.current().nextInt(0, xSize);
            int randY = ThreadLocalRandom.current().nextInt(0, ySize);
            newGrid[randX][randY].setMineStatus(true);
        }

        return newGrid;
    }

    private cell[][] scanForNearbyMineCount(cell[][] newGrid){
        //For every cell of the grid, checks all 8 surrounding cells for mines to establish a nearby mine count
        for(int x = 0; x < xSize; x++){
            for(int y = 0; y < ySize; y++){
                int minesFound = 0;

                //Set the amount of discovered mines
                for(cell c : getSurroundingCells(x, y, newGrid)){
                    if(c.getMineStatus()){
                        minesFound++;
                    }
                }
                newGrid[x][y].setNearbyMines(minesFound);
            }
        }

        return newGrid;
    }

    private cell[] getSurroundingCells(int cellX, int cellY, cell[][] grid){
        //Returns an array of all surrounding cells for a given cell
        int[] scanDirs = {-1, 0, 1};
        cell[] foundCells =  new cell[8];
        int foundIndex = 0;

        //Work through all modifiers of the X, Y indices
        for(int scanX = 0; scanX < scanDirs.length; scanX++){
            for(int scanY = 0; scanY < scanDirs.length; scanY++){
                int modifiedIndexX = cellX + scanDirs[scanX];
                int modifiedIndexY = cellY + scanDirs[scanY];
                //Only proceeds if the cell is in bounds
                if((modifiedIndexX >= 0 && modifiedIndexX < xSize - 1) && (modifiedIndexY >= 0 && modifiedIndexY < ySize - 1)){
                    if(modifiedIndexX != cellX || modifiedIndexY !=  cellY){
                        //Doesn't proceed if the index is the original cell itself
                        foundCells[foundIndex] = grid[modifiedIndexX][modifiedIndexY];
                        foundIndex++;
                    }
                }
            }
        }

        //Not all cells will have 8 surrounding cells due to edges and corners
        //This step makes a new array containing only the actual cells found above
        int finalCount = 0;
        for(cell c : foundCells){
            if(c != null){
                finalCount++;
            }
        }
        cell[] finalSet = new cell[finalCount];
        for(int i = 0; i < finalCount; i++){
            finalSet[i] = foundCells[i];
        }

        return finalSet;
    }

    private int[][] getRenderGrid(){
        //Returns a 'render grid' which does not contain cells but
        //just an integer representing which state they should be visually displayed as being in
        int[][] renderGrid = new int[xSize][ySize];
        for(int x = 0; x < xSize; x++){
            for(int y = 0; y < ySize; y++){
                renderGrid[x][y] = cellGrid[x][y].getRenderValue();
            }
        }
        return renderGrid;
    }

    //public int[][] makeGuess(int guessX, int guessY){
    //    
    //}

    public int[][] doFlag(int flagX, int flagY){
        if(cellGrid[flagX][flagY].getFlaggedStatus()){
            cellGrid[flagX][flagY].setFlaggedStatus(false);
        }else{
            cellGrid[flagX][flagY].setFlaggedStatus(true);
        }
        return getRenderGrid();
    }
}
