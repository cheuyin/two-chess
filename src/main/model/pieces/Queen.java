package model.pieces;

import model.Board;
import model.enums.Side;
import model.enums.Type;

import java.util.List;

// A Queen piece. The most powerful piece in the game. Combines the abilities of a Bishop and a Rook.
public class Queen extends Piece {
    // REQUIRES: initPos is a valid starting position for a Queen
    // EFFECTS: creates a Queen with the given attributes
    public Queen(Side color, String initPos, Board board) {
        super(color, initPos, board, Type.QUEEN, "Q");
    }

    // MODIFIES: legalMoves
    // EFFECTS: adds to legalMoves all the horizontal, vertical, and diagonal positions a Queen can move to, up to
    //          and including positions blocked by another piece
    @Override
    protected void addAllMoves(List<String> legalMoves) {
        String currCol = position.substring(0, 1);
        int currRow = Integer.parseInt(position.substring(1));
        int colIndex = Board.COLUMNS.indexOf(currCol);

        addHorizontalMoves(colIndex, currRow, legalMoves);
        addVerticalMoves(colIndex, currRow, legalMoves);
        addDiagonalMovesColIndexLesser(colIndex, currRow, legalMoves);
        addDiagonalMovesColIndexGreater(colIndex, currRow, legalMoves);
    }
}
