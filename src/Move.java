import java.util.Objects;

public class Move {
    private int row;
    private int col;
    private MoveType type;

    public enum MoveType {
        NORMAL, CAPTURE, CASTLE, EN_PASSANT, PROMOTION, DOUBLE_PUSH
    }

    public Move(int row, int col) {
        this.row = row;
        this.col = col;
        this.type = MoveType.NORMAL;
    }

    public Move(int row, int col, MoveType type) {
        this.row = row;
        this.col = col;
        this.type = type;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void setType(MoveType type) {
        this.type = type;
    }

    public MoveType getType() {
        return type;
    }

    public boolean isValid() {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Move other = (Move) obj;
        return row == other.row && col == other.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    @Override
    public String toString() {
        int chessRow = 8 - row;
        char chessCol = (char) ('a' + col);
        return chessCol + Integer.toString(chessRow);
    }
}