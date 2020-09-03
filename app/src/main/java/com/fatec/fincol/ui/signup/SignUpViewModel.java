package com.fatec.fincol.ui.signup;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.fatec.fincol.repository.UserRepository;

public class SignUpViewModel extends AndroidViewModel {

    private UserRepository mRepository;

    public SignUpViewModel(Application application) {
        super(application);
        mRepository = new UserRepository();
    }

    public void signUp(String email, String password, String name){
        mRepository.signUp(email, password, name);
    }
}
