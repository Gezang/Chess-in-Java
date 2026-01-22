import java.util.ArrayList;

public class Knight extends Piece {
    Knight(String color, Move position) {
        super(color, position);
    }

    Knight(String color, Move position, boolean hasMoved) {
        super(color, position, hasMoved);
    }

    public String getString() {
        return color + "N";
    }

    @Override
    public Piece clonePiece() {
        return new Knight(color, position, hasMoved);
    }

    public ArrayList<Move> getPossible(Game game) {
        ArrayList<Move> possible = new ArrayList<Move>();
        int row = position.getRow();
        int col = position.getCol();

        int[][] knightOffsets = {
                { +2, +1 }, { +2, -1 },
                { -2, +1 }, { -2, -1 },
                { +1, +2 }, { +1, -2 },
                { -1, +2 }, { -1, -2 }
        };

        for (int[] offset : knightOffsets) {
            int newRow = row + offset[0];
            int newCol = col + offset[1];
            Move newMove = new Move(newRow, newCol);
            if (!newMove.isValid()) {
                continue;
            }
            Piece capturePiece = game.getPiece(newMove);
            if (capturePiece == null) {
                possible.add(newMove);
            } else if (!capturePiece.getColor().equals(color)) {
                newMove.setType(Move.MoveType.CAPTURE);
                possible.add(newMove);
            }
        }
        return possible;
    }
}
