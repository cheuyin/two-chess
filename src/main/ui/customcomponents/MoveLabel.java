package ui.customcomponents;

import model.Move;

import javax.swing.*;

// Custom JLabel component that also associates itself with a move object
public class MoveLabel extends JLabel {
    private Move move;

    // EFFECTS: constructs a MoveLabel with given move
    public MoveLabel(Move move) {
        this.move = move;
    }

    // ===== getters =====
    public Move getMove() {
        return move;
    }
}
