package model;

import model.enums.Action;
import model.enums.Side;
import model.enums.Type;
import model.pieces.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

// Representation of the chess board where a game is played. Squares are denoted by standard algebraic chess notation.
// Contains functionality for moving pieces, checking for mates, keeping a move history, and more.
public class Board {
    private final HashMap<String, Piece> board = new HashMap<>();
    public static final List<String> COLUMNS = new ArrayList<>(Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h"));
    public static final List<String> ROWS = new ArrayList<>(Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8"));
    private Side currentTurn;
    private MoveList moveList;
    private boolean gameOver;
    private EventLog eventLog = EventLog.getInstance();

    // EFFECTS: creates new 8 x 8 chess board with squares using algebraic notation (e.g. "a4") for coordinates
    //          initialize White and Black pieces at their starting positions
    //          create a new move history to keep track of all the moves in the game
    public Board() {
        this.currentTurn = Side.WHITE;
        this.moveList = new MoveList();
        this.gameOver = false;

        for (String col : COLUMNS) {
            for (String row : ROWS) {
                String coordinate = col + row;
                this.board.put(coordinate, null);
            }
        }

        initWhitePieces();
        initBlackPieces();
    }

    // MODIFIES: this
    // EFFECTS: create and set all White pieces at their initial positions
    private void initWhitePieces() {
        // initialize pawns
        for (int colIndex = 0; colIndex < 8; colIndex++) {
            String pos = Board.COLUMNS.get(colIndex) + 2;
            Piece p = new Pawn(Side.WHITE, pos, this);
            board.put(pos, p);
        }
        // rooks
        Piece r1 = new Rook(Side.WHITE, "a1", this);
        Piece r2 = new Rook(Side.WHITE, "h1", this);
        board.put("a1", r1);
        board.put("h1", r2);

        // knights
        Piece n1 = new Knight(Side.WHITE, "b1", this);
        Piece n2 = new Knight(Side.WHITE, "g1", this);
        board.put("b1", n1);
        board.put("g1", n2);

        // bishops
        Piece b1 = new Bishop(Side.WHITE, "c1", this);
        Piece b2 = new Bishop(Side.WHITE, "f1", this);
        board.put("c1", b1);
        board.put("f1", b2);

        // king
        Piece k = new King(Side.WHITE, "e1", this);
        board.put("e1", k);

        // queen
        Piece q = new Queen(Side.WHITE, "d1", this);
        board.put("d1", q);
    }

    // MODIFIES this
    // EFFECTS: create and set all Black pieces at their initial positions
    private void initBlackPieces() {
        // initialize pawns
        for (int colIndex = 0; colIndex < 8; colIndex++) {
            String pos = Board.COLUMNS.get(colIndex) + 7;
            Piece p = new Pawn(Side.BLACK, pos, this);
            board.put(pos, p);
        }
        // rooks
        Piece r1 = new Rook(Side.BLACK, "a8", this);
        Piece r2 = new Rook(Side.BLACK, "h8", this);
        board.put("a8", r1);
        board.put("h8", r2);

        // knights
        Piece n1 = new Knight(Side.BLACK, "b8", this);
        Piece n2 = new Knight(Side.BLACK, "g8", this);
        board.put("b8", n1);
        board.put("g8", n2);

        // bishops
        Piece b1 = new Bishop(Side.BLACK, "c8", this);
        Piece b2 = new Bishop(Side.BLACK, "f8", this);
        board.put("c8", b1);
        board.put("f8", b2);

        // king
        Piece k = new King(Side.BLACK, "e8", this);
        board.put("e8", k);

        // queen
        Piece q = new Queen(Side.BLACK, "d8", this);
        board.put("d8", q);
    }

    // REQUIRES: there is a piece at fromPos and
    //           move from fromPos to toPos is a valid move for that piece
    // MODIFIES: this, piece
    // EFFECTS: moves the piece at fromPos to toPos
    //          makes it the opponent's turn to move
    //          checks if opponent is checkmated after this move
    //          add move to move history
    public void makeMove(String fromPos, String toPos) {
        Piece fromPiece = getPiece(fromPos);
        Piece toPiece = getPiece(toPos);

        movePiece(fromPos, toPos);
        currentTurn = (currentTurn == Side.WHITE) ? Side.BLACK : Side.WHITE;

        Move move = constructMove(fromPiece, fromPos, toPiece, toPos);
        moveList.addMove(move);

        if (isCheckmate()) {
            gameOver = true;
        }

        eventLog.logEvent(new Event("Piece moved from " + fromPos + " to " + toPos));
    }

    // REQUIRES: all parameters are accurate data of an actual move in a game
    // EFFECTS: Returns a new move object constructed from the given data
    private Move constructMove(Piece fromPiece, String fromPos, Piece toPiece, String toPos) {
        List<Action> actions = new ArrayList<>();
        Side fromSide = fromPiece.getSide();
        Type fromType = fromPiece.pieceType;
        String fromId = fromPiece.id;
        Side toSide = fromSide == Side.WHITE ? Side.BLACK : Side.WHITE;
        Type toType = toPiece == null ? Type.EMPTY : toPiece.pieceType;

        if (toPiece != null) {
            actions.add(Action.TAKE);
        }

        if (getGameOver()) {
            actions.add(Action.CHECKMATE);
        } else if (isCheck()) {
            actions.add(Action.CHECK);
        }

        return new Move(fromSide, fromType, fromPos, fromId, actions, toSide, toType, toPos);
    }

    // REQUIRES: there is a piece at fromPos and
    //           move from fromPos to toPos is a valid move for that piece
    // MODIFIES: this, piece
    // EFFECTS: moves piece at one position to another position on the board
    //          remove any opponent piece already at that position
    private void movePiece(String fromPos, String toPos) {
        Piece p = getPiece(fromPos);
        p.setPos(toPos);
        board.put(toPos, p);
        board.put(fromPos, null);
    }

    // EFFECTS: return true if current turn player is checkmated and false otherwise
    public boolean isCheckmate() {
        List<Piece> currentTurnPieces = new ArrayList<>();
        for (String move : board.keySet()) {
            if (board.get(move) != null && getPiece(move).getSide() == currentTurn) {
                currentTurnPieces.add(getPiece(move));
            }
        }

        for (Piece p : currentTurnPieces) {
            List<String> legalMoves = p.getLegalMoves();
            if (legalMoves.size() != 0) {
                return false;
            }
        }

        return true;
    }

    // EFFECTS: return true if current turn player is checked
    public boolean isCheck() {
        boolean result = false;
        for (String pos : board.keySet()) {
            if (getPiece(pos) != null) {
                List<String> legalMovesForThisPiece = getPiece(pos).getLegalMovesViolateCheck();
                for (String move : legalMovesForThisPiece) {
                    Piece newMove = getPiece(move);
                    boolean conditions = newMove != null && newMove.pieceType == Type.KING
                            && newMove.getSide() == currentTurn;
                    if (conditions) {
                        result = true;
                        break;
                    }
                }
            }
        }

        return result;
    }


    // REQUIRES: there is a piece at fromPos and
    //           move from fromPos to toPos is a valid move for that piece
    // MODIFIES: this, piece
    // EFFECTS: return true if given move will violate check
    //          else return false
    public boolean willViolateCheck(String fromPos, String toPos) {
        Piece tempPiece = getPiece(toPos);
        Piece tempPiece2 = getPiece(fromPos);
        Side pieceColor = tempPiece2.getSide();

        testMove(fromPos, toPos, tempPiece, tempPiece2, false);

        for (String pos : board.keySet()) {
            if (getPiece(pos) != null) {
                List<String> legalMovesForThisPiece = getPiece(pos).getLegalMovesViolateCheck();
                for (String move : legalMovesForThisPiece) {
                    Piece newMove = getPiece(move);
                    if (newMove != null && newMove.pieceType == Type.KING && newMove.getSide() == pieceColor) {
                        testMove(fromPos, toPos, tempPiece, tempPiece2, true);
                        return true;
                    }
                }
            }
        }

        testMove(fromPos, toPos, tempPiece, tempPiece2, true);
        return false;
    }

    // MODIFIES: this, piece
    // EFFECTS: helper method that performs a "test move" for legal move generation purposes
    //          makes a "move" on the chess board with option to revert back to original state
    private void testMove(String fromPos, String toPos, Piece tempPiece, Piece tempPiece2, boolean reset) {
        if (!reset) {
            board.put(toPos, tempPiece2);
            if (tempPiece2.pieceType == Type.PAWN) {
                tempPiece2.setPos(toPos, false);
            } else {
                tempPiece2.setPos(toPos);
            }
            board.put(fromPos, null);
        } else {
            board.put(toPos, tempPiece);
            board.put(fromPos, tempPiece2);

            // reset temp piece position back to original
            if (tempPiece2.pieceType == Type.PAWN) {
                tempPiece2.setPos(fromPos, false);
            } else {
                tempPiece2.setPos(fromPos);
            }
        }
    }

    // EFFECTS: removes all pieces from the board by setting the value at a position to null
    public void clearBoard() {
        for (String pos : getAllPositions()) {
            board.put(pos, null);
        }
    }

    // EFFECTS: adds given formatted move to the game's move history
    public void addMoveToMoveList(Move move) {
        moveList.addMove(move);
    }

    // ===== getters =====

    // EFFECTS: return formatted string of all the moves so far in the game
    public MoveList getMoveList() {
        return moveList;
    }

    // EFFECTS: retrieve a list of coordinates holding the pieces available to the current turn player
    //          e.g. ["e5", "e2", "g5"]
    public List<String> getAvailableCoords() {
        List<String> res = new ArrayList<>();
        for (String coord: board.keySet()) {
            if (getPiece(coord) != null && getPiece(coord).getSide() == currentTurn) {
                res.add(coord);
            }
        }

        return res;
    }

    // EFFECTS: get number of pieces on the board available to the current player
    public int getNumAvailablePieces() {
        return getAvailableCoords().size();
    }

    // EFFECTS: returns a list of length 64 of the coordinates of all the squares of the chess board
    public List<String> getAllPositions() {
        return new ArrayList<>(board.keySet());
    }

    // REQUIRES: pos is a valid position on the chess board
    // EFFECTS: return the piece at the given position or null if empty
    public Piece getPiece(String pos) {
        return board.get(pos);
    }

    public Side getCurrentTurn() {
        return currentTurn;
    }

    // EFFECTS: returns if the game is over or not (checkmate)
    public boolean getGameOver() {
        return gameOver;
    }

    // REQUIRES: one player has won the game and the game is over
    // EFFECTS: returns a formatted string of the game's winner, i.e. "White" or "Black"
    public String getWinner() {
        Side winnerColor = currentTurn == Side.WHITE ? Side.BLACK : Side.WHITE;
        String winner = winnerColor.toString();
        return winner.substring(0, 1).toUpperCase() + winner.substring(1).toLowerCase();
    }

    // ===== setters =====
    public void setCurrentTurn(Side currentTurn) {
        this.currentTurn = currentTurn;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public void setPiece(String pos, Piece p) {
        board.put(pos, p);
    }

    public void setMoveList(MoveList ml) {
        this.moveList = ml;
    }

    // ===== JSON functionality =====

    // EFFECTS: returns a JSON object representing this board
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("currentTurn", currentTurn);
        json.put("gameOver", gameOver);
        json.put("moveList", moveList.toJsonArray());
        json.put("pieces", piecesToJson());
        return json;
    }

    // EFFECTS: return representation of pieces on board as a JSON array
    private JSONArray piecesToJson() {
        JSONArray pieces = new JSONArray();
        for (String c : getAllPositions()) {
            Piece p = getPiece(c);
            if (p != null) {
                pieces.put(p.toJson());
            }
        }

        return pieces;
    }

}
