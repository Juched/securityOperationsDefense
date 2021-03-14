package com.sod.securityoperationsdefense.ui.upgrades;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UpgradeListViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public UpgradeListViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Upgrades fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}