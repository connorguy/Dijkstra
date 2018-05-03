import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
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
    static List<Node> path;
    final static int ROW_SIZE = 16;
    final static int COLUMN_SIZE = 15;
    final static int FINAL_INDEX = 8;

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        // Array 31 rows, 15 columns
        board = new Node[ROW_SIZE][COLUMN_SIZE];
        path = new ArrayList<Node>();

        // int[] input = getFileInput(); REMOVE COMMENTS FOR REAL FILE DATA

        // ----------------(For testing)----------------
        Random rand = new Random();
        int[] input = new int[233];
        for (int i = 0; i < 233; i++) // fill array with random data for tests
        {
            input[i] = rand.nextInt(50) + 1;
            // if (i % 3 == 0)
            // input[i] = -1;
        }
        fillHexArray(input);

        // For Testing the fill of the board
        testCase();
        // ----------------(For testing)----------------
        
        // Starting node is in the bottom left of the board
        // Node startingNode = board[15][0];
        // // Add the start to the list and then build path.
        // path.add(startingNode);
        // buildPath(startingNode);


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
                    System.out.print(" + ");
                else
                {
                    System.out.print(board[x][y].getIndex() + " ");
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
            scanner = new Scanner(new File("test.txt"));
        } catch (FileNotFoundException e)
        {
            // Bad Bad Not Good - great band
            e.printStackTrace();
        }

        int[] input = new int[233];
        int i = 0;
        while (scanner.hasNextInt() || i == 232)
        {
            // The text is in the format <index weight>
            // So need to skip every other input
            if (i % 2 == 0)
            {
                continue;
            }
            input[i++] = scanner.nextInt();
        }
        return input;
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
     * Recursive method that checks a nodes neighbors and chooses the best next
     * node.
     * 
     * @param node
     */
    private static void buildPath(Node startNode)
    {
        // Create a priority que for open node list
        Comparator<Node> nodeCompare = new NodeComparator();
        PriorityQueue<Node> openNodes = new PriorityQueue<Node>(20, nodeCompare);

        // Check if we have reached the destination node (node index[8])
        if (startNode.getIndex() == FINAL_INDEX)
            return;

    }

    /**
     * Creates a node array of all neighbors of odd indexed nodes based around the
     * hexagonal layout of the board.
     * 
     * @param node
     * @return
     */
    private Node[] getOddNeighbors(Node node)
    {
        final int numberOfNeighbors = 6;
        Node[] neighbors = new Node[numberOfNeighbors];
        neighbors[0] = board[node.getRow() - 1][node.getColumn()];
        neighbors[1] = board[node.getRow() - 1][node.getColumn() - 1];
        neighbors[2] = board[node.getRow() - 1][node.getColumn() + 1];
        neighbors[3] = board[node.getRow()][node.getColumn() + 1];
        neighbors[4] = board[node.getRow()][node.getColumn() - 1];
        neighbors[5] = board[node.getRow() + 1][node.getColumn()];
        return neighbors;
    }

    /**
     * Creates a node array of all neighbors of even indexed nodes based around the
     * hexagonal layout of the board.
     * 
     * @param node
     * @return
     */
    private Node[] getEvenNeighbors(Node node)
    {
        final int numberOfNeighbors = 6;
        Node[] neighbors = new Node[numberOfNeighbors];
        neighbors[0] = board[node.getRow() - 1][node.getColumn()];
        neighbors[1] = board[node.getRow()][node.getColumn() + 1];
        neighbors[2] = board[node.getRow()][node.getColumn() - 1];
        neighbors[3] = board[node.getRow() + 1][node.getColumn()];
        neighbors[4] = board[node.getRow() + 1][node.getColumn() - 1];
        neighbors[5] = board[node.getRow() + 1][node.getColumn() + 1];
        return neighbors;
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

