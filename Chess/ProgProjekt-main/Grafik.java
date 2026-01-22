import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

public class Grafik extends JFrame implements ActionListener {
    ChessButton[][] knappar = new ChessButton[8][8];
    Game game = new Game();
    ArrayList<Move> possible = new ArrayList<Move>();
    HashMap<String, Icon> pieceIcons = new HashMap<String, Icon>();
    JLabel displayText;
    JButton revertButton;

    Grafik() {
        super("Chess");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(8, 8));
        panel.setBackground(Color.BLACK);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j <= 7; j++) {
                ChessButton knapp = new ChessButton(new Move(i, j));
                knappar[i][j] = knapp;
                knapp.addActionListener(this);
                panel.add(knapp);
            }
        }
        displayText = new JLabel("White's turn");
        revertButton = new JButton("Revert Move");
        revertButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                game.revertLastMove();
                updateboard();
            }
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(revertButton);
        add(panel, "Center");
        add(displayText, "North");
        add(buttonPanel, "South");
        setVisible(true);

        for (String piece : new String[] { "bB", "bK", "bN", "bP", "bQ", "bR", "wB", "wK", "wN", "wP", "wQ", "wR" }) {
            ImageIcon icon = new ImageIcon("images/" + piece + ".png");
            Image img = icon.getImage();
            Image newimg = img.getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH);
            pieceIcons.put(piece, new ImageIcon(newimg));
        }
        game.startGame();
        updateboard();
    }

    public void actionPerformed(ActionEvent e) {
        // kanske ändra till >1
        boolean canMove = possible.size() > 0;
        ChessButton sq = (ChessButton) e.getSource();
        Move position = sq.getPosition();

        // Possible move
        if (canMove && possible.contains(position)) {
            Move move = possible.get(possible.indexOf(position)); // strange but keeps move type
            game.makeMove(move);
            game.updateStatus();
            togglePossible(null);
            updateboard();
            return;
        }
        // Select piece
        Piece pieceAtSq = game.getPiece(position);
        // No selectable piece at square
        if (pieceAtSq == null || !pieceAtSq.getColor().equals(game.iswhite() ? "w" : "b")) {
            togglePossible(null);

            // Visar felmeddelande ifall det inte är ett möjligt drag.
            String displayString = "Not a valid move. " + (game.iswhite() ? "White" : "Black") + "'s turn";
            displayText.setText(displayString);
            return;
        }
        // Update selected piece
        game.choosepiece(position);
        ArrayList<Move> newPossible = game.getPossible();

        if (newPossible.size() == 1) {
            Move move = newPossible.get(0);
            game.makeMove(move);
            game.updateStatus();
            togglePossible(null);
            updateboard();

            // Med yes no svar och dialog ruta:

            knappar[move.getRow()][move.getCol()].makeSpecial();
            int response = JOptionPane.showConfirmDialog(null,
                    "Endast ett drag tillgängligt, draget utförs automatiskt. Vill du göra detta drag?",
                    "Automatiskt move",
                    JOptionPane.YES_NO_OPTION);

            // Vill du utföra draget eller inte?
            if (response == JOptionPane.YES_OPTION) {
                knappar[move.getRow()][move.getCol()].makenormal();
            } else if (response == JOptionPane.NO_OPTION) {
                knappar[move.getRow()][move.getCol()].makenormal();
                game.revertLastMove();
                updateboard();
            } else {
                knappar[move.getRow()][move.getCol()].makenormal();
                game.revertLastMove();
                updateboard();
            }
            /*
             * // Utan dialogruta:
             * String displayString =
             * "Only one possible move. Press revert if you wish to undo the move, else: "
             * + (game.iswhite() ? "White" : "Black") + "'s turn";
             * displayText.setText(displayString);
             */

            return;

        }

        if (true) { // (newPossible.size() > 1) Så jävla efterblivet att jag väntat med att
                    // implementera detta
            togglePossible(newPossible);
        } else {
            updateboard();
        }

    }

    public void updateboard() {
        Piece[][] board = game.getBoard();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j <= 7; j++) {
                if (board[i][j] != null) {
                    // GetString() returns a string with the color and the type of the piece
                    String pic = board[i][j].getString();
                    knappar[i][j].setIcon(pieceIcons.get(pic));
                } else {
                    knappar[i][j].setIcon(null);
                }
            }
        }
        Game.Status status = game.getstatus();
        String displayString = (game.iswhite() ? "White" : "Black") + "'s turn";
        switch (status) {
            case CHECK:
                displayString += ". Check!";
                break;
            case CHECKMATE:
                displayString += ". Checkmate!";
                break;
            case STALEMATE:
                displayString += ". Stalemate!";
                break;
            default:
                break;
        }
        displayText.setText(displayString);
    }

    public void togglePossible(ArrayList<Move> newPossible) {
        // Clear old possible
        for (Move i : possible) {
            knappar[i.getRow()][i.getCol()].makenormal();
        }

        // Set new possible
        if (newPossible == null) {
            possible.clear();
        } else {
            for (Move i : newPossible) {
                knappar[i.getRow()][i.getCol()].makepossible();
            }
            possible = newPossible;
        }
    }

    public static void main(String[] args) {
        Grafik g = new Grafik();
    }
}
