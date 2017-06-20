package com.linovi.matrixproject.model; /**
 * Created by eren on 12.06.2017.
 */

import java.util.Random;

public class Matrix {

    private static Random random = new Random();
    private int rowNumber = 0;
    private int columnNumber = 0;
    public double[][] values = new double[0][];

    //General Constructor to create matrix
    public Matrix(int rowNumber, int columnNumber) {
        this.rowNumber = rowNumber;
        this.columnNumber = columnNumber;
        values = new double[rowNumber][columnNumber];
    }

    //Constructor for create matrix that formed random numbers 0 to 5
    public static Matrix random(int rowNumber, int columnNumber) {

        if (rowNumber <= 0 || columnNumber <= 0){
            System.out.println("Application stop !\nRule: Row and Column numbers of Matrices must be bigger than 0");
            System.exit(0);
        }
        Matrix newMatrix = new Matrix(rowNumber, columnNumber);
        for (int i = 0; i < rowNumber; i++)
            for (int j = 0; j < columnNumber; j++)
                newMatrix.values[i][j] = random.nextInt(5);
        return newMatrix;
    }

    //Getters and Setters
    public int getRowNumber() {
        return rowNumber;
    }

    public int getColumnNumber() {
        return columnNumber;
    }

    public double[][] getValues() {
        return values;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public void setColumnNumber(int columnNumber) {
        this.columnNumber = columnNumber;
    }

    public void setValues(double[][] values) {
        this.values = values;
    }
}
