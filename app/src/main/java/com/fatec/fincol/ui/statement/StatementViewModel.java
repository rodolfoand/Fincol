package com.fatec.fincol.ui.statement;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fatec.fincol.model.AccountVersion2;
import com.fatec.fincol.model.Transaction;
import com.fatec.fincol.repository.AccountRepository;
import com.fatec.fincol.repository.TransactionRepository;

import java.sql.Timestamp;
import java.util.List;

public class StatementViewModel extends ViewModel {

    public MutableLiveData<List<Transaction>> mTransactionList;

    private TransactionRepository mTransactionRepository;
    private AccountRepository mAccountRepository;
    public LiveData<AccountVersion2> mActiveAccount;

    public StatementViewModel() {
        this.mTransactionRepository = new TransactionRepository();
        this.mTransactionList = mTransactionRepository.mTransactionList;
        this.mActiveAccount = new MutableLiveData<>();
    }
    public void initialize(String uid_user){
        this.mAccountRepository = new AccountRepository(uid_user);
        mActiveAccount = mAccountRepository.mActiveAccount;
        mAccountRepository.setActiveAccount(uid_user);
    }

    public void getTransactions(String account, Timestamp filter){
        mTransactionRepository.getTransactions(account, filter);
    }
}