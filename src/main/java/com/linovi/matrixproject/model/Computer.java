package com.linovi.matrixproject.model;

/**
 * Created by eren on 12.06.2017.
 */
public class Computer {

    private String computerIp = "";
    private int computerPortNumber=0;

    //General Constructor
    public Computer(String computerIp, int computerPortNumber) {
        this.computerIp = computerIp;
        this.computerPortNumber = computerPortNumber;
    }

    //Getters and Setters
    public String getComputerIp() {
        return computerIp;
    }

    public void setComputerIp(String computerIp) {
        this.computerIp = computerIp;
    }

    public int getComputerPortNumber() {
        return computerPortNumber;
    }

    public void setComputerPortNumber(int computerPortNumber) {
        this.computerPortNumber = computerPortNumber;
    }
}
