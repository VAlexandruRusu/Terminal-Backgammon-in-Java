class Dices{
    private int dice1,dice2,bonusdice;
    Dices(int dice1,int dice2,int bonusdice){
        this.dice1=dice1;
        this.dice2=dice2;
        this.bonusdice=bonusdice;
    }
    Dices(){
        dice1=0;
        dice2=0;
        bonusdice=0;
    }
    public void SetDice(Dices otherdice){
        dice1=otherdice.dice1;
        dice2=otherdice.dice2;
        bonusdice=otherdice.bonusdice;
    }
    public void MakeNull(){
        dice1=0;
        dice2=0;
        bonusdice=0;
    }
    public boolean IsNull(){
        if(dice1==0 && dice2==0 && bonusdice==0)
            return true;
        return false;
    }
    public void SetDice(int dice1,int dice2,int bonusdice){
        this.dice1=dice1;
        this.dice2=dice2;
        this.bonusdice=bonusdice;
    }
    public int getDice1(){
        return dice1;
    }
    public int getDice2(){
        return dice2;
    }
    public int getBonusdice(){
        return bonusdice;
    }
}
