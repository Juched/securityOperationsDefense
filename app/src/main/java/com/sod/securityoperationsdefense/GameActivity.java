package com.sod.securityoperationsdefense;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.Window;
import android.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.sod.securityoperationsdefense.ui.upgrades.BusAdvancementsFragment;
import com.sod.securityoperationsdefense.ui.upgrades.CritInfoFragment;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        paused = false;
        //get the shared preferences file manager
        gameClass = new Game(getApplicationContext(), this);
        setContentView(R.layout.activity_game);

        FloatingActionButton fab = findViewById(R.id.fab);

        MutableLiveData<Integer> day = gameClass.getDay();
        TextView dayText = findViewById(R.id.day);
        dayText.setText("Day: "+ day.getValue());

        MutableLiveData<ArrayList<Double>> currentMoney = gameClass.getCurrentFunds();
        TextView spendableMoney = findViewById(R.id.spendableMoneyText);
        spendableMoney.setText("$ "+ currentMoney.getValue().toString());


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
                        gameClass.updater();
                    }
                }

            }
        };
        gameTimer.scheduleAtFixedRate(gameUpdate,0,200);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Double> now = currentMoney.getValue();
                now.set(0, now.get(0) + 10.0);
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
                spendableMoney.setText("$ "+ df2.format(changedValue.get(0)));
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

        infoStateViewModel = new ViewModelProvider(this).get(InfoStateViewModel.class);
        infoStateViewModel.setGameClass(gameClass);
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

    public void onSuccessfulAttack(ArrayList<String> attackInfo) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getApplicationContext());
        alert.setMessage("Oh no! A " + attackInfo.get(0) + " was perpetrated against your" +
                    " organization! The damages total to $" + attackInfo.get(2) + ".")
                .setNeutralButton("Darn!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // empty for now. all it needs to do is close the alert
                    }
                });
        alert.show();
    }


    public void onPreventedAttack() {
        AlertDialog.Builder alert = new AlertDialog.Builder(getApplicationContext());
        alert.setMessage("One of your countermeasures prevented an attack! Great job!")
            .setNeutralButton("Nice!", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // empty for now. all it needs to do is close the alert
                }
            });
        alert.show();
        }

    }
