package model.pieces;

import model.Board;
import model.enums.Side;
import model.enums.Type;

import java.util.List;

// A Knight piece. Moves in any direction in an "L" shape.
public class Knight extends Piece {
    // REQUIRES: initPos at a valid starting position for a knight
    // EFFECTS: creates a Knight with the given attributes
    public Knight(Side color, String initPos, Board board) {
        super(color, initPos, board, Type.KNIGHT, "N");
    }

    // MODIFIES: legalMoves
    // EFFECTS: adds to legalMoves all the positions a knight can reach on the board, ignoring constraints
    //          like the size of the board, checkmate violations, and friendly fire
    @Override
    protected void addAllMoves(List<String> legalMoves) {
        String currCol = position.substring(0, 1);
        int currRow = Integer.parseInt(position.substring(1));
        int colIndex = Board.COLUMNS.indexOf(currCol);

        if (colIndex + 2 < 8) {
            legalMoves.add(Board.COLUMNS.get(colIndex + 2) + (currRow + 1));
            legalMoves.add(Board.COLUMNS.get(colIndex + 2) + (currRow - 1));
        }

        if (colIndex - 2 >= 0) {
            legalMoves.add(Board.COLUMNS.get(colIndex - 2) + (currRow + 1));
            legalMoves.add(Board.COLUMNS.get(colIndex - 2) + (currRow - 1));
        }

        if (colIndex + 1 < 8) {
            legalMoves.add(Board.COLUMNS.get(colIndex + 1) + (currRow + 2));
            legalMoves.add(Board.COLUMNS.get(colIndex + 1) + (currRow - 2));
        }

        if (colIndex - 1 >= 0) {
            legalMoves.add(Board.COLUMNS.get(colIndex - 1) + (currRow + 2));
            legalMoves.add(Board.COLUMNS.get(colIndex - 1) + (currRow - 2));
        }
    }
}
