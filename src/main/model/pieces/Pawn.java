package model.pieces;

import model.Board;
import model.enums.Side;
import model.enums.Type;
import org.json.JSONObject;

import java.util.List;

// A Pawn piece. Can only move forward except for when it takes diagonally. If at a starting position, it gets the
// option to move two squares forward instead of just one square.
public class Pawn extends Piece {
    private Boolean atStartPos;

    // REQUIRES: initPos is valid starting position on board
    // EFFECTS:  creates a new Pawn with the given attributes
    public Pawn(Side color, String initPos, Board board) {
        super(color, initPos, board, Type.PAWN, "");
        setAtStartPos(true);
    }

    // REQUIRES: newPos is a valid new position for the piece
    // MODIFIES: this
    // EFFECTS: sets piece position to new position and removes Pawn's option to skip one square (no longer at initial
    //          position)
    @Override
    public void setPos(String newPos) {
        super.setPos(newPos);
        setAtStartPos(false);
    }

    // REQUIRES: newPos is a valid new position for the piece
    // MODIFIES: this
    // EFFECTS: sets Pawn position to new position with option to change or keep its special starting position status
    @Override
    public void setPos(String newPos, boolean changeStartPos) {
        super.setPos(newPos, changeStartPos);
        if (changeStartPos) {
            setAtStartPos(false);
        }
    }

    // MODIFIES: legalMoves
    // EFFECTS:  add all the moves a pawn can make without careful consideration for its position on the board
    @Override
    protected void addAllMoves(List<String> legalMoves) {
        String currCol = position.substring(0, 1);
        int currRow = Integer.parseInt(position.substring(1));
        addStandardMoves(legalMoves, currCol, currRow);
        addLegalDiagonalMoves(legalMoves, currCol, currRow);
    }

    // REQUIRES: currCol and currRow represent the current column and row of this piece
    // MODIFIES: legalMoves
    // EFFECTS: if at the starting position, add moving one or two squares forward as possible moves
    //          else, add moving one square forward as a possible move
    //          HOWEVER, add nothing if those squares are occupied
    private void addStandardMoves(List<String> legalMoves, String currCol, int currRow) {
        String oneForward;
        String twoForward = null;
        boolean isWhite = side == Side.WHITE;
        if (atStartPos) {
            oneForward = currCol + (currRow + (isWhite ? 1 : -1));
            twoForward = currCol + (currRow + (isWhite ? 2 : -2));

        } else {
            oneForward = currCol + (currRow + (isWhite ? 1 : -1));
        }
        if (board.getPiece(oneForward) == null) {
            legalMoves.add(oneForward);
        }
        if (board.getPiece(twoForward) == null && atStartPos) {
            legalMoves.add(twoForward);
        }
    }

    // REQUIRES: currCol and currRow represent the current column and row of this piece
    // MODIFIES: legalMoves
    // EFFECTS:  if there's an enemy piece at a pawn's nearest diagonals, then add those moves to list of legal moves
    private void addLegalDiagonalMoves(List<String> legalMoves, String currCol, int currRow) {
        boolean isWhite = side == Side.WHITE;
        int colIndex = Board.COLUMNS.indexOf(currCol);
        if (colIndex > 0) {
            String newCol1 = Board.COLUMNS.get(colIndex - 1);
            String newRow1 = Integer.toString(currRow + (isWhite ? 1 : -1));
            String newDiag1 = newCol1 + newRow1;
            if (board.getPiece(newDiag1) != null && !board.getPiece(newDiag1).getSide().equals(this.side)) {
                legalMoves.add(newDiag1);
            }
        }

        if (colIndex < 7) {
            String newCol2 = Board.COLUMNS.get(colIndex + 1);
            String newRow2 = Integer.toString(currRow + (isWhite ? 1 : -1));
            String newDiag2 = newCol2 + newRow2;
            if (board.getPiece(newDiag2) != null && !board.getPiece(newDiag2).getSide().equals(this.side)) {
                legalMoves.add(newDiag2);
            }
        }
    }

    // ===== setters =====
    public void setAtStartPos(boolean val) {
        atStartPos = val;
    }

    // ===== getters =====
    public boolean getAtStartPos() {
        return atStartPos;
    }

    // ===== JSON =====
    // EFFECTS: returns JSON object representation of current piece
    @Override
    public JSONObject toJson() {
        JSONObject data = super.toJson();

        data.put("atStartPos", atStartPos);

        return data;
    }
}
