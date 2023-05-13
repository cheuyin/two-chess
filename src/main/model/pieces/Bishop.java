package model.pieces;

import model.Board;
import model.enums.Side;
import model.enums.Type;

import java.util.List;

// a Bishop piece, which can move diagonally across the board
public class Bishop extends Piece {
    // REQUIRES: initPos at a valid starting position for a bishop
    // EFFECTS: creates a Bishop with initial attributes
    public Bishop(Side color, String initPos, Board board) {
        super(color, initPos, board, Type.BISHOP, "B");
    }

    // MODIFIES: legalMoves
    // EFFECTS: adds to legalMoves all the diagonal positions a bishop can traverse up to and including where it
    //          reaches a piece
    @Override
    protected void addAllMoves(List<String> legalMoves) {
        String currCol = position.substring(0, 1);
        int currRow = Integer.parseInt(position.substring(1));
        int colIndex = Board.COLUMNS.indexOf(currCol);

        addDiagonalMovesColIndexGreater(colIndex, currRow, legalMoves);
        addDiagonalMovesColIndexLesser(colIndex, currRow, legalMoves);
    }
}
