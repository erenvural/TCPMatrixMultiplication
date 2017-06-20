package com.linovi.matrixproject.controller;

import com.linovi.matrixproject.model.Matrix;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by eren on 12.06.2017.
 */
public class MatrixOperations {

    /*Method to concatenating partial matrices from all PCs than return to as a finalMatrix. It using when clients send
    partial matrices to server*/
    public static Matrix concatenateMatrices(Matrix[] matrixArray){
        int counter = matrixArray[0].getRowNumber();
        int rowOrder = 0;
        Matrix finalMatrix = new Matrix(counter*4, matrixArray[0].getColumnNumber());
        for (Matrix matrix : matrixArray){
            for (int i = 0; i < counter; i++){
                for (int j = 0; j < finalMatrix.getColumnNumber(); j++){
                    finalMatrix.values[rowOrder+i][j] = matrix.values[i][j];
                }
            }
            rowOrder = rowOrder+counter;
        }
      return finalMatrix;
    }

    /*Method to divide given matrix and return as new matrix according to row numbers and pc order. It using when
     send partial matrices to client socket from Test Class*/
    public Matrix divideMatrix(Matrix matrix, int counter, int pcOrder){
        Matrix tempMatrix = new Matrix(counter, matrix.getColumnNumber());
        int rowOrder = 0;
        switch(pcOrder){
            case 1:
                rowOrder = 0;
                break;
            case 2:
                rowOrder = counter;
                break;
            case 3:
                rowOrder = counter*2;
                break;
            case 4:
                rowOrder = counter*3;
                break;
        }

        for (int i = 0; i < counter; i++){
            for (int j = 0; j < tempMatrix.getColumnNumber(); j++){
                tempMatrix.values[i][j] = matrix.values[rowOrder+i][j];
            }
        }
        return tempMatrix;
    }

    //Method to generate matrix from given two dimensional array. It using for create matrix received arrays from client in server
    public static Matrix createMatrixFromArray(double[][] array){
        Matrix resultMatrix = new Matrix(array.length,array[0].length);
        for (int i = 0; i < resultMatrix.getRowNumber(); i++){
            for (int j = 0; j < resultMatrix.getColumnNumber(); j++){
                resultMatrix.values[i][j] = array[i][j];
            }
        }
        return resultMatrix;
    }

    //Method to return a regular multiplication result matrix
    public static Matrix regularMatrixMultiplication(Matrix matrix1, Matrix matrix2) {

        int aRow = matrix1.getRowNumber();
        int aColumn = matrix1.getColumnNumber();
        int bRow = matrix2.getRowNumber();
        int bColumn = matrix2.getColumnNumber();
        if (aColumn != bRow) {
            throw new IllegalArgumentException("ERROR\n Column Number of First Matrix is must be equal to Row Number of Second Matrix!");
        }else{
            Matrix resultMatrix = new Matrix(aRow,bColumn);
            for (int i = 0; i < aRow; i++) {
                for (int j = 0; j < bColumn; j++) {
                    for (int k = 0; k < aColumn; k++) {
                        resultMatrix.values[i][j] += matrix1.values[i][k] * matrix2.values[k][j];
                    }
                }
            }
            return resultMatrix;
        }
    }

    //Method to give output of a Matrix
    public static void writeMatrix(Matrix matrix){
        for( double[] value : matrix.values ) {
            System.out.println(Arrays.toString(value));
        }
    }

    //Helper (take row, print columns etc.) methods that can be used on demand.
    public static int[] getRow(Matrix matrix, int rowOrder){
        int columnNumber = matrix.getColumnNumber();
        int [] resultRow = new int[columnNumber];

        for (int i = 0 ; i <= columnNumber-1 ; i++) {
            int row = (int) matrix.values[rowOrder-1][i];
            resultRow[i] = row;
        }
        return resultRow;
    }

    public static int[] getColumn(Matrix matrix, int columnOrder){
        int rowNumber = matrix.getRowNumber();
        int [] resultColumn = new int[rowNumber];

        for (int i = 0 ; i <= rowNumber-1 ; i++) {
                int col = (int) matrix.values[i][columnOrder-1];
                resultColumn[i] = col;
        }
        return resultColumn;
    }

    public static void writeAllColumns(Matrix matrix){
        System.out.println("COLUMNS");
        int rowNumber = matrix.getRowNumber();
        int columnNumber = matrix.values[0].length;
        for (int i = 0 ; i <= rowNumber ; i++) {
            for (int j = 0 ; j < columnNumber-1; j++) {
                int col = (int) matrix.values[j][i];
                System.out.println(col);
            }
            System.out.println("-----");
        }
    }

    public static void writeAllRows (Matrix matrix){
        System.out.println("ROWS");
        int rowNumber = matrix.getRowNumber();
        int columnNumber = matrix.values[0].length;
        for (int i = 0 ; i < rowNumber ; i++) {
            for (int j = 0 ; j < columnNumber; j++) {
                int row = (int) matrix.values[i][j];
                System.out.print(row + " ");
            }
            System.out.println("\n------");
        }
    }

    public static ArrayList<Double> takeAllValuesMatrix(Matrix matrix){
        System.out.println("ALL VALUES");
        ArrayList<Double> allValues = new ArrayList();
        for (int i = 0; i < matrix.getRowNumber(); i++){
            for (int j  =0; j < matrix.getColumnNumber() ;j++){
                allValues.add(matrix.values[i][j]);
            }
        }
        return allValues;
    }
}
