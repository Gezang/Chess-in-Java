import javax.swing.*;
import java.awt.*;

public class ChessButton extends JButton {
    Move position;
    Color color;

    ChessButton(Move position) {
        super();
        this.position = position;
        boolean lightSquare = (position.getRow() + position.getCol()) % 2 == 0;
        this.color = lightSquare ? new Color(0xEE, 0xEE, 0xD2) : new Color(0x76, 0x96, 0x56);

        setBackground(color);
        setForeground(color);
        setBorderPainted(false);
        setOpaque(true);
    }

    public void makepossible() {
        Color c = new Color(0xBA, 0xCA, 0x44);
        setBackground(c);
        setForeground(c);
    }

    public void makenormal() {
        setBackground(color);

    }

    public void update(Piece piece) {
        if (piece != null) {
            setText(piece.getColor() + " " + piece.getClass().getSimpleName());
        } else {
            setText("");
        }
    }

    public Move getPosition() {
        return position;
    }
}
