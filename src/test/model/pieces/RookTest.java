package model.pieces;

import model.Board;
import model.enums.Side;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RookTest {
    Piece r1;
    Board board;

    @BeforeEach
    public void setup() {
        board = new Board();
        r1 = board.getPiece("h8");
    }

    @Test
    public void testConstructor() {
        Rook testRook = new Rook(Side.BLACK, "h8", board);
        assertEquals(Side.BLACK, testRook.getSide());
        assertEquals("h8", testRook.getCurrentPos());
    }

    @Test
    public void testGetLegalMovesNoMoves() {
        List<String> legalMoves = r1.getLegalMoves();
        assertEquals(0, legalMoves.size());
    }

    @Test
    // Test legal move generation when Rook is at the middle of the board
    public void testGetLegalMovesMiddleOfBoard() {
        board.makeMove("h7", "h5");
        board.makeMove("h8", "h6");
        board.makeMove("h6", "e6");
        board.makeMove("e6", "e5");
        List<String> legalMoves = r1.getLegalMoves();
        assertEquals(10, legalMoves.size());
        assertTrue(legalMoves.contains("a5"));
        assertTrue(legalMoves.contains("b5"));
        assertTrue(legalMoves.contains("c5"));
        assertTrue(legalMoves.contains("d5"));
        assertTrue(legalMoves.contains("e6"));
        assertTrue(legalMoves.contains("f5"));
        assertTrue(legalMoves.contains("g5"));
        assertTrue(legalMoves.contains("e4"));
        assertTrue(legalMoves.contains("e3"));
        assertTrue(legalMoves.contains("e2"));
    }
}
