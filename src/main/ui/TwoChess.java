package ui;

import model.Board;
import model.EventLog;
import model.pieces.Piece;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;

// Game controller of a chess game; manages GUI and logic components
// Code structure inspired by demo: https://docs.oracle.com/javase/tutorial/displayCode.html?code=https://docs.oracle.com/javase/tutorial/uiswing/examples/layout/BoxLayoutDemoProject/src/layout/BoxLayoutDemo.java
public class TwoChess {
    private static JFrame frame;
    private static Container contentPane;
    private ChessBoard chessBoard;
    private GamePanel gamePanel;
    private Board chessGame;
    private static final String JSON_STORE = "./data/board.json";
    private JsonWriter jsonWriter = new JsonWriter(JSON_STORE);
    private JsonReader jsonReader = new JsonReader(JSON_STORE);

    // EFFECTS: constructs and renders a new game
    public TwoChess() {
        this.chessGame = new Board();
        this.gamePanel = new GamePanel(this);
        this.chessBoard = new ChessBoard(this);
        initializeFrame();
        addComponentsToPane(contentPane);
        frame.setVisible(true);
    }

    // EFFECTS: starts program
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TwoChess();
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: makes move in a game and updates GUI
    public void makeMove(String fromPos, String toPos) {
        chessGame.makeMove(fromPos, toPos);
        gamePanel.updateMovesList();
    }

    // MODIFIES: this
    // EFFECTS: add chess board and game panel to main frame
    private void addComponentsToPane(Container container) {
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        JPanel chessBoardComponent = chessBoard.getBoard();
        chessBoardComponent.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel gamePanelComponent = gamePanel.getGamePanel();
        gamePanelComponent.setAlignmentX(Component.CENTER_ALIGNMENT);

        container.add(chessBoardComponent);
        container.add(gamePanelComponent);
    }

    // MODIFIES: this
    // EFFECTS: constructs main frame of program
    private void initializeFrame() {
        frame = new JFrame("TwoChess");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 700);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        contentPane = frame.getContentPane();
        ((JPanel) contentPane).setBorder(new EmptyBorder(50, 0, 50, 0));
        addFrameWindowListeners();
    }

    // MODIFIES: this
    // EFFECTS: add open/close window listeners to enable saving/loading functionality
    private void addFrameWindowListeners() {
        addWindowCloseEventListener();
        addWindowOpenedEventListener();
    }

    // MODIFIES: this
    // EFFECTS: add event listener for when program window is closed
    private void addWindowCloseEventListener() {
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (JOptionPane.showConfirmDialog(frame,
                        "Do you want to save the game?", "Save game?",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                    saveBoard();
                }

                // Print all events to console
                EventLog eventLog = EventLog.getInstance();
                for (model.Event event : eventLog) {
                    System.out.println(event.getDescription());
                }
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: add event listener for when program window is opened
    private void addWindowOpenedEventListener() {
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowOpened(java.awt.event.WindowEvent windowEvent) {
                if (JOptionPane.showConfirmDialog(frame,
                        "Do you want to load a saved game?", "Load game?",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                    loadBoard();
                }
            }
        });
    }


    // EFFECTS: saves the current game to file
    public void saveBoard() {
        try {
            jsonWriter.open();
            jsonWriter.write(chessGame);
            jsonWriter.close();
            System.out.println("Saved board to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads stored game board into current game
    public void loadBoard() {
        try {
            Board board = jsonReader.read();
            this.chessGame = board;
            this.gamePanel = new GamePanel(this);
            this.chessBoard = new ChessBoard(this);
            contentPane = frame.getContentPane();
            contentPane.removeAll();
            ((JPanel) contentPane).setBorder(new EmptyBorder(50, 0, 50, 0));
            addComponentsToPane(contentPane);
            gamePanel.updateMovesList();
            frame.revalidate();
            frame.repaint();
            System.out.println("Loaded board from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    // ===== getters =====
    public Board getChessGame() {
        return chessGame;
    }
}
