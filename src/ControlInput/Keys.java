package ControlInput;

public class Keys {
    static enum State {
        pressed, released;
    }

    public class Key {
        Integer code;
        State state = State.released;

        Key(Integer c) {
            code = c;
        }

        void release() {
            state = State.released;
        }

        void press() {
            state = State.released;
        }
        boolean pressed() {
            return state == State.pressed;
        }
    }

    Key first;
    Key second;

    public Keys(Integer fi, Integer sec) {
        first = new Key(fi);
        if (sec != null)
            second = new Key(sec);
    }
}
