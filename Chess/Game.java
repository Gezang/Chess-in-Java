import java.util.ArrayList;
import java.util.Stack;

public class Game {
    public enum Status {
        CHECK, CHECKMATE, STALEMATE, NONE
    }

    Status status = Status.NONE;
    Stack<GameState> gameStateHistory = new Stack<>();
    Piece[][] board = new Piece[8][8];
    Boolean whiteturn = true;
    Piece selectedPiece = null;
    Piece lastMoved = null;
    Move whiteKingPosition = new Move(7, 4);
    Move blackKingPosition = new Move(0, 4);

    public Piece[][] getBoard() {
        return board;
    }

    public void startGame() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                board[row][col] = null;
            }
        }
        // Setting up white pieces
        String[] colors = { "w", "b" };
        for (String color : colors) {
            int row = color.equals("w") ? 7 : 0;

            board[row][0] = new Rook(color, new Move(row, 0));
            board[row][7] = new Rook(color, new Move(row, 7));
            board[row][1] = new Knight(color, new Move(row, 1));
            board[row][6] = new Knight(color, new Move(row, 6));
            board[row][2] = new Bishop(color, new Move(row, 2));
            board[row][5] = new Bishop(color, new Move(row, 5));
            board[row][3] = new Queen(color, new Move(row, 3));
            board[row][4] = new King(color, new Move(row, 4));

            int pawnRow = color.equals("w") ? 6 : 1;
            for (int i = 0; i < 8; i++) {
                board[pawnRow][i] = new Pawn(color, new Move(pawnRow, i));
            }
        }
    }

    public Piece getPiece(Move position) {
        return board[position.getRow()][position.getCol()];
    }

    public Status getstatus() {
        return status;
    }

    public Piece getLastMoved() {
        return lastMoved;
    }

    public void choosepiece(Move position) {
        selectedPiece = board[position.getRow()][position.getCol()];
    }

    public Piece getSelectedPiece() {
        return selectedPiece;
    }

    public boolean iswhite() {
        return whiteturn;
    }

    public ArrayList<Move> getPossible() {
        ArrayList<Move> unfilteredMoves = selectedPiece.getPossible(this);
        ArrayList<Move> filteredMoves = new ArrayList<Move>();
        for (Move move : unfilteredMoves) {
            if (!isKingAttackedAfterMove(selectedPiece.getColor(), move)) {
                filteredMoves.add(move);
            }
        }
        return filteredMoves;
    }

    public void makeMove(Move move) {
        gameStateHistory.push(new GameState(board, whiteturn, whiteKingPosition, blackKingPosition, lastMoved, status));
        selectedPiece.makeMove(this, move);
        lastMoved = selectedPiece;
        selectedPiece = null;
        whiteturn = !whiteturn;
    }

    public void revertLastMove() {
        selectedPiece = null;
        if (!gameStateHistory.isEmpty()) {
            GameState prevState = gameStateHistory.pop();
            this.board = prevState.getBoard();
            this.whiteturn = prevState.iswhite();
            this.whiteKingPosition = prevState.getWhiteKingPosition();
            this.blackKingPosition = prevState.getBlackKingPosition();
            this.lastMoved = prevState.getLastMoved();
            this.status = prevState.getStatus();
        }
    }

    public boolean isCheck(String color) {
        // start at king position and check from there if any of the opponent's pieces
        // see the king
        Move kingPosition = color.equals("w") ? whiteKingPosition : blackKingPosition;
        int pawnDir = color.equals("w") ? -1 : 1;
        int row = kingPosition.getRow();
        int col = kingPosition.getCol();

        // pawns
        Move pawnLocations[] = {
                new Move(row + pawnDir, col + 1),
                new Move(row + pawnDir, col - 1)
        };
        for (Move pawnLocation : pawnLocations) {
            if (pawnLocation.isValid()) {
                Piece pieceAtSq = getPiece(pawnLocation);
                if (pieceAtSq != null && !pieceAtSq.getColor().equals(color) && pieceAtSq instanceof Pawn) {
                    return true;
                }
            }
        }
        int knightOffsets[][] = {
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
            Piece capturePiece = getPiece(newMove);
            if (capturePiece != null
                    && !capturePiece.getColor().equals(color)
                    && capturePiece instanceof Knight) {
                return true;
            }
        }

        // line pieces
        int[][] directions = {
                { +1, +1 }, { +1, -1 },
                { -1, +1 }, { -1, -1 },
                { +1, +0 }, { -1, +0 },
                { +0, +1 }, { +0, -1 }
        };
        for (int[] direction : directions) {
            boolean diagonal = direction[0] != 0 && direction[1] != 0;
            for (int i = 1; i < 8; i++) {
                Move newMove = new Move(row + i * direction[0], col + i * direction[1]);
                if (!newMove.isValid()) {
                    continue;
                }
                Piece pieceAtSq = getPiece(newMove);
                if (pieceAtSq != null
                        && !pieceAtSq.getColor().equals(color)
                        && (pieceAtSq instanceof Queen
                                || (diagonal && pieceAtSq instanceof Bishop)
                                || (!diagonal && pieceAtSq instanceof Rook))) {
                    return true;
                } else if (pieceAtSq != null) {
                    break;
                }
            }
        }
        return false;
    }

    public void updateStatus() {
        String color = whiteturn ? "w" : "b";
        boolean isCheck = isCheck(color);
        boolean hasMoves = hasMoves(color);
        selectedPiece = null;

        if (isCheck && !hasMoves) {
            status = Status.CHECKMATE;
        } else if (isCheck) {
            status = Status.CHECK;
        } else if (!hasMoves) {
            status = Status.STALEMATE;
        } else {
            status = Status.NONE;
        }
    }

    private boolean hasMoves(String color) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board[row][col];
                if (piece != null && piece.getColor().equals(color)) {
                    Move position = piece.getPosition();
                    ArrayList<Move> possibleMoves = piece.getPossible(this);

                    for (Move move : possibleMoves) {
                        selectedPiece = getPiece(position);
                        System.out.println(piece.toString() + " " + position);
                        if (!isKingAttackedAfterMove(color, move)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean isKingAttackedAfterMove(String color, Move move) {
        Move selectedPosition = selectedPiece.getPosition();
        makeMove(move);
        boolean isAttacked = isCheck(color);
        revertLastMove();
        selectedPiece = getPiece(selectedPosition);
        return isAttacked;
    }

    public void setKingPosition(String color, Move position) {
        if (color == "w") {
            whiteKingPosition = position;
        } else {
            blackKingPosition = position;
        }
    }
}
