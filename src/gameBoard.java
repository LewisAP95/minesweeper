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
                newGrid[x][y] = new cell(x, y);
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
                for(cell c : getSurroundingCells(newGrid[x][y], newGrid)){
                    if(c.getMineStatus()){
                        minesFound++;
                    }
                }
                newGrid[x][y].setNearbyMines(minesFound);
            }
        }

        return newGrid;
    }

    private cell[] getSurroundingCells(cell originCell, cell[][] grid){
        //Returns an array of all surrounding cells for a given cell
        int[] scanDirs = {-1, 0, 1};
        cell[] foundCells =  new cell[8];
        int foundIndex = 0;

        //Work through all modifiers of the X, Y indices
        for(int scanX = 0; scanX < scanDirs.length; scanX++){
            for(int scanY = 0; scanY < scanDirs.length; scanY++){
                int modifiedIndexX = originCell.getX() + scanDirs[scanX];
                int modifiedIndexY = originCell.getY() + scanDirs[scanY];
                //Only proceeds if the cell is in bounds
                if(boundsCheck(modifiedIndexX, modifiedIndexY)){
                    if(modifiedIndexX != originCell.getX() || modifiedIndexY !=  originCell.getY()){
                        //Doesn't proceed if both indices match the original cell itself
                        foundCells[foundIndex] = grid[modifiedIndexX][modifiedIndexY];
                        foundIndex++;
                    }
                }
            }
        }

        //Not all cells will have 8 surrounding cells due to edges and corners
        //This step makes a new array containing only the actual cells found above
        cell[] finalSet = new cell[foundIndex];
        for(int i = 0; i < foundIndex; i++){
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

    public int[][] makeGuess(int guessX, int guessY){
        if(!boundsCheck(guessX, guessY)){
            //Return a zeroed grid on an out of bounds guess
            return new int[xSize][ySize];
        }else if(cellGrid[guessX][guessY].getMineStatus()){
            //Return a grid with a cell value that will trigger a game over
            int[][] gameOverGrid = new int[xSize][ySize];
            gameOverGrid[0][0] = -99;
            return gameOverGrid;
        }else if(cellGrid[guessX][guessY].getGuessedStatus()){
            //If the cell has already been guessed, change nothing
            return getRenderGrid();
        }else{
            //Changes the right values on a guessed cell
            cell guessedCell = cellGrid[guessX][guessY];
            guessedCell.setGuessedStatus(true);
            guessedCell.setVisibleStatus(true);
            guessedCell.setFlaggedStatus(false);
            //If cell was a 0 for nearby mines, guesses all surrounding cells to reveal them
            //Proceeding recursively if any of those end up also being zero 
            if(guessedCell.getNearbyMines() == 0){
                for(cell c : getSurroundingCells(guessedCell, cellGrid)){
                    makeGuess(c.getX(), c.getY());
                }
            }
            return getRenderGrid();
        }
    }

    public int[][] doFlag(int flagX, int flagY){
        //Flips the flag status on the cell at the given coords
        //If the coords were invalid returns a same-size grid filled with 0s
        if(boundsCheck(flagX, flagY)){
            if(cellGrid[flagX][flagY].getFlaggedStatus()){
                cellGrid[flagX][flagY].setFlaggedStatus(false);
            }else{
                cellGrid[flagX][flagY].setFlaggedStatus(true);
            }
            return getRenderGrid();
        }else{
            return new int[xSize][ySize];
        }
    }

    private boolean boundsCheck(int x, int y){
        //Checks of a set of coords are within the bounds of the game grid and returns the result
        if((x >= 0 && x < xSize) && (y >= 0 && y < ySize)){
            return true;
        }else return false;
    }
}
