package persistence;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import model.Board;
import model.Move;
import model.MoveList;
import model.enums.Action;
import model.enums.Side;
import model.enums.Type;
import model.pieces.*;
import org.json.*;

// Represents a reader that reads board from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads board from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Board read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonBoard = new JSONObject(jsonData);
        return parseBoard(jsonBoard);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses board from JSON object and returns it
    private Board parseBoard(JSONObject jsonBoard) {
        Board board = new Board();
        board.clearBoard();
        addSimpleBoardData(board, jsonBoard);
        addMoveList(board, jsonBoard);
        addPieces(board, jsonBoard);
        return board;
    }

    // EFFECTS: transfer simple data (i.e. enums, booleans, strings) from JSON board to real board
    private void addSimpleBoardData(Board board, JSONObject jsonBoard) {
        Side currentTurn = Side.valueOf(jsonBoard.getString("currentTurn"));
        boolean gameOver = jsonBoard.getBoolean("gameOver");
        board.setCurrentTurn(currentTurn);
        board.setGameOver(gameOver);
    }

    // EFFECTS: transfer move history stored in JSON object to the real board
    private void addMoveList(Board board, JSONObject jsonBoard) {
        JSONArray moveList = jsonBoard.getJSONArray("moveList");
        MoveList ml = new MoveList();
        for (Object move : moveList) {
            ml.addMove(createMove((JSONObject) move));
        }
        board.setMoveList(ml);
    }

    // EFFECTS: constructs Move from JSONObject representation of a move
    private Move createMove(JSONObject move) {
        Side fromSide = Side.valueOf(move.getString("fromSide"));
        Type fromType = Type.valueOf(move.getString("fromType"));
        String fromPos = move.getString("fromPos");
        String fromId = move.getString("fromId");
        Side toSide = Side.valueOf(move.getString("toSide"));
        Type toType = Type.valueOf(move.getString("toType"));
        String toPos = move.getString("toPos");

        return new Move(fromSide, fromType, fromPos, fromId, new ArrayList<>(), toSide, toType, toPos);
    }


    // EFFECTS: transfer pieces stored in JSON to the real board
    private void addPieces(Board board, JSONObject jsonBoard) {
        JSONArray piecesArray = jsonBoard.getJSONArray("pieces");
        for (Object json : piecesArray) {
            JSONObject piece = (JSONObject) json;
            addPiece(board, piece);
        }
    }

    // EFFECTS: transfer a single piece represented in JSON to the real board
    private void addPiece(Board board, JSONObject pieceJson) {
        String piecePosition = pieceJson.getString("position");

        Piece constructedPiece = constructPiece(board, pieceJson);

        board.setPiece(piecePosition, constructedPiece);
    }

    // EFFECTS: parse JSON data for a piece and return a reconstructed real piece
    private Piece constructPiece(Board board, JSONObject pieceJson) {
        Side pieceColor = Side.valueOf(pieceJson.getString("side"));
        Type pieceType = Type.valueOf(pieceJson.getString("type"));
        String piecePosition = pieceJson.getString("position");

        switch (pieceType) {
            case BISHOP:
                return new Bishop(pieceColor, piecePosition, board);
            case KING:
                return new King(pieceColor, piecePosition, board);
            case KNIGHT:
                return new Knight(pieceColor, piecePosition, board);
            case PAWN:
                Pawn p = new Pawn(pieceColor, piecePosition, board);
                p.setAtStartPos(pieceJson.getBoolean("atStartPos"));
                return p;
            case QUEEN:
                return new Queen(pieceColor, piecePosition, board);
            case ROOK:
                return new Rook(pieceColor, piecePosition, board);
            default:
                return null;
        }
    }
}
