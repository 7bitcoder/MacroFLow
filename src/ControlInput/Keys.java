package ControlInput;

import org.jnativehook.keyboard.NativeKeyEvent;

public class Keys {
    public static class Key {
        enum State {pressed, released}

        enum Type {keyboard, mouse}

        private Type type = Type.mouse;
        private Integer code = null;
        private State state = State.released;

        Key() {
        }

        Key(Integer c) {
            code = c;
        }

        public boolean isSet() {
            return code != null;
        }

        public Integer get() {
            return code;
        }

        public void set(Integer c) {
            code = c;
        }

        public void press() {
            state = State.pressed;
        }

        public void release() {
            state = State.released;
        }

        public boolean pressed() {
            return state == State.pressed;
        }

        public void setType(Type t) {
            type = t;
        }

        public Type getType(Type t) {
            return type;
        }

        public void reset() {
            code = null;
            type = Type.keyboard;
            state = State.released;
        }

        @Override
        public boolean equals(Object o) {
            var key = ((Key) o);
            if (isSet())
                return code.equals(key.code);
            else if (key.isSet())
                return false;
            else
                return true;
        }

        public boolean equals(int co) {
            if (isSet())
                return code.equals(co);
            return false;
        }

        @Override
        public String toString() {
            if (isSet())
                return NativeKeyEvent.getKeyText(code);
            else
                return "NONE";
        }
    }

    Key first = new Key();
    Key second = new Key();

    public Keys() {
    }

    public Keys(Integer fi, Integer sec) {
        setKeys(fi, sec);
    }

    public void setKeys(Integer fi, Integer sec) {
        first.set(fi);
        second.set(sec);
    }

    public boolean isEmpty() {
        return !first.isSet() && !second.isSet();
    }

    public Key getFirst() {
        return first;
    }

    public Key getSecond() {
        return second;
    }

    @Override
    public boolean equals(Object o) {
        var keys = (Keys) o;
        if (isEmpty() && keys.isEmpty())
            return false;
        return first.equals(keys.first) && second.equals(keys.second);
    }

    @Override
    public String toString() {
        String str = "Not set";
        if (first.isSet()) {
            str = first.toString();
            if (second.isSet()) {
                str += "+" + second.toString();
            }
        }
        return str;
    }

    public void reset() {
        first.reset();
        second.reset();
    }
}
