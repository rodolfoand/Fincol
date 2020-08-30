package com.fatec.fincol.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.fatec.fincol.repository.UserRepository;

public class UserViewModel extends AndroidViewModel {

    private UserRepository mRepository;
    public LiveData<String> mUser;

    public UserViewModel(Application application) {
        super(application);
        mRepository = new UserRepository();
    }

    public void createUser(String email, String password){
        mRepository.createUser(email, password);
    }

    public LiveData<Boolean> isSignedIn(){
        return mRepository.isSignedIn();
    }

    public void signIn(String email, String password){
        mRepository.signIn(email, password);
    }

    public String getName(){
        return mRepository.getName();
    }

    public void signOut(){
        mRepository.signOut();
    }

}
