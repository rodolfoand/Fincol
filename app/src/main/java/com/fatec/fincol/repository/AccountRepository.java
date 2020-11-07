package com.fatec.fincol.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.fatec.fincol.model.AccountVersion2;
import com.fatec.fincol.model.Collaborator;
import com.fatec.fincol.model.Transaction;
import com.fatec.fincol.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountRepository {

    private UserRepository mUserRepository;
    public LiveData<User> mUser;
    private CollectionReference mAccountCollection;
    private CollectionReference mCollaboratorCollection;
    public MutableLiveData<AccountVersion2> mActiveAccount;
    public MutableLiveData<AccountVersion2> mDetailAccount;
    private String uid_user;
    public MutableLiveData<List<AccountVersion2>> mMyAccountList;
    public MutableLiveData<List<Collaborator>> mMyCollabAccountList;
    public MutableLiveData<List<User>> mUserAccountList;
    public MutableLiveData<List<Collaborator>> mCollabUserAccountList;

    public AccountRepository(String uid_user) {
        this.mUserRepository = new UserRepository();
        this.mUser = mUserRepository.mUser;
        mUserRepository.setUser();
        this.mAccountCollection = FirebaseFirestore.getInstance().collection("account");
        this.mCollaboratorCollection = FirebaseFirestore.getInstance().collection("collaborator");
        this.mActiveAccount = new MutableLiveData<>();
        this.mDetailAccount = new MutableLiveData<>();
        this.uid_user = uid_user;
        this.mMyAccountList = new MutableLiveData<>();
        this.mMyCollabAccountList = new MutableLiveData<>();
        this.mUserAccountList = new MutableLiveData<>();
        this.mCollabUserAccountList = new MutableLiveData<>();
        getMyAccounts();
    }

    public void updateAccount(String name){
        AccountVersion2 account = new AccountVersion2(name);
        mAccountCollection.add(account).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                account.setId(task.getResult().getId());
//                Collaborator collaborator = new Collaborator(mUser.getValue().getUid(), account.getId());
                Collaborator collaborator = new Collaborator(mUser.getValue().getUid(), account.getId(), Collaborator.StatusColl.ACTIVE, Collaborator.TypeColl.ADMIN);
                Log.d("Collab", collaborator.toString());
                mCollaboratorCollection.add(collaborator).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        getMyAccounts();



                        List<String> categoryList = new ArrayList<>();
                        categoryList.add("Recreation");
                        categoryList.add("Expense");
                        categoryList.add("Food");
                        categoryList.add("Shouldn't have spent it");
                        categoryList.add("Others");

                        for (String cat: categoryList) {
                            Map<String, Object> category = new HashMap<>();
                            category.put("name", cat);
                            mAccountCollection.document(account.getId()).collection("category").document(cat).set(category);
                        }

                    }
                });
                mAccountCollection.document(account.getId()).set(account);
                getMyAccounts();
            }
        });

        Log.d("Account", "END");
    }

    public void updateAccount(AccountVersion2 account){
        if (account.getId() != null)
            mAccountCollection.document(account.getId()).set(account);
        else
            mAccountCollection.add(account).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    account.setId(task.getResult().getId());
                    mAccountCollection.document(account.getId()).set(account);
//                    Collaborator collaborator = new Collaborator(mUser.getValue().getUid(), account.getId());
                    Collaborator collaborator = new Collaborator(mUser.getValue().getUid(), account.getId(), Collaborator.StatusColl.INACTIVE, Collaborator.TypeColl.ADMIN);
                    mCollaboratorCollection.add(collaborator).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            getMyAccounts();

                            List<String> categoryList = new ArrayList<>();
                            categoryList.add("Recreation");
                            categoryList.add("Expense");
                            categoryList.add("Food");
                            categoryList.add("Shouldn't have spent it");
                            categoryList.add("Others");

                            for (String cat: categoryList) {
                                Map<String, Object> category = new HashMap<>();
                                category.put("name", cat);
                                mAccountCollection.document(account.getId()).collection("category").document(cat).set(category);
                            }
                        }
                    });
                    mAccountCollection.document(account.getId()).set(account);
                    getMyAccounts();

                }
            });

    }

    public void getMyAccounts(){
        final List<AccountVersion2> myAccountList = new ArrayList<>();
        final List<Collaborator> myCollabList = new ArrayList<>();
        mCollaboratorCollection.whereEqualTo("user", uid_user)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Log.d("Account", queryDocumentSnapshots.getDocuments().size() + "");
                for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                    Log.d("Account", document.get("account").toString());

                    mAccountCollection.document(document.get("account").toString()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
//                            Log.d("Account", documentSnapshot.get("name").toString());
                            if (documentSnapshot.get("name") != null) {
                                AccountVersion2 account = new AccountVersion2(
                                        documentSnapshot.getId(),
                                        documentSnapshot.get("name").toString(),
                                        (documentSnapshot.get("accountImage") != null) ? documentSnapshot.get("accountImage").toString() : new String(),
                                        Double.parseDouble((documentSnapshot.get("balance") != null) ? documentSnapshot.get("balance").toString() : "0")
                                );
                                myAccountList.add(account);


                                Collaborator collaborator = new Collaborator(
                                        document.get("user").toString(),
                                        document.get("account").toString(),
                                        Collaborator.StatusColl.get(document.get("status").toString()),
                                        Collaborator.TypeColl.get(document.get("type").toString())
                                );
                                myCollabList.add(collaborator);
                            }
                        }
                    }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (myAccountList.size() == queryDocumentSnapshots.getDocuments().size()) mMyAccountList.setValue(myAccountList);
                            if (myCollabList.size() == queryDocumentSnapshots.getDocuments().size()) mMyCollabAccountList.setValue(myCollabList);

                            Log.d("AccountDEP1", myAccountList.size() + " R " + queryDocumentSnapshots.getDocuments().size());
                        }
                    });
                }
            }
        })
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                mMyAccountList.setValue(myAccountList);
//                mMyCollabList.setValue(myCollabList);
//            }
//        })
        ;

    }

    public MutableLiveData<AccountVersion2> getAccount(String id){
        mAccountCollection.document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                AccountVersion2 doc = new AccountVersion2();
                doc.setId(task.getResult().get("id").toString());
                doc.setName(task.getResult().get("name").toString());
                if (task.getResult().get("accountImage") != null)
                    doc.setAccountImage(task.getResult().get("accountImage").toString());
                mDetailAccount.setValue(doc);
            }
        });
        return mDetailAccount;
    }

    public void deleteAccount(String id){
        mAccountCollection.document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mCollaboratorCollection.whereEqualTo("account", id).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                            mCollaboratorCollection.document(document.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    getMyAccounts();
                                }
                            });
                        }
                    }
                });
            }
        }).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                getMyAccounts();
            }
        });
    }

    public LiveData<String> addCollaborator(String email, String id_account){
        MutableLiveData<String> message = new MutableLiveData<>();

        mUserRepository.mUserCollection.whereEqualTo("email",email).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        message.setValue(queryDocumentSnapshots.getDocuments().size() + "");
                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
//                            Collaborator collaborator = new Collaborator(document.getId(), id_account);
                            Collaborator collaborator = new Collaborator(document.getId(), id_account, Collaborator.StatusColl.PENDING, Collaborator.TypeColl.ADMIN);
                            mCollaboratorCollection.add(collaborator).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    getMyAccounts();
                                }
                            });
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                message.setValue(e.getMessage());
            }
        });

        return message;
    }

    public void getCollabs(String account_id){

        Log.d("CollabU", "getCollabs");
        Log.d("CollabU", "account: " + account_id);
        MutableLiveData<List<User>> users = new MutableLiveData<>();
        users.setValue(new ArrayList<>());
        mCollaboratorCollection.whereEqualTo("account", account_id).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.d("CollabU", "getCollabs.onSuccess");
                        Log.d("CollabU", "getCollabs.onSuccess.getDocuments: " + queryDocumentSnapshots.getDocuments().size());

                        List<User> listUsers = new ArrayList<>();
                        List<Collaborator> myCollabList = new ArrayList<>();
                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                            Log.d("CollabU", "User: "+document.get("user").toString());
                            Log.d("CollabU", "status: "+document.get("status").toString());


                            mUserRepository.mUserCollection.document(document.get("user").toString()).get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            Log.d("CollabU", "getCollabs.onSuccess.mUserCollection.onComplete");
                                            Log.d("CollabU", task.getResult().get("name").toString());

                                            User user = new User(task.getResult().get("uid").toString(),
                                                    task.getResult().get("name").toString(),
                                                    task.getResult().get("email").toString(),
                                                    task.getResult().get("profileImage") != null ? task.getResult().get("profileImage").toString() : "");
                                            listUsers.add(user);

                                            Collaborator collaborator = new Collaborator(
                                                    document.get("user").toString(),
                                                    document.get("account").toString(),
                                                    Collaborator.StatusColl.get(document.get("status").toString()),
                                                    Collaborator.TypeColl.get(document.get("type").toString())
                                            );
                                            myCollabList.add(collaborator);
                                        }
                                    }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (listUsers.size() == queryDocumentSnapshots.getDocuments().size()) mUserAccountList.setValue(listUsers);
                                    if (myCollabList.size() == queryDocumentSnapshots.getDocuments().size()) mCollabUserAccountList.setValue(myCollabList);
                                }
                            });
                        }

                    }
                });
    }

    public void setActive(String account_id){
        mCollaboratorCollection.whereEqualTo("user", mUser.getValue().getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                    Collaborator collaborator = new Collaborator(
                            document.get("user").toString(),
                            document.get("account").toString(),
                            Collaborator.StatusColl.get(document.get("status").toString()),
                            Collaborator.TypeColl.get(document.get("type").toString())
                    );
                    if (collaborator.getAccount().equals(account_id)){
                        collaborator.setStatus(Collaborator.StatusColl.ACTIVE);
                        mCollaboratorCollection.document(document.getId()).set(collaborator);
                    } else if (collaborator.getStatus() == Collaborator.StatusColl.ACTIVE){
                        collaborator.setStatus(Collaborator.StatusColl.INACTIVE);
                        mCollaboratorCollection.document(document.getId()).set(collaborator);
                    }
                }
            }
        }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                getMyAccounts();
            }
        });
    }

    public void setInactive(String account_id){
        mCollaboratorCollection.whereEqualTo("user", mUser.getValue().getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                    Collaborator collaborator = new Collaborator(
                            document.get("user").toString(),
                            document.get("account").toString(),
                            Collaborator.StatusColl.get(document.get("status").toString()),
                            Collaborator.TypeColl.get(document.get("type").toString())
                    );
                    if (collaborator.getAccount().equals(account_id)){
                        collaborator.setStatus(Collaborator.StatusColl.INACTIVE);
                        mCollaboratorCollection.document(document.getId()).set(collaborator).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                getMyAccounts();
                            }
                        });
                    }
                }
            }
        });
    }

    public void deleteCollab(String account_id){
        mCollaboratorCollection.whereEqualTo("user", mUser.getValue().getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                    Collaborator collaborator = new Collaborator(
                            document.get("user").toString(),
                            document.get("account").toString(),
                            Collaborator.StatusColl.get(document.get("status").toString()),
                            Collaborator.TypeColl.get(document.get("type").toString())
                    );
                    if (collaborator.getAccount().equals(account_id)){
                        mCollaboratorCollection.document(document.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                getMyAccounts();
                            }
                        });
                    }
                }
            }
        });
    }

    public void setActiveAccount(String uid_user){
        mCollaboratorCollection.whereEqualTo("user", uid_user)
                .whereEqualTo("status", "ACTIVE")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                            mAccountCollection.document(document.get("account").toString()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    AccountVersion2 account = new AccountVersion2(
                                            documentSnapshot.getId(),
                                            documentSnapshot.get("name").toString(),
                                            (documentSnapshot.get("accountImage") != null) ? documentSnapshot.get("accountImage").toString() : new String(),
                                            Double.parseDouble((documentSnapshot.get("balance") != null) ? documentSnapshot.get("balance").toString() : "0")
                                    );
                                    mActiveAccount.setValue(account);

                                    Timestamp currentDate = new Timestamp(new Date().getTime());
                                    mAccountCollection.document(account.getId()).collection("transaction-fixed")
                                            .whereLessThan("date", currentDate)
                                            .get()
                                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
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
                                                if (transaction.isFixed()){
                                                    transaction.setFixed(false);
                                                    mAccountCollection.document(account.getId())
                                                            .collection("transaction")
                                                            .add(transaction);

                                                    transaction.setFixed(true);
                                                    Calendar newDateCalendar = Calendar.getInstance();
                                                    newDateCalendar.setTimeInMillis(transaction.getDate().getTime());
                                                    newDateCalendar.add(Calendar.MONTH, 1);

                                                    newDateCalendar.set(Calendar.HOUR_OF_DAY, 0);
                                                    newDateCalendar.set(Calendar.MINUTE, 0);
                                                    newDateCalendar.set(Calendar.SECOND, 0);
                                                    newDateCalendar.set(Calendar.MILLISECOND, 0);

                                                    Log.d("DateFixed", "date: " + newDateCalendar.getTime().toString());

                                                    Timestamp newDate = new Timestamp(newDateCalendar.getTimeInMillis());

                                                    transaction.setDate(newDate);

                                                    mAccountCollection.document(account.getId())
                                                            .collection("transaction-fixed")
                                                            .document(document.getId()).update("date", newDate).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            Double balance = Double.parseDouble((documentSnapshot.get("balance") != null) ? documentSnapshot.get("balance").toString() : "0");
                                                            balance += transaction.getValue();
                                                            mAccountCollection.document(account.getId()).update("balance", balance);
                                                            setActiveAccount(uid_user);
                                                        }
                                                    });
                                                } else if (transaction.isRepeat() && transaction.getRepeatTime() > 1) {

                                                    int newRepeatTime = transaction.getRepeatTime() - 1;
                                                    transaction.setRepeatTime(newRepeatTime);
                                                    mAccountCollection.document(account.getId())
                                                            .collection("transaction")
                                                            .add(transaction);

                                                    Calendar newDateCalendar = Calendar.getInstance();
                                                    newDateCalendar.setTimeInMillis(transaction.getDate().getTime());
                                                    newDateCalendar.add(Calendar.MONTH, 1);

                                                    newDateCalendar.set(Calendar.HOUR_OF_DAY, 0);
                                                    newDateCalendar.set(Calendar.MINUTE, 0);
                                                    newDateCalendar.set(Calendar.SECOND, 0);
                                                    newDateCalendar.set(Calendar.MILLISECOND, 0);

                                                    Log.d("DateFixed", "date: " + newDateCalendar.getTime().toString());

                                                    Timestamp newDate = new Timestamp(newDateCalendar.getTimeInMillis());

                                                    transaction.setDate(newDate);

                                                    mAccountCollection.document(account.getId())
                                                            .collection("transaction-fixed")
                                                            .document(document.getId()).update("date", newDate, "repeatTime", newRepeatTime).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            Double balance = Double.parseDouble((documentSnapshot.get("balance") != null) ? documentSnapshot.get("balance").toString() : "0");
                                                            balance += transaction.getValue();
                                                            mAccountCollection.document(account.getId()).update("balance", balance);
                                                            setActiveAccount(uid_user);
                                                        }
                                                    });
                                                } else if (transaction.isRepeat() && transaction.getRepeatTime() <= 1){
                                                    mAccountCollection.document(account.getId())
                                                            .collection("transaction-fixed")
                                                            .document(document.getId()).delete();
                                                }
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    }
                });
    }

}
