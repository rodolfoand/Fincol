package com.fatec.fincol.ui.home;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.fatec.fincol.model.User;
import com.fatec.fincol.repository.UserRepository;

public class HomeFragViewModel extends AndroidViewModel {

    private MutableLiveData<String> mText;
    private UserRepository mRepository;

    public HomeFragViewModel(Application application) {
        super(application);

        mRepository = new UserRepository();

        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<User> getUser(){
        return mRepository.mUser;
    }
}