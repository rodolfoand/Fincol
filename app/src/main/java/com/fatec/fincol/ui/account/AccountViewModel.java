package com.fatec.fincol.ui.account;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.fatec.fincol.model.AccountVersion2;
import com.fatec.fincol.model.Collaborator;
import com.fatec.fincol.model.User;
import com.fatec.fincol.repository.AccountRepository;

import java.util.List;

public class AccountViewModel extends AndroidViewModel {

    private MutableLiveData<String> mText;
    private AccountRepository mAccountRepository;
    private LiveData<AccountVersion2> mActiveAccount;
    public LiveData<List<AccountVersion2>> mMyAccountList;
    public LiveData<List<User>> mAccountCollabUserList;
    public LiveData<List<Collaborator>> mMyCollabAccountList;
    public LiveData<List<Collaborator>> mCollabUserAccountList;


    public LiveData<AccountVersion2> mDetailAccount;

    public AccountViewModel(Application application) {
        super(application);
        this.mMyAccountList = new MutableLiveData<>();
        this.mAccountCollabUserList = new MutableLiveData<>();
        this.mMyCollabAccountList = new MutableLiveData<>();
        this.mCollabUserAccountList = new MutableLiveData<>();
    }

    public void initialize(String uid_user){
        this.mAccountRepository = new AccountRepository(uid_user);
        this.mActiveAccount = mAccountRepository.mActiveAccount;
        this.mMyAccountList = mAccountRepository.mMyAccountList;
        this.mDetailAccount = mAccountRepository.mDetailAccount;
        this.mAccountCollabUserList = mAccountRepository.mUserAccountList;
        this.mMyCollabAccountList = mAccountRepository.mMyCollabAccountList;
        this.mCollabUserAccountList = mAccountRepository.mCollabUserAccountList;


        mText = new MutableLiveData<>();
        mText.setValue("This is account fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void updateAccount(String name){
        mAccountRepository.updateAccount(name);
    }
    public void updateAccount(AccountVersion2 account){
        mAccountRepository.updateAccount(account);
    }

    public void getAccount(String id){
        mAccountRepository.getAccount(id);
    }


    public void deleteAccount(String id){
        mAccountRepository.deleteAccount(id);
    }

    public LiveData<String> addCollaborator(String email, String id_account){
        return mAccountRepository.addCollaborator(email, id_account);
    }

    public void getCollabs(String account_id){
        mAccountRepository.getCollabs(account_id);
    }

    public void setActive(String account_id){
        mAccountRepository.setActive(account_id);
    }
    public void setInactive(String account_id){
        mAccountRepository.setInactive(account_id);
    }
    public void reject(String account_id){
        mAccountRepository.deleteCollab(account_id);
    }

}