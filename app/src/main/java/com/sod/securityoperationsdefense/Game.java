package com.sod.securityoperationsdefense;
import android.text.Editable;

import java.io.Serializable;

public class Game implements Serializable {
    public double currentFunds;
    public double payRate;
    public int payDelay;
    public String test;

    public Game() {
        this.currentFunds = 1000.0;
        this.payRate = 25.0;
        this.payDelay = 1; // one second
        this.test = "oof";
    }
    public void setTest(String text){
        test = text;
    }

}
