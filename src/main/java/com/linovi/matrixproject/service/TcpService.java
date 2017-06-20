package com.linovi.matrixproject.service;

/**
 * Created by eren on 16.06.2017.
 */

import com.linovi.matrixproject.controller.ComputerOperations;
import com.linovi.matrixproject.controller.MatrixOperations;
import com.linovi.matrixproject.model.Matrix;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;

public class TcpService implements Serializable{

    private static Matrix[] matrixArray = new Matrix[4];
    private static Matrix finalMatrix = null;

    //Method works as Client: Sending result multiplication of 'partial matrix of first matrix and second matrix' as Array to server
    public static void startSender(final Matrix matrix1, final Matrix matrix2, final String host, final int port, final int pcNumber) {
        (new Thread() {
            @Override
            public void run()  {
                try {

                    Socket s = new Socket(host, port);
                    ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
                    while (true) {
                        //Creating object list and filling values array of received matrices
                        ArrayList<Object> listOfObjects = new ArrayList<>();
                        listOfObjects.add(matrix1.values);
                        listOfObjects.add(matrix2.values);

                        //Add to list number which computer was used
                        listOfObjects.add(pcNumber);

                        /*Send to server our objects to server side in list: 2 values of matrix as arrays (1 partial matrix and 1 matrix)
                        and significative pcNumber(it will be need to sorted concatening all partial matrices as a final result matrix) */
                        out.writeObject(listOfObjects);
                        out.flush();
                        Thread.sleep(250);
                    }
                } catch (SocketException se) {
                    //ComputerOperations.importMatrix();
                    System.out.println("\nOperation comlete !\nThe sockets are no longer open! Program is closing... ");
                    System.exit(0);
                } catch (IOException | InterruptedException e) {
                    System.out.println("Error in on the Client Side\nException Founded: " + e.toString());
                }
            }
        }).start();
    }

    //Server Method: Take the objects(Arrays) from the client and join them in a final result matrix
    public static void startServer(final int port)  {
        (new Thread() {
            @Override
            public void run() {
                ServerSocket ss;
                try {
                    ss = new ServerSocket(port);
                    Socket s = ss.accept();

                    //Receiving data from client, then store objects in ArrayList
                    ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
                    ArrayList<Object> received = (ArrayList<Object>) ois.readObject();

                    //Take received matrix arrays and create temporary new matrices for matrix multiplication
                    double[][] receivedArray1 = (double[][]) received.get(0);
                    double[][] receivedArray2 = (double[][]) received.get(1);
                    Matrix matrix1 = MatrixOperations.createMatrixFromArray(receivedArray1);
                    Matrix matrix2 = MatrixOperations.createMatrixFromArray(receivedArray2);
                    //Take significative pcNumber and store
                    int receivedPcNumber = (int) received.get(2);

                    /*Muliplication previous matrices and occurs a new result matrix. There will be 4 different matrix in this
                    way(1 matrix per a pc*/
                    Matrix partialResultMatrix = MatrixOperations.regularMatrixMultiplication(matrix1,matrix2);
                    //These matrices store  in matrix array with their signiticative pc Number
                    matrixArray[receivedPcNumber-1]=partialResultMatrix;

                    //Create new matrix that filling after all split and join operations
                    //Join all partial matrices in finalMatrix. [IF BLOCK : To make sure all partial matrices are available]
                    if (matrixArray[0] != null && matrixArray[1] != null && matrixArray[2] != null && matrixArray[3] !=null){
                        finalMatrix = MatrixOperations.concatenateMatrices(matrixArray);
                    }

                    //Give output of final matrix as text file to project's src path and close Operations.
                    // [IF BLOCK : To make sure that the final matrix build process is over]
                    if (finalMatrix != null){
                        System.out.println("\nTCP Seperated Multiplication Result writed with as name matrix-result.txt in project 'src' path\n");
                        ComputerOperations.exportMatrix(finalMatrix);
                        ois.close();
                        s.close();
                        ss.close();
                    }

                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("Error in on the Server Side\nException Founded: " + e.toString());
                }
            }
        }).start();
    }
}