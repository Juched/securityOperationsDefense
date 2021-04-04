package com.sod.securityoperationsdefense.ui.upgrades;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sod.securityoperationsdefense.Game;

public class SecMeasuresViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private static Game gameClass;

    public SecMeasuresViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Security Measures Upgrades");
    }

    public void setGameClass(Game mgameClass){
        SecMeasuresViewModel.gameClass = mgameClass;

    }

    public static Game getGameClass(){
        return SecMeasuresViewModel.gameClass;
    }

    public LiveData<String> getText() {
        return mText;
    }
}