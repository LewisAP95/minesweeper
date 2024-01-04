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
}
