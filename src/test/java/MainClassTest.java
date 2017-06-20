import com.linovi.matrixproject.controller.ComputerOperations;
import com.linovi.matrixproject.controller.MatrixOperations;
import com.linovi.matrixproject.model.Computer;
import com.linovi.matrixproject.model.Matrix;
import org.testng.Assert;
import org.testng.annotations.*;
import com.linovi.matrixproject.service.TcpService;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by eren on 20.06.2017.
 */
public class MainClassTest {
    private MatrixOperations matrixOperations = new MatrixOperations();
    private ComputerOperations computerOperations = new ComputerOperations();

    /* TESTING SUMMARY: -Divided TCP matrix multiplication test ends about 16 times faster than regular matrix multiplication test !
    * -Regular Matrix Multiplication Test is now disable. If you want execute that test, change enabled = true in Line 48.
    * -Although, I don't recommend run regular and tcp tests one after another because of the errors that can be caused by taking a lot of time.
    * If you want run MainClassTest completely, disable one of them.
    * -There is a unactive test method named testPossibleErrors in line 109. If you want see a example reason of test failure, you can use that
    *test with make enable test and remove the comment marks of a reason code block.
    * P.S: When testPossibleErrors method is enable run only method test not class test.*/

    @Test(enabled = true, groups = "testMatriceProperties") //Test ends in about 0.5 seconds
    public void testMatriceProperties() throws Exception {
        System.out.println("@Test - testMatriceProperties");
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

    @Test(enabled = false , groups = "matrixMultiplication", dependsOnMethods = { "testMatriceProperties" } ,
            expectedExceptions = IndexOutOfBoundsException.class) //Test ends in about 12 seconds
    public void testRegularMatrixMultiplication() throws Exception {
        System.out.println("@Test - testRegularMatrixMultiplication");
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

    @Test(enabled = true , groups = "matrixMultiplication" , dependsOnMethods = { "testMatriceProperties" }) //Test ends in about 0.75 seconds
    public void testMatricesTcpMultiplication() throws Exception {
        System.out.println("@Test - testMatricesTcpMultiplication");
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

    @Test(enabled = false, groups = "possibleErrors") //There are 2 reason for take error. Delete only 1 comment line for works correctly.
    public void testPossibleErrors() throws Exception {
        System.out.println("@Test - testPossibleErrors");

        //1) Rule breaking: Row and Column numbers of Matrices must be bigger than 0. The program closes.
        //Matrix testMatrix1 = Matrix.random(-5, 1000);

        //2) Rule Breaking: Column Number of First Matrix is must be equal to Row Number of Second Matrix. Throws IllegalArgumentException.
        /*Matrix matrix1 = Matrix.random(16, 16);
        Matrix matrix2 = Matrix.random(15, 16);
        Matrix regularResultMatrix = MatrixOperations.regularMatrixMultiplication(matrix1, matrix2);*/
    }

   //Configuration Testing Methods
   @BeforeClass
   public void beforeClass() {
       System.out.println("@BeforeClass\n");
   }

    @AfterClass
    public void afterClass() {
        System.out.println("@AfterClass");
    }

    @BeforeMethod
    public void beforeMethod() {
        System.out.println("@BeforeMethod");
    }

    @AfterMethod
    public void afterMethod() {
        System.out.println("@AfterMethod");
    }

    @BeforeGroups("matrixMultiplication")
    public void beforeMatriceProperty() {
        System.out.println("\n@BeforeMatrixMultiplication");
    }

    @AfterGroups("matrixMultiplication")
    public void afterMatriceProperty() {
        System.out.println("@AfterMatrixMultiplication\n");
    }

}