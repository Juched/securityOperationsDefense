package com.sod.securityoperationsdefense;

import android.widget.TextView;

import androidx.cardview.widget.CardView;

import java.io.Serializable;
import java.util.ArrayList;

public class Upgrade implements Serializable
{
    //private static GameActivity context;
    //private icon;
    private String name;
    private String description;
    private int level;
    private int costs = 15;
    private boolean update = false;

    public static final int MAX_LEVEL = 3;

    private static final ArrayList<Upgrade> allUpgrades = new ArrayList<Upgrade>();

    public Upgrade(GameActivity newContext, String theName, String descrip)
    {
        //Upgrade.context = newContext;
        this.name = theName;
        this.description = descrip;
        this.level = 0;

        allUpgrades.add(this);
    }



    //public GameActivity getContext() { return Upgrade.context; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getLevel() { return level; }
    public void levelUp() { level++; }
    public int getCost() { return costs; }
    public boolean isUpdated() { return update; }




    public static void reduceAllCosts(double reductionPercentage)
    {

    }


}