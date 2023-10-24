package main.net.adultsmath;

import java.util.Objects;

public record Variable(String key) {

    public Variable {
        if (key == null) throw new NullKeyConstructionException();
    }

    @Override
    public String toString() {
        return this.key();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj instanceof Variable variable) {
            return Objects.equals(this.key(), variable.key());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(key());
    }

    private static class NullKeyConstructionException extends RuntimeException {}
}
