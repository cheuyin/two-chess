package model;

import model.enums.Action;
import model.enums.Side;
import model.enums.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MoveTest {
    private Move m1;
    private Move m2;
    private List<Action> l1 = new ArrayList<>();
    private List<Action> l2 = new ArrayList<>();

    @BeforeEach
    public void setup() {
        l1.add(Action.TAKE);
        l1.add(Action.CHECK);
        l2.add(Action.CHECK);
        l2.add(Action.CHECK);
        l2.add(Action.CHECKMATE);
        m1 = new Move(Side.WHITE, Type.ROOK, "d6", "R", l1, Side.BLACK, Type.QUEEN, "h8");
        m2 = new Move(Side.BLACK, Type.KNIGHT, "f3", "N", l2, Side.WHITE, Type.KING, "a4");
    }

    @Test
    public void testConstructor() {
        assertEquals("Rxh8+", m1.getFormattedMove());
        assertEquals("Na4#", m2.getFormattedMove());
    }

    @Test
    public void testGetFormattedMove() {
        Move m3 = new Move(Side.WHITE, Type.PAWN, "f2", "", l1, Side.BLACK, Type.EMPTY, "f3");
        assertEquals("fxf3+", m3.getFormattedMove());
    }

    @Test
    public void testGetFormattedMoveNoCheckOrCheckmate() {
        Move m3 = new Move(Side.BLACK, Type.PAWN, "b7", "", new ArrayList<>(), Side.BLACK, Type.EMPTY,
                "b8");
        assertEquals("b8", m3.getFormattedMove());
    }


    @Test
    public void testGetFromType() {
        assertEquals(Type.ROOK, m1.getFromType());
        assertEquals(Type.KNIGHT, m2.getFromType());
    }

    @Test
    public void testGetFromSide() {
        assertEquals(Side.WHITE, m1.getFromSide());
        assertEquals(Side.BLACK, m2.getFromSide());
    }

    @Test
    public void testGetToPos() {
        assertEquals("h8", m1.getToPos());
        assertEquals("a4", m2.getToPos());
    }
}
