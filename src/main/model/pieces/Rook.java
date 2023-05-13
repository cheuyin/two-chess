package model.pieces;

import model.Board;
import model.enums.Side;
import model.enums.Type;

import java.util.List;

// A Rook piece. Can move vertically or horizontally across the board.
public class Rook extends Piece {
    // REQUIRES: initPos is a valid starting position for a Rook
    // EFFECTS: creates a Rook with the given initial attributes
    public Rook(Side color, String initPos, Board board) {
        super(color, initPos, board, Type.ROOK, "R");
    }

    // MODIFIES: legalMoves
    // EFFECTS: adds to legalMoves all the horizontal and vertical positions a rook can move to, up to and including
    //          positions blocked by another piece
    @Override
    protected void addAllMoves(List<String> legalMoves) {
        String currCol = position.substring(0, 1);
        int currRow = Integer.parseInt(position.substring(1));
        int colIndex = Board.COLUMNS.indexOf(currCol);

        addHorizontalMoves(colIndex, currRow, legalMoves);
        addVerticalMoves(colIndex, currRow, legalMoves);
    }
}
