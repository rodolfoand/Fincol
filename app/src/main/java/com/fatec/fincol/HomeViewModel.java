package com.fatec.fincol;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.fatec.fincol.model.User;
import com.fatec.fincol.repository.UserRepository;

public class HomeViewModel extends AndroidViewModel {

    private UserRepository mRepository;
    public LiveData<Boolean> isSignIn;

    public HomeViewModel(Application application) {
        super(application);
        mRepository = new UserRepository();
        this.isSignIn = mRepository.isSignIn;
    }

    public LiveData<User> getUser(){
        return mRepository.mUser;
    }
}
