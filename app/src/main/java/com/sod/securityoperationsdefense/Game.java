package com.sod.securityoperationsdefense;

public class Game {
    public double currentFunds;
    public double payRate;
    public int payDelay;

    public Game() {
        this.currentFunds = 1000.0;
        this.payRate = 25.0;
        this.payDelay = 1; // one second
    }
}
