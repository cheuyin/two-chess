package model;

import javax.swing.*;
import model.enums.Side;
import model.enums.Type;
import model.enums.Action;
import model.pieces.Piece;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {
    private Board board;

    @BeforeEach
    public void setup() {
        board = new Board();
    }

    @Test
    public void testConstructor() {
        assertEquals(Side.WHITE, board.getCurrentTurn());
        assertFalse(board.getGameOver());
        assertEquals(64, board.getAllPositions().size());
        assertEquals(0, board.getMoveList().getAllMoves().size());
    }

    @Test
    public void testMakeMoveWhitePiece() {
        Piece f2Pawn = board.getPiece("f2");
        assertEquals(Side.WHITE, board.getCurrentTurn());
        assertEquals(f2Pawn, board.getPiece("f2"));
        board.makeMove("f2", "f4");
        assertEquals(Side.BLACK, board.getCurrentTurn());
        assertNull(board.getPiece("f2"));
        assertEquals(f2Pawn, board.getPiece("f4"));
    }

    @Test
    public void testMakeMoveBlackPiece() {
        // Make dummy White move
        board.makeMove("g2", "g3");

        Piece b8Knight = board.getPiece("b8");
        assertEquals(Side.BLACK, board.getCurrentTurn());

        board.makeMove("b8", "c6");

        assertEquals(Side.WHITE, board.getCurrentTurn());
        assertNull(board.getPiece("b8"));
        assertEquals(b8Knight, board.getPiece("c6"));
    }

    @Test
    public void testAddMoveToMoveList() {
        List<Action> l1 = new ArrayList<>();
        l1.add(Action.TAKE);
        l1.add(Action.CHECK);
        List<Action> l2 = new ArrayList<>();
        l2.add(Action.CHECK);
        l2.add(Action.CHECK);
        l2.add(Action.CHECKMATE);
        Move m1 = new Move(Side.WHITE, Type.ROOK, "d6", "N", l2, Side.BLACK, Type.QUEEN, "h8");
        Move m2 = new Move(Side.BLACK, Type.KNIGHT, "f3", "K", l1, Side.WHITE, Type.KING, "a4");
        board.addMoveToMoveList(m1);
        board.addMoveToMoveList(m2);
        MoveList moveList = board.getMoveList();
        assertEquals(2, moveList.getAllMoves().size());
        assertEquals(1, moveList.getBlackMoves().size());
        assertEquals(1, moveList.getWhiteMoves().size());
    }

    @Test
    public void testMoveListAllMoves() {
        board.makeMove("e2", "e4");
        board.makeMove("e7", "e5");
        board.makeMove("g1", "f3");
        MoveList moveList = board.getMoveList();
        assertEquals(3, moveList.getAllMoves().size());
    }

    @Test
    public void testMoveListBlackMoves() {
        board.makeMove("e2", "e4");
        board.makeMove("e7", "e5");
        board.makeMove("g1", "f3");
        board.makeMove("f8", "c5");
        board.makeMove("d2", "d4");
        board.makeMove("e5", "d4");
        MoveList moveList = board.getMoveList();
        assertEquals(3, moveList.getBlackMoves().size());
    }

    @Test
    public void testMoveListWhiteMoves() {
        board.makeMove("e2", "e4");
        board.makeMove("e7", "e5");
        board.makeMove("g1", "f3");
        board.makeMove("f8", "c5");
        MoveList moveList = board.getMoveList();
        assertEquals(2, moveList.getWhiteMoves().size());
    }

    @Test
    public void testIsCheck() {
        board.makeMove("d2", "d3");
        board.makeMove("g7", "g5");
        board.makeMove("g1", "f3");
        board.makeMove("f8", "g7");
        board.makeMove("f3", "g5");

        assertFalse(board.isCheck());

        board.makeMove("g7", "c3");

        assertTrue(board.isCheck());
    }

    @Test
    public void testIsCheckmate() {
        board.makeMove("e2", "e4");
        board.makeMove("e7", "e5");
        board.makeMove("f1", "c4");
        board.makeMove("b8", "c6");
        board.makeMove("d1", "f3");
        board.makeMove("g7", "g5");

        assertFalse(board.isCheckmate());
        assertFalse(board.getGameOver());

        board.makeMove("f3", "f7");

        assertTrue(board.isCheckmate());
        assertTrue(board.getGameOver());
        assertEquals(Side.BLACK, board.getCurrentTurn());
    }

    @Test
    public void testWillViolateCheckNoViolate() {
        boolean result = board.willViolateCheck("b1", "c3");
        assertFalse(result);
    }

    @Test
    public void testWillViolateCheckViolate() {
        // Test move where a King is in check but move does not stop check
        board.makeMove("e2", "e4");
        board.makeMove("e7", "e5");
        board.makeMove("f1", "c4");
        board.makeMove("c7", "c6");
        board.makeMove("c4", "f7");

        boolean result = board.willViolateCheck("g8", "f6");
        assertTrue(result);
    }

    @Test
    public void testGetAvailableCoordsWhite() {
        // Board begins as White's turn
        List<String> res = board.getAvailableCoords();
        assertEquals(16, res.size());
        assertEquals(16, board.getNumAvailablePieces());
        assertTrue(res.contains("a2"));
        assertTrue(res.contains("b2"));
        assertTrue(res.contains("c2"));
        assertTrue(res.contains("d2"));
        assertTrue(res.contains("e2"));
        assertTrue(res.contains("f2"));
        assertTrue(res.contains("g2"));
        assertTrue(res.contains("h2"));
        assertTrue(res.contains("a1"));
        assertTrue(res.contains("b1"));
        assertTrue(res.contains("c1"));
        assertTrue(res.contains("d1"));
        assertTrue(res.contains("e1"));
        assertTrue(res.contains("f1"));
        assertTrue(res.contains("g1"));
        assertTrue(res.contains("h1"));
    }

    @Test
    public void testGetAvailableCoordsBlack() {
        // Move a piece to make it Black's turn
        board.makeMove("e2", "e4");

        List<String> res = board.getAvailableCoords();
        assertEquals(16, res.size());
        assertEquals(16, board.getNumAvailablePieces());
        assertTrue(res.contains("a8"));
        assertTrue(res.contains("b8"));
        assertTrue(res.contains("c8"));
        assertTrue(res.contains("d8"));
        assertTrue(res.contains("e8"));
        assertTrue(res.contains("f8"));
        assertTrue(res.contains("g8"));
        assertTrue(res.contains("h8"));
        assertTrue(res.contains("a7"));
        assertTrue(res.contains("b7"));
        assertTrue(res.contains("c7"));
        assertTrue(res.contains("d7"));
        assertTrue(res.contains("e7"));
        assertTrue(res.contains("f7"));
        assertTrue(res.contains("g7"));
        assertTrue(res.contains("h7"));
    }
}
