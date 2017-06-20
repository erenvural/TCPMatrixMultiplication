import com.linovi.matrixproject.controller.ComputerOperations;
import com.linovi.matrixproject.controller.MatrixOperations;
import com.linovi.matrixproject.model.Computer;
import com.linovi.matrixproject.model.Matrix;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.linovi.matrixproject.service.TcpService;

import java.util.ArrayList;

/**
 * Created by eren on 20.06.2017.
 */
public class MainClassTest {

    private MatrixOperations matrixOperations = new MatrixOperations();
    private ComputerOperations computerOperations = new ComputerOperations();

    @Test(enabled = true)
    public void testMatriceProperties() throws Exception {

        Matrix matrix1 = Matrix.random(1000, 1000);
        Matrix matrix2 = Matrix.random(1000, 1000);
        int matrix1Row = matrix1.getRowNumber();
        int matrix1Column = matrix1.getColumnNumber();
        int matrix2Row = matrix2.getRowNumber();
        int matrix2Column = matrix2.getColumnNumber();

        Assert.assertTrue(matrix1Row > 0);    //--------------------------------------------------
        Assert.assertTrue(matrix1Column > 0); //  Tests: Are matrix dimensions greater than 0 ?
        Assert.assertTrue(matrix2Row > 0);    //
        Assert.assertTrue(matrix2Column > 0); // --------------------------------------------------

        Assert.assertEquals(matrix1Column,matrix2Row); //Test: Is column number of first matrix and row number of second matrix equal?

        Assert.assertTrue(matrix1Row % 4 == 0); //Test: Is row number of first matrix power of 4 ?
    }

    @Test(enabled = true)
    public void testRegularMatrixMultiplication() throws Exception {

        Matrix matrix1 = Matrix.random(1000, 1000);
        Matrix matrix2 = Matrix.random(1000, 1000);
        Matrix regularResultMatrix = MatrixOperations.regularMatrixMultiplication(matrix1, matrix2);
        Assert.assertNotNull(regularResultMatrix);
        //Test: Is row number of multiplication result matrix equal to row number of first matrix ?
        Assert.assertEquals(matrix1.getRowNumber(), regularResultMatrix.getRowNumber());
        //Test: Is column number of multiplication result matrix equal to column number of second matrix ?
        Assert.assertEquals(matrix2.getColumnNumber(), regularResultMatrix.getColumnNumber());

        int [] matrix1firstArray = MatrixOperations.getRow(matrix1,1);
        int [] matrix2firstColumn = MatrixOperations.getColumn(matrix2,1);
        double firstRowAndColumnMultiplication = 0;
        for (int i = 0; i < matrix1.getRowNumber();i++){
            firstRowAndColumnMultiplication += matrix1firstArray[i] * matrix2firstColumn[i];
        }
        Assert.assertFalse(firstRowAndColumnMultiplication == 0); //Test: Could integer value be changed for the use next stage control?
        //Test: Are values of multiplication result are correct ? Use for [0][0] dimension !
        Assert.assertEquals(firstRowAndColumnMultiplication, regularResultMatrix.values[0][0]);
    }

    @Test(enabled = true, timeOut = 100000)
    public void testMatricesTcpMultiplication() throws Exception {

        ArrayList<Computer> computers = computerOperations.findComputers();
        Assert.assertNotNull(computers); //Test: Could it take computer from hosts text file ?
        Assert.assertEquals(computers.size(), 4); //Test: Are there 4 computers ?

        Matrix matrix1 = Matrix.random(1000, 1000);
        Matrix matrix2 = Matrix.random(1000, 1000);
        int divideCounter = matrix1.getRowNumber() / 4;
        Matrix tempMatrix1 = matrixOperations.divideMatrix(matrix1, divideCounter, 1);
        Matrix tempMatrix2 = matrixOperations.divideMatrix(matrix1, divideCounter, 2);
        Matrix tempMatrix3 = matrixOperations.divideMatrix(matrix1, divideCounter, 3);
        Matrix tempMatrix4 = matrixOperations.divideMatrix(matrix1, divideCounter, 4);

        Assert.assertTrue(tempMatrix1.getRowNumber() == tempMatrix4.getRowNumber()); //Test: Is matrix properly divided?

        String hostName;
        int portNumber;
        for (int i = 0; i < computers.size(); i++){
            hostName = computers.get(i).getComputerIp();
            portNumber = computers.get(i).getComputerPortNumber();
            Assert.assertNotNull(hostName); //Test: Can get hostname properly
            Assert.assertNotNull(portNumber); //Test: Can get portname properly
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