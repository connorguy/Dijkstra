import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 * Implementation of Dijkstra's on a hexagonal board.
 * 
 * @author connorguy
 *
 */
public class main
{
    static Node[][] board;
    static List<Node> closedNodes;
    static Comparator<Node> nodeCompare;
    static PriorityQueue<Node> openNodes;
    final static int ROW_SIZE = 16;
    final static int COLUMN_SIZE = 15;
    final static int FINAL_INDEX = 8;


    /**
     * @param args
     */
    /**
     * @param args
     */
    public static void main(String[] args)
    {
        // Array 31 rows, 15 columns that contain our organized nodes
        board = new Node[ROW_SIZE][COLUMN_SIZE];
        // List for the nodes that have been fully checked
        closedNodes = new ArrayList<Node>();
        // Create a priority que for open node list
        nodeCompare = new NodeComparator();
        openNodes = new PriorityQueue<Node>(233, nodeCompare);

        int[] input = getFileInput();

        // ----------------(For testing)----------------
        // Random rand = new Random();
        // int[] input = new int[233];
        // for (int i = 0; i < 233; i++) // fill array with random data for tests
        // {
        // input[i] = rand.nextInt(50) + 1;
        // // if (i % 3 == 0)
        // // input[i] = -1;
        // }
        fillHexArray(input);

        // For Testing the fill of the board
        testCase(); // Just prints out the board
        // ----------------(For testing)----------------
        
        // Starting node is in the bottom left of the board
        Node startingNode = board[15][0];
        startingNode.setVisited(true);
        startingNode.setCostToNode(0);
        // Add the start to the list and then build path.
        buildPath(startingNode);

        // Using the top right index as our end spot print out our path from the start.
        Node endNode = board[0][14];
        printPath(endNode);

        // We out.
    }

    private static void testCase()
    {
        // For Testing the fill of the board
        int total = 0;
        for (int x = 0; x < ROW_SIZE; x++)
        {
            for (int y = 0; y < COLUMN_SIZE; y++)
            {
                if (board[x][y] == null)
                    System.out.print(" nu ");
                else
                {
                    System.out.print(board[x][y].getWeight() + " ");
                    total++;
                }
            }
            System.out.println("");
        }
        System.out.println("** " + total);
    }

    // Fills an array with all the numbers from text file.
    private static int[] getFileInput()
    {
        // Read in the file into array
        Scanner scanner = null;
        try
        {
            File testCase = new File("src/test");
            scanner = new Scanner(testCase);
        } catch (FileNotFoundException e)
        {
            // Bad Bad Not Good - great band
            e.printStackTrace();
        }

        int[] input = new int[466];
        int arrayIndex = 0;
        // For whatever reason I can't get my reader to skip the index value - so read
        // the whole damn thing to an array.
        while (scanner.hasNextInt())
        {
            if (arrayIndex == 466)
                break;

            input[arrayIndex++] = scanner.nextInt();
        }

        // Size down the input to only contain the values we want
        int[] weights = new int[233];
        arrayIndex = 0;
        int actualCount = 0;
        for (int i : input)
        {
            // Skip even indexes - weights are on odd.
            if (actualCount % 2 != 0)
            {
                weights[arrayIndex++] = i;
            }
            actualCount++;
        }

        return weights;
    }

    /**
     * Fills hex array with the input from the file that was parsed. See docs for
     * exact details.
     * 
     * Fills first row with every other index. Subsequent rows are filled on odd
     * indexes first then on even indexes in order of i. When creating new nodes,
     * input is indexed from i but the nodes internal index variable is set to i+1
     * because that is how we have to reference it when printing out the path list.
     * Indexes that would be -1 are set to null so they cannot be accessed in the
     * search.
     * 
     * @param input
     */
    private static void fillHexArray(int[] input)
    {
        int elementsSize = input.length; // Might want to check this against the expected value.

        // Keep track of where we are on the board while we fill it
        int rowIndex = 0;
        int columnIndex = 0;

        // Fill the first row - which will be every other index
        for (int i = 0; i < 8; i++)
        {
            if (input[i] == -1)
                board[rowIndex][columnIndex] = null;
            else
                board[rowIndex][columnIndex] = new Node(rowIndex, columnIndex, input[i], i + 1);
            columnIndex = columnIndex + 2;
        }

        // Go through input and fill the board - see docs on board layout
        int evenOddSwitchCount = 1; // Used to tell whether filling odd or even index
        rowIndex = 1;
        columnIndex = 1; // Fill starts at i = 8 and on odd column.
        for (int i = 8; i < elementsSize; i++)
        {
            // Row column indexing rules
            if (columnIndex > COLUMN_SIZE - 2 && evenOddSwitchCount % 2 != 0) // Odd rule
            {
                // Case where we filled the odd indexes and now need to switch to even.
                evenOddSwitchCount++;
                columnIndex = 0;
            }
            else if (columnIndex > COLUMN_SIZE - 1 && evenOddSwitchCount % 2 == 0) // Even rule
            {
                // Case where we filled even and now we need to increase the row and switch to
                // odd indexes
                evenOddSwitchCount++;
                columnIndex = 1;
                rowIndex++;
            }
            
            
            if (evenOddSwitchCount % 2 != 0)
            {
                // fill spaces on the odd indexes
                if (input[i] == -1)
                    board[rowIndex][columnIndex] = null;
                else
                    board[rowIndex][columnIndex] = new Node(rowIndex, columnIndex, input[i], i + 1);
                columnIndex = columnIndex + 2;

            } else
            {
                // fill even spaces
                if (input[i] == -1)
                    board[rowIndex][columnIndex] = null;
                else
                    board[rowIndex][columnIndex] = new Node(rowIndex, columnIndex, input[i], i + 1);
                columnIndex = columnIndex + 2;
            }
            
        }
    }


    /**
     * Starting at the given end node we work our way back and add up our total
     * weight.
     * 
     * @param endNode
     */
    private static void printPath(Node endNode)
    {
        if (endNode == null)
        {
            System.out.println("Not a valid node to navigate to.");
            return;
        }
        Node pathNode = endNode;
        int totalWeight = 0;
        while (pathNode.getPreviousNode() != null)
        {
            // print the index we are at.
            System.out.println(pathNode.getIndex() + " ");
            // Add this nodes weight to the total weight. I THINK THIS CAN JUST BE OUR END
            // NODE DISTANCE
            // totalWeight = totalWeight + pathNode.getWeight();
            pathNode = pathNode.getPreviousNode();
        }
        totalWeight = pathNode.getWeight() + endNode.getCostToNode();
        System.out.println(pathNode.getIndex());
        System.out.println("Total weight: " + totalWeight);
    }


    /**
     * Starting from the node provided, adds all unvisited nodes to an openNode
     * queue. Pops the smallest node distance off the queue and calls a check to all
     * its neighbors. Once a node gets popped from the queue we add it to a
     * closedNodes list.
     * 
     * @param node
     */
    private static void buildPath(Node startNode)
    {
        // Add our starting node to the queue to kick things off. Bottom left.
        openNodes.add(startNode);

        // While we have a non-accessed end (this is wrong but a start) continue.
        while (!openNodes.isEmpty())
        {
            Node finishedNode = openNodes.poll();
            checkNeighbors(finishedNode);
            closedNodes.add(finishedNode);
        }

    }
    

    /**
     * Gets the neighbor node, calls either oddNeighbors or evenDepending. Call this
     * so we don't have to do the odd/even check elsewhere.
     * 
     * @param node
     * @return
     */
    private static void checkNeighbors(Node node)
    {
        if (node.getColumn() % 2 == 0)
        {
            getEvenNeighbors(node);
        } else
        {
            getOddNeighbors(node);
        }
    }


    /**
     * Calls the nodeCheck method on all neighbors of odd indexed nodes based around
     * the hexagonal layout of the board.
     * 
     * @param node
     * @return
     */
    private static void getOddNeighbors(Node node)
    {
        checkNode(getIndex(node.getRow() - 1, node.getColumn()), node);
        checkNode(getIndex(node.getRow() - 1, node.getColumn() - 1), node);
        checkNode(getIndex(node.getRow() - 1, node.getColumn() + 1), node);
        checkNode(getIndex(node.getRow(), node.getColumn() + 1), node);
        checkNode(getIndex(node.getRow(), node.getColumn() - 1), node);
        checkNode(getIndex(node.getRow() + 1, node.getColumn()), node);
    }


    /**
     * Calls the nodeCheck method on all neighbors of even indexed nodes based
     * around the hexagonal layout of the board.
     * 
     * @param node
     * @return
     */
    private static void getEvenNeighbors(Node node)
    {
        checkNode(getIndex(node.getRow() - 1, node.getColumn()), node);
        checkNode(getIndex(node.getRow(), node.getColumn() + 1), node);
        checkNode(getIndex(node.getRow(), node.getColumn() - 1), node);
        checkNode(getIndex(node.getRow() + 1, node.getColumn()), node);
        checkNode(getIndex(node.getRow() + 1, node.getColumn() - 1), node);
        checkNode(getIndex(node.getRow() + 1, node.getColumn() + 1), node);
    }


    /**
     * Checks the given node against the previous node and either sets a new smaller
     * cost to node if the node has already been visited, or if the node hasn't been
     * visited sets the now known cost to the node, marks it as visited, and adds it
     * to the open nodes queue.
     * 
     * @param node
     * @param previous
     */
    private static void checkNode(Node node, Node previous)
    {
        // Insure we don't for any reason add null nodes.
        if (node == null)
            return;
        // Don't add nodes we cannot access
        if (node.getWeight() == -1)
            return;

        // Get the new distance to the node based around the previous.
        int newDistance = node.getWeight() + previous.getCostToNode();
        // if the node has been visited but we need to set a new lower distance cost
        // value
        if (node.isVisited() && node.getCostToNode() > newDistance)
        {
            node.setCostToNode(newDistance);
            node.setPreviousNode(previous);
            return;
        }
        // if it hasn't been visited set parameters and add to the queue.
        if (!node.isVisited())
        {
            node.setCostToNode(newDistance);
            node.setPreviousNode(previous);
            node.setVisited(true);
            openNodes.add(node);
            return;
        }

    }

    // Gets index from the board while making sure it is in bounds.
    private static Node getIndex(int row, int column)
    {
        if (row >= ROW_SIZE || column >= COLUMN_SIZE)
            return null;

        try {
            return board[row][column];
        } catch(Exception e)
        {
            return null;
        }
    }

}

