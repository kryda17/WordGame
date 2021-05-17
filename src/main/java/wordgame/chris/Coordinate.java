package wordgame.chris;

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
}
