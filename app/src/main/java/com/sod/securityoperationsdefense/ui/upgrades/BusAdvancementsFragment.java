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

        MutableLiveData<ArrayList<Upgrade>> upgrades = gameClass.getBusUpgrades();

        int[] ids = new int[]{R.id.upgradeOneName, R.id.upgradeTwoName, R.id.upgradeThreeName, R.id.upgradeFourName};
        int[] pBars = new int[]{R.id.upgradeOneProgress, R.id.upgradeTwoProgress, R.id.upgradeThreeProgress, R.id.upgradeFourProgress};
        int[] uCards = new int[]{R.id.upgradeOneCard, R.id.upgradeTwoCard, R.id.upgradeThreeCard, R.id.upgradeFourCard};

        gameClass.getBusUpgrades().observe(this.gameClass.getGameForContext(),new Observer<ArrayList<Upgrade>>() {
            /**
             * Called when the data is changed.
             *
             * @param upgrades The new data
             */
            @Override
            public void onChanged(ArrayList<Upgrade> upgrades) {
                GameActivity.displayUpgrades(root, upgrades, ids, pBars);
            }
        });



        GameActivity.manageUpgrades(root,upgrades,ids,pBars,uCards);

        return root;
    }




    public View onUpdate(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        busAdvViewModel = new ViewModelProvider(this).get(BusAdvancementsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_bus_adv, container, false);

        return root;
    }

}
