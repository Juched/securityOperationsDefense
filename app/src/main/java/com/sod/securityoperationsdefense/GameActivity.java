package com.sod.securityoperationsdefense;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.Window;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.SharedPreferences;
import android.widget.TextView;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;


public class GameActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private Game gameClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //get the shared preferences file manager
        gameClass = new Game(getApplicationContext());
        setContentView(R.layout.activity_game);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        //toolbar.setVisibility(View.GONE);
        //getSupportActionBar().hide();
        FloatingActionButton fab = findViewById(R.id.fab);
        MutableLiveData<ArrayList<Double>> currentMoney = gameClass.getCurrentFunds();
        TextView spendableMoney = findViewById(R.id.spendableMoneyText);
        spendableMoney.setText("$ "+ currentMoney.getValue().toString());


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Double> now = currentMoney.getValue();
                now.set(0, now.get(0) + 10.0);
                currentMoney.setValue(now);
            }
        });

        currentMoney.observe(this,new Observer<ArrayList<Double>>() {
            @Override
            public void onChanged(ArrayList<Double> changedValue) {
                TextView spendableMoney = findViewById(R.id.spendableMoneyText);
                spendableMoney.setText("$ "+ changedValue.get(0).toString());
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
//        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
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