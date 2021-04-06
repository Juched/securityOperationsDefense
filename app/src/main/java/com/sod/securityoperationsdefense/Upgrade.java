package com.sod.securityoperationsdefense;

import android.widget.TextView;

import androidx.cardview.widget.CardView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Upgrade implements Serializable
{
    //private static GameActivity context;
    //private icon;
    private String name;
    private String description;
    private int level;
    private int costs;
    private boolean update = false;

    public static final int MAX_LEVEL = 3;

    private static final ArrayList<Upgrade> allUpgrades = new ArrayList<Upgrade>();

    public Upgrade(GameActivity newContext, String theName, String descrip)
    {
        //Upgrade.context = newContext;
        this.name = theName;
        this.description = descrip;
        this.level = 0;

        int localStart = 37;

        Random rando = new Random();

        this.costs = Math.abs(rando.nextInt()%localStart) + (localStart/2);

        allUpgrades.add(this);
    }



    //public GameActivity getContext() { return Upgrade.context; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getLevel() { return level; }
    public void levelUp()
    {
        level++;


        costs += Math.pow(15, level + 1);

        Random rando = new Random();
        costs += (rando.nextInt()%15) + 7 - (rando.nextInt()%20);
    }
    public int getCost() { return costs; }
    public boolean isUpdated() { return update; }


    private void reduceCost(double reductionPercentage)
    {
        // 15 % reduction ==> 1 -> .85 and some extra flavor
        this.costs *= Math.pow(1-reductionPercentage, this.level + 1);
    }

    public static void reduceAllCosts(double reductionPercentage)
    {

        for(int i = 0; i< allUpgrades.size(); i++)
        {
            allUpgrades.get(i).reduceCost(reductionPercentage);
        }
    }


}