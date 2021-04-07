package com.sod.securityoperationsdefense;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.Window;
import android.app.Fragment;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.LimitLine.LimitLabelPosition;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;

import com.sod.securityoperationsdefense.ui.upgrades.BusAdvancementsFragment;
import com.sod.securityoperationsdefense.ui.upgrades.BusAdvancementsViewModel;
import com.sod.securityoperationsdefense.ui.upgrades.CritInfoFragment;
import com.sod.securityoperationsdefense.ui.upgrades.CritInfoViewModel;
import com.sod.securityoperationsdefense.ui.upgrades.InfoStateFragment;
import com.sod.securityoperationsdefense.ui.upgrades.InfoStateViewModel;
import com.sod.securityoperationsdefense.ui.upgrades.SecMeasuresFragment;
import com.sod.securityoperationsdefense.ui.upgrades.SecMeasuresViewModel;
import com.sod.securityoperationsdefense.ui.upgrades.StatsViewModel;

import androidx.cardview.widget.CardView;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.SharedPreferences;
import android.widget.TextView;
import android.graphics.*;

import org.w3c.dom.Text;

import java.io.IOException;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class GameActivity extends AppCompatActivity {
    private Boolean paused;
    private AppBarConfiguration mAppBarConfiguration;
    private Game gameClass;
    private InfoStateViewModel infoStateViewModel;
    private SecMeasuresViewModel secMeasuresViewModel;
    private CritInfoViewModel critInfoViewModel;
    private BusAdvancementsViewModel busAdvancementsViewModel;
    private StatsViewModel statsViewModel;
    public Handler mHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //call the super class
        super.onCreate(savedInstanceState);
        //set the paused flag
        paused = false;
        //declare a handler to force UI thread when needed
        mHandler = new Handler();
        //Start the game object
        gameClass = new Game(getApplicationContext(), this);

        //initialize all the viewmodels which provide the game object to our fragments
        infoStateViewModel = new ViewModelProvider(this).get(InfoStateViewModel.class);
        secMeasuresViewModel = new ViewModelProvider(this).get(SecMeasuresViewModel.class);
        critInfoViewModel = new ViewModelProvider(this).get(CritInfoViewModel.class);
        busAdvancementsViewModel = new ViewModelProvider(this).get(BusAdvancementsViewModel.class);
        statsViewModel = new ViewModelProvider(this).get(StatsViewModel.class);
        //give them the game class
        infoStateViewModel.setGameClass(gameClass);
        secMeasuresViewModel.setGameClass(gameClass);
        critInfoViewModel.setGameClass(gameClass);
        busAdvancementsViewModel.setGameClass(gameClass);
        statsViewModel.setGameClass(gameClass);
        //set the basic layout
        setContentView(R.layout.activity_game);
        //declare the pause button
        FloatingActionButton fab = findViewById(R.id.fab);
        //set the day
        MutableLiveData<Integer> day = gameClass.getDay();
        TextView dayText = findViewById(R.id.day);
        dayText.setText("Day: "+ day.getValue());

        //set the initial money
        MutableLiveData<ArrayList<Double>> currentMoney = gameClass.getCurrentFunds();
        TextView spendableMoney = findViewById(R.id.spendableMoneyText);
        spendableMoney.setText("$ "+ currentMoney.getValue().get(currentMoney.getValue().size()-1));


        //setup the chart
       LineChart chart;

        {   // // Chart Style // //
            chart = (LineChart) findViewById(R.id.chart1);

            // background color
            chart.setBackgroundColor(Color.WHITE);

            // disable description text
            chart.getDescription().setEnabled(false);

            // enable touch gestures
            chart.setTouchEnabled(true);

            // set listeners

            chart.setDrawGridBackground(true);




            // enable scaling and dragging
            chart.setDragEnabled(true);
            chart.setScaleEnabled(true);
            // chart.setScaleXEnabled(true);
            // chart.setScaleYEnabled(true);

            // force pinch zoom along both axis
            chart.setPinchZoom(true);
        }

        XAxis xAxis;
        {   // // X-Axis Style // //
            xAxis = chart.getXAxis();
            xAxis.setGranularity(1.0f);

            int min = currentMoney.getValue().size()-8;
            int max = currentMoney.getValue().size()-1;

            if(min < 0){
                min = 0;
            }

            xAxis.setAxisMinimum(min);
            xAxis.setAxisMaximum(max);

            // vertical grid lines
            xAxis.enableGridDashedLine(10f, 10f, 0f);
        }

        YAxis yAxis;
        {   // // Y-Axis Style // //
            yAxis = chart.getAxisLeft();

            // disable dual axis (only use LEFT axis)
            chart.getAxisRight().setEnabled(false);

            // horizontal grid lines
            yAxis.enableGridDashedLine(10f, 10f, 0f);

            // axis range
            if(currentMoney.getValue().size()>8){
                yAxis.setAxisMinimum((float)(double)currentMoney.getValue().get(currentMoney.getValue().size() - 8) - 20);
            }else{
                yAxis.setAxisMinimum(0.0f);
            }

        }

        //populate the array of datapoints for the chart
        ArrayList<Entry> values = new ArrayList<>();
        int i = 0;
        ArrayList<Double> money = currentMoney.getValue();
        for(i = 0; i<money.size();i ++){
            values.add(new Entry(i+1,(float)(double) money.get(i)));
        }

        LineDataSet set1;
        //update the dataset
        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            set1.notifyDataSetChanged();
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, "DataSet 1");

            set1.setDrawIcons(false);

            // draw dashed line
            set1.enableDashedLine(10f, 5f, 0f);

            // black lines and points
            set1.setColor(Color.BLACK);
            set1.setCircleColor(Color.BLACK);

            // line thickness and point size
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);

            // draw points as solid circles
            set1.setDrawCircleHole(false);

            // customize legend entry
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);

            // text size of values
            set1.setValueTextSize(9f);

            // draw selection line as dashed
            set1.enableDashedHighlightLine(10f, 5f, 0f);

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1); // add the data sets

            // create a data object with the data sets
            LineData data = new LineData(dataSets);

            // set data
            chart.setData(data);
        }
        // draw points over time
        chart.animateX(300);


        // get the legend (only possible after setting data) and DISABLE IT
        Legend l = chart.getLegend();
        l.setEnabled(false);

        //setup the day progress indicator
        LinearProgressIndicator dayProgress = (LinearProgressIndicator) findViewById(R.id.dayProgress);

        //Loop the game!
        Timer gameTimer = new Timer();
        TimerTask gameUpdate = new TimerTask() {
            @Override
            public void run() {
                //check if paused
                if(!paused){
                    //tick every 200ms and send an update to the game thread
                    if(dayProgress.getProgress() < dayProgress.getMax()) {
                        dayProgress.setProgressCompat(dayProgress.getProgress() + 2, true);
                        gameClass.updater();
                    }else{
                        //every new day we update all new values like day and add money
                        day.postValue(day.getValue() + 1);
                        dayProgress.setProgressCompat(dayProgress.getMin(),true);
                        ArrayList<Double> money = currentMoney.getValue();
                        //update the amount of money to the next day
                        money.add(currentMoney.getValue().get(currentMoney.getValue().size() - 1));
                        currentMoney.postValue(money);
                    }
                }

            }
        };
        gameTimer.scheduleAtFixedRate(gameUpdate,0,200);

        //setup the pause button
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paused = !paused;
                Intent intent = new Intent(view.getContext(), SettingsActivity.class);
                startActivity(intent);
                paused = !paused;
            }
        });

        //on change of day, set the day text
        day.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                dayText.setText("Day: " + integer);
            }
        });

        //all the stuff that happens when the money updates
        currentMoney.observe(this,new Observer<ArrayList<Double>>() {
            @Override
            public void onChanged(ArrayList<Double> changedValue) {
                //change spendable money
                TextView spendableMoney = findViewById(R.id.spendableMoneyText);
                DecimalFormat df2 = new DecimalFormat("#0.00");
                spendableMoney.setText("$ "+ df2.format(changedValue.get(changedValue.size() - 1)));

                //logic for chart changing
                ArrayList<Entry> values = new ArrayList<>();

                int i = 0;
                ArrayList<Double> money = changedValue;
                for(i = 0; i<money.size();i ++){
                    values.add(new Entry(i+1,(float)(double) money.get(i)));
                }

                //readd all data to a dataset
                LineDataSet set1;

                set1 = (LineDataSet) chart.getData().getDataSetByIndex(0);
                set1.setValues(values);
                set1.notifyDataSetChanged();

                XAxis xAxis;
                {   // // X-Axis Style // //
                    xAxis = chart.getXAxis();

                    int min = changedValue.size()-8;
                    int max = changedValue.size()-1;

                    if(min < 0){
                        min = 0;
                    }

                    xAxis.setAxisMinimum(min);
                    xAxis.setAxisMaximum(max);

                    // vertical grid lines
                    xAxis.enableGridDashedLine(10f, 10f, 0f);
                }
                YAxis yAxis;
                {   // // Y-Axis Style // //
                    yAxis = chart.getAxisLeft();

                    if(currentMoney.getValue().size()>8){
                        yAxis.setAxisMinimum((float)(double)currentMoney.getValue().get(currentMoney.getValue().size() - 8) - 20);
                    }else{
                        yAxis.setAxisMinimum(0.0f);
                    }

                }

                chart.getData().notifyDataChanged();
                chart.notifyDataSetChanged();

                // draw points over time
                chart.animateX(200);
            }
        });


        //add the drawer
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        //add the navigation logic
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.critInfo, R.id.infoState, R.id.secMeas, R.id.genBusUpgrades)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);



        NavigationUI.setupWithNavController(navigationView, navController);


    }

    public void hideDescription()
    {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                ((TextView) findViewById(R.id.upgrade_description)).setVisibility(View.GONE);
            }
        });
    }

    public void displayUpgrades(View root, ArrayList<Upgrade> upgrades, int[] ids, int[] pBars)
    {
        for(int i = 0; i < upgrades.size(); i++)
        {
            ((TextView) root.findViewById(ids[i])).setText(String.format("%s\nCost: $%d", upgrades.get(i).getName(), upgrades.get(i).getCost()));
            LinearProgressIndicator upgradeProgress = (LinearProgressIndicator) root.findViewById(pBars[i]);
            upgradeProgress.setMax(Upgrade.MAX_LEVEL);
            upgradeProgress.setMin(0);
            upgradeProgress.setProgressCompat(upgrades.get(i).getLevel(), true);

        }
    }

    public void manageUpgrades(View root, MutableLiveData<ArrayList<Upgrade>> upgradeList, int[] ids, int[] pBars, int[] uCards)
    {
        ArrayList<Upgrade> upgrades = upgradeList.getValue();

        displayUpgrades(root, upgrades, ids, pBars);

        CardView upgradeListener = root.findViewById(uCards[0]);

        upgradeListener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buyUpgrade(root, upgradeList, pBars, 0);
            }
        });

        upgradeListener.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showDescription(root, upgradeList, 0);
                return true;
            }
        });

        upgradeListener = root.findViewById(uCards[1]);

        upgradeListener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buyUpgrade(root, upgradeList, pBars, 1);
            }
        });

        upgradeListener.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showDescription(root, upgradeList, 1);
                return true;
            }
        });

        upgradeListener = root.findViewById(uCards[2]);

        upgradeListener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buyUpgrade(root, upgradeList, pBars, 2);
            }
        });

        upgradeListener.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showDescription(root, upgradeList, 2);
                return true;
            }
        });

        upgradeListener = root.findViewById(uCards[3]);

        upgradeListener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buyUpgrade(root, upgradeList, pBars, 3);
            }
        });

        upgradeListener.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showDescription(root, upgradeList, 3);
                return true;
            }
        });
    }

    public void buyUpgrade(View root, MutableLiveData<ArrayList<Upgrade>> upgradeList, int [] pBars, int upCount)
    {
        ArrayList<Upgrade> upgrades = upgradeList.getValue();
        Upgrade u = upgrades.get(upCount);

        if(Game.getTheCurrentFunds() >= u.getCost() && u.getLevel() < Upgrade.MAX_LEVEL)
        {
            Game.spendMoney(u.getCost()); // spend the money
            u.levelUp(); // should call the observer to change the logic for
            upgradeList.postValue(upgrades);

            LinearProgressIndicator upgradeProgress = (LinearProgressIndicator) root.findViewById(pBars[upCount]);
            upgradeProgress.setProgressCompat(upgrades.get(upCount).getLevel(), true);
        }
    }

    public void showDescription(View root, MutableLiveData<ArrayList<Upgrade>> upgradeList, int upCount)
    {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                ArrayList<Upgrade> upgrades = upgradeList.getValue();
                Upgrade u = upgrades.get(upCount);

                TextView uDescrip = (TextView) findViewById(R.id.upgrade_description);

                uDescrip.setVisibility(View.VISIBLE);
                uDescrip.setText(u.getDescription());
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.game, menu);
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onStop() {
        super.onStop();
        gameClass.saveAll();

    }

    public void onSuccessfulAttack(ArrayList<String> attackInfo, double cost) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder alert = new AlertDialog.Builder(getApplicationContext());
                alert.setMessage("Oh no! A " + attackInfo.get(0) + " was perpetrated against your" +
                        " organization! The damages total to $" + cost + ".")
                        .setNeutralButton("Darn!", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // empty for now. all it needs to do is close the alert

                            }
                        });
                paused = !paused;
                alert.show();
                paused = !paused;
            }
        });
    }


    public void onPreventedAttack() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder alert = new AlertDialog.Builder(getApplicationContext());
                alert.setMessage("One of your countermeasures prevented an attack! Great job!")
                        .setNeutralButton("Nice!", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // empty for now. all it needs to do is close the alert

                            }
                        });
                paused = !paused;
                alert.show();
                paused = !paused;
            }
        });

        }

    }
