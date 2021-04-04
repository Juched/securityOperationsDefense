package com.sod.securityoperationsdefense.ui.upgrades;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sod.securityoperationsdefense.Game;

public class CritInfoViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private static Game gameClass;

    public CritInfoViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Critical Information Upgrades");
    }

    public void setGameClass(Game mgameClass){
        CritInfoViewModel.gameClass = mgameClass;

    }

    public static Game getGameClass(){
        return gameClass;
    }

    public LiveData<String> getText() {
        return mText;
    }
}