import java.util.Random;
import java.util.Scanner;
class GameBoard{
    private BoardCell[][] gameboard;//The gameboard object contains the gameboard itself (made out of boardcells) and two repositories for each color (these contain the nr of pieces eliminated and pieces taken off table)
    private Repository WhiteRepository;
    private Repository BlackRepository;
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_WHITE = "\u001B[37;1m";
    private static final String ANSI_RED_BACKGROUND = "\u001B[48;5;197m";
    private static final String ANSI_GREEN_BACKGROUND = "\u001B[48;5;70m";
    private static final String ANSI_BROWN_BACKGROUND = "\u001B[48;5;94m";//Declared colors for making the game look nicer. Can be changed manually using ANSI 
    GameBoard(){//The constructor
        this.gameboard = new BoardCell[16][12];//initialize a gameboard and two repositories
        WhiteRepository = new Repository();
        BlackRepository = new Repository();
    }
    public void CellToBoard(int length, int width, BoardCell x){//puts boardcelldata in the gameboard array
        gameboard[length][width] = x;
    }
    //NORMAL STARTING BOARD 
     public void InitializeBoard(){//initialize the board (starting positions)
        int i,j;
        for(i=0;i<5;i++){
            gameboard[i][0].setColor(1);
            gameboard[15-i][0].setColor(2);
            gameboard[i][6].setColor(2);
            gameboard[15-i][6].setColor(1);
            }
        for(i=0;i<3;i++){
            gameboard[i][4].setColor(2);
            gameboard[15-i][4].setColor(1);
        }
        for(i=0;i<2;i++){
            gameboard[i][11].setColor(1);
            gameboard[15-i][11].setColor(2);
        }
    }
    public void PrintGameBoard(){//Prints the board margins with backgrounds then points to PrintInColor which puts marks(or not) in the color needed
        int i,j;
        clearScreen();
        System.out.println();//print the top half
        System.out.print(" 012345  67891011 \n");
        for(i=0;i<16;i++)
            System.out.print(ANSI_BROWN_BACKGROUND + "-" + ANSI_RESET); //print the top box
        System.out.println();
        for(i=0;i<8;i++){//print the top half of the map 
            System.out.print(ANSI_BROWN_BACKGROUND+"|"+ANSI_RESET);//start with the border (colored brown)
            for(j=0;j<12;j++){
                if(j==6){//if it's middle print double border to represent the half of the map 
                  System.out.print(ANSI_BROWN_BACKGROUND+"||"+ANSI_RESET); 
                  PrintInColor(gameboard[i][j].getColor(),j);//then print the piece that is there 
                }
                else
                    PrintInColor(gameboard[i][j].getColor(),j);//if not just print the piece that is there
            }
            System.out.print(ANSI_BROWN_BACKGROUND+"|"+ANSI_RESET);//Print the end of the board border 
            System.out.println();//and go to the next line
        }
        for(j=0;j<2;j++){//print the middle separator
            for(i=0;i<16;i++)
                System.out.print(ANSI_BROWN_BACKGROUND + "-" + ANSI_RESET); 
            System.out.println();
        }
        for(i=8;i<16;i++){//print the bottom half of the map (same thing)
            System.out.print(ANSI_BROWN_BACKGROUND+"|"+ANSI_RESET);
            for(j=0;j<12;j++){
                if(j==6){
                  System.out.print(ANSI_BROWN_BACKGROUND+"||"+ANSI_RESET); 
                  PrintInColor(gameboard[i][j].getColor(),j+1);
                }
                else
                    PrintInColor(gameboard[i][j].getColor(),j+1);
            }
            System.out.print(ANSI_BROWN_BACKGROUND+"|"+ANSI_RESET);
            System.out.println();
        }
        for(i=0;i<16;i++)
            System.out.print(ANSI_BROWN_BACKGROUND + "-" + ANSI_RESET); //print the bottom box and the column numbers in the next line
        System.out.println();
        System.out.print(" 012345  67891011 \n");
        if(BlackRepository.PrintRepository()!=0)
            System.out.print("\nBlack has: "+BlackRepository.PrintRepository()+" pieces off table\n");
        if(WhiteRepository.PrintRepository()!=0)
            System.out.print("\nWhite has: "+WhiteRepository.PrintRepository()+" pieces off table \n");
    }
    public void PrintInColor(int colornr, int colsnr){//prints the pieces with the backgrounds that it must have
        switch(colornr){//Depending on the colorn number print the mark: 1 = first player , 2 = second player , 3= empty. 
            case 1://Print first player mark (white)
                if(colsnr%2==0)//As columns are consecutively colored red and green, use a modulo to know which background color to choose then apply the colornr to know which color the mark must have 
                    System.out.print(ANSI_RED_BACKGROUND+ ANSI_WHITE + "O"+ ANSI_RESET);
                else
                    System.out.print(ANSI_GREEN_BACKGROUND+ ANSI_WHITE + "O"+ ANSI_RESET);
                break;
            case 2:
                if(colsnr%2==0)//black player
                    System.out.print(ANSI_RED_BACKGROUND + ANSI_BLACK + "O"+ ANSI_RESET);
                else
                    System.out.print(ANSI_GREEN_BACKGROUND + ANSI_BLACK + "O"+ ANSI_RESET);
                break;
            case 3:
                if(colsnr%2==0)//no player(empty space)
                    System.out.print(ANSI_RED_BACKGROUND +" " + ANSI_RESET);
                else
                    System.out.print(ANSI_GREEN_BACKGROUND+ ANSI_WHITE + " "+ ANSI_RESET);
                break;
        }
    }
    public int BoardCellCounter(int color){//counts how many pieces of a certain color there are 
        int i,j,nr=0;
        for(i=0;i<16;i++)
            for(j=0;j<12;j++)
                if(gameboard[i][j].getColor()==color)
                    nr++;
        return nr;
    }
    public boolean IsDone(){//counts how many white and black pieces there are. If none return true, else false.
    int i,j,nrwhite=0,nrblack=0;
        for(i=0;i<16;i++)
            for(j=0;j<12;j++){
                if(gameboard[i][j].getColor()==1)
                    nrwhite++;
                if(gameboard[i][j].getColor()==2)
                    nrblack++;
            }
        if(nrwhite==0 || nrblack==0)
            return true;
        return false;
    }
    public void PlayPvPGame(Player p1, Player p2){//Controls the game flow. As long as there are pieces left switch between player turns when there are no more pieces print the winner name and a message
        int nr=0;
        while(IsDone()==false){//as long as there are pieces left on the board keep playing
            if(nr==0){
                PlayerTurn(p1);//p1 always starts (as decided in Table.java
                nr=1;//switch player
            }
            else{
                PlayerTurn(p2);
                nr=0;
            }
        }
        if(BoardCellCounter(p1.GetColor())==0)
            System.out.println(p1.GetName()+" has won, congratulations!");
        else
            System.out.println(p2.GetName()+" has won, congratulations!");
    }
    public void PlayerTurn(Player p){//Controls the player turn. Rolls two dices, asks for input(from where to take, where to put), checks if the input is valid, checks if the dice rolls confirm the input then moves pieces.
        int dice1,dice2,bonusdice,eliminateornot,colnroffmap;
        int sum=9;
        Scanner sinput = new Scanner(System.in);
        bonusdice=0;//Variable which changes if there is a bonus roll (aka dice1 = dice 2, such as 6-6)
        System.out.println(p.GetName()+ " is rolling the dice:");
        dice1=randInt(1,6);//Generate two random dice rolls between 1-6
        dice2=randInt(1,6);//set them manually after to test special cases if needed
        if(dice1==dice2)
            bonusdice=2;
        Dices PlayerDice = new Dices(dice1,dice2,bonusdice);//create a new dice with the information
        do{
        System.out.println("Rolls: "+dice1+" and "+dice2);
        if( (p.GetColor()==1 && WhiteRepository.getNumber()==0) || (p.GetColor()==2 && BlackRepository.getNumber()==0) ){//If there are no pieces off table that must be placed
            System.out.println(p.GetName()+"'s turn");
            //System.out.println("Printing candostuff");
            //System.out.println(CanDoStuff(p,PlayerDice.getDice1(),PlayerDice.getDice2()));
            if(CanDoStuff(p,PlayerDice.getDice1(),PlayerDice.getDice2())==true || CanEliminatePieces(p,WhiteRepository,BlackRepository)==true){//If it can move pieces
            if(CanEliminatePieces(p,WhiteRepository,BlackRepository)==true){//And can start eliminating pieces
                System.out.println("can eliminate");
                eliminateornot=ChooseWhetherToEliminate(sinput,"You can eliminate pieces from the table. Do you want to eliminate (1) or move your pieces (2)?");
                    if(eliminateornot==1){//eliminate pieces
                        System.out.println("Dice INFO BEFORE:");
                        System.out.println("DICE1= "+PlayerDice.getDice1());
                        System.out.println("DICE2= "+PlayerDice.getDice2());
                        System.out.println("BONUSDICE= "+PlayerDice.getBonusdice());
                        PlayerDice.SetDice(EliminateStuff(p,PlayerDice,sinput));//Eliminates pieces according to the dices. If the input is invalid it won't modify the dice object
                        System.out.println("Dice INFO AFTER:");
                        System.out.println("DICE1= "+PlayerDice.getDice1());
                        System.out.println("DICE2= "+PlayerDice.getDice2());
                        System.out.println("BONUSDICE= "+PlayerDice.getBonusdice());
                    }
                    else{//If the player wants to move pieces instead (and can move pieces)
                        System.out.println("Dice INFO BEFORE:");
                        System.out.println("DICE1= "+PlayerDice.getDice1());
                        System.out.println("DICE2= "+PlayerDice.getDice2());
                        System.out.println("BONUSDICE= "+PlayerDice.getBonusdice());
                        PlayerDice.SetDice(FullMove(p,PlayerDice,sinput));//Points to another method which controls the movement
                        System.out.println("Dice INFO AFTER:");
                        System.out.println("DICE1= "+PlayerDice.getDice1());
                        System.out.println("DICE2= "+PlayerDice.getDice2());
                        System.out.println("BONUSDICE= "+PlayerDice.getBonusdice());
                        }
                        System.out.println("Playerdice is null? "+PlayerDice.IsNull());
                }
                else{//can't eliminate, just move pieces
                    System.out.println("Dice INFO BEFORE:");
                    System.out.println("DICE1= "+PlayerDice.getDice1());
                    System.out.println("DICE2= "+PlayerDice.getDice2());
                    System.out.println("BONUSDICE= "+PlayerDice.getBonusdice());
                    PlayerDice.SetDice(FullMove(p,PlayerDice,sinput));
                    System.out.println("Dice INFO AFTER:");
                    System.out.println("DICE1= "+PlayerDice.getDice1());
                    System.out.println("DICE2= "+PlayerDice.getDice2());
                    System.out.println("BONUSDICE= "+PlayerDice.getBonusdice());
                }
            }
            else{//The player can't do anything (dices do not point to a valid move)
                System.out.println("Sadly there is no valid move");
                PlayerDice.MakeNull();
            }
        }
        else{//The player must put pieces that are off the table first before proceding
            System.out.println("Rolls: "+dice1+" and "+dice2);
            System.out.print("\nYou have pieces off table that must be placed back in order to continue playing.\n");
            if(CanGetOut(p,dice1,dice2)==false){//If dices do not allow the player to leave
                System.out.print("Sadly your dices do not allow you to leave.\n");
                PlayerDice.MakeNull();//Make the dice null (ends the player turn)
            }
            else{//If dices allow the player to move ask for column to put the piece to
                    colnroffmap=AskForColInput(sinput,"Where do you want to move the piece that is off table? Input column number.");
                    if( colnroffmap<6 || colnroffmap>11)//repeate trying for dices in case the input was invalid
                        System.out.println("Invalid input");
                    else{//valid column input (in terms of raw numbers)
                        int opponentcolor,maphalf,linenr;
                        if(p.GetColor()==1){//get some required data depending on the player 
                            opponentcolor=2;
                            linenr=0;
                            maphalf=1;
                        }
                        else{
                            opponentcolor=1;
                            linenr=15;
                            maphalf=2;
                        }   
                        if( ((dice1+colnroffmap==12) || (dice2+colnroffmap==12)) && (NrPiecesOnColumn(opponentcolor,maphalf,colnroffmap)<2)){//If the dices validate the move AND the opponent doesn't have a house there
                            GetOut(p,colnroffmap,WhiteRepository,BlackRepository);//get out (put a piece from outside the board into the board)
                            if(bonusdice!=0)//If the player has bonus dices just decrement that value
                                bonusdice--;
                            else//Else get out with one of the dices 
                                if(dice1+colnroffmap==12)
                                    dice1=0;
                                else
                                    dice2=0;
                            PlayerDice.SetDice(dice1,dice2,bonusdice);//Update the player dice accordingly
                        }
                    }
            }
        }
        PrintGameBoard();//Print the game board in the end
        }while(PlayerDice.IsNull()==false);//Player turn ends when the dice is null
    }
    public Dices EliminateStuff(Player p,Dices PlayerDice, Scanner sinput){//Controls the elimination of pieces once all of them are in the player's house
        int coltoeliminate,dice1,dice2,bonusdice;
        coltoeliminate=AskForColEliminateInput(sinput,"From which column do you want to eliminate a piece?");
        dice1=PlayerDice.getDice1();//save the dice information in separate variables (for ease of use)
        dice2=PlayerDice.getDice2();
        bonusdice=PlayerDice.getBonusdice();
        switch(CheckEliminateWithDice(p,dice1,dice2,coltoeliminate)){//Checks if one/two/both dices eliminate pieces
            case 0://not valid
                PrintGameBoard();
                System.out.println("Invalid column to eliminate from");
            break;
            case 1://first dice only
                if(bonusdice!=0)//If it is a bonus dice make twice the moves
                    bonusdice--;
                else
                    dice1=0;
                EliminateFrom(p,coltoeliminate,WhiteRepository,BlackRepository);//Eliminates piece from the board and prints the new board
                PrintGameBoard();
            break;
            case 2://second dice only
                if(bonusdice!=0)
                    bonusdice--;
                else
                    dice2=0;
                EliminateFrom(p,coltoeliminate,WhiteRepository,BlackRepository);
                PrintGameBoard();
            break;
            case 3://both dices
                if(bonusdice!=0){//If player still has bonus dices
                    if(bonusdice==1){//If it's one make bonusdice and one of the dices zero 
                        bonusdice=0;
                        dice1=0;
                    }
                    else{//If it's two just make bonusdice 0 
                        bonusdice=0;
                    }
                }
                else{//If there is no bonusdice make both dices zero
                    dice2=0;
                    dice1=0;
                }
                EliminateFrom(p,coltoeliminate,WhiteRepository,BlackRepository);//Eliminate piece from the board and print the new board
                PrintGameBoard();
            break;
        }
        Dices EliminatedDice = new Dices(dice1,dice2,bonusdice);//Creates a new dice with the new variables and returns it (the playerturn method will update the player dice with this dice)
        return EliminatedDice;
    }
    public Dices FullMove(Player p,Dices PlayerDice, Scanner sinput){//Method which controls a player makign a "full move" - moving piece from point A to point B depending on the dices
        int boardhalffrom,colnrfrom,boardhalfto,colnrto,bonusdice=0,dice1,dice2;
        dice1=PlayerDice.getDice1();//copy the dice information into these variables for ease of use
        dice2=PlayerDice.getDice2();
        bonusdice=PlayerDice.getBonusdice();
        System.out.println("Rolls: "+dice1+" and "+dice2);//Print the dice rolls, ask for input from where to take and where to place the piece
        boardhalffrom=AskForMapInput(sinput,"Which half of the board do you want to move a piece FROM? 1.top half | 2.bottom half");
        colnrfrom=AskForColInput(sinput,"Which column do you want to take that piece FROM? Input a number between 0-11.");
        boardhalfto=AskForMapInput(sinput,"Which half of the board do you want to move a piece TO? 1.top halft | 2.bottom half");
        colnrto=AskForColInput(sinput,"Which column do you want to take that piece TO? Input a number between 0-11.");
        if(CheckMovePieceFromTo(p,boardhalffrom,colnrfrom,boardhalfto,colnrto)==true){//If the input is valid (user did not input an invalid row (like 20) AND it can move there (no house)
            switch(AccountForDiceRoll(p,boardhalffrom,colnrfrom,boardhalfto,colnrto,dice1,dice2)){//Then it must check if the dice rolls allow for such a move and return the nr of the dice moved.
                case 0://No dices were moved (the move is invalid)
                    System.out.println("False move(check dices)");
                break;
                case 1://Only first dice was moved
                    MovePieceFromTo(p,boardhalffrom,colnrfrom,boardhalfto,colnrto);//Move that piece as it is a valid move
                    if(bonusdice!=0)//If it is a bonus dice make twice the moves
                        bonusdice--;
                    else
                        dice1=0;//Else make this dice 0 as it was used
                break;
                case 2://Only second dice was moved
                    MovePieceFromTo(p,boardhalffrom,colnrfrom,boardhalfto,colnrto); 
                    if(bonusdice!=0)
                        bonusdice--;
                    else
                        dice2=0;
                break;
                case 3://Both dices were moved
                    MovePieceFromTo(p,boardhalffrom,colnrfrom,boardhalfto,colnrto);
                    if(bonusdice!=0){//If there is a bonus dice
                        if(bonusdice==1){//If it's 1 make it 0 and one of the dices 0 too 
                            bonusdice=0;
                            dice1=0;
                        }
                        else
                            bonusdice=0;//if it's 2 make it 0 instantly
                    }
                    else{//if there is no bonus dice make both dices zero
                        dice1=0;
                        dice2=0;
                    }
                break;
                default://Shouldn't happen as it the value returned can only be 1/2/3
                    System.out.println("Something went wrong when checking the dices");
                break;
            }
        }
        else{//If the input is invalid tell the user and keep checking for dices
            PrintGameBoard();//Print the game board
            System.out.println("Invalid input, input again");
        }
        //System.out.println("DICES AFTER MOVE: dice1="+dice1+" dice2="+dice2);
        Dices MoveDice = new Dices(dice1,dice2,bonusdice);//update the dice and return it to the player turn method
        return MoveDice;
    }
    public int CheckEliminateWithDice(Player p,int dice1,int dice2,int coltoeliminate){//Returns the number of dices that eliminate a piece
        int result=0,smallerdice,maphalf;
        if(dice1-dice2>0) //get the smaller dice and the maphalf to check if it can eliminate (white eliminates bottom half, black eliminates top)
            smallerdice=2;
        else
            smallerdice=1;
        if(p.GetColor()==1)
            maphalf=2;
        else 
            maphalf=1;
        if(NrPiecesOnColumn(p.GetColor(),maphalf,coltoeliminate)>0){//If there is a piece that it can eliminate from that column
            //System.out.println("I can eliminate from this column");
            if(12-dice1==coltoeliminate)//dice1 eliminates 
                result=1;
            if(12-dice2==coltoeliminate && result==0)//dice 2 eliminates
                result=2;
            if(12-dice1-dice2==coltoeliminate && result==0)//the combination of dices eliminate
                result=3;
            if(result==0){//if neither of the 3 possible dice combinations eliminate the piece check special cases (eg. a player has no pieces on col 6, 1 piece on col 5 and one of the dice is 6
                int i=6;
                while(i<coltoeliminate && NrPiecesOnColumn(p.GetColor(),maphalf,i)==0)//as long as there are no pieces from the most far away column in the house to the column selected keep going
                    i++;
                //System.out.println("i:"+i);
                if(i==coltoeliminate){//it can elminiate from here
                    //System.out.println("special cases");
                    if(smallerdice==1 && coltoeliminate<smallerdice)//first dice eliminates
                        result=1;
                    if(smallerdice==2 && coltoeliminate<smallerdice && result==0)//second dice eliminates
                        result=2;
                    if(smallerdice==1 && coltoeliminate>smallerdice && (12-dice2<coltoeliminate) && result==0)//first dice eliminates
                        result=2;
                    if(smallerdice==2 && coltoeliminate>smallerdice && (12-dice1<coltoeliminate) && result==0)//second dice eliminates
                        result=1;
                    if((12-dice1-dice2<coltoeliminate) && result==0)//both dices eliminate
                        result=3;
                }
            }
        }
        //System.out.println("result:"+result);
        return result;//return the result in the end for playerturn to analyze
    }
    public void EliminateFrom(Player player, int col,Repository WhiteRepository, Repository BlackRepository){//Eliminates a player's last piece from a given column
        int maphalf;
        switch(player.GetColor()){
            case 1://white piece eliminates from bottom half
                maphalf=2;
                gameboard[FindLastPiece(maphalf,col)][col].setColor(3);//eliminate the last piece of the given column from the gameboard
                WhiteRepository.IncrementEliminated();//increases the number of eliminated pieces in the repository (used to check if the game is over and stuff)
            break;
            case 2://black piece
                maphalf=1;
                gameboard[FindLastPiece(maphalf,col)][col].setColor(3);
                BlackRepository.IncrementEliminated();
        }
    }
    public boolean CanGetOut(Player player,int dice1,int dice2){//Checks if the player's dices allow him to move a piece from outside the board into the opponents house
        int linenr,ok=0,maphalf,opponentcolor,j=6;
        if(player.GetColor()==1){//get data depending on the player
            opponentcolor=2;
            linenr=0;
            maphalf=1;
        }
        else{
            opponentcolor=1;
            linenr=15;
            maphalf=2;
        }
        while(ok==0 && j<=11){//The algorithm checks if there is a free (or at most ONE enemy piece) in the opponent's house (houses range from column 6-11) AND one of the dices allow such a move
            if(NrPiecesOnColumn(opponentcolor,maphalf,j)<2 && ((dice1+j==12)||(dice2+j==12) ) )
                ok=1;
            else
                j++;
        }  
        if(ok==1)
            return true;
        return false;
    }
    public boolean CanEliminatePieces(Player player,Repository WhiteRepository, Repository BlackRepository){//Method which checks if the player can eliminate pieces 
        int i,nr=0,maphalf;
        if(player.GetColor()==1)//get maphalf as usual
            maphalf=2;
        else
            maphalf=1;
        for(i=6;i<=11;i++){
            nr+=NrPiecesOnColumn(player.GetColor(),maphalf,i);//Sums the nr of pieces that are in the main house of the player
        }
        if(maphalf==1 && (nr+BlackRepository.getEliminated()==15))//If there are 15 pieces in total from the pieces already eliminated + the peices in the house (15 total) it can eliminate
            return true;
        else
            if(maphalf==2 && (nr+WhiteRepository.getEliminated()==15))
                return true;
        return false;
    }
    public void GetOut(Player player,int coloutto,Repository WhiteRepository,Repository BlackRepository){//Puts a piece from outside the board into the opponent's house
        int linenr,maphalf,opponentcolor;
        if(player.GetColor()==1){//get data as usual
            opponentcolor=2;
            linenr=0;
            maphalf=1;
        }
        else{
            opponentcolor=1;
            linenr=15;
            maphalf=2;
        }
        if(gameboard[linenr][coloutto].getColor()==opponentcolor){//if getting out removes an opponent piece increment the opponent's repository and decrement the player's
            if(opponentcolor==2){
                BlackRepository.Increment();
                WhiteRepository.Decrement();
                }
            else{
                WhiteRepository.Increment();
                BlackRepository.Decrement();
            }
            gameboard[FindLastPiece(maphalf,coloutto)][coloutto].setColor(player.GetColor());//put the piece from outside the board into that column
        }
        else{//If putting a piece into the opponent's house doesn't remove anything
            if(gameboard[linenr][coloutto].getColor()==3)//If the column the player wants to put a piece in is completely empty put the player piece there
                gameboard[linenr][coloutto].setColor(player.GetColor());
            else//if it makes a house of players color
                gameboard[FreeSpaceInLine(maphalf,coloutto)][coloutto].setColor(player.GetColor());//put a piece in the first free position of that column
            if(opponentcolor==2)//and again change repositories
                WhiteRepository.Decrement();
            else
                BlackRepository.Decrement();
        }
    }
    public int AskForMapInput(Scanner sinput,String S){//asks for map input using a string to know whether we ask for a move FROM or TO, returns number
        int boardhalf;
        do{
            System.out.println(S);
            boardhalf=sinput.nextInt();
            System.out.println();
        }while(boardhalf!=1 && boardhalf!=2);
        return boardhalf;
    }
    public int AskForColInput(Scanner sinput,String S){//asks for col input (FROM/TO depending on string),returns number
        int col;
        do{
            System.out.println(S);
            col=sinput.nextInt();
            System.out.println();
        }while(col<0 || col>11);
        return col;
    }
    public int AskForColEliminateInput(Scanner sinput,String S){//asks for col input (FROM/TO depending on string),returns number
        int col;
        do{
            System.out.println(S);
            col=sinput.nextInt();
            System.out.println();
        }while(col<6 || col>11);
        return col;
    }
    public int ChooseWhetherToEliminate(Scanner sinput,String S){//Asks the player if it wants to eliminate a piece or move (if it can do both)
        int i=0;
        System.out.println(S);
        do{
            i=sinput.nextInt();
        }while(i!=1 && i!=2);
        return i;
    }
    public boolean CanDoStuff(Player p,int dice1,int dice2){//Find every piece and check for every position with the dices
        int ok=0,colmove=11,switched=0,maphalf;
        if(p.GetColor()==1)
            maphalf=2;
        else
            maphalf=1;
        while(ok==0 && switched<=1){
            while(colmove>=0 && CheckValidTakeFrom(p,maphalf,colmove)==false){//as long as it doesn't find a valid piece keep decrementing
                colmove--;
            }
            if(colmove<0){//If it didn't find a piece (lol?) switch map halfs
                if(maphalf==1){
                    maphalf=2;
                    switched++;
                    colmove=12;
                }
                else{
                    maphalf=1;
                    switched++;
                    colmove=12;
                }
            }
            else{//if it did find a piece check for possible exits and if they validate the dice(s)
                int checkmaphalf=maphalf,checkcol=11,switchedtwice=0;
                while(ok==0 && switchedtwice<=1){//go again through the map 
                    checkcol=11;
                    while(checkcol>=0 && ok==0){//and go through the columns to check
                        if(CheckValidMoveTo(p,checkmaphalf,maphalf,checkcol,colmove)==true && AccountForDiceRoll(p,maphalf,colmove,checkmaphalf,checkcol,dice1,dice2)!=0)//if it found a valid move with dice stop
                            ok=1;
                        else
                            checkcol--;//else keep searching this map half
                    }
                    if(checkcol<0 && ok==0){//if it found a valid move do nothing else. If not check in the second half of the map
                        if(checkmaphalf==1){
                            checkmaphalf=2;
                            switchedtwice++;
                        }
                        else{
                            checkmaphalf=1;
                            switchedtwice++;
                        }
                    }
                }
            }
            colmove--;//decrement the colmove in case the validmoveto and accountfordiceroll don't produce a satisfactory result (so keep searching this half of the map)
        }
        if(ok==0)
            return false;
        return true;
    }
    public int AccountForDiceRoll(Player player, int maphalffrom,int colnrfrom, int maphalfto, int colnrto, int dice1,int dice2){//returns the dice that must be zeroed
        int value=0;//will be returned
        if(player.GetColor()==1){
            if( (maphalffrom+maphalfto)%2==0){//moving in the same half
                switch(maphalffrom){//top side
                    case 1:
                        if(colnrfrom-dice1==colnrto)//first dice moves there
                            value=1;
                        else
                        if(colnrfrom-dice2==colnrto)//second dice moves there
                            value=2;
                        else
                        if(colnrfrom-dice1-dice2==colnrto && (NrPiecesOnColumn(2,maphalffrom,colnrfrom-dice1)<2 || NrPiecesOnColumn(2,maphalffrom,colnrfrom-dice2)<2))//if one of the dices can move out and it uses both of them
                            value=3;
                        else   
                            value=0;
                    break;
                    case 2://bottom side
                        if(colnrfrom+dice1==colnrto)
                            value=1;
                        else
                        if(colnrfrom+dice2==colnrto)
                            value=2;
                        else
                        if(colnrfrom+dice1+dice2==colnrto && (NrPiecesOnColumn(2,maphalffrom,colnrfrom+dice1)<2 || NrPiecesOnColumn(2,maphalffrom,colnrfrom+dice2)<2))
                            value=3;
                        else   
                            value=0;
                    break;
                }
            }
            else{//between maps (white case, from top to bottom)
                if(dice1==colnrfrom+colnrto+1)//first dice used
                    value=1;
                else
                if(dice2==colnrfrom+colnrto+1)//second dice used
                    value=2;
                else
                if(dice1+dice2==colnrfrom+colnrto+1){//both dices used
                    if(colnrfrom<dice1 && colnrfrom<dice2){//if both dices cross maps
                        if(NrPiecesOnColumn(2,maphalfto,dice1-colnrfrom-1)<2 || NrPiecesOnColumn(2,maphalfto,dice2-colnrfrom-1)<2)//if one of the dices goes to an empty/1-piece position it's ok
                            value=3;
                        else
                            value=0;
                    }
                    else{
                        if(colnrfrom<=dice1){//only the first dice crosses the map
                            if(NrPiecesOnColumn(2,maphalfto,dice1-colnrfrom-1)<2 || NrPiecesOnColumn(2,maphalffrom,colnrfrom-dice2)<2)//if one of the dices goes to an empty/1-piece position it's ok
                                value=3;
                            else
                                value=0;
                        }
                        else{//only the second dice crosses the map 
                            if(colnrfrom<=dice2){
                            if(NrPiecesOnColumn(2,maphalfto,dice2-colnrfrom-1)<2 || NrPiecesOnColumn(2,maphalffrom,colnrfrom-dice1)<2)//if one of the dices goes to an empty/1-piece position it's ok
                                value=3;
                            else
                                value=0;
                            }
                            else{//if neither dices cross the map but their sum does 
                            if(NrPiecesOnColumn(2,maphalfto,colnrfrom-dice1)<2 || NrPiecesOnColumn(2,maphalffrom,colnrfrom-dice2)<2)//if one of the dices goes to an empty/1-piece position it's ok
                                value=3;
                            else
                                value=0;
                            }
                        }
                    }
                }//if neither of the three dices confirm the position then the input was wrong so return 0
                else value=0;
            }
        }
        else{//black case
            if( (maphalffrom+maphalfto)%2==0){//moving in the same half
                switch(maphalffrom){//top side
                    case 1:
                        if(colnrfrom+dice1==colnrto)
                            value=1;
                        else
                        if(colnrfrom+dice2==colnrto)
                            value=2;
                        else
                        if(colnrfrom+dice1+dice2==colnrto  && (NrPiecesOnColumn(1,maphalffrom,colnrfrom+dice1)<2 || NrPiecesOnColumn(1,maphalffrom,colnrfrom+dice2)<2))
                            value=3;
                        else   
                            value=0;
                    break;
                    case 2://bottom side
                        if(colnrfrom-dice1==colnrto)
                            value=1;
                        else
                        if(colnrfrom-dice2==colnrto)
                            value=2;
                        else
                        if(colnrfrom-dice1-dice2==colnrto  && (NrPiecesOnColumn(1,maphalffrom,colnrfrom-dice1)<2 || NrPiecesOnColumn(1,maphalffrom,colnrfrom-dice2)<2))
                            value=3;
                        else   
                            value=0;
                    break;
                }
            }
            else{//between maps (black case, from bottom to top)
                if(dice1==colnrfrom+colnrto+1)//1st dice
                    value=1;
                else
                if(dice2==colnrfrom+colnrto+1)//2nd dice
                    value=2;
                else
                if(dice1+dice2==colnrfrom+colnrto+1){//their sum crosses 
                    if(colnrfrom<dice1 && colnrfrom<dice2){//if both dices cross maps
                        if(NrPiecesOnColumn(1,maphalfto,dice1-colnrfrom-1)<2 || NrPiecesOnColumn(1,maphalfto,dice2-colnrfrom-1)<2)//if one of the dices goes to an empty/1-piece position it's ok
                            value=3;
                        else
                            value=0;
                    }
                    else{
                        if(colnrfrom<dice1){//only the first dice crosses the map
                            if(NrPiecesOnColumn(1,maphalfto,dice1-colnrfrom-1)<2 || NrPiecesOnColumn(1,maphalffrom,colnrfrom-dice2)<2)//if one of the dices goes to an empty/1-piece position it's ok
                                value=3;
                            else
                                value=0;
                        }
                        else{ 
                            if(colnrfrom<dice2){//only the second dice crosses the map
                            if(NrPiecesOnColumn(1,maphalfto,dice2-colnrfrom-1)<2 || NrPiecesOnColumn(1,maphalffrom,colnrfrom-dice1)<2)//if one of the dices goes to an empty/1-piece position it's ok
                                value=3;
                            else
                                value=0;
                            }
                            else{//only their sum crosses the map 
                                if(NrPiecesOnColumn(1,maphalfto,colnrfrom-dice2)<2 || NrPiecesOnColumn(1,maphalffrom,colnrfrom-dice1)<2)//if one of the dices goes to an empty/1-piece position it's ok
                                    value=3;
                                else
                                    value=0;
                            }
                        }
                    }
                }
                else value=0;
            }
        }
        if(dice1==0 && value == 1)//ignore zeroed dices
            value=0;
        if(dice2==0 && value == 2)
            value=0;
        //System.out.println("VALUEEEEE IS: "+value);
        return value;
    }
    public boolean CheckValidTakeFrom(Player player,int maphalf,int colnr){//Checks if there is a player piece in the half of the gameboard and the col number
        boolean value=false;
        switch(maphalf){
            case 1://top half of the map, act normally
                if(gameboard[0][colnr].getColor()==player.GetColor())//there is a valid mark there
                    value=true;
            break;
            case 2://bottom half, line is 15
                if(gameboard[15][colnr].getColor()==player.GetColor())//there is a valid mark there
                    value=true;
            break;
        }
        return value;
    }
    public int NrPiecesOnColumn(int playercolor,int maphalf,int colnr){//return the number of pieces a player has on a column
        int nr=0,linenr;
        switch(maphalf){
            case 1://top half
                linenr=0;
                while(gameboard[linenr][colnr].getColor()==playercolor && linenr<8){//as long as there are pieces keep going until (worst case) it reaches the half of the map 
                    nr++;
                    linenr++;
                }
            break;
            case 2://bottom half
                linenr=15;
                while(gameboard[linenr][colnr].getColor()==playercolor && linenr>7){
                    nr++;
                    linenr--;
                }
            break;
        }
        return nr;
    }
    public boolean CheckValidMoveTo(Player player, int maphalfto,int maphalffrom, int colnrto, int colnrfrom){//Check if it can move a piece from col x to col y
        boolean value=true;
        int opponentcolor;
        if(player.GetColor()==1)    
            opponentcolor=2;
        else
            opponentcolor=1;
        if((maphalfto+maphalffrom)%2==0){//moving inside the same half
            switch(maphalfto){
                case 1://top half of the map
                    if(player.GetColor()==1 && colnrto>colnrfrom)//can't move a white piece backwards
                        value=false;
                    if(player.GetColor()==2 && colnrto<colnrfrom)//can't move a black piece backwards
                        value=false;
                    if(NrPiecesOnColumn(opponentcolor,maphalfto,colnrto)>1)//can't move where the opponent already has a house
                        value=false;
                break;
                case 2://bottom half of the maph
                    if(player.GetColor()==1 && colnrto<colnrfrom)//can't move a white piece backwards
                        value=false;
                    if(player.GetColor()==2 && colnrto>colnrfrom)//can't move a black piece backwards
                        value=false;
                    if(NrPiecesOnColumn(opponentcolor,maphalfto,colnrto)>1)//can't move where the opponent already has a house
                        value=false;
                break;
            }
        }
        else{//moving from one half to another
            if(player.GetColor()==1 && maphalffrom==2 && maphalfto==1)//can't move a white piece backwards 
                value=false;
            if(player.GetColor()==2 && maphalffrom==1 && maphalfto==2)//can't move a black piece backwards
                value=false;
            if(value==true && maphalfto==2){//Moving a white piece from top to bottom
                if(NrPiecesOnColumn(opponentcolor,maphalfto,colnrto)>1)//can't move where the opponent already has a house
                        value=false;
            }
            if(value==true && maphalfto==1){//Moving a black piece from bottop to top
                if(NrPiecesOnColumn(opponentcolor,maphalfto,colnrto)>1)//can't move where the opponent already has a house
                        value=false;
            }
        }
        //System.out.println("PRINTING CHECK VALID MOVE TO INFORMATION");
       // System.out.println("OPPCOLOR="+opponentcolor+" MAPHALFTO="+maphalfto+" COLNRTO="+colnrto+" AND NrPiecesOnColumn IS:"+NrPiecesOnColumn(opponentcolor,maphalfto,colnrto));
        return value;
    }
    public int FreeSpaceInLine(int maphalf,int colnr){//Returns the number of the first free space in a certain column of pieces on the board
        int nr=-1;
        switch(maphalf){
            case 1:
                nr=0;
                while((gameboard[nr][colnr].getColor()==1 || gameboard[nr][colnr].getColor()==2) && nr<8)//as long as there are pieces in that column
                    nr++;
            break;
            case 2:
                nr=15;
                while((gameboard[nr][colnr].getColor()==1 || gameboard[nr][colnr].getColor()==2) && nr>7)
                    nr--;
            break;
        }
        return nr;
    }
    public int FindLastPiece(int maphalf,int colnr){//Returns the number of the last mark in a certain column of pieces on the board
        int nr=-1;
        switch(maphalf){
            case 1:
                nr=0;
                while((gameboard[nr][colnr].getColor()==1 || gameboard[nr][colnr].getColor()==2) && nr<8)//as long as there are pieces in that column
                    nr++;
                if(nr>0)
                    nr--;
            break;
            case 2:
                nr=15;
                while((gameboard[nr][colnr].getColor()==1 || gameboard[nr][colnr].getColor()==2) && nr>7)
                    nr--;
                if(nr<15)
                    nr++;
            break;
        }
        return nr;
    }
    public boolean CheckMovePieceFromTo(Player player,int maphalffrom,int colnrfrom,int maphalfto,int colnrto){//just combines two previous methods and returns a boolean (to not make the page too messy)
        if(CheckValidTakeFrom(player,maphalffrom,colnrfrom)==true && CheckValidMoveTo(player,maphalfto,maphalffrom,colnrto,colnrfrom)==true)
            return true;
        return false;
    }
    public void MovePieceFromTo(Player player,int maphalffrom,int colnrfrom,int maphalfto,int colnrto){//Moves a piece to the position specified 
        int linenrfrom,linenrto,takefromlinenr,taketolinenr;
        if(maphalffrom==1)
            linenrfrom=0;
        else
            linenrfrom=15;
        if(maphalfto==1)
            linenrto=0;
        else
            linenrto=15;
        takefromlinenr=FreeSpaceInLine(maphalffrom,colnrfrom);//find the piece to take from the column
        if(maphalffrom==1)
            takefromlinenr--;
        else
            takefromlinenr++;
        taketolinenr=FreeSpaceInLine(maphalfto,colnrto);//and find the position to put that piece to
        if(player.GetColor()==1){//white piece
            if(taketolinenr>0 && gameboard[FindLastPiece(maphalfto,colnrto)][colnrto].getColor()==2){//must take a black piece out and replace it with white
                System.out.println("Took one piece out from black");
                gameboard[takefromlinenr][colnrfrom].setColor(3);
                gameboard[FindLastPiece(maphalfto,colnrto)][colnrto].setColor(1);
                BlackRepository.Increment();//And increase the number of black pieces outside the table that must be put back 
               // System.out.println("Black Repo is:"+BlackRepository.PrintRepository());
            } 
            else{//else just change colors normally
                gameboard[takefromlinenr][colnrfrom].setColor(3);
                gameboard[taketolinenr][colnrto].setColor(1);
            }
        }
        else{//black piece
            if(taketolinenr>0 && gameboard[FindLastPiece(maphalfto,colnrto)][colnrto].getColor()==1){//take a white piece out
                System.out.println("Took one piece out from white:");
                gameboard[takefromlinenr][colnrfrom].setColor(3);
                gameboard[FindLastPiece(maphalfto,colnrto)][colnrto].setColor(2);
                WhiteRepository.Increment();
                //System.out.println("white Repo is:"+WhiteRepository.PrintRepository());
            } 
            else{
            gameboard[takefromlinenr][colnrfrom].setColor(3);
            gameboard[taketolinenr][colnrto].setColor(2);
            }
        }
    }
    public static int randInt(int min, int max) {//rolls a dice between 1-6
        Random rand = new Random();// nextInt is normally exclusive of the top value, so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }
    public static void clearScreen() {//Clears the terminal and flushes
    System.out.print("\033[H\033[2J");  
    System.out.flush();  
    }  
    
}
