package com.fatec.fincol.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.fatec.fincol.repository.UserRepository;

public class UserViewModel extends AndroidViewModel {

    private UserRepository mRepository;
    public LiveData<Boolean> isSignIn;

    public UserViewModel(Application application) {
        super(application);
        mRepository = new UserRepository();
        this.isSignIn = mRepository.isSignIn;
    }

    public void signUp(String email, String password, String name){
        mRepository.signUp(email, password, name);
    }

    public void signIn(String email, String password){
        mRepository.signIn(email, password);
    }

    public String getEmail(){
        return mRepository.getEmail();
    }

    public void signOut(){
        mRepository.signOut();
    }

    public void deleteUser() {
        mRepository.deleteUser();
    }

}
