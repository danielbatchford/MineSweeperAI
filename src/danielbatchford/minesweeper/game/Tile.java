package danielbatchford.minesweeper.game;

import java.util.Objects;

public class Tile {

    private final int x;
    private final int y;
    private Status status;
    private int value;
    private boolean isMine;

    Tile(int x, int y) {

        this.x = x;
        this.y = y;
        this.status = Status.UNCOVERED;
        this.value = 0;
        this.isMine = false;
    }

    int getX() {

        return this.x;
    }

    int getY() {

        return this.y;
    }

    void setToMine() {

        this.isMine = true;
    }

    boolean isMine() {

        return this.isMine;
    }

    int getValue() {

        return this.value;
    }

    void setValue(int value) {

        this.value = value;
    }

    Status getStatus() {

        return this.status;
    }

    void setStatus(Status status) {

        this.status = status;
    }

    boolean isCovered() {

        return this.status.equals(Status.COVERED);
    }

    boolean isUncovered() {

        return this.status.equals(Status.UNCOVERED);
    }

    boolean isFlagged() {

        return this.status.equals(Status.FLAGGED);
    }

    void toggleFlagged() {

        this.status = this.status.equals(Status.FLAGGED) ? Status.COVERED : Status.FLAGGED;
    }

    int[] getColorMap() {

        return this.status.getColorMap();
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tile tile = (Tile) o;
        return x == tile.x &&
                y == tile.y &&
                value == tile.value &&
                isMine == tile.isMine &&
                status == tile.status;
    }

    @Override
    public int hashCode() {

        return Objects.hash(x, y, status, value, isMine);
    }

    @Override
    public String toString() {

        return String.format("X: %s, Y: %s, Status: %s, IsMine: %s, Value: %s", x, y, status, isMine, value);
    }

}
