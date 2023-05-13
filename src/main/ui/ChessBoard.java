package ui;

import model.Board;
import model.pieces.Piece;
import ui.customcomponents.Square;

import javax.swing.*;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

// Constructs and runs a graphical chess board where the game is played
public class ChessBoard {
    private TwoChess controller;
    private Board chessGame;
    private JPanel chessBoard;
    private Piece selectedPiece;
    private HashMap<String, Square> squares;

    // EFFECTS: constructs and renders a chess board with an associated game as dictated by the parent controller
    public ChessBoard(TwoChess controller) {
        this.controller = controller;
        this.chessGame = controller.getChessGame();
        this.squares = new HashMap<>();

        createBoard();
    }

    // MODIFIES: this
    // EFFECTS: renders a chess board
    private void createBoard() {
        chessBoard = new JPanel(new GridLayout(8, 8));
        chessBoard.setMaximumSize(new Dimension(500, 500));

        for (int i = 7; i >= 0; i--) {
            for (int j = 0; j < 8; j++) {
                String coordinate = Board.COLUMNS.get(j) + Board.ROWS.get(i);

                Color defaultColor;

                if ((i + j) % 2 == 1) {
                    defaultColor = Color.WHITE;
                } else {
                    defaultColor = Color.GRAY;
                }

                Square square = new Square(defaultColor, coordinate);

                square.addMouseListener(squareClickHandler());

                squares.put(coordinate, square);
                chessBoard.add(square);
            }
        }

        render();
    }

    // MODIFIES: this
    // EFFECTS: returns an event listener that produces highlighting/move-making when a board square is clicked
    private MouseAdapter squareClickHandler() {
        return new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Square clickedSquare = (Square) e.getSource();
                String coord = clickedSquare.getCoordinate();
                Piece p = chessGame.getPiece(coord);
                boolean validPieceSelection = p != null && p.getSide() == chessGame.getCurrentTurn();
                boolean validMove = selectedPiece != null && selectedPiece.getLegalMoves().contains(coord);

                if (validPieceSelection) {
                    selectedPiece = p;
                } else if (validMove) {
                    controller.makeMove(selectedPiece.getCurrentPos(), coord);
                    selectedPiece = null;
                }

                render();
            }
        };
    }

    // MODIFIES: this
    // EFFECTS: update the renders of the entire board
    private void render() {
        List<String> legalMoves = selectedPiece == null ? new ArrayList<>() : selectedPiece.getLegalMoves();

//        if (selectedPiece != null) {
//            legalMoves = selectedPiece.getLegalMoves();
//        }

        List<Square> squaresList = new ArrayList<>(squares.values());

        // Render correct square color
        for (Square square : squaresList) {
            String coord = square.getCoordinate();
            boolean containsSelectedPiece = selectedPiece != null && coord.equals(selectedPiece.getCurrentPos());

            if (legalMoves.contains(coord)) {
                square.setBackground(Color.GREEN);
            } else if (containsSelectedPiece) {
                square.setBackground(Color.PINK);
            } else {
                square.setBackground(containsSelectedPiece ? Color.PINK : square.getDefaultColor());
            }
        }

        // Render correct piece image in the square
        for (Square square : squaresList) {
            square.removeAll();

//            String coord = square.getCoordinate();
            if (chessGame.getPiece(square.getCoordinate()) != null) {
                square.add(PieceImage.getImageForPiece(chessGame.getPiece(square.getCoordinate())));
            }
        }

        chessBoard.repaint();
        chessBoard.revalidate();
    }

    // ===== getters =====
    public JPanel getBoard() {
        return chessBoard;
    }
}
