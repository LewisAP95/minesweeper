public class cell {
    private int nearbyMines;
    private boolean isMine;
    private boolean isFlagged;
    private boolean isVisible;
    private boolean beenGuessed;

    public cell(int nearbyMines, boolean isMine, boolean isFlagged, boolean isVisible){
        this.nearbyMines = nearbyMines;
        this.isMine = isMine;
        this.isFlagged = isFlagged;
        this.isVisible = isVisible;
        beenGuessed = false;
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

    public int setNearbyMines(){
        return nearbyMines;
    }

    public boolean setMineStatus(){
        return isMine;
    }

    public boolean setFlaggedStatus(){
        return isFlagged;
    }

    public boolean setVisibleStatus(){
        return isVisible;
    }

    public boolean setGuessedStatus(){
        return beenGuessed;
    }
}
