
import java.util.Scanner;
import java.io.File;
import java.text.NumberFormat;

/**
 * Write a description of class ConstantInterpreter here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class constantInterpreter
{
    public static final File kConstant = new File("kConstants.txt");
    public static Scanner kConstantReader;
    public static String[][] constantData;
    public static int globalArraySize;

    public constantInterpreter() throws java.io.FileNotFoundException
    {
        kConstantReader = new Scanner(kConstant);
        initializeArray();
    }

    public static void initializeArray() throws java.io.FileNotFoundException
    {
        int iteratorSum = 0;
        Scanner iterator = new Scanner(kConstant);
        while(iterator.hasNextLine())
        {
            iteratorSum++;
            iterator.nextLine();
        }
        constantData = new String[iteratorSum][2];
        globalArraySize = iteratorSum;
        iteratorSum = 0;
        while(kConstantReader.hasNextLine())
        {
            constantData[iteratorSum / 2][(iteratorSum + 2) % 2] = kConstantReader.nextLine();
            System.out.println("Added to " + iteratorSum + ", " + (iteratorSum + 2) % 2 + " with " + constantData[iteratorSum][(iteratorSum + 2) % 2]);
            iteratorSum++;
        }
    }

    public static int retrieveInt(String retriever)
    {
        for(int i = 0; i < globalArraySize; i++)
        {
            if(constantData[i][0].equals(retriever))
            {
                return Integer.parseInt(constantData[i][1]);
            }
        }
        return -1;
    }

    public static double retrieveDouble(String retriever) throws java.text.ParseException
    {
        for(int i = 0; i < globalArraySize; i++)
        {
            if(constantData[i][0].equals(retriever))
            {
                return Double.parseDouble(constantData[i][1]);
            }
        }
        return -1.0;
    }

    public static float retrieveFloat(String retriever)
    {
        for(int i = 0; i < globalArraySize; i++)
        {
            if(constantData[i][0].equals(retriever))
            {
                return Float.parseFloat(constantData[i][1]);
            }
        }
        return -1.0f;
    }

    public static String retrieveString(String retriever)
    {
        for(int i = 0; i < globalArraySize; i++)
        {
            if(constantData[i][0].equals(retriever))
            {
                return constantData[i][1];
            }
        }
        return "null";
    }
}
