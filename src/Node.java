public class Node implements Comparable<Node>
{
    final static int ROW_COUNT = 15;
    final static int COLUMN_COUNT = 14;

    private int row;
    private int column;
    private int index;
    private int weight;
    // private int distance; // Approximate euclidean distance from target as a
    // heuristics.
    // private int directionalWeight; // The sum of weight + distance
    private Node previousNode; // Parent node path
    private Integer costToNode; // The current known cost to the node. Integer so we can set to null initially

    public Node(int row, int column, int weight, int index)
    {
        this.row = row;
        this.column = column;
        this.weight = weight;
        this.index = index;
        // setDistance();
        costToNode = null;
    }

    // private void setDistance()
    // {
    // // Calculate distance from end point via pythagorean theorem.
    // // Since the last node is top right we can always
    // int normalizedXFromRight = (COLUMN_COUNT - 1) - column;
    // this.distance = (row * row) + (normalizedXFromRight * normalizedXFromRight);
    // this.distance = (int) Math.sqrt(this.distance); // Give a rounded number -
    // should be ok.
    // }

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

    // public int getAvgWeight()
    // {
    // directionalWeight = distance + weight;
    // return directionalWeight;
    // }

    public Node getPreviousNode()
    {
        return previousNode;
    }

    public void setPreviousNode(Node previousNode)
    {
        this.previousNode = previousNode;
    }

    public int getCostToNode()
    {
        return costToNode;
    }

    public void setCostToNode(int costToNode)
    {
        // Nodes with weight -1 can't be accessed
        if (this.weight == -1)
            this.costToNode = null;
        else
            this.costToNode = costToNode;
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
        String node = String.format("[%d,%d] Index: [%d] Weight: %d", row, column, index, weight);
        return node;
    }

    /**
     * Implements comparable by looking at weight variable.
     * 
     * @param other
     * @return
     */
    @Override
    public int compareTo(Node other)
    {
        if (this.weight == other.weight)
            return 0;
        if (this.weight > other.weight)
            return 1;
        else
            return -1;
    }
}
