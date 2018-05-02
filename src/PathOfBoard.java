import java.util.ArrayList;
import java.util.List;

public class PathOfBoard
{
    private List<Node> pathIndexs;
    private List<Integer> pathIndexValues;

    public PathOfBoard()
    {
        pathIndexs = new ArrayList<Node>();
        pathIndexValues = new ArrayList<Integer>();

    }

    protected List<Node> getPath()
    {
        return pathIndexs;
    }

    protected int getTotalWeight()
    {
        return 0;
    }

    public boolean addNode(Node index)
    {
        return pathIndexs.add(index);
    }

}