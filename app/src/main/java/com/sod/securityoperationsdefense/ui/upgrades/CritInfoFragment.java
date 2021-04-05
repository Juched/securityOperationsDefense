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

import java.util.ArrayList;

public class CritInfoFragment extends Fragment {
    private CritInfoViewModel criticalInfoViewModel;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        criticalInfoViewModel = new ViewModelProvider(this).get(CritInfoViewModel.class);
        View root = inflater.inflate(R.layout.fragment_crit_info_sec, container, false);


//        while(gameClass == null)
//        {
//            gameClass = CritInfoViewModel.getGameClass();
//        }
//
//        gameClass = CritInfoViewModel.getGameClass();
//        TableLayout upgrades = container.findViewById(R.id.crit_upgrades_list);
//        upgrades.removeAllViewsInLayout();
//
//        gameClass.getCritInfoUpgrades().observe(getViewLifecycleOwner(), new Observer<ArrayList<CardView>>() {
//            @Override
//            public void onChanged(ArrayList<CardView> cardViews) {
//                // Only four cards...
//                for(int i = 0; i < cardViews.size(); i+=2)
//                {
//                    try{
//                        TableRow newRow = new TableRow(gameClass.getGameForContext());
//
//                        newRow.addView(cardViews.get(i));
//                        newRow.addView(cardViews.get(i+1));
//
//                        upgrades.addView(newRow);
//                    } catch (Exception e) {
//
//                    }
//                }
//            }
//        });
//
//        ArrayList<CardView> cardViews = gameClass.getCritInfoUpgrades().getValue();
//        for(int i = 0; i < cardViews.size(); i+=2)
//        {
//            try{
//                TableRow newRow = new TableRow(gameClass.getGameForContext());
//
//                newRow.addView(cardViews.get(i));
//                newRow.addView(cardViews.get(i+1));
//
//                upgrades.addView(newRow);
//            } catch (Exception e) {
//
//            }
//        }

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
