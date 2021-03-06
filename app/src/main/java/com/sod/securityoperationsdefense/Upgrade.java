// Andrew Whiteman, Dylan Pryor
package com.sod.securityoperationsdefense;

import android.widget.TextView;

import androidx.cardview.widget.CardView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Upgrade implements Serializable
{

    private String name;
    private String description;
    private int level;
    private int costs;
    private boolean update;
    /* max level of any upgrade */
    public static final int MAX_LEVEL = 3;
    /* List of all upgrades */
    private static final ArrayList<Upgrade> allUpgrades = new ArrayList<Upgrade>();

    public Upgrade(GameActivity newContext, String theName, String descrip)
    {
        //Upgrade.context = newContext;
        this.name = theName;
        this.description = descrip;
        this.level = 0;
        this.update = false;

        int localStart = 37;

        Random rando = new Random();

        /* initial cost */
        this.costs = Math.abs(rando.nextInt()%localStart) + (localStart/2);

        allUpgrades.add(this);
    }



    /* Public gettors for instance data */
    public String getName() { return name; }
    public String getDescription() { return String.format("%s (Level %s): \n%s",name, this.getLevel(), description); }
    public int getLevel() { return Math.min(level, Upgrade.MAX_LEVEL); }
    public int getCost() { return costs; }
    public boolean isUpdated() { return update; }
    public void toggleUpdate() { update = false; }


    /* levels up an upgrade and finds next cost */
    public void levelUp()
    {
        level++;
        this.update = true;

        costs += Math.pow(15, level + 1);

        Random rando = new Random();
        costs += (rando.nextInt()%15) + 7 - (rando.nextInt()%20);
    }

    /* reduces cost of a single Upgrade */
    private void reduceCost(double reductionPercentage)
    {
        // 15 % reduction ==> 1 -> .85 and some extra flavor
        this.costs *= Math.pow(1-reductionPercentage, this.level + 1);
    }

    /* reduces costs of all Upgrades */
    public static void reduceAllCosts(double reductionPercentage)
    {

        for(int i = 0; i< allUpgrades.size(); i++)
        {
            allUpgrades.get(i).reduceCost(reductionPercentage);
        }
    }


}