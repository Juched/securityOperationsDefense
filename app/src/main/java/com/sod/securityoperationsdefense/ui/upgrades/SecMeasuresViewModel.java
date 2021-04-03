package com.sod.securityoperationsdefense.ui.upgrades;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SecMeasuresViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SecMeasuresViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Security Measures Upgrades");
    }

    public LiveData<String> getText() {
        return mText;
    }
}