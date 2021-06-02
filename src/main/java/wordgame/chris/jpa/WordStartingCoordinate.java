package wordgame.chris.jpa;


import java.util.Objects;

public class WordStartingCoordinate extends Coordinate {

        private Alignment alignment;

    public WordStartingCoordinate(int x, int y, Alignment alignment) {
        super(x, y);
        this.alignment = alignment;
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
        if (!super.equals(o)) return false;
        WordStartingCoordinate that = (WordStartingCoordinate) o;
        return alignment == that.alignment;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), alignment);
    }

    @Override
        public String toString() {
            String s = "";
            if (alignment == Alignment.HORISONTAL) {
                s += "H";
            }
            else s += "V";
            return s += super.toString();
        }
    }
