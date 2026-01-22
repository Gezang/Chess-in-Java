import java.util.ArrayList;

public class Bishop extends Piece {
    Bishop(String color, Move position) {
        super(color, position);
    }

    Bishop(String color, Move position, boolean hasMoved) {
        super(color, position, hasMoved);
    }

    public String getString() {
        return color + "B";
    }

    @Override
    public Piece clonePiece() {
        return new Bishop(color, position, hasMoved);
    }

    public ArrayList<Move> getPossible(Game game) {
        int[][] directions = { { 1, 1 }, { -1, -1 }, { 1, -1 }, { -1, 1 } };
        return getLineMoves(game, directions);
    }
}
