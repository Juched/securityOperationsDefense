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
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

import androidx.lifecycle.Observer;
import kotlin.text.UStringsKt;

public class Game {
    private static MutableLiveData<ArrayList<Double>> currentFunds;
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

        ArrayList<Upgrade> mbusUpgrades;
        ArrayList<Upgrade> mcritUpgrades;
        ArrayList<Upgrade> minfoUpgrades;
        ArrayList<Upgrade> msecUpgrades;


        // upgrades lists
        HashMap<String, ArrayList<CardView>> upgrades; // does not need saved

        double attackR; // needs to be saved
        HashMap<Integer, Double> preventionRs; // needs to be saved

        // create and populate the attack map
        HashMap<Integer,ArrayList<String>> attacks = new HashMap<Integer,ArrayList<String>>(); // does not need saved
        ArrayList<String> info = new ArrayList<String>();
        info.add("Phishing");
        info.add("Insert phishing def here");
        info.add("15.00");
        attacks.put(0, info);

        info = new ArrayList<String>();
        info.add("Brute-force");
        info.add("Insert brute-force def here");
        info.add("15.00");
        attacks.put(1, info);

        info = new ArrayList<String>();
        info.add("DDoS");
        info.add("Insert DDoS def here");
        info.add("15.00");
        attacks.put(2, info);

        info = new ArrayList<String>();
        info.add("Insider");
        info.add("Insert insider attack def here");
        info.add("15.00");
        attacks.put(3, info);

        info = new ArrayList<String>();
        info.add("Ransomware");
        info.add("Insert ransomware def here");
        info.add("15.00");
        attacks.put(4, info);

        info = new ArrayList<String>();
        info.add("Man-in-the-Middle");
        info.add("Insert MiM def here");
        info.add("15.00");
        attacks.put(5, info);




        try {
            currFunds = (ArrayList<Double>) ObjectSerializer.deserialize(
                    sharedPreferences.getString("currFunds", ObjectSerializer.serialize(new ArrayList<Double>())));
            if (currFunds.size() < 1) {
                currFunds.add(0.0);
            }
            payR = (Double) ObjectSerializer.deserialize(
                    sharedPreferences.getString("payR", ObjectSerializer.serialize(new Double(20))));
            payD = (Integer) ObjectSerializer.deserialize(
                    sharedPreferences.getString("payD", ObjectSerializer.serialize(new Integer(0))));
            tDay = sharedPreferences.getInt("day", new Integer(1));
            if (tDay == 0) {
                tDay = 1;
            }
            attackR = (Double) ObjectSerializer.deserialize(sharedPreferences.getString("attackR", ObjectSerializer.serialize(new Double(0.005))));
            preventionRs = (HashMap<Integer, Double>) ObjectSerializer.deserialize(sharedPreferences.getString("preventionRs", ObjectSerializer.serialize(new HashMap<Integer, Double>())));
            if(preventionRs.size() == 0){
                preventionRs.put(0, 0.0);
                preventionRs.put(1, 0.0);
                preventionRs.put(2, 0.0);
                preventionRs.put(3, 0.0);
                preventionRs.put(4, 0.0);
                preventionRs.put(5, 0.0);
            }
            mbusUpgrades = (ArrayList<Upgrade>) ObjectSerializer.deserialize(sharedPreferences.getString("busUpgrades", ObjectSerializer.serialize(new ArrayList<Upgrade>())));
            mcritUpgrades = (ArrayList<Upgrade>) ObjectSerializer.deserialize(sharedPreferences.getString("critUpgrades", ObjectSerializer.serialize(new ArrayList<Upgrade>())));
            minfoUpgrades = (ArrayList<Upgrade>) ObjectSerializer.deserialize(sharedPreferences.getString("infoUpgrades", ObjectSerializer.serialize(new ArrayList<Upgrade>())));
            msecUpgrades = (ArrayList<Upgrade>) ObjectSerializer.deserialize(sharedPreferences.getString("secUpgrades", ObjectSerializer.serialize(new ArrayList<Upgrade>())));
        } catch (IOException | ClassNotFoundException e) {
            //no game exists
            currFunds = new ArrayList<Double>(1);
            currFunds.set(0, 0.0);
            payR = 10.0;
            payD = 1;
            tDay = 1;
            attackR = 0.005;
            preventionRs = new HashMap<Integer, Double>();
            preventionRs.put(0, 0.0);
            preventionRs.put(1, 0.0);
            preventionRs.put(2, 0.0);
            preventionRs.put(3, 0.0);
            preventionRs.put(4, 0.0);
            preventionRs.put(5, 0.0);
            mbusUpgrades = new ArrayList<Upgrade>();
            minfoUpgrades = new ArrayList<Upgrade>();
            mcritUpgrades = new ArrayList<Upgrade>();
            msecUpgrades = new ArrayList<Upgrade>();

        }

        if(mbusUpgrades.size() == 0){
            this.busUpgrades = new MutableLiveData<ArrayList<Upgrade>>();
            this.critInfoUpgrades = new MutableLiveData<ArrayList<Upgrade>>();
            this.infoStateUpgrades = new MutableLiveData<ArrayList<Upgrade>>();
            this.secUpgrades = new MutableLiveData<ArrayList<Upgrade>>();
            this.makeUpgrades();
        }else {
            this.busUpgrades = new MutableLiveData<ArrayList<Upgrade>>(mbusUpgrades);
            this.critInfoUpgrades = new MutableLiveData<ArrayList<Upgrade>>(mcritUpgrades);
            this.infoStateUpgrades = new MutableLiveData<ArrayList<Upgrade>>(minfoUpgrades);
            this.secUpgrades = new MutableLiveData<ArrayList<Upgrade>>(msecUpgrades);
        }

        currentFunds = new MutableLiveData<ArrayList<Double>>(currFunds);
        currentFunds.setValue(currFunds);
        this.currentFunds = new MutableLiveData<ArrayList<Double>>(currFunds);
        this.currentFunds.setValue(currFunds);
        this.payRate = new MutableLiveData<Double>(payR);
        this.payDelay = new MutableLiveData<Integer>(payD); // one second
        this.day = new MutableLiveData<Integer>(tDay);
        this.attackRate = new MutableLiveData<Double>(attackR);

        this.attackList = attacks;
        this.noOfAttacks = attacks.size();

        this.preventionRate = new MutableLiveData<HashMap<Integer, Double>>(preventionRs);


        // observer methods for upgrades
        this.doBusinessUpgrade();
        this.doCritUpgrade();
        this.doSecUpgrade();
        this.doiStateUpgrade();

        this.day.observe(this.game,new Observer<Integer>(){

            /**
             * Called when the data is changed.
             *
             * @param integer The new data
             */
            @Override
            public void onChanged(Integer integer) {
                ArrayList<Double> money = currentFunds.getValue();
                money.set(money.size()-1,money.get(money.size()-1) + payRate.getValue());
                currentFunds.postValue(money);
            }
        });
    }
    /* creates */
    private void makeUpgrades()
    {

        // Business upgrades ~ and all upgrades
        String[] businessUpgrades = {"Open New Location", "Cost Benefit Analysis", "Cut Employee Salaries", "Pizza Party"};
        String[] critUpgrades = {"2-Factor Authentication", "Get Latest Software", "Ransomware Prevention", "Data Integrity Validation"};
        String[] secUpgrades = {"Phishing Awareness Training", "Monitoring and Logging", "Firewalls", "Hire More Security Personnel"};
        String[] iStateUpgrades = {"Data Excryption", "More Data Storage", "More Processing Power", "More Secure Transmission"};

        this.busUpgrades.setValue(this.PopulateUpgradeList(businessUpgrades));
        this.critInfoUpgrades.setValue(this.PopulateUpgradeList(critUpgrades));
        this.secUpgrades.setValue(this.PopulateUpgradeList(secUpgrades));
        this.infoStateUpgrades.setValue(this.PopulateUpgradeList(iStateUpgrades));
    }

    /*
    *   TODO:
    *    1. add a flag to determine whether we are in the creation part or not
    *    2. compare cards to determine which cards have changed
    */
    private void doBusinessUpgrade() {
        this.busUpgrades.observe(game, new Observer<ArrayList<Upgrade>>() {
            @Override
            public void onChanged(ArrayList<Upgrade> upgrades) {
                for (Upgrade card: upgrades) {
                    String cardName = card.getName();
                    int cost = card.getCost();
                    switch (cardName) {
                        case "Open New Location":
                            // pay rate +5%, attack rate +5%
                            Double payR = payRate.getValue();
                            payR += 5.0;
                            payRate.postValue(payR);

                            Double att = attackRate.getValue();
                            att += 0.005;
                            attackRate.postValue(att);
                            break;

                        case "Cost Benefit Analysis":
                            // upgrade cost -5%
                            Upgrade.reduceAllCosts(0.05);
                            break;

                        case "Cut Employee Salaries":
                            // money +5%
                            // insider attack PR +5%
                            HashMap<Integer, Double> pr = preventionRate.getValue();
                            Double d = pr.get(3);
                            d += 5.0;
                            pr.replace(3, d);
                            preventionRate.postValue(pr);

                            ArrayList<Double> curr = currentFunds.getValue();
                            Double lastVal = curr.get(curr.size() - 1);
                            Double newVal = lastVal + lastVal*0.05;
                            curr.set(curr.size()-1, newVal);
                            currentFunds.postValue(curr);
                            break;

                        case "Pizza Party":
                            // insider attack PR +5%
                            HashMap<Integer, Double> prev = preventionRate.getValue();
                            Double rate = prev.get(3);
                            rate += 5.0;
                            prev.replace(3, rate);
                            preventionRate.postValue(prev);
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
                    int cost = card.getCost();
                    switch (cardName) {
                        case "2-Factor Authentication":
                            // brute force PR +5%
                            HashMap<Integer, Double> prev = preventionRate.getValue();
                            Double rate = prev.get(1);
                            rate += 5.0;
                            prev.replace(1, rate);
                            preventionRate.postValue(prev);
                            break;

                        case "Get Latest Software":
                            // attack rate -4%
                            Double att = attackRate.getValue();
                            att -= 0.004;
                            attackRate.postValue(att);
                            break;

                        case "Ransomware Prevention":
                            // ransomware PR +5%
                            HashMap<Integer, Double> pr = preventionRate.getValue();
                            Double r = pr.get(4);
                            r += 5.0;
                            pr.replace(4, r);
                            preventionRate.postValue(pr);
                            break;

                        case "Data Integrity Validation":
                            // pay rate -4%, attack rate -5%
                            Double payR = payRate.getValue();
                            payR -= 4.0;
                            payRate.postValue(payR);

                            Double a = attackRate.getValue();
                            a -= 0.005;
                            attackRate.postValue(a);
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
                    int cost = card.getCost();
                    switch (cardName) {
                        case "Phishing Awareness Training":
                            // phishing PR +5%
                            HashMap<Integer, Double> pr = preventionRate.getValue();
                            Double r = pr.get(0);
                            r += 5.0;
                            pr.replace(0, r);
                            preventionRate.postValue(pr);
                            break;

                        case "Monitoring and Logging":
                            // attack rate -10%
                            Double att = attackRate.getValue();
                            att -= 0.01;
                            attackRate.postValue(att);
                            break;

                        case "Firewalls":
                            // DDoS PR +5%
                            HashMap<Integer, Double> prev = preventionRate.getValue();
                            Double rate = prev.get(2);
                            rate += 5.0;
                            prev.replace(2, rate);
                            preventionRate.postValue(prev);
                            break;

                        case "Hire More Security Personnel":
                            // pay rate -4%, attack rate -5%
                            Double payR = payRate.getValue();
                            payR -= 4.0;
                            payRate.postValue(payR);

                            Double a = attackRate.getValue();
                            a -= 0.005;
                            attackRate.postValue(a);
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
                    int cost = card.getCost();
                    switch (cardName) {
                        case "Data Excryption":
                            // man in middle PR +5%
                            HashMap<Integer, Double> pr = preventionRate.getValue();
                            Double d = pr.get(5);
                            d += 5.0;
                            pr.replace(5, d);
                            preventionRate.postValue(pr);
                            break;

                        case "More Data Storage":
                            // brute force PR +5%
                            HashMap<Integer, Double> prev = preventionRate.getValue();
                            Double rate = prev.get(1);
                            rate += 5.0;
                            prev.replace(1, rate);
                            preventionRate.postValue(prev);
                            break;

                        case "More Processing Power":
                            // payRate +5%
                            Double payR = payRate.getValue();
                            payR += 5.0;
                            payRate.postValue(payR);
                            break;

                        case "More Secure Transmission":
                            // man in middle PR +5%, pay rate -5%
                            HashMap<Integer, Double> p = preventionRate.getValue();
                            Double r = p.get(5);
                            r += 5.0;
                            p.replace(5, r);
                            preventionRate.postValue(p);

                            Double pay = payRate.getValue();
                            pay -= 5.0;
                            payRate.postValue(pay);
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

    public static double getTheCurrentFunds()
    {
        ArrayList<Double> money = Game.currentFunds.getValue();
        return money.get(money.size() - 1);
    }

    public static void spendMoney(double cost)
    {
        ArrayList<Double> money = Game.currentFunds.getValue();
        money.set(money.size() - 1, money.get(money.size() - 1) - cost);
        Game.currentFunds.postValue(money);

        /* Can add logic here for - $ and if it hits BANKRUPTCY */
    }

    public MutableLiveData<ArrayList<Double>> getCurrentFunds() {
        return Game.currentFunds;
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

    // minor bug in here somewhere
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
                double attCost = bankAcct - Double.parseDouble(this.attackList.get(attackType).get(2));

                ArrayList<Double> money = this.currentFunds.getValue();
                money.set(moneyIndex, attCost);
                this.currentFunds.postValue(money);
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
                    ObjectSerializer.serialize(Game.currentFunds.getValue())).apply();
            sharedPreferences.edit().putString("payR", ObjectSerializer.serialize(payRate.getValue())).apply();
            sharedPreferences.edit().putString("payD", ObjectSerializer.serialize(payDelay.getValue())).apply();
            sharedPreferences.edit().putInt("day",day.getValue()).apply();
            sharedPreferences.edit().putString("attackR",ObjectSerializer.serialize(attackRate.getValue())).apply();
            sharedPreferences.edit().putString("preventionRs", ObjectSerializer.serialize(preventionRate.getValue())).apply();
            sharedPreferences.edit().putString("busUpgrades", ObjectSerializer.serialize(busUpgrades.getValue())).apply();
            sharedPreferences.edit().putString("critUpgrades", ObjectSerializer.serialize(critInfoUpgrades.getValue())).apply();
            sharedPreferences.edit().putString("secUpgrades", ObjectSerializer.serialize(secUpgrades.getValue())).apply();
            sharedPreferences.edit().putString("infoUpgrades", ObjectSerializer.serialize(infoStateUpgrades.getValue())).apply();
            sharedPreferences.edit().commit();

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("SOD", "Couldn't create a file");
        }
    }
}
