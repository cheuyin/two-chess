package model;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

// A record containing all the moves so far in a chess game. Contains functionality for returning a formatted list
// of all the moves.
public class MoveList {
    private final List<Move> moveList;
    private EventLog eventLog = EventLog.getInstance();

    // EFFECTS: creates an empty ArrayList to store moves
    public MoveList() {
        this.moveList = new ArrayList<>();
    }

    // REQUIRES: formattedMove is indeed a real move and formatted correctly
    // MODIFIES: this
    // EFFECTS:  if the most recent pair of moves is not full, then add move to the pair
    //           else, create a new pair with formattedMove
    public void addMove(Move move) {
//        if (moveHistory.size() > 0 && getLastMovePair().size() <= 1) {
//            getLastMovePair().add(move);
//        } else {
//            List<Move> newMovePair = new ArrayList<>();
//            newMovePair.add(move);
//            moveHistory.add(newMovePair);
//        }

        eventLog.logEvent(new Event("A piece was moved: new move object added to the list of moves"));

        this.moveList.add(move);
    }

    // ===== getters =====

    public List<Move> getAllMoves() {
        return moveList;
    }

    // EFFECTS: returns filtered list of all moves containing only Black side's moves
    public List<Move> getBlackMoves() {
        List<Move> blackMoves = new ArrayList<>();

        for (int i = 0; i < moveList.size(); i++) {
            if (i % 2 == 1) {
                blackMoves.add(moveList.get(i));
            }
        }

        eventLog.logEvent(new Event("Created filtered list of only Black moves"));

        return blackMoves;
    }

    // EFFECTS: returns filtered list of all moves containing only White side's moves
    public List<Move> getWhiteMoves() {
        List<Move> whiteMoves = new ArrayList<>();

        for (int i = 0; i < moveList.size(); i++) {
            if (i % 2 == 0) {
                whiteMoves.add(moveList.get(i));
            }
        }

        eventLog.logEvent(new Event("Create filtered list of only White moves"));

        return whiteMoves;
    }

    // EFFECTS: returns the move history as a JSON array
    public JSONArray toJsonArray() {
        JSONArray jsonMoves = new JSONArray();

        for (Move move : moveList) {
            jsonMoves.put(move.toJson());
        }

        return jsonMoves;
    }
}
