package dark.common.prefab;

public class Pair<L, R>
{
    private final L element;
    private final R element2;

    public Pair(L one, R two)
    {
        this.element = one;
        this.element2 = two;
    }

    public L getOne()
    {
        return element;
    }

    public R getTwo()
    {
        return element2;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == null || !(o instanceof Pair))
        {
            return false;
        }
        Pair pairo = (Pair) o;
        return this.element.equals(pairo.getOne()) && this.element2.equals(pairo.getTwo());
    }

    @Override
    public int hashCode()
    {
        return element.hashCode() ^ element2.hashCode();
    }
}