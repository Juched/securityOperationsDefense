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
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

import kotlin.text.UStringsKt;

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

        this.busUpgrades = new MutableLiveData<ArrayList<CardView>>();
        this.critInfoUpgrades = new MutableLiveData<ArrayList<CardView>>();
        this.infoStateUpgrades = new MutableLiveData<ArrayList<CardView>>();
        this.secUpgrades = new MutableLiveData<ArrayList<CardView>>();

        // upgrades lists
        HashMap<String, ArrayList<CardView>> upgrades;

        // TODO: fill these
        double attackR = 1;
        HashMap<Integer, Double> preventionRs;


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
        } catch (IOException | ClassNotFoundException e) {
            //no game exists
            currFunds = new ArrayList<Double>(1);
            currFunds.set(0, 0.0);
            payR = 10.0;
            payD = 1;
            tDay = 1;
        }
//        }
        this.currentFunds = new MutableLiveData<ArrayList<Double>>(currFunds);
        this.currentFunds.setValue(currFunds);
        this.payRate = new MutableLiveData<Double>(payR);
        this.payDelay = new MutableLiveData<Integer>(payD); // one second
        this.day = new MutableLiveData<Integer>(tDay);
        this.attackRate = new MutableLiveData<>(attackR);

        this.makeUpgrades();
    }

    private void makeUpgrades()
    {

        // Business upgrades
        String[] businessUpgrades = {"Boosted Morale", "pizza party"};
        String[] critUpgrades = {"MFA ~ 2 factor", "less ransom"};
        String[] secUpgrades = {"training", "better CBA"};
        String[] iStateUpgrades = {"Boosted Morale", "better CBA"};

        this.busUpgrades.setValue(this.PopulateUpgradeList(businessUpgrades));
        this.critInfoUpgrades.setValue(this.PopulateUpgradeList(critUpgrades));
        this.secUpgrades.setValue(this.PopulateUpgradeList(secUpgrades));
        this.infoStateUpgrades.setValue(this.PopulateUpgradeList(iStateUpgrades));


    }

    private ArrayList<CardView> PopulateUpgradeList(String[] names)
    {
        ArrayList<CardView> allUpgradesInCategory = new ArrayList<CardView>();
        for(String name : names)
        {
            allUpgradesInCategory.add(new Upgrade(this.game, name, "").UpgradeCard());
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

    public MutableLiveData<ArrayList<CardView>> getBusUpgrades(){return this.busUpgrades;}
    public MutableLiveData<ArrayList<CardView>> getCritInfoUpgrades(){return this.critInfoUpgrades;}
    public MutableLiveData<ArrayList<CardView>> getInfoStateUpgrades(){return this.infoStateUpgrades;}
    public MutableLiveData<ArrayList<CardView>> getSecUpgrades(){return this.secUpgrades;}

    public GameActivity getGameForContext() {
        return game;
    }

    public void showBusUpgrades()
    {
       // TableLayout upgradeTable = (TableLayout) game.findViewById(R.id.upgrades_list);

    }

    // minor bug in here somewhere
    public void updater(){
//        double attackTest = Math.random();
//        if (attackTest <=  this.attackRate.getValue()) {
//            int attackType = ThreadLocalRandom.current().nextInt(0, noOfAttacks);
//
//            // check if the attack will be prevented by an upgrade, and if so, pass that to ui
//            if (isAttackPrevented(attackType)) {
//                //TODO: tell ui that attackType attack was prevented. nothing else happens
//            } else {
//                /* TODO: on a successful att, make a text box for the front end to show that
//                    includes the name of attack, short description of attack, and cost of attack.*/
//
//                // subtract money from currentFunds according to attack cost
//                int moneyIndex = this.currentFunds.getValue().size() - 1;
//                double bankAcct = this.currentFunds.getValue().get(moneyIndex);
//                double attCost = bankAcct * Double.parseDouble(attackList.get(attackType).get(2));
//                this.currentFunds.getValue().set(moneyIndex, attCost);
//            }
//        }

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
            sharedPreferences.edit().commit();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("SOD", "Couldn't create a file");
        }
    }
}


class Upgrade
{
    private GameActivity context;
    //private icon;
    private String name;
    private String description;

    public Upgrade(GameActivity newContext, String theName, String descrip)
    {
        this.context = newContext;
        this.name = theName;
        this.description = descrip;
    }

//    public GameActivity getContext() { return context; }
//    public String getName() { return name; }
//    public String getDescription() { return description; }

    // returns the CardView rep of the upgrade
    public CardView UpgradeCard()
    {
        CardView upgrade = new CardView(this.context);

        TextView upName = new TextView(this.context);
        upName.setText(this.name);
        upgrade.addView(upName);

        return upgrade;
    }

}