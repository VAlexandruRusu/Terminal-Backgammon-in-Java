class Player{

    private String name;
    private byte color;
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";
    Player(String name,byte color){
        this.name=name;
        this.color=color;
    }
    public String GetName(){return name;}
    public byte GetColor(){return color;}
    public void PrintData(){
        if(color==1)
            System.out.println(ANSI_WHITE + "Name: " + name + "\n" + "White" + ANSI_RESET);
        else
            System.out.println(ANSI_BLACK + "Name: "+ name + "\n"  + "Black" + ANSI_RESET);
    }
}
