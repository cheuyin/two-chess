package persistence;

import model.Board;
import model.enums.Side;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

// Test class for a JSON reader object that retrieves a chess game board from file
class JsonReaderTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            Board board = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderStarterBoard() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyBoard.json");
        try {
            Board bd = reader.read();
            assertEquals(Side.WHITE, bd.getCurrentTurn());
            assertEquals(16, bd.getNumAvailablePieces());
            assertEquals(0, bd.getMoveList().getAllMoves().size());
            assertFalse(bd.getGameOver());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralBoard() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralBoard.json");

        try {
            Board board = reader.read();
            assertEquals(Side.BLACK, board.getCurrentTurn());
            assertEquals(16, board.getNumAvailablePieces());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}