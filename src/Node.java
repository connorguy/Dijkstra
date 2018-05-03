
public class Node
{
    final static int ROW_COUNT = 15;
    final static int COLUMN_COUNT = 14;

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
        // Calculate distance from end point via pythagorean theorem.
        // Since the last node is top right we can always 
        int normalizedXFromRight = (COLUMN_COUNT - 1) - column;
        this.distance = (row * row) + (normalizedXFromRight * normalizedXFromRight);
        this.distance = (int) Math.sqrt(this.distance); // Give a rounded number - should be ok.
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

    public int getAvgWeight()
    {
        directionalWeight = distance + weight;
        return directionalWeight;
    }

    public boolean isEqual(Node comp)
    {
        if (comp.index != index)
            return false;
        else
            return true;
    }

    public String toString()
    {
        String node = String.format("[%d,%d] Index: [%d] Weight: %d Distance: %d", row, column, index, weight,
                getAvgWeight());
        return node;
    }
}
