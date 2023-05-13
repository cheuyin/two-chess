package model.pieces;

import model.Board;
import model.enums.Side;
import model.enums.Type;
import org.json.JSONObject;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

// A general chess piece. Contains data for the piece's color, board, type, position, and more. Main functionality
// is to generate a list of valid moves for a piece given its position
public abstract class Piece {
    protected final Side side;
    protected final Board board;
    public final Type pieceType;
    public final String id;
    protected String position;

    // EFFECTS: creates a new chess piece with the given color, initial position, chess board, type, and algebraic id
    public Piece(Side side, String initPos, Board board, Type pieceType, String id) {
        this.side = side;
        this.position = initPos;
        this.board = board;
        this.pieceType = pieceType;
        this.id = id;
    }

    // MODIFIES: legalMoves
    // EFFECTS:  add all the moves a piece can make without robust consideration for its position on the board
    protected abstract void addAllMoves(List<String> legalMoves);

    // REQUIRES: colIndex and currRow represent the current column index and row number of a piece, respectively
    // MODIFIES: legalMoves
    // EFFECTS: adds all the available horizontal moves for a rook-like piece, up to and including positions occupied by
    //          another piece
    protected void addHorizontalMoves(int colIndex, int currRow, List<String> legalMoves) {
        for (int i = colIndex + 1; i < 8; i++) {
            String newPos1 = Board.COLUMNS.get(i) + currRow;
            legalMoves.add(newPos1);
            if (board.getPiece(newPos1) != null) {
                break;
            }
        }

        for (int i = colIndex - 1; i >= 0; i--) {
            String newPos2 = Board.COLUMNS.get(i) + currRow;
            legalMoves.add(newPos2);
            if (board.getPiece(newPos2) != null) {
                break;
            }
        }
    }

    // REQUIRES: colIndex and currRow represent the current column index and row number of the piece, respectively
    // MODIFIES: legalMoves
    // EFFECTS: adds all the vertical moves for a rook-like piece, up to and including positions occupied by
    //          another piece
    protected void addVerticalMoves(int colIndex, int currRow, List<String> legalMoves) {
        for (int i = currRow + 1; i < 9; i++) {
            String newPos1 = Board.COLUMNS.get(colIndex) + i;
            legalMoves.add(newPos1);
            if (board.getPiece(newPos1) != null) {
                break;
            }
        }

        for (int i = currRow - 1; i >= 0; i--) {
            String newPos2 = Board.COLUMNS.get(colIndex) + i;
            legalMoves.add(newPos2);
            if (board.getPiece(newPos2) != null) {
                break;
            }
        }
    }

    // REQUIRES: colIndex and currRow represent the current column index and row number of the piece, respectively
    // MODIFIES: legalMoves
    // EFFECTS: adds all the diagonal moves for a bishop-like piece for columns with a greater index than current
    //          stops up to and including a position that contains another piece
    protected void addDiagonalMovesColIndexGreater(int colIndex, int currRow, List<String> legalMoves) {
        int rowIndex1 = currRow + 1;
        for (int i = colIndex + 1; i < 8; i++) {
            String newPos1 = Board.COLUMNS.get(i) + rowIndex1;
            legalMoves.add(newPos1);
            if (board.getPiece(newPos1) != null) {
                break;
            }
            rowIndex1++;
        }

        int rowIndex2 = currRow - 1;
        for (int i = colIndex + 1; i < 8; i++) {
            String newPos2 = Board.COLUMNS.get(i) + rowIndex2;
            legalMoves.add(newPos2);
            if (board.getPiece(newPos2) != null) {
                break;
            }
            rowIndex2--;
        }
    }

    // REQUIRES: colIndex and currRow represent the current column index and row number of the piece, respectively
    // MODIFIES: legalMoves
    // EFFECTS: adds all the diagonal moves for a bishop-like piece for columns with a lesser index than current
    //          stops up to and including a position that contains another piece
    protected void addDiagonalMovesColIndexLesser(int colIndex, int currRow, List<String> legalMoves) {
        int rowIndex1 = currRow + 1;
        for (int i = colIndex - 1; i >= 0; i--) {
            String newPos1 = Board.COLUMNS.get(i) + rowIndex1;
            legalMoves.add(newPos1);
            if (board.getPiece(newPos1) != null) {
                break;
            }
            rowIndex1++;
        }

        int rowIndex2 = currRow - 1;
        for (int i = colIndex - 1; i >= 0; i--) {
            String newPos2 = Board.COLUMNS.get(i) + rowIndex2;
            legalMoves.add(newPos2);
            if (board.getPiece(newPos2) != null) {
                break;
            }
            rowIndex2--;
        }
    }

    // MODIFIES: legalMoves
    // EFFECTS:  remove from legalMoves any moves that are out of bounds of the chess board
    protected void removeOutOfBoundsMoves(List<String> legalMoves) {
        List<String> boardPositions = board.getAllPositions();
        legalMoves.removeIf(move -> !boardPositions.contains(move));
    }

    // MODIFIES: legalMoves
    // EFFECTS:  remove from legalMoves any moves that target a piece's own teammate
    protected void removeFriendlyFireMoves(List<String> legalMoves) {
        legalMoves.removeIf(move -> board.getPiece(move) != null && (board.getPiece(move).getSide() == side));
    }

    // MODIFIES: legalMoves
    // EFFECTS:  remove from legalMoves any moves that violate a current check situation
    protected void removeViolateCheckMoves(List<String> legalMoves) {
        legalMoves.removeIf(move -> board.willViolateCheck(getCurrentPos(), move));
    }

    // ===== setters =====

    // REQUIRES: newPos is a valid new position for the piece
    // MODIFIES: this
    // EFFECTS: sets piece position to new position
    public void setPos(String newPos) {
        this.position = newPos;
    }

    // REQUIRES: newPos is a valid new position for the piece
    //           piece is a Pawn
    // MODIFIES: this
    // EFFECTS: sets piece position to new position with option to change or keep its special starting position status
    public void setPos(String newPos, boolean changeStartPos) {
        // ONLY CALL THIS IF YOU ARE A PAWN
        this.position = newPos;
    }

    // ===== getters =====

    // EFFECTS: return list of strings representing the legal moves available to a piece
    public List<String> getLegalMoves() {
        List<String> legalMoves = getLegalMovesViolateCheck();
        removeViolateCheckMoves(legalMoves);
        return legalMoves;
    }

    // EFFECTS: return list of strings representing the legal moves available to a piece, HOWEVER does not care if the
    //          move violates check
    public List<String> getLegalMovesViolateCheck() {
        List<String> legalMoves = new ArrayList<>();
        addAllMoves(legalMoves);
        removeOutOfBoundsMoves(legalMoves);
        removeFriendlyFireMoves(legalMoves);
        return legalMoves;
    }

    // EFFECTS: returns the piece's color (i.e. WHITE or BLACK)
    public Side getSide() {
        return this.side;
    }

    // EFFECTS: return string representing a piece's current position on the chess board
    public String getCurrentPos() {
        return this.position;
    }

    // ===== JSON =====

    // EFFECTS: returns JSON object representation of current piece
    public JSONObject toJson() {
        JSONObject data = new JSONObject();

        data.put("side", side);
        data.put("type", pieceType);
        data.put("position", position);

        return data;
    }
}
