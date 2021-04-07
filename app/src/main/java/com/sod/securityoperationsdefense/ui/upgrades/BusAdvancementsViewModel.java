//Andrew Whiteman and Parth Parekh
package com.sod.securityoperationsdefense.ui.upgrades;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sod.securityoperationsdefense.Game;

public class BusAdvancementsViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    static private Game gameClass;

    public BusAdvancementsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("General Business Advancements Upgrades");
    }

    public void setGameClass(Game mgameClass){
        BusAdvancementsViewModel.gameClass = mgameClass;

    }

    public static Game getGameClass(){
        return gameClass;
    }

    public LiveData<String> getText() {
        return mText;
    }

}