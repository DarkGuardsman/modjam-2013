package dark.common;

public class Pair<L, R>
{
    private final L element;
    private final R element2;

    public Pair(L left, R right)
    {
        this.element = left;
        this.element2 = right;
    }

    public L getKey()
    {
        return element;
    }

    public R getValue()
    {
        return element2;
    }

    @Override
    public int hashCode()
    {
        return element.hashCode() ^ element2.hashCode();
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == null)
            return false;
        if (!(o instanceof Pair))
            return false;
        Pair pairo = (Pair) o;
        return this.element.equals(pairo.getKey()) && this.element2.equals(pairo.getValue());
    }

}