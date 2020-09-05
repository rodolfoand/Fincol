package com.fatec.fincol;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.fatec.fincol.repository.UserRepository;

public class MainViewModel extends AndroidViewModel {

    private UserRepository mRepository;
    public LiveData<Boolean> isSignIn;

    public MainViewModel(Application application) {
        super(application);
        mRepository = new UserRepository();
        this.isSignIn = mRepository.isSignIn;
    }

    public void setIsSignIn() {
        mRepository.setIsSignIn();
    }
}
