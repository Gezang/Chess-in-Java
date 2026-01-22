import java.util.ArrayList;

public class Pawn extends Piece {
    Pawn(String color, Move position) {
        super(color, position);
    }

    Pawn(String color, Move position, boolean hasMoved) {
        super(color, position, hasMoved);
    }

    public String getString() {
        return color + "P";
    }

    @Override
    public Piece clonePiece() {
        return new Pawn(color, position, hasMoved);
    }

    public void makeMove(Game game, Move move) {
        Piece[][] board = game.getBoard();

        if (move.getRow() == 0 || move.getRow() == 7) {
            board[move.getRow()][move.getCol()] = new Queen(color, move); // can add more fun promotion logic here
        } else {
            board[move.getRow()][move.getCol()] = this;
            if (move.getType() == Move.MoveType.EN_PASSANT) {
                board[position.getRow()][move.getCol()] = null;
            }
        }
        board[position.getRow()][position.getCol()] = null;
        position = move;
    }

    public ArrayList<Move> getPossible(Game game) {
        boolean white = color.equals("w");
        ArrayList<Move> possible = new ArrayList<Move>();
        int pushDir = white ? -1 : 1;

        Move push1 = new Move(position.getRow() + pushDir, position.getCol());
        if (game.getPiece(push1) == null) { // add promotion
            possible.add(push1);
            if (position.getRow() == (white ? 6 : 1)) {
                Move push2 = new Move(position.getRow() + 2 * pushDir, position.getCol(), Move.MoveType.DOUBLE_PUSH);
                if (game.getPiece(push2) == null) {
                    possible.add(push2);
                }
            }
        }

        Move[] captures = {
                new Move(position.getRow() + pushDir, position.getCol() + 1, Move.MoveType.CAPTURE),
                new Move(position.getRow() + pushDir, position.getCol() - 1, Move.MoveType.CAPTURE)
        };
        for (Move capture : captures) {
            if (!capture.isValid()) {
                continue;
            }
            Piece capturePiece = game.getPiece(capture);
            if (capturePiece != null
                    && !capturePiece.getColor().equals(color)) {
                possible.add(capture);
            }
            // en passant
            Move enPassantSquare = new Move(position.getRow(), capture.getCol());
            Piece enPassantCapturePiece = game.getPiece(enPassantSquare);
            if (enPassantCapturePiece != null
                    && !enPassantCapturePiece.getColor().equals(color)
                    && enPassantCapturePiece.getPosition().getType() == Move.MoveType.DOUBLE_PUSH
                    && game.getLastMoved() == enPassantCapturePiece) {
                capture.setType(Move.MoveType.EN_PASSANT);
                possible.add(capture);
            }
        }
        return possible;
    }
}
