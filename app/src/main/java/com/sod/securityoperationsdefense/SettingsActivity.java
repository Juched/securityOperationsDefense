package com.sod.securityoperationsdefense;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.SeekBar;

public class SettingsActivity extends AppCompatActivity {
    // variables needed throughout this and other classes
    static CheckBox music;
    static boolean musicOn = true;
    static SeekBar seekbar = null;
    private AudioManager manager;
    @Override
    // defines what happens when the settings page is loaded - gives each button or interactive
    // object the properties needed to perform the action that the user expects
    protected void onCreate(Bundle savedInstanceState) {
        // load the preferences file and display the correct layout
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = this.getSharedPreferences("SOD.Gamefile", Context.MODE_PRIVATE);
        setContentView(R.layout.activity_settings);

        // define the action for the arrow - return to the previous page
        ImageView arrow = findViewById(R.id.imageView3);
        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // define the action for the instructions button - show the instructions page
        Button instructions = findViewById(R.id.button);
        instructions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), InstructionsActivity.class);
                startActivity(intent);
            }
        });

        // define the actions to be taken if the user confirms or denies their reset request
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        // clear all of the stored preferences for the game to give the
                        // user a "clean slate" and restore the state of the game
                        // to the beginning
                        sharedPreferences.edit().remove("currFunds").apply();
                        sharedPreferences.edit().remove("payR").apply();
                        sharedPreferences.edit().remove("payD").apply();
                        sharedPreferences.edit().remove("day").apply();
                        sharedPreferences.edit().remove("attackR").apply();
                        sharedPreferences.edit().remove("preventionRs").apply();
                        sharedPreferences.edit().remove("busUpgrades").apply();
                        sharedPreferences.edit().remove("critUpgrades").apply();
                        sharedPreferences.edit().remove("secUpgrades").apply();
                        sharedPreferences.edit().remove("infoUpgrades").apply();
                        sharedPreferences.edit().commit();

                        // return to the homescreen of the app
                        Intent intent = new Intent(getApplicationContext(),FullscreenActivity.class);
                        startActivity(intent);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        // define the action for a user making a reset request - show the dialog
        // box asking them to confirm or deny their request
        Button resetButton = findViewById(R.id.button2);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();


            }
        });

        // define the action for the music on box being checked - if the box is checked,
        // set the music on variable to true (to be used in the FullscreenActivity class)
        music = findViewById(R.id.checkBox);
        music.setChecked(musicOn);
        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (music.isChecked()) {
                    musicOn = true;
                } else {
                    musicOn = false;
                }
                FullscreenActivity.playMusic(musicOn);
            }
        });

        // define action for using the sliding volume bar
        seekbar = findViewById(R.id.seekBar);
        // set up an AudioManager to control the device audio with the seek bar
        manager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);

        // define the maximum device volume and the current device volume
        // set the slider to the position of the current device volume
        seekbar.setMax(manager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        seekbar.setProgress(manager.getStreamVolume(AudioManager.STREAM_MUSIC));

        // define what happens when the bar is moved - when the position changes, update
        // the volume accordingly
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                manager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, AudioManager.FLAG_PLAY_SOUND);
            }

            // the following methods are required, but intentionally left empty - nothing should
            // happen at the beginning/end of the user sliding the bar
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

}