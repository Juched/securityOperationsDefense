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

import java.util.ArrayList;

public class CritInfoFragment extends Fragment {
    private CritInfoViewModel criticalInfoViewModel;
    private Game gameClass;

    /* view created for Critical Information upgrades */
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        criticalInfoViewModel = new ViewModelProvider(this).get(CritInfoViewModel.class);
        View root = inflater.inflate(R.layout.fragment_crit_info_sec, container, false);


        while(gameClass == null)
        {
            gameClass = BusAdvancementsViewModel.getGameClass();
        }

        MutableLiveData<ArrayList<Upgrade>> upgrades = gameClass.getCritInfoUpgrades();

        int[] ids = new int[]{R.id.upgradeOneName, R.id.upgradeTwoName, R.id.upgradeThreeName, R.id.upgradeFourName};
        int[] pBars = new int[]{R.id.upgradeOneProgress, R.id.upgradeTwoProgress, R.id.upgradeThreeProgress, R.id.upgradeFourProgress};
        int[] uCards = new int[]{R.id.upgradeOneCard, R.id.upgradeTwoCard, R.id.upgradeThreeCard, R.id.upgradeFourCard};

        gameClass.getCritInfoUpgrades().observe(this.gameClass.getGameForContext(),new Observer<ArrayList<Upgrade>>() {
            /**
             * Called when the data is changed.
             *
             * @param upgrades The new data
             */
            @Override
            public void onChanged(ArrayList<Upgrade> upgrades) {
                gameClass.getGameForContext().displayUpgrades(root, upgrades, ids, pBars);
            }
        });

        gameClass.getGameForContext().manageUpgrades(root,upgrades,ids,pBars,uCards);
        this.gameClass.getGameForContext().hideDescription();


        return root;
    }

    /* view updated for Critical Information upgrades */
    public View onUpdate(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        criticalInfoViewModel = new ViewModelProvider(this).get(CritInfoViewModel.class);
        View root = inflater.inflate(R.layout.fragment_crit_info_sec, container, false);



        return root;
    }



}
