package com.fatec.fincol.ui.profile;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.fatec.fincol.model.User;
import com.fatec.fincol.repository.UserRepository;

public class ProfileViewModel extends AndroidViewModel {

    private UserRepository mRepository;
    public LiveData<User> mUser;

    public ProfileViewModel(Application application) {
        super(application);
        mRepository = new UserRepository();
        this.mUser = mRepository.mUser;
        mRepository.setUser();
    }

    public void updateProfile(String name){
        mRepository.updateProfile(name);
    }

    public void updateProfile(String name, String profileImage){
        mRepository.updateProfile(name, profileImage);
    }

    public void signOut(){
        mRepository.signOut();
    }

    public void deleteUser() {
        mRepository.deleteUser();
    }
}
