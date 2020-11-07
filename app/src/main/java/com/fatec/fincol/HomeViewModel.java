package com.fatec.fincol;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.fatec.fincol.model.AccountVersion2;
import com.fatec.fincol.model.User;
import com.fatec.fincol.repository.AccountRepository;
import com.fatec.fincol.repository.UserRepository;

public class HomeViewModel extends AndroidViewModel {

    private UserRepository mRepository;
    public LiveData<User> mUser;
    private AccountRepository mAccountRepository;
    public LiveData<AccountVersion2> mActiveAccount;

    public HomeViewModel(Application application) {
        super(application);
        this.mRepository = new UserRepository();
        this.mUser = mRepository.mUser;
        mRepository.setUser();
        this.mActiveAccount = new MutableLiveData<>();

    }
    public void initialize(String uid_user){
        this.mAccountRepository = new AccountRepository(uid_user);
        mActiveAccount = mAccountRepository.mActiveAccount;
        mAccountRepository.setActiveAccount(uid_user);
    }
}
