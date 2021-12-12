package util;

import java.util.Objects;

public record TwoIntegersKey(int integer1, int integer2) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TwoIntegersKey that = (TwoIntegersKey) o;
        return integer1 == that.integer1 && integer2 == that.integer2;
    }

    @Override
    public int hashCode() {
        return Objects.hash(integer1, integer2);
    }
}
