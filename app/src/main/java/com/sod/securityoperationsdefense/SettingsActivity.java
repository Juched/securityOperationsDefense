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
    static CheckBox music;
    static boolean musicOn = true;
    static SeekBar seekbar = null;
    private AudioManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = this.getSharedPreferences("SOD.Gamefile", Context.MODE_PRIVATE);
        setContentView(R.layout.activity_settings);

        ImageView arrow = findViewById(R.id.imageView3);
        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button instructions = findViewById(R.id.button);

        instructions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), InstructionsActivity.class);
                startActivity(intent);
            }
        });

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        sharedPreferences.edit().remove("currFunds").apply();
                        sharedPreferences.edit().remove("payR").apply();
                        sharedPreferences.edit().remove("payD").apply();
                        sharedPreferences.edit().remove("day").apply();
                        sharedPreferences.edit().remove("attackR").apply();
                        sharedPreferences.edit().remove("preventionRs").apply();
                        sharedPreferences.edit().commit();


                        Intent intent = new Intent(getApplicationContext(),FullscreenActivity.class);
                        startActivity(intent);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        Button resetButton = findViewById(R.id.button2);

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });

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

        seekbar = findViewById(R.id.seekBar);
        manager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);

        seekbar.setMax(manager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        seekbar.setProgress(manager.getStreamVolume(AudioManager.STREAM_MUSIC));

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                manager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, AudioManager.FLAG_PLAY_SOUND);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

}