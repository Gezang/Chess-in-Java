import java.util.ArrayList;

public abstract class Piece {

    protected String color;
    protected Move position;
    protected boolean hasMoved = false;

    Piece(String color, Move position) {
        this.color = color;
        this.position = position;
    }

    Piece(String color, Move position, boolean hasMoved) {
        this.color = color;
        this.position = position;
        this.hasMoved = hasMoved;
    }

    public String getColor() {
        return color;
    }

    public Move getPosition() {
        return position;
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    public void makeMove(Game game, Move move) {
        Piece[][] board = game.getBoard();
        board[move.getRow()][move.getCol()] = this;
        board[position.getRow()][position.getCol()] = null;
        position = move;
        hasMoved = true;
    }

    public abstract String getString();

    public abstract ArrayList<Move> getPossible(Game game);

    public abstract Piece clonePiece();

    public ArrayList<Move> getLineMoves(Game game, int[][] directions) {
        ArrayList<Move> possible = new ArrayList<Move>();
        Move position = getPosition();
        int row = position.getRow();
        int col = position.getCol();

        for (int[] direction : directions) {
            for (int i = 1; i < 8; i++) {
                Move newMove = new Move(row + i * direction[0], col + i * direction[1]);
                if (!newMove.isValid()) {
                    continue;
                }
                Piece pieceAtSq = game.getPiece(newMove);
                if (pieceAtSq == null) {
                    possible.add(newMove);
                } else if (!pieceAtSq.getColor().equals(getColor())) {
                    newMove.setType(Move.MoveType.CAPTURE);
                    possible.add(newMove);
                    break;
                } else {
                    break;
                }
            }

        }
        return possible;
    }
}