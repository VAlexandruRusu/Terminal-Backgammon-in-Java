class Repository{
    private int nr;
    private int eliminated;
    Repository(){
        this.nr=0;
        this.eliminated=0;
    }
    public void Increment(){
        nr++;
    }
    public void Decrement(){
        nr--;
    }
    public int PrintRepository(){
        return nr;
    }
    public int getNumber(){
        return nr;
    }
    public int getEliminated(){
        return eliminated;
    }
    public void IncrementEliminated(){
        eliminated++;
    }
    public void DecrementEliminated(){
        eliminated--;
    }
}
