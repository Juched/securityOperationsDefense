package com.sod.securityoperationsdefense;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.Editable;
import android.util.Log;
import android.widget.ArrayAdapter;


import androidx.lifecycle.MutableLiveData;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class Game implements Serializable {
    private MutableLiveData<ArrayList<Double>> currentFunds;
    private MutableLiveData<Integer> payDelay;
    private MutableLiveData<Double> payRate;
    private SharedPreferences sharedPreferences;



    public Game(Context c) {
        sharedPreferences = c.getSharedPreferences("SOD.Gamefile", Context.MODE_PRIVATE);
        // check here if we already have a saved game state open
        ArrayList<Double> currFunds;
        Double payR;
        int payD;
        try {
            currFunds = (ArrayList<Double>) ObjectSerializer.deserialize(
                    sharedPreferences.getString("currFunds", ObjectSerializer.serialize(new ArrayList<Double>())));
            if(currFunds.size() < 1){
                currFunds.add(0.0);
            }
            payR = (Double) ObjectSerializer.deserialize(
                    sharedPreferences.getString("payR", ObjectSerializer.serialize(new Double(0))));
            payD = (Integer) ObjectSerializer.deserialize(
                    sharedPreferences.getString("payD", ObjectSerializer.serialize(new Integer(0))));
        } catch (IOException | ClassNotFoundException e) {
            //no game exists
            currFunds = new ArrayList<Double>(1);
            currFunds.set(0,0.0);
            payR = 10.0;
            payD = 1;

        }
        this.currentFunds = new MutableLiveData<ArrayList<Double>>(currFunds);
        this.currentFunds.setValue(currFunds);
        this.payRate = new MutableLiveData<Double>(payR);
        this.payDelay = new MutableLiveData<Integer>(payD); // one second

    }


    public MutableLiveData<ArrayList<Double>> getCurrentFunds() {
        return this.currentFunds;
    }
    public MutableLiveData<Double> getPayRate() {
        return this.payRate;
    }
    public MutableLiveData<Integer> getPayDelay() {
        return this.payDelay;
    }

    public void saveAll(){
        try{
            sharedPreferences.edit().putString("currFunds",
                    ObjectSerializer.serialize(currentFunds.getValue())).apply();
            sharedPreferences.edit().putString("payR", ObjectSerializer.serialize(payRate.getValue())).apply();
            sharedPreferences.edit().putString("payD", ObjectSerializer.serialize(payDelay.getValue())).apply();

            sharedPreferences.edit().commit();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("SOD", "Couldn't create a file");
        }
    }
}
