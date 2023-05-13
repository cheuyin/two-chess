//package ui;
//
//import model.Board;
//import model.pieces.Piece;
//import persistence.JsonReader;
//import persistence.JsonWriter;
//
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.util.List;
//import java.util.Scanner;
//
//// UI for the chess game. Processes user input to move pieces, start new matches, view move histories, store data,
//// and more
//public class Game {
//    private boolean resetMatch;
//    private Board board;
//    private final Scanner input;
//    private static final String JSON_STORE = "./data/board.json";
//    private JsonWriter jsonWriter;
//    private JsonReader jsonReader;
//
//    // EFFECTS: creates and runs a new chess game
//    public Game() {
//        this.input = new Scanner(System.in);
//        this.board = new Board();
//        this.jsonWriter = new JsonWriter(JSON_STORE);
//        this.jsonReader = new JsonReader(JSON_STORE);
//
//        runGame();
//    }
//
//    // MODIFIES: this
//    // EFFECTS: runs the chess game in a loop until game exits
//    public void runGame() {
//        handleGameStart();
//
//        while (true) {
//            resetMatch = false;
//            System.out.print("Press m to access the menu, or any other key to skip: ");
//            String menuCommand = input.nextLine();
//            System.out.println();
//
//            if (menuCommand.equals("m")) {
//                displayMenu();
//                if (resetMatch) {
//                    continue;
//                }
//            }
//
//            makePlayerMove();
//
//            if (board.getGameOver()) {
//                System.out.println("Game Over! " + board.getWinner() + " wins!");
//                displayMenuGameOver();
//            }
//        }
//    }
//
//    // EFFECTS: show game exit message and menu
//    private void handleGameExit() {
//        while (true) {
//            System.out.print("Do you want to save the game? Enter y or n: ");
//
//            String saveGame = input.next();
//            input.nextLine();
//
//            if (!saveGame.equals("n") & !saveGame.equals("y")) {
//                System.out.println("Your selection is invalid. Please try again.");
//                continue;
//            } else if (saveGame.equals("n")) {
//                break;
//            } else if (saveGame.equals("y")) {
//                saveBoard();
//                break;
//            }
//        }
//
//        exitGame();
//    }
//
//    // EFFECTS: show game start message and menu
//    private void handleGameStart() {
//        System.out.println("===== Hello, welcome to TwoChess! ======\n");
//
//        while (true) {
//            System.out.print("Do you want to load a previous game? Enter y or n: ");
//
//            String loadGame = input.next();
//            input.nextLine();
//
//            if (!loadGame.equals("n") & !loadGame.equals("y")) {
//                System.out.println("Your selection is invalid. Please try again.");
//                continue;
//            } else if (loadGame.equals("n")) {
//                System.out.println("A new game will begin.");
//                break;
//            } else if (loadGame.equals("y")) {
//                System.out.println("Previous game will be loaded.");
//                loadBoard();
//                if (board.getGameOver()) {
//                    System.out.println("This game is already over!");
//                    displayMenuGameOver();
//                }
//                break;
//            }
//        }
//    }
//
//    // MODIFIES: this
//    // EFFECTS: make the current turn player move a piece
//    private void makePlayerMove() {
//        String originalTurn = capitalize(board.getCurrentTurn().toString());
//        printCurrentTurnMessage();
//        String chosenPiece = retrieveChosenPiece();
//        String chosenMove = retrieveChosenMove(chosenPiece);
//        board.makeMove(chosenPiece, chosenMove);
//        System.out.println(originalTurn + " made the move " + board.getMostRecentMove() + ".\n");
//    }
//
//    // EFFECTS: retrieve coordinate of piece that the current player wants to move
//    private String retrieveChosenPiece() {
//        int numAvailablePieces = board.getNumAvailablePieces();
//        List<String> availableCoords = board.getAvailableCoords();
//        String currentSelection;
//
//        System.out.println("You have " + numAvailablePieces + " pieces.");
//
//        while (true) {
//            System.out.print("Your pieces are at " + getAvailableCoordsString() + ".\nSelect a piece: ");
//
//            currentSelection = input.next().toLowerCase();
//            input.nextLine();
//            System.out.println();
//
//            if (!availableCoords.contains(currentSelection)) {
//                System.out.println("Sorry. There is not a valid piece at " + currentSelection + ".Please try again.");
//                continue;
//            }
//
//            String selectedPieceType = capitalize(board.getPiece(currentSelection).pieceType.toString());
//            System.out.println("You selected a " + selectedPieceType + ".");
//
//            if (noLegalMoves(currentSelection)) {
//                System.out.println("Sorry, this piece has no legal moves. Please select another.\n");
//                continue;
//            }
//
//            break;
//        }
//
//        return currentSelection;
//    }
//
//    // REQUIRES: chosenPiece is the coordinate of the piece that the player has just decided to move
//    // EFFECTS: retrieve from the current turn player the coordinate that they want to move their chosen piece to
//    private String retrieveChosenMove(String chosenPiece) {
//        boolean inputValid = false;
//        Piece piece = board.getPiece(chosenPiece);
//        List<String> listOfLegalMoves = piece.getLegalMoves();
//        String choice = null;
//
//        System.out.println("This piece has legal moves at " + getLegalMovesString(piece));
//
//        while (!inputValid) {
//            System.out.print("Enter a move from one of the available squares: ");
//
//            choice = input.next();
//            input.nextLine();
//
//            if (listOfLegalMoves.contains(choice)) {
//                inputValid = true;
//            } else {
//                System.out.println(choice + " is not a valid selection. Try again.");
//            }
//        }
//
//        return choice;
//    }
//
//    // EFFECTS: shows the in-game menu
//    private void displayMenu() {
//        printFullMenuOptions();
//        loop: while (true) {
//            System.out.print("Choice: ");
//            String selection = input.next();
//            input.nextLine();
//            System.out.println();
//
//            switch (selection) {
//                case "1":
//                    break loop;
//                case "2":
//                    printMoveHistory();
//                    break;
//                case "3":
//                    resetMatch();
//                    break loop;
//                case "4":
//                    handleGameExit();
//                    break loop;
//                default:
//                    System.out.println("Selection invalid. Try again.");
//            }
//        }
//    }
//
//    // EFFECTS: shows the modified menu for when the game is over and one player has won
//    private void displayMenuGameOver() {
//        System.out.println("\t1. Show move history");
//        System.out.println("\t2. New match");
//        System.out.println("\t3. Exit game");
//        loop: while (true) {
//            System.out.print("Choice: ");
//            String selection = input.next();
//            input.nextLine();
//            System.out.println();
//
//            switch (selection) {
//                case "1":
//                    printMoveHistory();
//                    break;
//                case "2":
//                    resetMatch();
//                    break loop;
//                case "3":
//                    handleGameExit();
//                    break loop;
//                default:
//                    System.out.println("Selection invalid. Try again.");
//            }
//        }
//    }
//
//    // MODIFIES: this
//    // EFFECTS: resets the current match by creating a new board and resetting the game variables
//    public void resetMatch() {
//        this.board = new Board();
//        this.resetMatch = true;
//
//        System.out.println("Match is reset!\n");
//    }
//
//    // EFFECTS: stops the game loop
//    public void exitGame() {
//        System.out.println("Exiting the game!");
//        System.exit(0);
//    }
//
//    // ===== print functions =====
//
//    // EFFECTS: prints all the selection options for the full in-game menu
//    private void printFullMenuOptions() {
//        System.out.println("===== MENU =====");
//        System.out.println("\t1. Continue match");
//        System.out.println("\t2. Show move history");
//        System.out.println("\t3. Reset match");
//        System.out.println("\t4. Exit game");
//    }
//
//    // EFFECTS: prints current move history of the game
//    public void printMoveHistory() {
//        System.out.println();
//        System.out.println("Move history: " + board.getMoveHistory());
//        System.out.println();
//    }
//
//    // EFFECTS: prints message describing which player's turn it is to move
//    private void printCurrentTurnMessage() {
//        String currentPlayer = capitalize(board.getCurrentTurn().toString());
//        System.out.println("It is " + currentPlayer + "'s turn.");
//    }
//
//    // ===== helpers =====
//
//    // EFFECTS: saves the current game to file
//    public void saveBoard() {
//        try {
//            jsonWriter.open();
//            jsonWriter.write(board);
//            jsonWriter.close();
//            System.out.println("Saved board to " + JSON_STORE);
//        } catch (FileNotFoundException e) {
//            System.out.println("Unable to write to file: " + JSON_STORE);
//        }
//    }
//
//    // MODIFIES: this
//    // EFFECTS: loads stored game board into current game
//    public void loadBoard() {
//        try {
//            Board board = jsonReader.read();
//            this.board = board;
//            System.out.println("Loaded board from " + JSON_STORE);
//        } catch (IOException e) {
//            System.out.println("Unable to read from file: " + JSON_STORE);
//        }
//    }
//
//    // EFFECTS: returns the given string with the first character in uppercase and the rest in lowercase
//    private String capitalize(String str) {
//        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
//    }
//
//    // REQUIRES: piece at currentSelection is not null
//    // EFFECTS: returns true if the piece at currentSelection has no legal moves
//    //          else false
//    private boolean noLegalMoves(String currentSelection) {
//        return board.getPiece(currentSelection).getLegalMoves().size() == 0;
//    }
//
//    // EFFECTS: retrieve the available coordinates to a player as a formatted string
//    //          e.g. "e5, e2, g5"
//    public String getAvailableCoordsString() {
//        return String.join(", ", board.getAvailableCoords()).trim();
//    }
//
//    // EFFECTS: return list of legal moves available to a piece as a formatted string
//    //          e.g. "e4, e5, b4"
//    public String getLegalMovesString(Piece p) {
//        List<String> legalMoves = p.getLegalMoves();
//        return String.join(", ", legalMoves).trim();
//    }
//}
