package com.sod.securityoperationsdefense.ui.upgrades;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CritInfoViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CritInfoViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Critical Information Upgrades");
    }

    public LiveData<String> getText() {
        return mText;
    }
}