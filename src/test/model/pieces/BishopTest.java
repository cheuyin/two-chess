package model.pieces;

import model.Board;
import model.enums.Side;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BishopTest {
    Piece b1;
    Board board;

    @BeforeEach
    public void setup() {
        board = new Board();
        b1 = new Bishop(Side.BLACK, "f8", board);
    }

    @Test
    public void testConstructor() {
        assertEquals(Side.BLACK, b1.getSide());
        assertEquals("f8", b1.getCurrentPos());
    }

    @Test
    public void testGetLegalMovesNoLegalMoves() {
        List<String> legalMoves = b1.getLegalMoves();
        assertEquals(0, legalMoves.size());
    }

    @Test
    public void testGetLegalMovesOpenDiagonal() {
        Piece p = board.getPiece("e7");
        board.makeMove("e7", "e6");
        List<String> legalMoves = b1.getLegalMoves();
        assertEquals(5, legalMoves.size());
        assertTrue(legalMoves.contains("e7"));
        assertTrue(legalMoves.contains("d6"));
        assertTrue(legalMoves.contains("c5"));
        assertTrue(legalMoves.contains("b4"));
        assertTrue(legalMoves.contains("a3"));
    }
}
