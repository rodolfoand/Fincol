package com.fatec.fincol.ui.statement;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class StatementViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public StatementViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is statement fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}