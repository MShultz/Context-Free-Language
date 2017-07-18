import java.util.Objects;

/**
 * Created by Mary on 7/17/2017.
 */
public class KVP {
    private Node key;
    private int value;

    public KVP(Node key, int value) {
        this.key = key;
        this.value = value;
    }

    public void incrementValue(){
        ++value;
    }

    public int getValue() {
        return value;
    }

    public Node getKey() {
        return key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KVP kvp = (KVP) o;
        return Objects.equals(key.toString(), kvp.getKey().toString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(key.toString());
    }

    public void resetValue(){
        this.value = 0;
    }
}
