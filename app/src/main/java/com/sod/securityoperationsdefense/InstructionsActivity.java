//Jillian Zaffiro
package com.sod.securityoperationsdefense;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class InstructionsActivity extends AppCompatActivity {
    @Override
    // define what happens when the instruction screen is loaded into view
    protected void onCreate(Bundle savedInstanceState) {
        // display the correct layout and load all of the saved state info
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = this.getSharedPreferences("SOD.Gamefile", Context.MODE_PRIVATE);
        setContentView(R.layout.activity_instructions);

        // define what happens when the arrow is clicked - return to the settings
        // main page (since the instructions can only be accessed from settings)
        ImageView arrow = findViewById(R.id.instArrow);
        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}
