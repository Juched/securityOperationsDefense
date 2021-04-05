package com.sod.securityoperationsdefense.ui.upgrades;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import java.util.ArrayList;

import static java.lang.String.*;

public class BusAdvancementsFragment extends Fragment {

    private BusAdvancementsViewModel busAdvViewModel;
    private Game gameClass;
    public void setGameClass(Game mGameClass){
        this.gameClass = mGameClass;
    }
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        busAdvViewModel = new ViewModelProvider(this).get(BusAdvancementsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_bus_adv, container, false);

        while(gameClass == null)
        {
            gameClass = BusAdvancementsViewModel.getGameClass();
        }

        ArrayList<Upgrade> upgrades = gameClass.getBusUpgrades().getValue();

        int[] ids = new int[]{R.id.upgradeOneName, R.id.upgradeTwoName, R.id.upgradeThreeName, R.id.upgradeFourName};
        int[] pBars = new int[]{R.id.upgradeOneProgress, R.id.upgradeTwoProgress, R.id.upgradeThreeProgress, R.id.upgradeFourProgress};
        int[] uCards = new int[]{R.id.upgradeOneCard, R.id.upgradeOneCard, R.id.upgradeOneCard, R.id.upgradeOneCard};

        for(int i = 0; i < upgrades.size(); i++)
        {
            ((TextView) root.findViewById(ids[i])).setText(String.format("%s\nCost: $%d", upgrades.get(i).getName(), upgrades.get(i).getCost()));
            LinearProgressIndicator upgradeProgress = (LinearProgressIndicator) root.findViewById(pBars[i]);
            upgradeProgress.setMax(Upgrade.MAX_LEVEL);
            upgradeProgress.setMin(0);
            upgradeProgress.setProgressCompat(upgrades.get(i).getLevel(), true);

        }

        CardView buyUpgrade = root.findViewById(R.id.upgradeOneCard);

        buyUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameActivity.updateUpgrades(root, upgrades, pBars, 0);
            }
        });

        buyUpgrade = root.findViewById(R.id.upgradeTwoCard);

        buyUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameActivity.updateUpgrades(root, upgrades, pBars, 1);
            }
        });

        buyUpgrade = root.findViewById(R.id.upgradeThreeCard);

        buyUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameActivity.updateUpgrades(root, upgrades, pBars, 2);
            }
        });

        buyUpgrade = root.findViewById(R.id.upgradeFourCard);

        buyUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameActivity.updateUpgrades(root, upgrades, pBars, 3);
            }
        });

        return root;
    }




    public View onUpdate(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        busAdvViewModel = new ViewModelProvider(this).get(BusAdvancementsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_bus_adv, container, false);

//
        return root;
    }

}
