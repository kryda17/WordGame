package wordgame.chris.jpa;

import java.util.Objects;

public class Coordinate {

    private int x;
    private int y;
    private Alignment alignment;

    public Coordinate(int x, int y, Alignment alignment) {
        this.x = x;
        this.y = y;
        this.alignment = alignment;
    }

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Alignment getAlignment() {
        return alignment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate that = (Coordinate) o;
        return x == that.x && y == that.y && alignment == that.alignment;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, alignment);
    }

    @Override
    public String toString() {
        String s = "";
        if (alignment == Alignment.HORISONTAL) {
            s += "H";
        }
        else s += "V";
        return s+= x + ":" + y;
    }
}
