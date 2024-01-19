import java.util.concurrent.ThreadLocalRandom;

public class gameBoard {
    private int xSize;
    private int ySize;
    private int mineCount;
    private cell[][] cellGrid;
    private gameController controller;

    public gameBoard(int xSize, int ySize, int mineCount, gameController controller){
        this.xSize = xSize;
        this.ySize = ySize;
        this.mineCount = mineCount;
        this.controller = controller;
    }

    private void updateController(int errorCode){
        switch (errorCode) {
            //Normal, no issues update case
            case 0:
                controller.update(getRenderGrid());
                break;
            
            //Out of grid bounds error case
            case 1:
                controller.update(1);
                break;

            //Game over case
            case 2:
                controller.update(2);
                break;

            default:
                break;
        }
    }

    private void createNewGameGrid(int firstGuessX, int firstGuessY){
        cellGrid = scanForNearbyMineCount(layMines(generateBlankGrid(), firstGuessX, firstGuessY));
        makeGuess(firstGuessX, firstGuessY);
    }

    private cell[][] generateBlankGrid(){
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

    private cell[][] layMines(cell[][] newGrid, int firstGuessX, int firstGuessY){
        //Takes in a grid and, for the desired number of mines, randomly generates X and Y to 'place' these mines at
        //Doesn't generate a mine on the cell of the first guess or any directly surrounding cells
        cell[] firstGuessSurroundingCells = getSurroundingCells(newGrid[firstGuessX][firstGuessY], newGrid);
        int minesPlaced = 0;
        while(minesPlaced < mineCount){
            //Generate the random coordinates
            int randX = ThreadLocalRandom.current().nextInt(0, xSize);
            int randY = ThreadLocalRandom.current().nextInt(0, ySize);

            //Sets a flag to skip generating a mine on the current cell if it was the first guessed or any surrounding ones
            boolean skipCell = false;
            if(randX == firstGuessX && randY == firstGuessY){
                skipCell = true;
            }
            for(cell c : firstGuessSurroundingCells){
                if(c == newGrid[randX][randY]){
                    skipCell = true;
                }
            }

            if(!skipCell){
                newGrid[randX][randY].setMineStatus(true);
                minesPlaced++;
            }
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

    public void makeGuess(int guessX, int guessY){
        if(!boundsCheck(guessX, guessY)){
            //Call an out of bounds error on out of bounds guess
            updateController(1);
        }else if(cellGrid == null){
            //If no cellGrid exists yet due to this being the first guess, creates one
            createNewGameGrid(guessX, guessY);
        }else if(cellGrid[guessX][guessY].getMineStatus()){
            //Call update with a game over code if cell is a mine
            updateController(2);
        }else if(cellGrid[guessX][guessY].getGuessedStatus()){
            //If the cell has already been guessed, change nothing
            ;
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
            //May run too many updates on a zero cell cascade, needs testing
            updateController(0);
        }
    }

    public void doFlag(int flagX, int flagY){
        //Flips the flag status on the cell at the given coords
        //If the coords were invalid calls and update with the out of bounds error code
        if(boundsCheck(flagX, flagY)){
            if(cellGrid[flagX][flagY].getFlaggedStatus()){
                cellGrid[flagX][flagY].setFlaggedStatus(false);
            }else{
                cellGrid[flagX][flagY].setFlaggedStatus(true);
            }
            updateController(0);
        }else{
            updateController(1);
        }
    }

    private boolean boundsCheck(int x, int y){
        //Checks of a set of coords are within the bounds of the game grid and returns the result
        if((x >= 0 && x < xSize) && (y >= 0 && y < ySize)){
            return true;
        }else return false;
    }
}
