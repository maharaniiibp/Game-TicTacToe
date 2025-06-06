import java.util.Random;
import java.util.Scanner;

public class Board {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // Coin flip untuk tentukan giliran
        System.out.println("Tic-Tac-Toe: Human (O) vs Komputer (X)");
        int turn = coinFlip(scanner);
        Board x = new Board(turn);
        System.out.println((turn == 1 ? "Human (O)" : "Komputer (X)") + " mulai duluan!");
        System.out.println("Masukkan baris (0-2) dan kolom (0-2) dipisah spasi.");
        
        while (!x.gameOver()) {
            x.disp();
            if (x.turn == 1) { // Giliran human
                System.out.print("Langkahmu (baris kolom): ");
                try {
                    int row = scanner.nextInt();
                    int col = scanner.nextInt();
                    if (!x.setBoard(row, col)) {
                        System.out.println("Langkah tidak valid! Coba lagi.");
                        continue;
                    }
                } catch (Exception e) {
                    System.out.println("Input salah! Masukkan dua angka (0-2).");
                    scanner.nextLine(); // Bersihkan input
                    continue;
                }
            } else { // Giliran komputer
                System.out.println("Langkah komputer:");
                x.computerMove();
            }
        }
        
        // Tampilkan hasil akhir
        x.disp();
        int result = x.winner();
        if (result == 1) {
            System.out.println("Human (O) menang!");
        } else if (result == -1) {
            System.out.println("Komputer (X) menang!");
        } else {
            System.out.println("Seri!");
        }
        scanner.close();
    }
    
    private int[][] data;   // menyimpan nilai 0 / 1 / -1
    private int turn;       // menyimpan nilai 1 / -1
    
    /*
    constructor
    inisialisasi board dengan nilai 0
    dan turn dengan 1 atau -1 sesuai input user
    */
    public Board(int turn ){
        this.data = new int[3][3];
        this.turn = turn;
    }
    
    // cetak board di layar
    public void disp(){
        for(int i=0; i<3; i++){
            for(int j=0;j<3;j++){
                switch(this.data[i][j]){
                    case 0  -> System.out.print("  -  ");
                    case -1 -> System.out.print("  X  ");
                    case 1  -> System.out.print("  O  ");
                }
            }
            System.out.println();
        }
        System.out.println("\n\n");
    }
    
    /*
    setting board pada baris dan kolom yang telah ditentukan
    dengan nilai 1 atau -1 sesuai dengan giliran/ turn
    */
    public boolean setBoard(int brs, int kol){
        if(this.data[brs][kol]==0){
            this.data[brs][kol] = turn;
            turn = - turn;
            return true;
        }
        else
            return false;
    }
    
    public int winner(){
        // Cek baris
        for (int i = 0; i < 3; i++) {
            if (data[i][0] != 0 && data[i][0] == data[i][1] && data[i][1] == data[i][2]) {
                return data[i][0];
            }
        }
        // Cek kolom
        for (int j = 0; j < 3; j++) {
            if (data[0][j] != 0 && data[0][j] == data[1][j] && data[1][j] == data[2][j]) {
                return data[0][j];
            }
        }
        // Cek diagonal
        if (data[0][0] != 0 && data[0][0] == data[1][1] && data[1][1] == data[2][2]) {
            return data[0][0];
        }
        if (data[0][2] != 0 && data[0][2] == data[1][1] && data[1][1] == data[2][0]) {
            return data[0][2];
        }
        return 0; // Belum ada pemenang
    }
    
    public boolean gameOver(){
        // Ada pemenang
        if (winner() != 0) {
            return true;
        }
        // Cek papan penuh (seri)
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (data[i][j] == 0) {
                    return false; // Masih ada sel kosong
                }
            }
        }
        return true; // Papan penuh, seri
    }
    
    public void resetBoard(){
        for(int i=0; i<3; i++)
            for(int j=0; j<3; j++)
                this.data[i][j] = 0;
    }
    
    // Langkah komputer menggunakan Minimax
    public void computerMove() {
        int[] bestMove = minimax(0, -1); // -1 untuk komputer (X)
        setBoard(bestMove[1], bestMove[2]);
    }
    
    // Algoritma Minimax untuk pilih langkah terbaik
    private int[] minimax(int depth, int player) {
        int[] best = {0, -1, -1}; // {skor, baris, kolom}
        int bestScore = (player == -1) ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        // Kasus dasar: cek jika permainan selesai
        int win = winner();
        if (win == -1) return new int[]{-10 - depth, -1, -1}; // Komputer menang
        if (win == 1) return new int[]{10 + depth, -1, -1};   // Human menang
        if (gameOver()) return new int[]{0, -1, -1};          // Seri

        // Coba semua langkah yang mungkin
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (data[i][j] == 0) {
                    // Lakukan langkah
                    data[i][j] = player;
                    turn = -player; // Simulasi ganti giliran
                    int[] score = minimax(depth + 1, -player);
                    data[i][j] = 0; // Undo langkah
                    turn = player;  // Kembalikan giliran

                    // Update langkah terbaik untuk komputer (maksimasi) atau human (minimasi)
                    if (player == -1 && score[0] > bestScore) {
                        bestScore = score[0];
                        best[1] = i;
                        best[2] = j;
                    } else if (player == 1 && score[0] < bestScore) {
                        bestScore = score[0];
                        best[1] = i;
                        best[2] = j;
                    }
                }
            }
        }
        best[0] = bestScore;
        return best;
    }

    // Fungsi coin flip untuk tentukan giliran awal
    private static int coinFlip(Scanner scanner) {
        Random rand = new Random();
        System.out.print("Pilih head atau tails: ");
        String playerChoice = scanner.nextLine().trim().toLowerCase();
        while (!playerChoice.equals("head") && !playerChoice.equals("tails")) {
            System.out.print("Input salah! Pilih head atau tails: ");
            playerChoice = scanner.nextLine().trim().toLowerCase();
        }
        
        int randomNum = rand.nextInt(100) + 1; // Acak 1-100
        String coinResult = (randomNum % 2 == 0) ? "tails" : "head";
        System.out.println("Hasil coin flip: " + coinResult);
        
        return (playerChoice.equals(coinResult)) ? 1 : -1; // 1 untuk human, -1 untuk komputer
    }
   
}
