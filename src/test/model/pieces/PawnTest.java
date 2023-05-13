package model.pieces;

import model.Board;
import model.enums.Side;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PawnTest {
    Board board;
    Pawn p1;
    Pawn p2;

    @BeforeEach
    public void setup() {
        board = new Board();
        p1 = new Pawn(Side.WHITE, "e2", board);
        p2 = new Pawn(Side.BLACK, "h7", board);
    }

    @Test
    public void testConstructor() {
        assertEquals(Side.WHITE, p1.getSide());
        assertEquals(Side.BLACK, p2.getSide());

        assertEquals("e2", p1.getCurrentPos());
        assertEquals("h7", p2.getCurrentPos());

        assertEquals(true, p1.getAtStartPos());
        assertEquals(true, p2.getAtStartPos());
    }

    @Test
    public void testSetPosChangeStartStatus() {
        assertEquals("e2", p1.getCurrentPos());
        assertEquals(true, p1.getAtStartPos());
        p1.setPos("e4", true);
        assertEquals("e4", p1.getCurrentPos());
        assertEquals(false, p1.getAtStartPos());
    }

    @Test
    public void testSetPosNoChangeStartStatus() {
        assertEquals("e2", p1.getCurrentPos());
        assertEquals(true, p1.getAtStartPos());
        p1.setPos("e4", false);
        assertEquals("e4", p1.getCurrentPos());
        assertEquals(true, p1.getAtStartPos());
    }

    @Test
    public void testGetLegalMovesAtInitialPos() {
        Piece p = board.getPiece("c2");
        List<String> legalMoves = p.getLegalMoves();
        assertEquals(2, legalMoves.size());
        assertTrue(legalMoves.contains("c3"));
        assertTrue(legalMoves.contains("c4"));
    }

    @Test
    public void testGetLegalMovesNotAtInitialPos() {
        Piece p = board.getPiece("c2");
        board.makeMove("c2", "c4");
        List<String> legalMoves = p.getLegalMoves();
        assertEquals(1, legalMoves.size());
        assertTrue(legalMoves.contains("c5"));
    }

    @Test
    public void testGetLegalMovesEnemyTake() {
        Piece friendly = board.getPiece("c2");
        board.makeMove("c2", "c4");
        Piece enemy = board.getPiece("d7");
        board.makeMove("d7", "d5");
        List<String> legalMoves = friendly.getLegalMoves();
        assertEquals(2, legalMoves.size());
        assertTrue(legalMoves.contains("c5"));
        assertTrue(legalMoves.contains("d5"));
    }
}
