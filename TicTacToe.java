public class TicTacToe {

    public static void main(String[] args) {
        
        Board x;
        int turn = -1;
        x = new Board(turn);
        x.disp();
        x.setBoard(1,1);
        x.disp();
        x.setBoard(1,0);
        x.disp();
        x.resetBoard();
        x.disp();
    }
    
}
