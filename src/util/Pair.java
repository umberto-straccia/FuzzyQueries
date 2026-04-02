package util;

public class Pair<K, V>  {
    private K d1;
    private V d2;

    public Pair(K d1, V d2) {
        this.d1 = d1;
        this.d2 = d2;
    }

    public K getD1() {
        return d1;
    }

    public void setD1(K d1) {
        this.d1 = d1;
    }

    public V getD2() {
        return d2;
    }

    public void setD2(V d2) {
        this.d2 = d2;
    }

    @Override
    public boolean equals (Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Pair)) {
            return false;
        }
        Pair pair = (Pair) o;
        return this.d1.equals(pair.d1) && this.d2.equals(pair.d2);
    }
}