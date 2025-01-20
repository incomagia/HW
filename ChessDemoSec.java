import java.util.Scanner;

public class ChessDemoSec {

    private static final String EMPTY_CELL = "   ";
    private static final String PLAYER_ONE_PAWN = " p1";
    private static final String PLAYER_TWO_PAWN = " p2";
    private static final int ROWS = 8;
    private static final int COLS = 8;

    private String[][] board = new String[ROWS][COLS];

    private boolean isPlayerOneTurn = true;

    public ChessDemoSec() {
        initializeBoard();
    }

    private void initializeBoard() {
    board[0][0] = PLAYER_ONE_PAWN;
    board[0][1] = PLAYER_ONE_PAWN;
    board[0][2] = PLAYER_ONE_PAWN;
    board[0][3] = PLAYER_ONE_PAWN;
    board[0][4] = PLAYER_ONE_PAWN;
    board[0][5] = PLAYER_ONE_PAWN;
    board[0][6] = PLAYER_ONE_PAWN;
    board[0][7] = PLAYER_ONE_PAWN;
    
    board[1][0] = PLAYER_ONE_PAWN;
    board[1][1] = PLAYER_ONE_PAWN;
    board[1][2] = PLAYER_ONE_PAWN;
    board[1][3] = PLAYER_ONE_PAWN;
    board[1][4] = PLAYER_ONE_PAWN;
    board[1][5] = PLAYER_ONE_PAWN;
    board[1][6] = PLAYER_ONE_PAWN;
    board[1][7] = PLAYER_ONE_PAWN;

    // Initialize Player Two pawns
    board[7][0] = PLAYER_TWO_PAWN;
    board[7][1] = PLAYER_TWO_PAWN;
    board[7][2] = PLAYER_TWO_PAWN;
    board[7][3] = PLAYER_TWO_PAWN;
    board[7][4] = PLAYER_TWO_PAWN;
    board[7][5] = PLAYER_TWO_PAWN;
    board[7][6] = PLAYER_TWO_PAWN;
    board[7][7] = PLAYER_TWO_PAWN;

    board[6][0] = PLAYER_TWO_PAWN;
    board[6][1] = PLAYER_TWO_PAWN;
    board[6][2] = PLAYER_TWO_PAWN;
    board[6][3] = PLAYER_TWO_PAWN;
    board[6][4] = PLAYER_TWO_PAWN;
    board[6][5] = PLAYER_TWO_PAWN;
    board[6][6] = PLAYER_TWO_PAWN;
    board[6][7] = PLAYER_TWO_PAWN;

        for (int i = 2; i < ROWS - 2; i++) {
            for (int j = 0; j < COLS; j++) {
                board[i][j] = EMPTY_CELL;
            }
        }
    }

    private void displayBoard() {
        System.out.println("  +---+---+---+---+---+---+---+---+");
        for (int row = ROWS - 1; row >= 0; row--) {
            System.out.print((row + 1) + " |");
            for (int col = 0; col < COLS; col++) {
                System.out.print((board[row][col] == null ? "   " : board[row][col]) + "|");
            }
            System.out.println("\n  +---+---+---+---+---+---+---+---+");
        }
        System.out.println("    a   b   c   d   e   f   g   h");
    }

    private boolean isValidMove(String input) {
        if (input.length() != 4) {
            return false;
        }

        char startCol = input.charAt(0);
        char startRow = input.charAt(1);
        char endCol = input.charAt(2);
        char endRow = input.charAt(3);

        return startCol >= 'a' && startCol <= 'h' &&
               endCol >= 'a' && endCol <= 'h' &&
               startRow >= '1' && startRow <= '8' &&
               endRow >= '1' && endRow <= '8';
    }

    private boolean processMove(String input) {
        int startCol = input.charAt(0) - 'a';
        int startRow = input.charAt(1) - '1';
        int endCol = input.charAt(2) - 'a';
        int endRow = input.charAt(3) - '1';

        String currentPawn = isPlayerOneTurn ? PLAYER_ONE_PAWN : PLAYER_TWO_PAWN;
        String opponentPawn = isPlayerOneTurn ? PLAYER_TWO_PAWN : PLAYER_ONE_PAWN;

        if (!board[startRow][startCol].equals(currentPawn)) {
            return false;
        }

        int direction = isPlayerOneTurn ? 1 : -1;
        boolean isForwardMove = (endRow - startRow == direction) && (startCol == endCol) && board[endRow][endCol].equals(EMPTY_CELL);
        boolean isCaptureMove = (endRow - startRow == direction) && (Math.abs(endCol - startCol) == 1) && board[endRow][endCol].equals(opponentPawn);

        if (isForwardMove || isCaptureMove) {
            board[startRow][startCol] = EMPTY_CELL;
            board[endRow][endCol] = currentPawn;
            return true;
        }

        return false;
    }

    private boolean checkForTie(String input, String lastInput) {
        return input.equalsIgnoreCase("tie") && lastInput.equalsIgnoreCase("tie");
    }

    public void startGame() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Добро пожаловать в демо шахматы!");

        String lastInput = "";
        boolean tieRequested = false;

        while (true) {
            displayBoard();
            String currentPlayer = isPlayerOneTurn ? "Игрок 1 (p1)" : "Игрок 2 (p2)";
            System.out.println(currentPlayer + ", ваш ход! Введите ход (например, a1a2) или 'tie' для ничьей:");

            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("tie")) {
                if (tieRequested) {
                    System.out.println("Игра окончена! Ничья по соглашению игроков.");
                    break;
                } else {
                    System.out.println(currentPlayer + " предложил ничью. Ожидание ответа другого игрока.");
                    tieRequested = true;
                    isPlayerOneTurn = !isPlayerOneTurn;
                    continue;
                }
            } else {
                tieRequested = false;
            }

            if (!isValidMove(input)) {
                System.out.println("Неверный формат хода. Попробуйте ещё раз.");
                continue;
            }

            if (!processMove(input)) {
                System.out.println("Недопустимый ход. Попробуйте ещё раз.");
                continue;
            }

            if (isGameOver()) {
                displayBoard();
                System.out.println(currentPlayer + " выигрывает игру! Поздравляем!");
                break;
            }

            isPlayerOneTurn = !isPlayerOneTurn;
        }

        scanner.close();
    }

    private boolean isGameOver() {
        boolean playerOneHasPawns = false;
        boolean playerTwoHasPawns = false;

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (board[row][col].equals(PLAYER_ONE_PAWN)) {
                    playerOneHasPawns = true;
                }
                if (board[row][col].equals(PLAYER_TWO_PAWN)) {
                    playerTwoHasPawns = true;
                }
            }
        }

        return !playerOneHasPawns || !playerTwoHasPawns;
    }

    public static void main(String[] args) {
        ChessDemoSec game = new ChessDemoSec();
        game.startGame();
    }
}
