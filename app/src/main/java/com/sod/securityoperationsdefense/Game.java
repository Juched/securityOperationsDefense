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
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;

import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

import androidx.lifecycle.Observer;
import kotlin.text.UStringsKt;

public class Game {
    private MutableLiveData<ArrayList<Double>> currentFunds;
    private MutableLiveData<Integer> payDelay;
    private MutableLiveData<Double> payRate;
    private SharedPreferences sharedPreferences;
    private MutableLiveData<Integer> day;
    private GameActivity game;

    // Serializable lists of upgrades
    private MutableLiveData<ArrayList<Upgrade>>  busUpgrades;
    private MutableLiveData<ArrayList<Upgrade>>  critInfoUpgrades;
    private MutableLiveData<ArrayList<Upgrade>>  infoStateUpgrades;
    private MutableLiveData<ArrayList<Upgrade>>  secUpgrades;

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

        this.busUpgrades = new MutableLiveData<ArrayList<Upgrade>>();
        this.critInfoUpgrades = new MutableLiveData<ArrayList<Upgrade>>();
        this.infoStateUpgrades = new MutableLiveData<ArrayList<Upgrade>>();
        this.secUpgrades = new MutableLiveData<ArrayList<Upgrade>>();

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
            if (currFunds.size() < 1) {
                currFunds.add(0.0);
            }
            payR = (Double) ObjectSerializer.deserialize(
                    sharedPreferences.getString("payR", ObjectSerializer.serialize(new Double(0))));
            payD = (Integer) ObjectSerializer.deserialize(
                    sharedPreferences.getString("payD", ObjectSerializer.serialize(new Integer(0))));
            tDay = sharedPreferences.getInt("day", new Integer(1));
            if (tDay == 0) {
                tDay = 1;
            }
            attackR = (Double) ObjectSerializer.deserialize(sharedPreferences.getString("attackR", ObjectSerializer.serialize(new Double(0))));
            preventionRs = (HashMap<Integer, Double>) ObjectSerializer.deserialize(sharedPreferences.getString("preventionRs", ObjectSerializer.serialize(new HashMap<Integer, Double>())));
            if(preventionRs.size() == 0){
                preventionRs.put(0, 0.0);
                preventionRs.put(1, 0.0);
                preventionRs.put(2, 0.0);
                preventionRs.put(3, 0.0);
                preventionRs.put(4, 0.0);
                preventionRs.put(5, 0.0);
            }
        } catch (IOException | ClassNotFoundException e) {
            //no game exists
            currFunds = new ArrayList<Double>(1);
            currFunds.set(0, 0.0);
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
        this.attackRate = new MutableLiveData<>(attackR);
        this.attackRate = new MutableLiveData<Double>(0.01);
        this.attackList = attacks;
        this.noOfAttacks = attacks.size();
        this.attackRate = new MutableLiveData<Double>(attackR);
        this.preventionRate = new MutableLiveData<HashMap<Integer, Double>>(preventionRs);

        this.makeUpgrades();

        // observer methods for upgrades
        this.doBusinessUpgrade();
        this.doCritUpgrade();
        this.doSecUpgrade();
        this.doiStateUpgrade();
    }

    private void makeUpgrades()
    {
        // Business upgrades
        String[] businessUpgrades = {"Boosted Morale", "pizza party", "dummy 1.0", "dumby 2"};
        String[] critUpgrades = {"MFA ~ 2 factor", "less ransom", "dummy 1", "place holder"};
        String[] secUpgrades = {"training", "better CBA", "exquisite jazz hands", "killer crocs"};
        String[] iStateUpgrades = {"Boosted Morale", "better CBA", "transmitting failure", "area 51 storage"};

        this.busUpgrades.setValue(this.PopulateUpgradeList(businessUpgrades));
        this.critInfoUpgrades.setValue(this.PopulateUpgradeList(critUpgrades));
        this.secUpgrades.setValue(this.PopulateUpgradeList(secUpgrades));
        this.infoStateUpgrades.setValue(this.PopulateUpgradeList(iStateUpgrades));
    }

    private void doBusinessUpgrade() {
        this.busUpgrades.observe(game, new Observer<ArrayList<Upgrade>>() {
            @Override
            public void onChanged(ArrayList<Upgrade> upgrades) {
                for (Upgrade card: upgrades) {
                    String cardName = card.getName();
//                    String desc = card.getDescription();
//                    int level = card.getLevel();
                    int cost = card.getCost();

                    ArrayList<Double> curr = currentFunds.getValue();
                    Double lastVal = curr.get(curr.size() - 1);
                    Double newVal = lastVal - (double) cost;
                    curr.add(newVal);
                    currentFunds.postValue(curr);

                    switch (cardName) {
                        case "Boosted Morale":
                            payRate.postValue(5.0);
                            break;

                        case "pizza party":
                            HashMap<Integer, Double> pr = preventionRate.getValue();
                            Double d = pr.get(2);
                            d += 5.0;
                            pr.replace(2, d);
                            preventionRate.postValue(pr);
                            break;

                        case "dummy 1.0":
                            break;

                        case "dumby 2":
                            break;
                    }
                }
            }
        });
    }

    private void doCritUpgrade() {
        this.critInfoUpgrades.observe(game, new Observer<ArrayList<Upgrade>>() {
            @Override
            public void onChanged(ArrayList<Upgrade> upgrades) {
                for (Upgrade card: upgrades) {
                    String cardName = card.getName();
//                    String desc = card.getDescription();
//                    int level = card.getLevel();
                    int cost = card.getCost();

                    ArrayList<Double> curr = currentFunds.getValue();
                    Double lastVal = curr.get(curr.size() - 1);
                    Double newVal = lastVal - (double) cost;
                    curr.add(newVal);
                    currentFunds.postValue(curr);

                    switch (cardName) {
                        case "MFA ~ 2 factor":
                            payRate.postValue(5.0);
                            break;

                        case "less ransom":
                            HashMap<Integer, Double> pr = preventionRate.getValue();
                            Double d = pr.get(2);
                            d += 5.0;
                            pr.replace(2, d);
                            preventionRate.postValue(pr);
                            break;

                        case "dummy 1":
                            break;

                        case "place holder":
                            break;
                    }
                }
            }
        });
    }

    private void doSecUpgrade() {
        this.secUpgrades.observe(game, new Observer<ArrayList<Upgrade>>() {
            @Override
            public void onChanged(ArrayList<Upgrade> upgrades) {
                for (Upgrade card: upgrades) {
                    String cardName = card.getName();
//                    String desc = card.getDescription();
//                    int level = card.getLevel();
                    int cost = card.getCost();

                    ArrayList<Double> curr = currentFunds.getValue();
                    Double lastVal = curr.get(curr.size() - 1);
                    Double newVal = lastVal - (double) cost;
                    curr.add(newVal);
                    currentFunds.postValue(curr);

                    switch (cardName) {
                        case "training":
                            payRate.postValue(5.0);
                            break;

                        case "better CBA":
                            HashMap<Integer, Double> pr = preventionRate.getValue();
                            Double d = pr.get(2);
                            d += 5.0;
                            pr.replace(2, d);
                            preventionRate.postValue(pr);
                            break;




                        case "exquisite jazz hands":
                            break;

                        case "killer crocs":
                            break;
                    }
                }
            }
        });
    }

    private void doiStateUpgrade() {
        this.infoStateUpgrades.observe(game, new Observer<ArrayList<Upgrade>>() {
            @Override
            public void onChanged(ArrayList<Upgrade> upgrades) {
                for (Upgrade card: upgrades) {
                    String cardName = card.getName();
//                    String desc = card.getDescription();
//                    int level = card.getLevel();
                    int cost = card.getCost();

                    ArrayList<Double> curr = currentFunds.getValue();
                    Double lastVal = curr.get(curr.size() - 1);
                    Double newVal = lastVal - (double) cost;
                    curr.add(newVal);
                    currentFunds.postValue(curr);

                    switch (cardName) {
                        case "Boosted Morale":
                            payRate.postValue(5.0);
                            break;

                        case "better CBA":
                            HashMap<Integer, Double> pr = preventionRate.getValue();
                            Double d = pr.get(2);
                            d += 5.0;
                            pr.replace(2, d);
                            preventionRate.postValue(pr);
                            break;

                        case "transmitting failure":
                            break;

                        case "area 51 storage":
                            break;
                    }
                }
            }
        });
    }

    private ArrayList<Upgrade> PopulateUpgradeList(String[] names)
    {
        ArrayList<Upgrade> allUpgradesInCategory = new ArrayList<Upgrade>();
        for(String name : names)
        {
            allUpgradesInCategory.add(new Upgrade(this.game, name, ""));
        }
        return allUpgradesInCategory;
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

    /* Please double check that I use the mutable types correctly */

    public void setPayRate(double pay) { this.payRate.postValue(pay); }
    public void setPayDelay(int delay) { this.payDelay.postValue(delay); }



    public MutableLiveData<ArrayList<Upgrade>> getBusUpgrades(){return this.busUpgrades;}
    public MutableLiveData<ArrayList<Upgrade>> getCritInfoUpgrades(){return this.critInfoUpgrades;}
    public MutableLiveData<ArrayList<Upgrade>> getInfoStateUpgrades(){return this.infoStateUpgrades;}
    public MutableLiveData<ArrayList<Upgrade>> getSecUpgrades(){return this.secUpgrades;}

    public GameActivity getGameForContext() {
        return game;
    }

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
