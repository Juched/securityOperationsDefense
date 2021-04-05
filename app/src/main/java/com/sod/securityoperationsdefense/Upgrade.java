package com.sod.securityoperationsdefense;

import android.widget.TextView;

import androidx.cardview.widget.CardView;

public class Upgrade
{
    private GameActivity context;
    //private icon;
    private String name;
    private String description;
    private int level;
    private int costs = 15;

    public Upgrade(GameActivity newContext, String theName, String descrip)
    {
        this.context = newContext;
        this.name = theName;
        this.description = descrip;
        this.level = 1;
    }



    public GameActivity getContext() { return context; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getLevel() { return level; }
    public void levelUp() { level++; }
    public int getCost() { return costs; }

    // returns the CardView rep of the upgrade
    public CardView UpgradeCard()
    {
        CardView upgrade = new CardView(this.context);

        TextView upName = new TextView(this.context);
        upName.setText(this.name + " Level " + this.level);
        upgrade.addView(upName);

        return upgrade;
    }

}