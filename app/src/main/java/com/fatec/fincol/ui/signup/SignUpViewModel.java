package com.fatec.fincol.ui.signup;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.fatec.fincol.repository.UserRepository;

public class SignUpViewModel extends AndroidViewModel {

    private UserRepository mRepository;
    public LiveData<Boolean> isSignIn;

    public SignUpViewModel(Application application) {
        super(application);
        mRepository = new UserRepository();
        this.isSignIn = mRepository.isSignIn;
    }

    public LiveData<String> signUp(String email, String password, String name){
        return mRepository.signUp(email, password, name);
    }
}
