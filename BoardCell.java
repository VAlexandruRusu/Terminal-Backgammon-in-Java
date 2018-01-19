class BoardCell{
    private int color;
    
    BoardCell(int color){//generator when placing a piece there
        this.color=color;
    }
    public void setColor(int color){
        this.color=color;
    }
    public int getColor(){
        return color;
    }
}
