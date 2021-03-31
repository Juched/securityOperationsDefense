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


public class GameActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private Game gameClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //get the shared preferences file manager
        SharedPreferences sharedPreferences = this.getSharedPreferences("SOD.Gamefile", Context.MODE_PRIVATE);
        // check here if we already have a saved game state open
        try {
            gameClass = (Game) ObjectSerializer.deserialize(
                    sharedPreferences.getString("game", ObjectSerializer.serialize(new Game())));
        } catch (IOException | ClassNotFoundException e) {
            //no game exists
            gameClass = new Game();

        }
        setContentView(R.layout.activity_game);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        //toolbar.setVisibility(View.GONE);
        //getSupportActionBar().hide();
        FloatingActionButton fab = findViewById(R.id.fab);
        MutableLiveData<Double> currentMoney = gameClass.getCurrentFunds();
        TextView spendableMoney = findViewById(R.id.spendableMoneyText);
        spendableMoney.setText("$ "+ currentMoney.getValue().toString());


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double now = currentMoney.getValue();
                now = now + 10;
                currentMoney.setValue(now);
            }
        });

        currentMoney.observe(this,new Observer<Double>() {
            @Override
            public void onChanged(Double changedValue) {
                TextView spendableMoney = findViewById(R.id.spendableMoneyText);
                spendableMoney.setText("$ "+ changedValue.toString());
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
        SharedPreferences sharedPreferences = this.getSharedPreferences("SOD.Gamefile", Context.MODE_PRIVATE);

    try{
        sharedPreferences.edit().putString("game",
                ObjectSerializer.serialize(gameClass)).apply();
        sharedPreferences.edit().commit();
    } catch (IOException e) {
        e.printStackTrace();
        Log.e("SOD", "Couldn't create a file");
    }
    }

}