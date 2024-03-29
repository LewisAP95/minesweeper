public class cell {
    private int nearbyMines;
    private boolean isMine;
    private boolean isFlagged;
    private boolean isVisible;
    private boolean beenGuessed;
    private int xCoord;
    private int yCoord;

    public cell(int x, int y){
        nearbyMines = 0;
        isMine = false;
        isFlagged = false;
        isVisible = false;
        beenGuessed = false;
        xCoord = x;
        yCoord = y;
    }

    public int getRenderValue(){
        //Returns a number based on the state the cell is in
        // -1 for cells not yet revealed
        // -2 for flagged cells
        // -3 for revealed mines (only used on game loss)
        // And for all else it returns the number of nearby mines
        if(!isFlagged){
            if(!isVisible){
                return -1;
            }else if(isMine){
                return -3;
            }else{
                return nearbyMines;
            }
        }else{
            return -2;
        }
    }

    public int getNearbyMines(){
        return nearbyMines;
    }

    public boolean getMineStatus(){
        return isMine;
    }

    public boolean getFlaggedStatus(){
        return isFlagged;
    }

    public boolean getVisibleStatus(){
        return isVisible;
    }

    public boolean getGuessedStatus(){
        return beenGuessed;
    }

    public int getX(){
        return xCoord;
    }

    public int getY(){
        return yCoord;
    }

    public void setNearbyMines(int count){
        nearbyMines = count;
    }

    public void setMineStatus(boolean status){
        isMine = status;
    }

    public void setFlaggedStatus(boolean status){
        isFlagged = status;
    }

    public void setVisibleStatus(boolean status){
        isVisible = status;
    }

    public void setGuessedStatus(boolean status){
        beenGuessed = status;
    }
}
