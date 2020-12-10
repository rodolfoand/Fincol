package com.fatec.fincol.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.fatec.fincol.model.CategoryExpense;
import com.fatec.fincol.model.Transaction;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionRepository {

    private CollectionReference mAccountCollection;
    public MutableLiveData<List<Transaction>> mTransactionList;
    public MutableLiveData<List<String>> mCategoryList;
    public MutableLiveData<List<CategoryExpense>> mCategoryExpenseList;

    public TransactionRepository() {
        this.mAccountCollection = FirebaseFirestore.getInstance().collection("account");
        this.mTransactionList = new MutableLiveData<>();
        this.mCategoryList = new MutableLiveData<>();
        this.mCategoryExpenseList = new MutableLiveData<>();
        this.mCategoryExpenseList.setValue(new ArrayList<>());
    }

    public LiveData<String> addTransaction(String account_id, Transaction transaction, List<String> categoryList){

        MutableLiveData<String> message = new MutableLiveData<>();


        mAccountCollection.document(account_id).collection("transaction")
                .add(transaction)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        SimpleDateFormat dfDate = new SimpleDateFormat("MMyyyy");


                        if (transaction.getType().equals(Transaction.Type.Expense) && categoryList.size() > 0){


                            for (String cat: categoryList) {
                                String catExpName = cat + "-" + dfDate.format(transaction.getDate());
                                Map<String, Object> category = new HashMap<>();
                                category.put("name", cat);
                                mAccountCollection.document(account_id).collection("category").document(cat).set(category);

                                mAccountCollection.document(account_id).collection("category-expense").document(catExpName).get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                double value = transaction.getValue();
                                                if (task.isSuccessful()){
                                                    if (task.getResult().contains("value"))
                                                        value += Double.parseDouble(task.getResult().get("value").toString());
                                                }

                                                Map<String, Object> category = new HashMap<>();
                                                category.put("name", cat);
                                                category.put("value", value);
                                                mAccountCollection.document(account_id).collection("category-expense").document(catExpName).set(category);
                                            }
                                        });

                            }
                        } else {
                            if (transaction.getType().equals(Transaction.Type.Expense)) {
                                String catExpName = "Others" + "-" + dfDate.format(transaction.getDate());
                                mAccountCollection.document(account_id).collection("category-expense").document(catExpName).get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                double value = transaction.getValue();
                                                if (task.isSuccessful()) {
                                                    if (task.getResult().contains("value"))
                                                        value += Double.parseDouble(task.getResult().get("value").toString());
                                                }

                                                Map<String, Object> category = new HashMap<>();
                                                category.put("name", "Others");
                                                category.put("value", value);
                                                mAccountCollection.document(account_id).collection("category-expense").document(catExpName).set(category);
                                            }
                                        });
                            }
                        }




                        mAccountCollection.document(account_id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                Double balance = Double.parseDouble((documentSnapshot.get("balance") != null) ? documentSnapshot.get("balance").toString() : "0");
                                balance += transaction.getValue();
                                mAccountCollection.document(account_id).update("balance", balance);

                                if (transaction.isFixed()) {
                                    Calendar newDateCalendar = Calendar.getInstance();
                                    newDateCalendar.setTimeInMillis(transaction.getDate().getTime());
                                    newDateCalendar.add(Calendar.MONTH, 1);

                                    newDateCalendar.set(Calendar.HOUR_OF_DAY, 0);
                                    newDateCalendar.set(Calendar.MINUTE, 0);
                                    newDateCalendar.set(Calendar.SECOND, 0);
                                    newDateCalendar.set(Calendar.MILLISECOND, 0);

                                    Timestamp newDate = new Timestamp(newDateCalendar.getTimeInMillis());

                                    transaction.setDate(newDate);
                                    mAccountCollection.document(account_id).collection("transaction-fixed").add(transaction);
                                }else if (transaction.isRepeat()){
                                    Calendar newDateCalendar = Calendar.getInstance();
                                    newDateCalendar.setTimeInMillis(transaction.getDate().getTime());
                                    newDateCalendar.add(Calendar.MONTH, 1);

                                    newDateCalendar.set(Calendar.HOUR_OF_DAY, 0);
                                    newDateCalendar.set(Calendar.MINUTE, 0);
                                    newDateCalendar.set(Calendar.SECOND, 0);
                                    newDateCalendar.set(Calendar.MILLISECOND, 0);

                                    Timestamp newDate = new Timestamp(newDateCalendar.getTimeInMillis());

                                    transaction.setDate(newDate);
                                    mAccountCollection.document(account_id).collection("transaction-fixed").add(transaction);
                                }

                            }
                        }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                message.setValue(String.valueOf(task.isSuccessful()));
                            }
                        });
                    }
                });
        return message;
    }

    public void getTransactions(String account, Timestamp filter){
        List<Transaction> transactionList = new ArrayList<>();
        mAccountCollection.document(account).collection("transaction").whereGreaterThan("date", filter).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {


                for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {

                    Transaction.Type type = Transaction.Type.get(document.get("type").toString());
                    Date date =  document.getDate("date");
                    Double value = document.getDouble("value");
                    String stringLocation = document.get("stringLocation").toString();
                    String description = document.get("description").toString();
                    Boolean fixed = document.getBoolean("fixed");
                    Boolean repeat = document.getBoolean("repeat");
                    Integer repeatTime = Integer.parseInt(document.get("repeatTime").toString());

                    Timestamp timestamp = new Timestamp(date.getTime());

                            Transaction transaction = new Transaction(
                            type,
                            timestamp,
                            value,
                            stringLocation,
                            description,
                            fixed,
                            repeat,
                            repeatTime
                    );

                    transactionList.add(transaction);
                }
            }
        }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                mTransactionList.setValue(new ArrayList<>());
                mTransactionList.setValue(transactionList);
            }
        });
    }

    public void getCategories(String account){
        List<String> stringList = new ArrayList<>();
        mAccountCollection.document(account).collection("category").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()){
                    stringList.add(document.getId());
                }
            }
        }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                mCategoryList.setValue(stringList);
            }
        });
    }

    public void getCategoryExpenses(String account, List<String> stringList){
        //mCategoryExpenseList.setValue(new ArrayList<>());
        for (String cat : stringList){
            mAccountCollection.document(account).collection("category-expense").document(cat).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.contains("name")){
                        List<CategoryExpense> categoryExpenseList = new ArrayList<>();
                        categoryExpenseList.addAll(mCategoryExpenseList.getValue());
                        CategoryExpense categoryExpense = new CategoryExpense(documentSnapshot.get("name").toString(), (documentSnapshot.getDouble("value")).floatValue());
                        categoryExpenseList.add(categoryExpense);
                        mCategoryExpenseList.setValue(categoryExpenseList);
                    }
                }
            });
        }
    }
}
