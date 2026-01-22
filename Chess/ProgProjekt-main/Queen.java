import java.util.ArrayList;

public class Queen extends Piece {
    Queen(String color, Move position) {
        super(color, position);
    }

    Queen(String color, Move position, boolean hasMoved) {
        super(color, position, hasMoved);
    }

    public String getString() {
        return color + "Q";
    }

    @Override
    public Piece clonePiece() {
        return new Queen(color, position, hasMoved);
    }

    public ArrayList<Move> getPossible(Game game) {
        int[][] directions = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 },
                { 1, 1 }, { -1, -1 }, { 1, -1 }, { -1, 1 } };
        return getLineMoves(game, directions);
    }
}
