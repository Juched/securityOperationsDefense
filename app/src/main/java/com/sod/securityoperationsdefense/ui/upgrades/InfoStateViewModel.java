package com.sod.securityoperationsdefense.ui.upgrades;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class InfoStateViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public InfoStateViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Information State Upgrades");
    }

    public LiveData<String> getText() {
        return mText;
    }
}