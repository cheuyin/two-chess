package model.pieces;

import model.Board;
import model.enums.Side;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class KingTest {
    Piece k1;
    Board board;

    @BeforeEach
    public void setup() {
        board = new Board();
    }

    @Test
    public void testConstructor() {
        King testKing = new King(Side.WHITE, "g5", board);
        assertEquals(Side.WHITE, testKing.getSide());
        assertEquals("g5", testKing.getCurrentPos());
    }

    @Test
    public void testGetLegalMovesNoLegalMoves() {
        Piece blackKing = board.getPiece("e8");
        List<String> legalMoves = blackKing.getLegalMoves();
        assertEquals(0, legalMoves.size());
    }

    @Test
    public void testGetLegalMovesFullRange() {
        // Move White King to c4
        board.makeMove("e2", "e3");
        board.makeMove("e1", "e2");
        board.makeMove("e2", "d3");
        board.makeMove("d3", "c4");
        k1 = board.getPiece("c4");

        List<String> legalMoves = k1.getLegalMoves();
        assertEquals(8, legalMoves.size());
        assertTrue(legalMoves.contains("b5"));
        assertTrue(legalMoves.contains("b4"));
        assertTrue(legalMoves.contains("b3"));
        assertTrue(legalMoves.contains("c5"));
        assertTrue(legalMoves.contains("d5"));
        assertTrue(legalMoves.contains("c3"));
        assertTrue(legalMoves.contains("d3"));
        assertTrue(legalMoves.contains("d4"));
    }

    @Test
    public void testGetLegalMovesLeftEdge() {
        // Move White King to c4
        board.makeMove("e2", "e3");
        board.makeMove("e1", "e2");
        board.makeMove("e2", "d3");
        board.makeMove("d3", "c4");
        board.makeMove("c4", "b3");
        board.makeMove("b3", "a3");
        k1 = board.getPiece("a3");

        List<String> legalMoves = k1.getLegalMoves();
        assertEquals(3, legalMoves.size());

        assertTrue(legalMoves.contains("a4"));
        assertTrue(legalMoves.contains("b4"));
        assertTrue(legalMoves.contains("b3"));
    }

    @Test
    public void testGetLegalMovesRightEdge() {
        // Move Black king to right edge of board
        board.makeMove("e7", "e6");
        board.makeMove("e8", "e7");
        board.makeMove("e7", "f6");
        board.makeMove("f6", "g6");
        board.makeMove("g6", "h6");

        Piece blackKing = board.getPiece("h6");

        List<String> legalMoves = blackKing.getLegalMoves();
        System.out.println(legalMoves);

        assertEquals(3, legalMoves.size());

        assertTrue(legalMoves.contains("g6"));
        assertTrue(legalMoves.contains("g5"));
        assertTrue(legalMoves.contains("h5"));
    }
}
