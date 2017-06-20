package com.linovi.matrixproject.controller;

import com.linovi.matrixproject.model.Computer;
import com.linovi.matrixproject.model.Matrix;

import java.io.*;
import java.util.*;

/**
 * Created by eren on 12.06.2017.
 */
public class ComputerOperations {

    //Computer Arraylist for using when taking computer properties from reading hosts text file
    public static ArrayList<Computer> computerList = new ArrayList();

    //Method to taking computer properties from reading hosts text file and return them as Computer ArrayList
    public ArrayList<Computer> findComputers() throws IOException {
        File file = new File("src\\main\\resources\\hosts.txt");
        BufferedReader reader;
        reader = new BufferedReader(new FileReader(file));
        String line = reader.readLine();

        while (line!=null) {
            String[] parts = line.split(":");
            String computerIp = parts[0];
            int computerPort = Integer.parseInt(parts[1]);
            Computer computer = new Computer(computerIp,computerPort);
            addComputer(computer);
            line = reader.readLine();
        }
        return computerList;
    }

    //Method to export out matrix tcp multiplication result to txt file in our project's src path
    public static void exportMatrix (Matrix matrix){
        try{
            PrintWriter writer = new PrintWriter("src\\matrix-result.txt", "UTF-8");
            for( double[] value : matrix.values ) {
                writer.println(Arrays.toString(value));
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Method to read result matrix from text file then give output
    public static void importMatrix () throws IOException {
        System.out.println("Tcp Final Matrix");
        try (BufferedReader br = new BufferedReader(new FileReader("src\\matrix-result.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        }

    }

    public static void addComputer(Computer computer){
        computerList.add(computer);
    }

}
