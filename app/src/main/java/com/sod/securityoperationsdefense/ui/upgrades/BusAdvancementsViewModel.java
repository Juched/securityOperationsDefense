package com.sod.securityoperationsdefense.ui.upgrades;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BusAdvancementsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public BusAdvancementsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("General Business Advancements Upgrades");
    }

    public LiveData<String> getText() {
        return mText;
    }
}