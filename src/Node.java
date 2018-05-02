
public class Node
{
    private int row;
    private int column;
    private int index;
    private int weight;
    private int distance;
    private int directionalWeight;

    public Node(int row, int column, int weight, int index)
    {
        this.row = row;
        this.column = column;
        this.weight = weight;
        this.index = index;
        setDistance();
    }

    private void setDistance()
    {
        // Calculate distance from end point.
        this.distance = (row * row) + (column * column);
    }

    public int getDistance()
    {
        return distance;
    }

    public int getRow()
    {
        return row;
    }

    public int getColumn()
    {
        return column;
    }

    public int getWeight()
    {
        return weight;
    }

    public int getIndex()
    {
        return index;
    }

    public int getDirectionalWeight()
    {
        return directionalWeight;
    }

    public boolean isEqual(Node comp)
    {
        if (comp.column != column)
            return false;
        if (comp.row != row)
            return false;
        if (comp.weight != weight)
            return false;
        else
            return true;
    }

    public String toString()
    {
        String node = String.format("Index: [%d,%d]/[%d] Weight: %d Distance: %d", row, column, index, weight,
                distance);
        return node;
    }
}
