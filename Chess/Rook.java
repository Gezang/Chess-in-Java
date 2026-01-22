import java.util.ArrayList;

public class Rook extends Piece {
    Rook(String color, Move position) {
        super(color, position);
    }

    Rook(String color, Move position, boolean hasMoved) {
        super(color, position, hasMoved);
    }

    public String getString() {
        return color + "R";
    }

    @Override
    public Piece clonePiece() {
        return new Rook(color, position, hasMoved);
    }

    public ArrayList<Move> getPossible(Game game) {
        int[][] directions = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };
        return getLineMoves(game, directions);
    }

}