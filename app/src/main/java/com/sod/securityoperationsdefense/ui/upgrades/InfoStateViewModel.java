//Andrew Whiteman and Parth Parekh
package com.sod.securityoperationsdefense.ui.upgrades;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sod.securityoperationsdefense.Game;

public class InfoStateViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private static Game gameClass;
    public InfoStateViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Information State Upgrades");
    }
    public void setGameClass(Game mgameClass){
        InfoStateViewModel.gameClass = mgameClass;

    }

    public static Game getGameClass(){
        return gameClass;
    }

    public LiveData<String> getText() {
        return mText;
    }
}