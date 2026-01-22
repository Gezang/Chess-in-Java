public class GameState {
    private Piece[][] board;
    private boolean whiteturn;
    private Move whiteKingPosition;
    private Move blackKingPosition;
    private Piece lastMoved;
    private Game.Status status;

    public GameState(Piece[][] board, boolean whiteturn, Move whiteKingPosition, Move blackKingPosition,
            Piece lastMoved, Game.Status status) {
        // Deep copy the board to store the current state
        this.board = new Piece[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                this.board[i][j] = board[i][j] != null ? board[i][j].clonePiece() : null;
            }
        }
        this.whiteturn = whiteturn;
        this.whiteKingPosition = whiteKingPosition;
        this.blackKingPosition = blackKingPosition;
        this.status = status;
        if (lastMoved == null) {
            this.lastMoved = null;
        } else {
            Move lastMovedPosition = lastMoved.getPosition();
            this.lastMoved = this.board[lastMovedPosition.getRow()][lastMovedPosition.getCol()];
        }
    }

    // Getters for the game state
    public Piece[][] getBoard() {
        return board;
    }

    public boolean iswhite() {
        return whiteturn;
    }

    public Move getWhiteKingPosition() {
        return whiteKingPosition;
    }

    public Move getBlackKingPosition() {
        return blackKingPosition;
    }

    public Piece getLastMoved() {
        return lastMoved;
    }

    public Game.Status getStatus() {
        return status;
    }
}
