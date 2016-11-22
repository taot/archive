package me.taot.mcache2;

import java.util.Arrays;

public class Key {

    private final Object[] comps;

    public Key(Object... comps) {
        this.comps = Arrays.copyOf(comps, comps.length);
    }

    @Override
    public String toString() {
        return "Key " + Arrays.toString(comps);
    }

    @Override
    public boolean equals(Object o) {
        if (! (o instanceof Key)) {
            return false;
        }
        Key other = (Key) o;
        return Arrays.equals(this.comps, other.comps);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(comps);
    }
}
