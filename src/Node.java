public class Node implements Comparable<Node>
{
    final static int ROW_COUNT = 15;
    final static int COLUMN_COUNT = 14;

    private int row;
    private int column;
    private int index;
    private int weight;
    private Node previousNode; // Parent node path
    private int costToNode; // The current known cost to the node. Integer so we can set to null initially
    private boolean visited;

    public Node(int row, int column, int weight, int index)
    {
        this.row = row;
        this.column = column;
        this.weight = weight;
        this.index = index;
        costToNode = 0;
        visited = false;
        previousNode = null;
    }

    public boolean isVisited()
    {
        return visited;
    }

    public void setVisited(boolean visited)
    {
        this.visited = visited;
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
            this.costToNode = 500;
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
