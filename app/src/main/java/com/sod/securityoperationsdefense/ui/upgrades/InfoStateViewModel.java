package com.sod.securityoperationsdefense.ui.upgrades;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sod.securityoperationsdefense.Game;

public class InfoStateViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private Game gameClass;
    public InfoStateViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Information State Upgrades");
    }
    public void setGameClass(Game mgameClass){
        this.gameClass = mgameClass;

    }

    public Game getGameClass(){
        return this.gameClass;
    }

    public LiveData<String> getText() {
        return mText;
    }
}