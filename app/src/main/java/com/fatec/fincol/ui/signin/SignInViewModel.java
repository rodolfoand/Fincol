package com.fatec.fincol.ui.signin;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.fatec.fincol.repository.UserRepository;

public class SignInViewModel extends AndroidViewModel {

    private UserRepository mRepository;
    public LiveData<Boolean> isSignIn;

    public SignInViewModel( Application application) {
        super(application);
        mRepository = new UserRepository();
        this.isSignIn = mRepository.isSignIn;
    }

    public void signIn(String email, String password){
        mRepository.signIn(email, password);
    }

    public void setIsSignIn() {
        mRepository.setIsSignIn();
    }

}
