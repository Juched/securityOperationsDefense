package com.sod.securityoperationsdefense.ui.upgrades;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.sod.securityoperationsdefense.Game;
import com.sod.securityoperationsdefense.GameActivity;
import com.sod.securityoperationsdefense.R;
import com.sod.securityoperationsdefense.Upgrade;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class StatsFragment extends Fragment {
    private StatsViewModel statsViewModel;
    private Game gameclass;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        statsViewModel = new ViewModelProvider(this).get(StatsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_stats, container, false);

        gameclass = StatsViewModel.getGameClass();

        //Update the attack rate on creation
        double attackRate = gameclass.attackRate.getValue();
        double attackChance = (1 - (Math.pow(1-attackRate,50))) * 100.00;
        TextView attackRText = (TextView) root.findViewById(R.id.attackRate);
        DecimalFormat df2 = new DecimalFormat("#0.0#");
        attackRText.setText(df2.format(attackChance) + "%/day");

        int[][] ids = new int[][]{
                {R.id.att1,R.id.att1t},
                {R.id.att2,R.id.att2t},
                {R.id.att3,R.id.att3t},
                {R.id.att4,R.id.att4t},
                {R.id.att5,R.id.att5t},
                {R.id.att6,R.id.att6t}
        };

        for(int[] e: ids){

        }

        return root;
    }



}
