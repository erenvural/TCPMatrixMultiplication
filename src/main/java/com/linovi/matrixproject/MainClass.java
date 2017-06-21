package com.linovi.matrixproject; /**
 * Created by eren on 20.06.2017.
 */
import com.linovi.matrixproject.service.TcpService;
import com.linovi.matrixproject.controller.ComputerOperations;
import com.linovi.matrixproject.controller.MatrixOperations;
import com.linovi.matrixproject.model.Computer;
import com.linovi.matrixproject.model.Matrix;

import java.util.*;

import java.io.IOException;

/**
 * Created by eren on 12.06.2017.
 */

public class MainClass {

    private static MatrixOperations matrixOperations = new MatrixOperations();
    private static ComputerOperations computerOperations = new ComputerOperations();
    private static boolean matrixRules = true;

    public static void main(String[] args) {
        //Finding Available Computers from text file. Now there are 4 computers (their ip's:localhost)
        ArrayList<Computer> computers = computerOperations.findComputers();

        //Creating 2 Random Matrix
        Matrix matrix1 = Matrix.random(1000, 1000);
        Matrix matrix2 = Matrix.random(1000, 1000);
        //Matrix matrix1 = Matrix.random(16, 16); //Code line for create small size Matrices
        //Matrix matrix2 = Matrix.random(16, 16); //Code line for create small size Matrices
        System.out.println("Randomly generated First Matrix ("+matrix1.getRowNumber()+" X "+matrix1.getColumnNumber()+")");
        MatrixOperations.writeMatrix(matrix1);
        System.out.println("\nRandomly generated Second Matrix ("+matrix2.getRowNumber()+" X "+matrix2.getColumnNumber()+")");
        MatrixOperations.writeMatrix(matrix2);
        System.out.println("___________________________________");

        /*Settings of Matrix Rules: Let Matrix 1 = AxB , Matrix 2 = CxD ;
        Rule 1: B must be equal C (This rule is a mathematical necessity)
        Rule 2: A is must be 4 or power of 4 (Setted for workflow of program)*/
        if (matrix1.getColumnNumber() != matrix2.getRowNumber()){
            System.out.println("ERROR: Column number of first matrix and row number of second matrix must be equal !");
            matrixRules = false;
        }
        if ((matrix1.getRowNumber()) % 4 != 0 ){
            System.out.println("ERROR: Row number of first matrix is equal to power of 4 ! (For example: 8, 16, 100 etc.)");
            matrixRules = false;
        }
        //If matrix rules are not followed, program will be close
        while (!matrixRules){
            System.out.println("Program closing becasue of the matrix rules are not followed.");
            System.exit(0);
        }

        //Finding counter value for matrix divide operation
        int divideCounter = matrix1.getRowNumber() / computers.size();

        //Seperated Matrix are creating. These are will be use for tcp matrix multiplication
        Matrix tempMatrix1 = matrixOperations.divideMatrix(matrix1, divideCounter, 1);
        Matrix tempMatrix2 = matrixOperations.divideMatrix(matrix1, divideCounter, 2);
        Matrix tempMatrix3 = matrixOperations.divideMatrix(matrix1, divideCounter, 3);
        Matrix tempMatrix4 = matrixOperations.divideMatrix(matrix1, divideCounter, 4);


        //!! Activable code block can be used on demand !!
        /*//Finding regular multiplication matrix use to provide proof regular multiplication is same with tcp multiplication
        System.out.println("\nRegular Multiplication Result ("+matrix1.getRowNumber()+" X "+matrix2.getColumnNumber()+")");
        Matrix regularResultMatrix = MatrixOperations.regularMatrixMultiplication(matrix1, matrix2);
        MatrixOperations.writeMatrix(regularResultMatrix);
        System.out.println();*/

        //Send to partial matrix to different sockets for do divide, multiplication and join operations and finished final matrix result
        String hostName;
        int portNumber;
        for (int i = 0; i < computers.size(); i++){
            hostName = computers.get(i).getComputerIp();
            portNumber = computers.get(i).getComputerPortNumber();
            TcpService.startServer(portNumber);
            if (i == 0){
                TcpService.startSender(tempMatrix1, matrix2, hostName, portNumber, 1);
            }else if (i == 1){
                TcpService.startSender(tempMatrix2, matrix2, hostName, portNumber, 2);
            }else if (i == 2){
                TcpService.startSender(tempMatrix3, matrix2, hostName, portNumber, 3);
            }else if (i == 3){
                TcpService.startSender(tempMatrix4, matrix2, hostName, portNumber, 4);
            }
        }
    }
}