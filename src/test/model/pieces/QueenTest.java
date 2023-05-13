package model.pieces;

import model.Board;
import model.enums.Side;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class QueenTest {
    Piece q1;
    Board board;

    @BeforeEach
    public void setup() {
        board = new Board();
        q1 = board.getPiece("d1");
    }

    @Test
    public void testConstructor() {
        Queen testQueen = new Queen(Side.WHITE, "h7", board);
        assertEquals(Side.WHITE, testQueen.getSide());
        assertEquals("h7", testQueen.getCurrentPos());
    }

    @Test
    public void testGetLegalMovesNoMoves() {
        List<String> legalMoves = q1.getLegalMoves();
        assertEquals(0, legalMoves.size());
    }

    @Test
    // Test legal move generation when Queen is at the middle of the board
    public void testGetLegalMovesMiddleOfBoard() {
        board.makeMove("e2", "e3");
        board.makeMove("d1", "f3");
        board.makeMove("f3", "d5");
        List<String> legalMoves = q1.getLegalMoves();
        assertEquals(19, legalMoves.size());
        assertTrue(legalMoves.contains("c6"));
        assertTrue(legalMoves.contains("d6"));
        assertTrue(legalMoves.contains("e6"));
        assertTrue(legalMoves.contains("a5"));
        assertTrue(legalMoves.contains("b5"));
        assertTrue(legalMoves.contains("c5"));
        assertTrue(legalMoves.contains("e5"));
        assertTrue(legalMoves.contains("f5"));
        assertTrue(legalMoves.contains("g5"));
        assertTrue(legalMoves.contains("h5"));
        assertTrue(legalMoves.contains("c4"));
        assertTrue(legalMoves.contains("d4"));
        assertTrue(legalMoves.contains("e4"));
        assertTrue(legalMoves.contains("b3"));
        assertTrue(legalMoves.contains("d3"));
        assertTrue(legalMoves.contains("f3"));
        assertTrue(legalMoves.contains("b7"));
        assertTrue(legalMoves.contains("d7"));
        assertTrue(legalMoves.contains("f7"));
    }
}
