package persistence;

import model.Board;
import model.enums.Side;
import model.enums.Type;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

// Test class for a JSON writer object that stores a chess game board to file
class JsonWriterTest {
    //NOTE TO CPSC 210 STUDENTS: the strategy in designing tests for the JsonWriter is to
    //write data to a file and then use the reader to read it back in and check that we
    //read in a copy of what was written out.

    @Test
    void testWriterInvalidFile() {
        try {
            Board board = new Board();
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyBoard() {
        try {
            Board board = new Board();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyBoard.json");
            writer.open();
            writer.write(board);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyBoard.json");
            board = reader.read();

            assertEquals(Side.WHITE, board.getCurrentTurn());
            assertEquals(16, board.getNumAvailablePieces());
            assertEquals(0, board.getMoveList().getAllMoves().size());
            assertFalse(board.getGameOver());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralBoard() {
        try {
            Board board = new Board();
            board.makeMove("e2", "e4");
            board.makeMove("b8", "c6");
            board.makeMove("g1", "f3");
            board.makeMove("d7", "d5");
            board.makeMove("f1", "d3");
            board.makeMove("d5", "e4");
            board.makeMove("d3", "e4");
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralBoard.json");
            writer.open();
            writer.write(board);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralBoard.json");
            board = reader.read();
            assertEquals(Side.BLACK, board.getCurrentTurn());
            assertFalse(board.getGameOver());
            assertEquals(15, board.getNumAvailablePieces());
            assertEquals(Type.KNIGHT, board.getPiece("c6").pieceType);
            assertNull(board.getPiece("d5"));
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterCheckmateBoardBlackWins() {
        try {
            Board board = new Board();
            board.makeMove("f2", "f3");
            board.makeMove("e7", "e6");
            board.makeMove("g2", "g4");
            board.makeMove("d8", "h4");
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralBoard.json");
            writer.open();
            writer.write(board);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralBoard.json");
            board = reader.read();
            assertEquals(Side.WHITE, board.getCurrentTurn());
            assertTrue(board.getGameOver());
            assertEquals("Black", board.getWinner());
            assertEquals(16, board.getNumAvailablePieces());
            assertEquals(Type.QUEEN, board.getPiece("h4").pieceType);
            assertEquals(Side.BLACK, board.getPiece("h4").getSide());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterCheckmateBoardWhiteWins() {
        try {
            Board board = new Board();
            board.makeMove("e2", "e3");
            board.makeMove("f7", "f6");
            board.makeMove("d1", "g4");
            board.makeMove("g7", "g5");
            board.makeMove("g4", "h5");
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralBoard.json");
            writer.open();
            writer.write(board);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralBoard.json");
            board = reader.read();
            assertEquals(Side.BLACK, board.getCurrentTurn());
            assertTrue(board.getGameOver());
            assertEquals("White", board.getWinner());
            assertEquals(16, board.getNumAvailablePieces());
            assertEquals(Type.QUEEN, board.getPiece("h5").pieceType);
            assertEquals(Side.WHITE, board.getPiece("h5").getSide());
            assertNull(board.getPiece("d1"));
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}