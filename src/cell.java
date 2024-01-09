public class cell {
    private int nearbyMines;
    private boolean isMine;
    private boolean isFlagged;
    private boolean isVisible;
    private boolean beenGuessed;

    public cell(){
        nearbyMines = 0;
        isMine = false;
        isFlagged = false;
        isVisible = false;
        beenGuessed = false;
    }

    public int getRenderValue(){
        if(isVisible){
            if(isFlagged){
                return -2;
            }else{
                return nearbyMines;
            }
        }else{
            return -1;
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
