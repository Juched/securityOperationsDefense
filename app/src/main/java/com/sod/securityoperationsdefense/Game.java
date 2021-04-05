package com.sod.securityoperationsdefense;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.text.Editable;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TableLayout;


import androidx.cardview.widget.CardView;
import androidx.lifecycle.MutableLiveData;

import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class Game {
    private MutableLiveData<ArrayList<Double>> currentFunds;
    private MutableLiveData<Integer> payDelay;
    private MutableLiveData<Double> payRate;
    private SharedPreferences sharedPreferences;
    private MutableLiveData<Integer> day;
    private GameActivity game;

    // Serializable lists of upgrades
    private MutableLiveData<ArrayList<CardView>>  busUpgrades;
    private MutableLiveData<ArrayList<CardView>>  critInfoUpgrades;
    private MutableLiveData<ArrayList<CardView>>  infoStateUpgrades;
    private MutableLiveData<ArrayList<CardView>>  secUpgrades;

    public MutableLiveData<Double> attackRate; // starts at 0.01

    private int noOfAttacks;
    // contains att. info. key is integer (0-5), and list contains attack title, desc, and cost
    private HashMap<Integer, ArrayList<String>> attackList;
    // contains att. prevention rates. values of the keys correspond to the attack in the above list
    public MutableLiveData<HashMap<Integer, Double>> preventionRate;



    public Game(Context c, GameActivity game) {
        this.game = game;
        sharedPreferences = c.getSharedPreferences("SOD.Gamefile", Context.MODE_PRIVATE);
        // check here if we already have a saved game state open; default game context
        ArrayList<Double> currFunds;
        Double payR;
        int payD;
        int tDay;

        // upgrades lists
        HashMap<String, ArrayList<CardView>> upgrades; // does not need saved

        double attackR; // needs to be saved
        HashMap<Integer, Double> preventionRs; // needs to be saved

        // create and populate the attack map
        HashMap<Integer,ArrayList<String>> attacks = new HashMap<Integer,ArrayList<String>>(); // does not need saved
        ArrayList<String> info = new ArrayList<String>();
        info.add("Phishing");
        info.add("Insert phishing def here");
        info.add("15.0");
        attacks.put(0, info);

        info = new ArrayList<String>();
        info.add("Brute-force");
        info.add("Insert brute-force def here");
        info.add("15.0");
        attacks.put(1, info);

        info = new ArrayList<String>();
        info.add("DDoS");
        info.add("Insert DDoS def here");
        info.add("15.0");
        attacks.put(2, info);

        info = new ArrayList<String>();
        info.add("Insider");
        info.add("Insert insider attack def here");
        info.add("15.0");
        attacks.put(3, info);

        info = new ArrayList<String>();
        info.add("Ransomware");
        info.add("Insert ransomeware def here");
        info.add("15.0");
        attacks.put(4, info);

        info = new ArrayList<String>();
        info.add("Man-in-the-Middle");
        info.add("Insert MiM def here");
        info.add("15.0");
        attacks.put(5, info);




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
            tDay = sharedPreferences.getInt("day", new Integer(1));
            if(tDay == 0){
                tDay =1;
            }
            attackR = (Double) ObjectSerializer.deserialize(sharedPreferences.getString("attackR", ObjectSerializer.serialize(new Double(0))));
            preventionRs = (HashMap<Integer, Double>) ObjectSerializer.deserialize(sharedPreferences.getString("preventionRs", ObjectSerializer.serialize(new HashMap<Integer, Double>())));
        } catch (IOException | ClassNotFoundException e) {
            //no game exists
            currFunds = new ArrayList<Double>(1);
            currFunds.set(0,0.0);
            payR = 10.0;
            payD = 1;
            tDay = 1;
            attackR = 0.01;
            preventionRs = new HashMap<Integer, Double>();
            preventionRs.put(0, 0.0);
            preventionRs.put(1, 0.0);
            preventionRs.put(2, 0.0);
            preventionRs.put(3, 0.0);
            preventionRs.put(4, 0.0);
            preventionRs.put(5, 0.0);

        }
        this.currentFunds = new MutableLiveData<ArrayList<Double>>(currFunds);
        this.currentFunds.setValue(currFunds);
        this.payRate = new MutableLiveData<Double>(payR);
        this.payDelay = new MutableLiveData<Integer>(payD); // one second
        this.day = new MutableLiveData<Integer>(tDay);
        this.attackRate = new MutableLiveData<Double>(0.01);
        this.attackList = attacks;
        this.noOfAttacks = attacks.size();
        this.attackRate = new MutableLiveData<Double>(attackR);
        this.preventionRate = new MutableLiveData<HashMap<Integer, Double>>(preventionRs);



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
    public MutableLiveData<Integer> getDay(){return this.day;}


    public void showBusUpgrades()
    {
       // TableLayout upgradeTable = (TableLayout) game.findViewById(R.id.upgrades_list);

    }

    public void updater(){
        double attackTest = Math.random();
        if (attackTest <=  this.attackRate.getValue()) {
            int attackType = ThreadLocalRandom.current().nextInt(0, this.noOfAttacks);

            // check if the attack will be prevented by an upgrade, and if so, pass that to ui
            if (isAttackPrevented(attackType)) {
                game.onPreventedAttack();
            } else {
                game.onSuccessfulAttack(this.attackList.get(attackType));
                // subtract money from currentFunds according to attack cost
                int moneyIndex = this.currentFunds.getValue().size() - 1;
                double bankAcct = this.currentFunds.getValue().get(moneyIndex);
                double attCost = bankAcct * Double.parseDouble(this.attackList.get(attackType).get(2));
                this.currentFunds.getValue().set(moneyIndex, attCost);
            }
        }

    }

    private boolean isAttackPrevented(int attack) {
        double preventionCheck = Math.random();
        if (preventionCheck <= this.preventionRate.getValue().get(attack)){
            return true;
        }
        return false;
    }

    public void saveAll(){
        try{
            sharedPreferences.edit().putString("currFunds",
                    ObjectSerializer.serialize(currentFunds.getValue())).apply();
            sharedPreferences.edit().putString("payR", ObjectSerializer.serialize(payRate.getValue())).apply();
            sharedPreferences.edit().putString("payD", ObjectSerializer.serialize(payDelay.getValue())).apply();
            sharedPreferences.edit().putInt("day",day.getValue()).apply();
            sharedPreferences.edit().putString("attackR",ObjectSerializer.serialize(attackRate.getValue())).apply();
            sharedPreferences.edit().putString("preventionRs", ObjectSerializer.serialize(preventionRate.getValue())).apply();
            sharedPreferences.edit().commit();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("SOD", "Couldn't create a file");
        }
    }
}
