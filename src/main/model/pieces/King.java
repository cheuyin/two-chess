package model.pieces;

import model.Board;
import model.enums.Side;
import model.enums.Type;

import java.util.List;

// A King piece. It can move one square in any direction. The game ends when it is checkmated.
public class King extends Piece {
    // REQUIRES: initPos is valid starting position on board
    // EFFECTS:  creates a King piece with the given attributes
    public King(Side color, String initPos, Board board) {
        super(color, initPos, board, Type.KING, "K");
    }

    // MODIFIES: legalMoves
    // EFFECTS:  add all the moves a king can make without consideration for its position on the board
    @Override
    protected void addAllMoves(List<String> legalMoves) {
        String currCol = position.substring(0, 1);
        int currRow = Integer.parseInt(position.substring(1));
        int colIndex = Board.COLUMNS.indexOf(currCol);
        legalMoves.add(currCol + (currRow + 1));
        legalMoves.add(currCol + (currRow - 1));

        if (colIndex > 0) {
            String newCol1 = Board.COLUMNS.get(colIndex - 1);
            legalMoves.add(newCol1 + (currRow + 1));
            legalMoves.add(newCol1 + currRow);
            legalMoves.add(newCol1 + (currRow - 1));
        }

        if (colIndex < 7) {
            String newCol2 = Board.COLUMNS.get(colIndex + 1);
            legalMoves.add(newCol2 + (currRow + 1));
            legalMoves.add(newCol2 + currRow);
            legalMoves.add(newCol2 + (currRow - 1));
        }
    }
}
