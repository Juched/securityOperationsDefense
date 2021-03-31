package com.sod.securityoperationsdefense;
import android.text.Editable;

import androidx.lifecycle.MutableLiveData;

import java.io.Serializable;

public class Game implements Serializable {
    private MutableLiveData<Double> currentFunds;
    private MutableLiveData<Integer> payDelay;
    private MutableLiveData<Double> payRate;




    public Game() {
        this.currentFunds = new MutableLiveData<Double>(0.0);
        this.payRate = new MutableLiveData<Double>(25.0);
        this.payDelay = new MutableLiveData<Integer>(1); // one second

    }

    public void setCurrentFunds(double currentFunds) {
        this.currentFunds.setValue(currentFunds);

    }

    public MutableLiveData<Double> getCurrentFunds() {
        return this.currentFunds;
    }
}
