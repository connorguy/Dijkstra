import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * 560 Project Group 2
 * 
 * Implementation of A* on a hexoginal board.
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

        // int[] input = getFileInput();
        // For testing
        Random rand = new Random();
        int[] input = new int[233];
        for (int i = 0; i < 233; i++)
        {
            // if (i % (rand.nextInt(9) + 1) == 0)
            // input[i] = -1;
            // else
                input[i] = rand.nextInt(50) + 1;
        }
        fillHexArray(input);

        // For Testing the fill of the board
        testCase();
        
        // Starting node is in the bottom left of the board
        Node startingNode = board[15][0];
        // Add the start to the list and then build path.
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
                board[rowIndex][columnIndex] = new Node(rowIndex, columnIndex, input[i], i + 1);
                columnIndex = columnIndex + 2;

            } else
            {
                // fill even spaces
                board[rowIndex][columnIndex] = new Node(rowIndex, columnIndex, input[i], i + 1);
                columnIndex = columnIndex + 2;
            }
            
        }
    }

    /*
     * Recursive method that checks a nodes neighbors and chooses the best next
     * node.
     */
    private static void buildPath(Node node)
    {
        // Check if we have reached the destination node (node index[8])
        if (node.getIndex() == FINAL_INDEX)
            return;

        int numberOfSides = 6;
        Node[] listPaths = new Node[numberOfSides];
        // Get neighbor nodes going clockwise
        listPaths[0] = getIndex(node.getRow() + 2, node.getColumn()); // Up
        listPaths[1] = getIndex(node.getRow() + 1, node.getColumn() + 2); // Up Right
        listPaths[2] = getIndex(node.getRow() - 1, node.getColumn() + 2); // Down Right
        listPaths[3] = getIndex(node.getRow() - 2, node.getColumn()); // Down
        listPaths[4] = getIndex(node.getRow() - 1, node.getColumn() - 2); // Down Left
        listPaths[5] = getIndex(node.getRow() + 1, node.getColumn() - 2); // Up Left

        // Check which neighbor node has the lowest cost.
        // Exclude previous node.
        Node min = null;
        Node previousNode = path.get(path.size() - 1);
        for (Node n : listPaths)
        {

            // If the nodes directionalWeight is smaller than the current min AND it isn't
            // the node we just went to.
            if (n != null)
            {
                if (min == null)
                    min = n;

                if (n.getAvgWeight() < min.getAvgWeight() && min.getAvgWeight() != (-1)
                        && !previousNode.isEqual(n))
                    min = n;
            }
        }
        // Add the smallest path node
        path.add(min);

        System.out.println(min.toString());

        // Recursively call till you find the top right node
        buildPath(min);
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
