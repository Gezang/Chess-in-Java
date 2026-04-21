import java.util.ArrayList;

public class King extends Piece {
    King(String color, Move position) {
        super(color, position);
    }

    King(String color, Move position, boolean hasMoved) {
        super(color, position, hasMoved);
    }

    public String getString() {
        return color + "K";
    }

    @Override
    public Piece clonePiece() {
        return new King(color, position, hasMoved);
    }

    public ArrayList<Move> getPossible(Game game) {
        ArrayList<Move> possible = new ArrayList<Move>();
        int row = position.getRow();
        int col = position.getCol();

        int[][] kingOffsets = {
                { +1, +1 }, { +1, -1 },
                { -1, +1 }, { -1, -1 },
                { +1, +0 }, { -1, +0 },
                { +0, +1 }, { +0, -1 }
        };

        for (int[] offset : kingOffsets) {
            int newRow = row + offset[0];
            int newCol = col + offset[1];
            Move newMove = new Move(newRow, newCol);

            if (newMove.isValid()) {
                Piece capturePiece = game.getPiece(newMove);
                if (capturePiece == null) {
                    possible.add(newMove);
                } else if (!capturePiece.getColor().equals(color)) {
                    newMove.setType(Move.MoveType.CAPTURE);
                    possible.add(newMove);
                }
            }
        }

        // castling
        if (hasMoved || game.isCheck(color)) {
            return possible;
        }
        // O-O
        if (game.getPiece(new Move(row, 5)) == null
                && !game.isKingAttackedAfterMove(color, new Move(row, 5))
                && game.getPiece(new Move(row, 6)) == null
                && game.getPiece(new Move(row, 7)) instanceof Rook
                && !game.getPiece(new Move(row, 7)).hasMoved()) {
            possible.add(new Move(row, 6, Move.MoveType.CASTLE));
        }
        // O-O-O
        if (game.getPiece(new Move(row, 3)) == null
                && !game.isKingAttackedAfterMove(color, new Move(row, 3))
                && game.getPiece(new Move(row, 2)) == null
                && game.getPiece(new Move(row, 1)) == null
                && game.getPiece(new Move(row, 0)) instanceof Rook
                && !game.getPiece(new Move(row, 0)).hasMoved()) {
            possible.add(new Move(row, 2, Move.MoveType.CASTLE));
        }

        return possible;
    }

    public void makeMove(Game game, Move move) {
        super.makeMove(game, move);
        game.setKingPosition(color, move);
        if (move.getType() == Move.MoveType.CASTLE) {
            int row = move.getRow();
            int rookCol = move.getCol() == 6 ? 7 : 0;
            int rookNewCol = move.getCol() == 6 ? 5 : 3;
            Piece rook = game.getPiece(new Move(row, rookCol));
            rook.makeMove(game, new Move(row, rookNewCol));
        }
    }
}
