import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class Backgammon{

    private static String name;
    private static byte color;

    public static void main(String[] args){
        int i=0,j=0,dice1,dice2;
        Scanner input = new Scanner(System.in);
        System.out.println("Player 1 data:");System.out.println();System.out.println("Input name:"); name = input.nextLine();
        do{
            System.out.println("Choose color: 1.White 2.Black"); 
            color = input.nextByte(); 
            input.nextLine();
        }while(color != 1 && color != 2);//force a color
        Player p1 = new Player(name,color);//create a player with the name and color
        System.out.println("\nPlayer 2 data:\nInput name:\n");
        name = input.nextLine();
        if(color==1)
            color=2;
        else
            color=1;
        Player p2 = new Player(name,color);//create the other player using the input name and the opposite color
        GameBoard board = new GameBoard();//Allocate memory for a new (empty) game board
        for(i=0;i<16;i++)
            for(j=0;j<12;j++){
                    BoardCell cell = new BoardCell(3); //creates 16x12 boardcells
                    board.CellToBoard(i,j,cell);//and puts them into the gameboard
            }
        clearScreen();//clear the terminal (make the board align in the top left corner of the terminal to look nicer
        board.InitializeBoard(); 
        board.PrintGameBoard();
        System.out.println("Let's roll to see who starts first: rolling the dice");
        do{
            dice1= randInt(1,6);
            dice2= randInt(1,6);
        }while(dice1==dice2); //Manually set dices to test special cases if needed
        System.out.println("Dices: "+p1.GetName()+" rolled " +dice1+" and "+p2.GetName()+" rolled "+dice2);
        if(dice1>dice2){//create a game with the two players, the one with the higher dice will always be the first one in the board.PlayPvPGame method
            System.out.println(p1.GetName()+" starts");
            board.PlayPvPGame(p1,p2);
        }
        else{
            System.out.println(p2.GetName()+" starts");
            board.PlayPvPGame(p2,p1);
        }
    }
    private static int randInt(int min, int max) {//Generates a random number for the dices
    Random rand = new Random();// Usually this can be a field rather than a method variable
    // nextInt is normally exclusive of the top value, so add 1 to make it inclusive
    int randomNum = rand.nextInt((max - min) + 1) + min;
    return randomNum;
    }
    private static void clearScreen() {//Clears the terminal and flushes
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
    } 
}
