//Andrew Whiteman and Parth Parekh
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
import java.util.HashMap;
//the stats view
public class StatsFragment extends Fragment {
    private StatsViewModel statsViewModel;
    private Game gameclass;
    //when the view is created
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        statsViewModel = new ViewModelProvider(this).get(StatsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_stats, container, false);
        //grab the game class
        gameclass = StatsViewModel.getGameClass();

        //Update the attack rate on creation
        double attackRate = gameclass.attackRate.getValue();
        double attackChance = (1 - (Math.pow(1-attackRate,50))) * 100.00;
        TextView attackRText = (TextView) root.findViewById(R.id.attackRate);
        DecimalFormat df2 = new DecimalFormat("#0.0#");
        attackRText.setText(df2.format(attackChance) + "%/day");
        //ids of all of the views
        int[][] ids = new int[][]{
                {R.id.att1t,R.id.att1t2},
                {R.id.att2t,R.id.att2t2},
                {R.id.att3t,R.id.att3t2},
                {R.id.att4t,R.id.att4t2},
                {R.id.att5t,R.id.att5t2},
                {R.id.att6t,R.id.att6t2}
        };
        //get the attacks
        HashMap<Integer, ArrayList<String>> attackList = gameclass.getAttInfo();

        //loop through everything and update
        int i = 0;
        for(int[] e: ids){
            //set title
            TextView title = root.findViewById(e[0]);
            TextView info = root.findViewById(e[1]);
            ArrayList<String> attack = attackList.get(i);

            title.setText(attack.get(0));
            //get the amount that each attack will cost you and display
            double cost = Double.parseDouble(attack.get(2)) * gameclass.getCurrentFunds().getValue().get(gameclass.getCurrentFunds().getValue().size() - 1);

            info.setText("Cost: $" + df2.format(cost));
            i = i + 1;
        }

        return root;
    }



}
