package model;

import model.enums.Action;
import model.enums.Side;
import model.enums.Type;
import model.pieces.Piece;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

// Represents a single move in a game. Main purpose of this class is to format that move in standard algebraic
// notation. E.g. "Bxh4+" means a Bishop takes the piece at h4 and leaves a check.
public class Move {
    private final Side fromSide;
    private final Type fromType;
    private final String fromPos;
    private final String fromId;
    private final List<Action> actions;
    private final Side toSide;
    private final Type toType;
    private final String toPos;

    // REQUIRES: fromPiece and toPiece are valid pieces, and toPos represents the original position of toPiece
    //           fromPiece represents the piece that was moved and toPos is its destination square on the board
    // EFFECTS: creates a new move record with given information about the piece that was moved and its destination
    public Move(Side fromSide, Type fromType, String fromPos, String fromId, List<Action> actions, Side toSide,
                Type toType, String toPos) {
        this.fromSide = fromSide;
        this.fromType = fromType;
        this.fromPos = fromPos;
        this.fromId = fromId;
        this.actions = actions;
        this.toSide = toSide;
        this.toType = toType;
        this.toPos = toPos;
    }

    // ===== getters =====

    // EFFECTS: return formatted move in standard chess algebraic notation (e.g. "e4")
    public String getFormattedMove() {
        String record = "";

        if (actions.contains(Action.TAKE)) {
            if (fromType == Type.PAWN) {
                record += fromPos.substring(0, 1);
            }
            record += fromId + "x" + toPos;
        } else {
            record += fromId + toPos;
        }

        if (actions.contains(Action.CHECKMATE)) {
            record += "#";
        } else if (actions.contains(Action.CHECK)) {
            record += "+";
        }

        return record;
    }

    // EFFECTS: returns the list of actions resulting from this move
    //          e.g. if a move resulted in a take and a check, then return a list containing Action.TAKE and
    //               Action.CHECK as such
    private List<Action> getActions() {
        return actions;
    }

    public Type getFromType() {
        return fromType;
    }

    public Side getFromSide() {
        return fromSide;
    }

    public String getToPos() {
        return toPos;
    }

    // ===== json functionality =====

    // EFFECTS: returns JSON object representation of this class
    public JSONObject toJson() {
        JSONObject data = new JSONObject();

        data.put("fromSide", fromSide);
        data.put("fromType", fromType);
        data.put("fromPos", fromPos);
        data.put("fromId", fromId);
        data.put("actions", actions);
        data.put("toSide", toSide);
        data.put("toType", toType);
        data.put("toPos", toPos);

        return data;
    }
}
