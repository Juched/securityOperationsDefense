//Andrew Whiteman and Parth Parekh
package com.sod.securityoperationsdefense.ui.upgrades;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sod.securityoperationsdefense.Game;

public class StatsViewModel extends ViewModel {


    private static Game gameClass;

    public StatsViewModel() {

    }

    public void setGameClass(Game mgameClass){
        StatsViewModel.gameClass = mgameClass;


    }

    public static Game getGameClass(){
        return StatsViewModel.gameClass;
    }


}