package model.pieces;

import model.Board;
import model.enums.Side;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class KnightTest {
    Piece n1;
    Board board;

    @BeforeEach
    public void setup() {
        board = new Board();

        // Move Black Knight to f6
        board.makeMove("g8", "f6");
        n1 = board.getPiece("f6");
    }

    @Test
    public void testConstructor() {
        Knight testKnight = new Knight(Side.BLACK, "b6", board);
        assertEquals(Side.BLACK, testKnight.getSide());
        assertEquals("b6", testKnight.getCurrentPos());
    }

    @Test
    public void testGetLegalMovesBlockedByFriendlies() {
        List<String> legalMoves = n1.getLegalMoves();
        assertEquals(5, legalMoves.size());
        assertTrue(legalMoves.contains("d5"));
        assertTrue(legalMoves.contains("e4"));
        assertTrue(legalMoves.contains("g4"));
        assertTrue(legalMoves.contains("h5"));
        assertTrue(legalMoves.contains("g8"));
    }

    @Test
    // Test legal move generation when Knight is at the right edge of the board
    public void testGetLegalMovesRightEdge() {
        board.makeMove("f6", "h5");
        List<String> legalMoves = n1.getLegalMoves();
        assertEquals(3, legalMoves.size());
        assertTrue(legalMoves.contains("f6"));
        assertTrue(legalMoves.contains("f4"));
        assertTrue(legalMoves.contains("g3"));
    }

    @Test
    // Test legal move generation when Knight is at the left edge of the board
    public void testGetLegalMovesLeftEdge() {
        board.makeMove("b1", "a3");
        Piece whiteKnight = board.getPiece("a3");
        List<String> legalMoves = whiteKnight.getLegalMoves();
        assertEquals(3, legalMoves.size());
        assertTrue(legalMoves.contains("b5"));
        assertTrue(legalMoves.contains("c4"));
        assertTrue(legalMoves.contains("b1"));
    }
}
