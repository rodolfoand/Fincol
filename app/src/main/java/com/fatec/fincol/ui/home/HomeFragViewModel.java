package com.fatec.fincol.ui.home;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.fatec.fincol.model.AccountVersion2;
import com.fatec.fincol.repository.AccountRepository;


public class HomeFragViewModel extends AndroidViewModel {

    private MutableLiveData<String> mText;
    private AccountRepository mAccountRepository;
    public LiveData<AccountVersion2> mActiveAccount;

    public HomeFragViewModel(Application application) {
        super(application);
        this.mActiveAccount = new MutableLiveData<>();
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }
    public void initialize(String uid_user){
        this.mAccountRepository = new AccountRepository(uid_user);
        mActiveAccount = mAccountRepository.mActiveAccount;
        mAccountRepository.setActiveAccount(uid_user);
    }

    public LiveData<String> getText() {
        return mText;
    }

}