package com.sod.securityoperationsdefense;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        paused = false;
        //get the shared preferences file manager
        gameClass = new Game(getApplicationContext(), this);

        infoStateViewModel = new ViewModelProvider(this).get(InfoStateViewModel.class);
        secMeasuresViewModel = new ViewModelProvider(this).get(SecMeasuresViewModel.class);
        critInfoViewModel = new ViewModelProvider(this).get(CritInfoViewModel.class);
        busAdvancementsViewModel = new ViewModelProvider(this).get(BusAdvancementsViewModel.class);

        infoStateViewModel.setGameClass(gameClass);
        secMeasuresViewModel.setGameClass(gameClass);
        critInfoViewModel.setGameClass(gameClass);
        busAdvancementsViewModel.setGameClass(gameClass);


        setContentView(R.layout.activity_game);

        FloatingActionButton fab = findViewById(R.id.fab);

        MutableLiveData<Integer> day = gameClass.getDay();
        TextView dayText = findViewById(R.id.day);
        dayText.setText("Day: "+ day.getValue());

        MutableLiveData<ArrayList<Double>> currentMoney = gameClass.getCurrentFunds();
        TextView spendableMoney = findViewById(R.id.spendableMoneyText);
        spendableMoney.setText("$ "+ currentMoney.getValue().get(currentMoney.getValue().size()-1));

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

            yAxis.setAxisMinimum(0f);
        }



        ArrayList<Entry> values = new ArrayList<>();
        int i = 0;
        ArrayList<Double> money = currentMoney.getValue();
        for(i = 0; i<money.size();i ++){
            values.add(new Entry(i,(float)(double) money.get(i)));
        }

        LineDataSet set1;

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


        // get the legend (only possible after setting data)
        Legend l = chart.getLegend();
        l.setEnabled(false);


        LinearProgressIndicator dayProgress = (LinearProgressIndicator) findViewById(R.id.dayProgress);

        Timer gameTimer = new Timer();
        TimerTask gameUpdate = new TimerTask() {
            @Override
            public void run() {
                if(!paused){
                    if(dayProgress.getProgress() < dayProgress.getMax()) {
                        dayProgress.setProgressCompat(dayProgress.getProgress() + 1, true);
                        gameClass.updater();
                    }else{
                        day.postValue(day.getValue() + 1);
                        dayProgress.setProgressCompat(dayProgress.getMin(),true);
                        ArrayList<Double> money = currentMoney.getValue();
                        money.add(currentMoney.getValue().get(currentMoney.getValue().size() - 1));
                        currentMoney.postValue(money);
                    }
                }

            }
        };
        gameTimer.scheduleAtFixedRate(gameUpdate,0,200);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Double> now = currentMoney.getValue();
                now.set(currentMoney.getValue().size() - 1, now.get(currentMoney.getValue().size() - 1) + 10.0);
                currentMoney.setValue(now);
                paused = !paused;

            }
        });

        day.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                dayText.setText("Day: " + integer);
            }
        });

        currentMoney.observe(this,new Observer<ArrayList<Double>>() {
            @Override
            public void onChanged(ArrayList<Double> changedValue) {
                TextView spendableMoney = findViewById(R.id.spendableMoneyText);
                DecimalFormat df2 = new DecimalFormat("#0.00");
                spendableMoney.setText("$ "+ df2.format(changedValue.get(changedValue.size() - 1)));

                ArrayList<Entry> values = new ArrayList<>();

                int i = 0;
                ArrayList<Double> money = changedValue;
                for(i = 0; i<money.size();i ++){
                    values.add(new Entry(i,(float)(double) money.get(i)));
                }

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
                chart.getData().notifyDataChanged();
                chart.notifyDataSetChanged();

                // draw points over time
                chart.animateX(200);
            }
        });



        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.game, menu);
        return true;
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

}