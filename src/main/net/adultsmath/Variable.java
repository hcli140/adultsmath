package main.net.adultsmath;

import java.util.Objects;

public class Variable {
    public static Variable ARBITRARY;
    public final String key;

    public Variable (String key) {this.key = key;}

    @Override
    public boolean equals (Object obj) {
        if (obj == this) return true;
        if (obj instanceof Variable) {
            return Objects.equals(this.key, ((Variable) obj).key);
        }
        return false;
    }
}
