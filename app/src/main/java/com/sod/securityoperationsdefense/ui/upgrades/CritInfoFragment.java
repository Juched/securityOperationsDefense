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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.sod.securityoperationsdefense.Game;
import com.sod.securityoperationsdefense.R;
import com.sod.securityoperationsdefense.Upgrade;

import java.util.ArrayList;

public class CritInfoFragment extends Fragment {
    private CritInfoViewModel criticalInfoViewModel;
    private Game gameClass;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        criticalInfoViewModel = new ViewModelProvider(this).get(CritInfoViewModel.class);
        View root = inflater.inflate(R.layout.fragment_crit_info_sec, container, false);


        while(gameClass == null)
        {
            gameClass = SecMeasuresViewModel.getGameClass();
        }

        ArrayList<Upgrade> upgrades = gameClass.getCritInfoUpgrades().getValue();

        int[] ids = new int[]{R.id.upgradeOneName, R.id.upgradeTwoName, R.id.upgradeThreeName, R.id.upgradeFourName};

        for(int i = 0; i < upgrades.size(); i++)
        {
            ((TextView) root.findViewById(ids[i])).setText(String.format("%s\nCost: $%d", upgrades.get(i).getName(), upgrades.get(i).getCost()));
        }

        return root;

    }

    public View onUpdate(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        criticalInfoViewModel = new ViewModelProvider(this).get(CritInfoViewModel.class);
        View root = inflater.inflate(R.layout.fragment_crit_info_sec, container, false);



        return root;
    }

    /* */
    public void changeList(@NonNull LayoutInflater inflater, ViewGroup container, @IdRes int id)
    {
        /** /
        FrameLayout rootLayout = (FrameLayout)findViewById(android.R.id.content);
        rootLayout.removeViewAt(rootLayout.getChildCount()-1);
        View.inflate(this, R.layout.overlay_layout, rootLayout);
        /** /
        View insertPoint =(View) inflater.inflate(R.layout.upgrade_list, container, false).findViewById(id); // edited.
        insertPoint.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
        /**/
    }
    /**/

}
