package com.fatec.fincol.ui.home;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.fatec.fincol.model.AccountVersion2;
import com.fatec.fincol.model.CategoryExpense;
import com.fatec.fincol.repository.AccountRepository;
import com.fatec.fincol.repository.TransactionRepository;

import java.util.List;


public class HomeFragViewModel extends AndroidViewModel {

    private MutableLiveData<String> mText;
    private AccountRepository mAccountRepository;
    public LiveData<AccountVersion2> mActiveAccount;
    public MutableLiveData<List<CategoryExpense>> mCategoryExpenseList;


    private TransactionRepository mTransactionRepository;
    public MutableLiveData<List<String>> mCategoryList;

    public HomeFragViewModel(Application application) {
        super(application);
        this.mActiveAccount = new MutableLiveData<>();
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");

        mTransactionRepository = new TransactionRepository();
        mCategoryList = mTransactionRepository.mCategoryList;
        mCategoryExpenseList = mTransactionRepository.mCategoryExpenseList;

    }
    public void initialize(String uid_user){
        this.mAccountRepository = new AccountRepository(uid_user);
        mActiveAccount = mAccountRepository.mActiveAccount;
        mAccountRepository.setActiveAccount(uid_user);
    }

    public void getCategories(String account){
        mTransactionRepository.getCategories(account);
    }

    public void getCategoryExpenses(String account, List<String> stringList){
        mTransactionRepository.getCategoryExpenses(account, stringList);
    }

    public void setAccount(String uid_user){
        mAccountRepository.setActiveAccount(uid_user);
    }

    public LiveData<String> getText() {
        return mText;
    }

}