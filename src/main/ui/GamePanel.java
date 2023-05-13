package ui;

import model.Move;

import javax.swing.*;
import ui.customcomponents.MoveLabel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// GUI components of move list and filters
public class GamePanel implements ActionListener {
    private TwoChess controller;
    private JPanel gamePanel;
    private JPanel movesList;
//    private JButton resetButton;
    private JScrollPane scrollPane;
    private String movesFilter; // "All", "White", or "Black"
    private MoveLabel currentSelectedMove;
    private JLabel moveDetails;

    // EFFECTS: constructs game panel with given controller
    public GamePanel(TwoChess controller) {
        this.controller = controller;
        this.gamePanel = new JPanel();
        this.movesFilter = "All";
        gamePanel.setLayout(new BoxLayout(gamePanel, BoxLayout.Y_AXIS));

        JPanel movesPanel = createMovesPanel();
        gamePanel.add(movesPanel);
        movesPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

//        JButton resetButton = createResetButton();
//        gamePanel.add(resetButton);
//        resetButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    // MODIFIES: this
    // EFFECTS: updates move list panel to newest version
    public void updateMovesList() {
        movesList.removeAll();

//        List<Move> allMoves = controller.getChessGame().getMoveHistory().getAllMoves();
        List<Move> blackMoves = controller.getChessGame().getMoveList().getBlackMoves();
        List<Move> whiteMoves = controller.getChessGame().getMoveList().getWhiteMoves();

        if (movesFilter.equals("All")) {
            for (int i = 0; i < controller.getChessGame().getMoveList().getAllMoves().size(); i++) {
                // Add bold move numbering at appropriate places
                if (i % 2 == 0) {
                    movesList.add(createMoveNumber(i / 2 + 1));
                }
                movesList.add(createMove(controller.getChessGame().getMoveList().getAllMoves().get(i)));
            }
        } else if (movesFilter.equals("White")) {
            for (int i = 0; i < whiteMoves.size(); i++) {
                movesList.add(createMoveNumber(i + 1));
                movesList.add(createMove(whiteMoves.get(i)));
            }
        } else {
            for (int i = 0; i < blackMoves.size(); i++) {
                movesList.add(createMoveNumber(i + 1));
                movesList.add(createMove(blackMoves.get(i)));
            }
        }

        movesList.revalidate();
        movesList.repaint();
    }

    // EFFECTS: creates JLabel with formatted text of a move number, e.g "2."
    private JLabel createMoveNumber(int num) {
        JLabel moveNumber = new JLabel(" " + num + ".");
        Font font = new Font("Courier", Font.BOLD,15);
        moveNumber.setFont(font);
        return moveNumber;
    }

    // EFFECTS: constructs a move label based on a move
    private MoveLabel createMove(Move move) {
        MoveLabel newMove = new MoveLabel(move);
        newMove.setText(move.getFormattedMove());
        newMove.addMouseListener(moveClickHandler());
        return newMove;
    }

    // EFFECTS: constructs a move panel container containing a move list, filter dropdown menu, and narration label
    private JPanel createMovesPanel() {
        JPanel movesPanel = new JPanel();
        movesPanel.setLayout(new BoxLayout(movesPanel, BoxLayout.Y_AXIS));

        JPanel movesDisplay = createMovesListPanel();
        movesDisplay.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel moveDetails = createMoveDetails();
        moveDetails.setAlignmentX(Component.CENTER_ALIGNMENT);

        movesPanel.add(movesDisplay);
        movesPanel.add(moveDetails);

        return movesPanel;
    }

    // EFFECTS: constructs move list panel with move list and filter dropdown menu
    private JPanel createMovesListPanel() {
        JPanel movesListPanel = new JPanel();
        movesListPanel.add(createMovesList());
        movesListPanel.add(createMovesListOptions());
        return movesListPanel;
    }

    // MODIFIES: this
    // EFFECTS: initializes move list panel and puts it inside a scrollable container
    private JScrollPane createMovesList() {
        movesList = new JPanel(new FlowLayout(FlowLayout.LEFT));
        scrollPane = new JScrollPane(movesList);
        scrollPane.setPreferredSize(new Dimension(300, 60));

        return scrollPane;
    }

    // EFFECTS: constructs dropdown component for choosing filter options
    private JComboBox createMovesListOptions() {
        String[] movesDisplayOptions = { "All", "White", "Black" };
        JComboBox optionsList = new JComboBox(movesDisplayOptions);
        optionsList.setSelectedIndex(0);
        optionsList.addActionListener(this);

        return optionsList;
    }

//    private JButton createResetButton() {
//        resetButton = new JButton();
//        resetButton.setText("Reset");
//        resetButton.addActionListener(this);
//        return resetButton;
//    }

    // MODIFIES: this
    // EFFECTS: creates label for detailed description of a move
    private JLabel createMoveDetails() {
        moveDetails = new JLabel();
        moveDetails.setText("<Click on a move for details>");
        return moveDetails;
    }

    // EFFECTS: returns mouse click handler that highlights and updates move detail label when a move is clicked
    private MouseAdapter moveClickHandler() {
        return new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                MoveLabel selectedMoveLabel = (MoveLabel) e.getSource();
                Move move = selectedMoveLabel.getMove();
                if (currentSelectedMove != null) {
                    currentSelectedMove.setForeground(Color.BLACK);
                }
                selectedMoveLabel.setForeground(Color.BLUE);
                currentSelectedMove = selectedMoveLabel;
                String longMoveDescription = createLongMoveDescription(move);
                moveDetails.setText(longMoveDescription);
            }
        };
    }

    // EFFECTS: produces string of a detailed description of a move
    private String createLongMoveDescription(Move move) {
        String fromSide = capitalizeString(move.getFromSide().toString());
        String fromType = capitalizeString(move.getFromType().toString());
        return fromSide + " " + fromType + " moves to " + move.getToPos();
    }

    // EFFECTS: returns given string with first letter capitalized and other letters lower-cased
    private String capitalizeString(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    // MODIFIES: this
    // EFFECTS: filters move list when a filter option is updated
    @Override
    public void actionPerformed(ActionEvent e) {
        JComboBox filterDropdown = (JComboBox) e.getSource();
        movesFilter = filterDropdown.getSelectedItem().toString();
        updateMovesList();
    }

    // ===== getters =====
    public JPanel getGamePanel() {
        return gamePanel;
    }

}
