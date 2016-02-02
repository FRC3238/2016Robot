package org.usfirst.frc.team3238.robot;

import java.io.File;
import java.util.Scanner;

/**
 * Write a description of class ConstantInterpreter here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class ConstantInterpreter
{
    public Scanner kConstantReader;
    public String[][] constantData;
    public int globalArraySize;
    static String fileName;
    public File in;

    public ConstantInterpreter(String kConstantFileName)
            throws java.io.FileNotFoundException
    {
        fileName = kConstantFileName;
        in = new File(kConstantFileName);
        kConstantReader = new Scanner(in);
        initializeArray();
    }

    public void initializeArray() throws java.io.FileNotFoundException
    {
        int iteratorSum = 0;
        Scanner iterator = new Scanner(in);
        while(iterator.hasNextLine())
        {
            iteratorSum++;
            iterator.nextLine();
        }
        iterator.close();
        constantData = new String[iteratorSum][2];
        globalArraySize = iteratorSum;
        iteratorSum = 0;
        while(kConstantReader.hasNextLine())
        {
            constantData[iteratorSum / 2][(iteratorSum + 2) % 2] = kConstantReader
                    .nextLine();
            System.out.println("Added to " + iteratorSum + ", "
                    + (iteratorSum + 2) % 2 + " with "
                    + constantData[iteratorSum][(iteratorSum + 2) % 2]);
            iteratorSum++;
        }
    }

    public int retrieveInt(String retriever)
    {
        for(int i = 0; i < globalArraySize / 2; i++)
        {
            if(constantData[i][0].equals(retriever))
            {
                return Integer.parseInt(constantData[i][1]);
            }
        }
        return -1;
    }

    public double retrieveDouble(String retriever)
            throws java.text.ParseException
    {
        for(int i = 0; i < globalArraySize / 2; i++)
        {
            if(constantData[i][0].equals(retriever))
            {
                return Double.parseDouble(constantData[i][1]);
            }
        }
        return -1.0;
    }

    public float retrieveFloat(String retriever)
    {
        for(int i = 0; i < globalArraySize / 2; i++)
        {
            if(constantData[i][0].equals(retriever))
            {
                return Float.parseFloat(constantData[i][1]);
            }
        }
        return -1.0f;
    }

    public String retrieveString(String retriever)
    {
        for(int i = 0; i < globalArraySize / 2; i++)
        {
            if(constantData[i][0].equals(retriever))
            {
                return constantData[i][1];
            }
        }
        return "null";
    }
}
