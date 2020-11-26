package com.fatec.fincol.ui.transaction;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.fatec.fincol.model.Transaction;
import com.fatec.fincol.repository.TransactionRepository;

import java.util.List;

public class TransactionViewModel extends AndroidViewModel {

    private TransactionRepository mTransactionRepository;
    public MutableLiveData<List<String>> mCategoryList;

    public TransactionViewModel(@NonNull Application application) {
        super(application);
        mTransactionRepository = new TransactionRepository();
        mCategoryList = mTransactionRepository.mCategoryList;
    }

    public LiveData<String> addTransaction(String account_id, Transaction transaction, List<String> categoryList){
        return mTransactionRepository.addTransaction(account_id, transaction, categoryList);
    }

    public void getCategories(String account){
        mTransactionRepository.getCategories(account);
    }
}
