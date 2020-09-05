package com.fatec.fincol;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.fatec.fincol.model.User;
import com.fatec.fincol.repository.UserRepository;

public class HomeViewModel extends AndroidViewModel {

    private UserRepository mRepository;
    public LiveData<User> mUser;

    public HomeViewModel(Application application) {
        super(application);
        this.mRepository = new UserRepository();
        this.mUser = mRepository.mUser;
        mRepository.setUser();
    }
}
