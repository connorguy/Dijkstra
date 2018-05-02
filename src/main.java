import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * 
 */

/**
 * 560 Project Group 2
 * 
 * @author connorguy
 *
 */
public class main
{
    static Node[][] board;
    static List<Node> path;
    final static int ROW_COUNT = 31;
    final static int COLUMN_COUNT = 15;
    final static int FINAL_INDEX = 8;
    PathOfBoard pathObj = new PathOfBoard();

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        // Array 31 rows, 15 columns
        board = new Node[ROW_COUNT][COLUMN_COUNT];
        path = new ArrayList<Node>();

        // int[] input = getFileInput();
        // For testing
        Random rand = new Random();
        int[] input = new int[233];
        for (int i = 0; i < 233; i++)
        {
            if (i % (rand.nextInt(9) + 1) == 0)
                input[i] = -1;
            else
                input[i] = rand.nextInt(50) + 1;
        }
        fillHexArray(input);

        // For Testing the fill of the board
        testCase();

    }

    private static void testCase()
    {
        // For Testing the fill of the board
        int total = 0;
        for (int x = 0; x < ROW_COUNT; x++)
        {
            for (int y = 0; y < COLUMN_COUNT; y++)
            {
                if (board[x][y] == null)
                    System.out.print(" + ");
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

    // Fills hex array with the input from the file that was parsed.
    private static void fillHexArray(int[] input)
    {
        int index = 0;
        int row = 0;
        for (int i = 0; i < input.length; i++)
        {
            board[row][index] = new Node(row, index, input[i], i);
            index = index + 2; // skip an index everytime

            // If we are at the end of the row then we need to reset row index and increment
            // the row.
            if (index >= 15)
            {
                row++;
                // Odd rows index will start at 1.
                if (row % 2 == 0)
                    index = 0;
                else
                    index = 1;
            }
        }
    }

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
            if (n.getDirectionalWeight() < min.getDirectionalWeight() || min == null && !previousNode.isEqual(n))
                min = n;
        }
        // Add the smallest path node
        path.add(min);

        // Recursively call till you find the top right node
        buildPath(min);
    }

    // Check all the sides of the hex and pick the route with the smallest weight.
    private static void checkSides(int row, int column)
    {
        int numberOfSides = 6;
        Node[] listPaths = new Node[numberOfSides];
        // Going clockwise
        listPaths[0] = getIndex(row + 2, column); // Up
        listPaths[1] = getIndex(row + 1, column + 2); // Up Right
        listPaths[2] = getIndex(row - 1, column + 2); // Down Right
        listPaths[3] = getIndex(row - 2, column); // Down
        listPaths[4] = getIndex(row - 1, column - 2); // Down Left
        listPaths[5] = getIndex(row + 1, column - 2); // Up Left

        int minNumber = 0;
        int minIndex = 0;
        for (int i = 0; i < numberOfSides; i++)
        {
            if (minNumber > listPaths[i].getWeight() && listPaths[i].getWeight() != (-1))
            {
                minNumber = listPaths[i].getWeight();
                minIndex = i;
            }
        }

        // Do a recursive call
    }

    // Gets index from the board while making sure it is in bounds.
    private static Node getIndex(int row, int column)
    {
        if (row > ROW_COUNT)
            return null;
        if (column > COLUMN_COUNT)
            return null;
        return board[row][column];
    }

}
