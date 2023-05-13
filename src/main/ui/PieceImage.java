package ui;

import model.enums.Side;
import model.enums.Type;
import model.pieces.Piece;

import javax.swing.*;

// Image of a chess piece
public class PieceImage {

    // EFFECTS: returns JLabel containing a 50px image of the given piece
    public static JLabel getImageForPiece(Piece p) {
        if (p == null) {
            return null;
        }

        String imgPath = "images/" + ((p.getSide() == Side.WHITE) ? "w" : "b");
        if (p.pieceType == Type.PAWN) {
            imgPath += "P";
        } else if (p.pieceType == Type.BISHOP) {
            imgPath += "B";
        } else if (p.pieceType == Type.ROOK) {
            imgPath += "R";
        } else if (p.pieceType == Type.KNIGHT) {
            imgPath += "N";
        } else if (p.pieceType == Type.KING) {
            imgPath += "K";
        } else if (p.pieceType == Type.QUEEN) {
            imgPath += "Q";
        }
        imgPath += ".png";

        ImageIcon icon = createImageIcon(imgPath);
        return new JLabel(icon);
    }

    // EFFECTS: retrieves image from given path
    // Source: https://docs.oracle.com/javase/tutorial/uiswing/components/icon.html
    private static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = PieceImage.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
}
