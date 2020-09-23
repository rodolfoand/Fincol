package com.fatec.fincol.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.fatec.fincol.model.AccountVersion2;
import com.fatec.fincol.model.Collaborator;
import com.fatec.fincol.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AccountRepository {

    private UserRepository mUserRepository;
    public LiveData<User> mUser;
    private CollectionReference mAccountCollection;
    private CollectionReference mCollaboratorCollection;
    public MutableLiveData<AccountVersion2> mActiveAccount;
    public MutableLiveData<AccountVersion2> mDetailAccount;
    private String uid_user;
    public MutableLiveData<List<AccountVersion2>> mMyAccountList;

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
        getMyAccounts();
    }

    public void updateAccount(String name){
        AccountVersion2 account = new AccountVersion2(name);
        mAccountCollection.add(account).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                account.setId(task.getResult().getId());
                Collaborator collaborator = new Collaborator(mUser.getValue().getUid(), account.getId());
                mCollaboratorCollection.add(collaborator).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        getMyAccounts();
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
                    Collaborator collaborator = new Collaborator(mUser.getValue().getUid(), account.getId());
                    mCollaboratorCollection.add(collaborator).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            getMyAccounts();
                        }
                    });
                    mAccountCollection.document(account.getId()).set(account);
                    getMyAccounts();

                }
            });

    }

    public void getMyAccounts(){
        final List<AccountVersion2> myAccountList = new ArrayList<>();
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
                                        (documentSnapshot.get("accountImage") != null) ? documentSnapshot.get("accountImage").toString() : new String()
                                );
                                myAccountList.add(account);
                            }
                        }
                    }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (myAccountList.size() == queryDocumentSnapshots.getDocuments().size()) mMyAccountList.setValue(myAccountList);

                            Log.d("AccountDEP1", myAccountList.size() + " R " + queryDocumentSnapshots.getDocuments().size());
                        }
                    });
                }
            }
        }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                mMyAccountList.setValue(myAccountList);
            }
        });

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

}
