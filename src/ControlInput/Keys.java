package ControlInput;

public class Keys {
    static enum State {
        pressed, released;
    }

    public class Key {
        int code;
        State state = State.released;

        Key(int c) {
            code = c;
        }

    }

    Key first;
    Key second;

    public Keys(int fi, int sec) {
        first = new Key(fi);
        second = new Key(sec);
    }

    public Keys(int fi) {
        first = new Key(fi);
    }

    public boolean equals(Object o) {
        // no need for (o instanceof Point) by design
        return first.code == ((Keys) o).first.code && second.code == ((Keys) o).second.code;
    }

    public int hashCode() {
        return first.code * 31 + second.code;
    }
}
