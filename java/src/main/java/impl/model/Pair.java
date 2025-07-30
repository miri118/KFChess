package impl.model;

public class Pair {
        public final int r;
        public final int c;
        public Pair(int r, int c) { this.r = r; this.c = c; }
        @Override public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Pair)) return false;
            Pair other = (Pair) o;
            return r == other.r && c == other.c;
        }
        @Override public int hashCode() {
            return 31 * r + c;
        }
        @Override public String toString() { return "("+r+","+c+")"; }
    }
