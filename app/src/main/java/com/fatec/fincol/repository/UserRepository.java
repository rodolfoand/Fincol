package com.fatec.fincol.repository;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.fatec.fincol.R;
import com.fatec.fincol.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserRepository {

    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    public MutableLiveData<Boolean> isSignIn;
    public CollectionReference mUserCollection;
    public MutableLiveData<User> mUser;

    public UserRepository() {
        this.mAuth = FirebaseAuth.getInstance();
        this.isSignIn = new MutableLiveData<>();
        this.mUser = new MutableLiveData<>();
        this.mUserCollection = FirebaseFirestore.getInstance().collection("user");
    }

    public void setIsSignIn(){
        firebaseUser = mAuth.getCurrentUser();
        isSignIn.setValue(firebaseUser != null);
    }

    public void signIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        isSignIn.setValue(task.isSuccessful());
                    }
                });
    }

    public void signOut(){
        mAuth.signOut();
    }

    public LiveData<String> signUp(String email, String password, String name){

        MutableLiveData<String> message = new MutableLiveData<>();

        mAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                firebaseUser = authResult.getUser();
                updateProfile(name);
                AccountRepository accountRepository = new  AccountRepository(firebaseUser.getUid());
                accountRepository.updateAccount("Main");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                message.setValue(e.getMessage());
            }
        }).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                isSignIn.setValue(task.isSuccessful());
            }
        });

        return message;
    }

    public void updateProfile(String name){
        final User user = new User(firebaseUser.getUid(), name, firebaseUser.getEmail());
        if (mUser.getValue() != null) user.setProfileImage(mUser.getValue().getProfileImage());
        mUserCollection.document(firebaseUser.getUid()).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("signup", user.getName());
                setUser(user);
            }
        });
    }

    public void updateProfile(String name, String profileImage){
        User user = new User(firebaseUser.getUid(), name, firebaseUser.getEmail(), profileImage);
        mUserCollection.document(firebaseUser.getUid()).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                setUser(user);
            }
        });
    }

    public void deleteUser(){
        mUserCollection.document(firebaseUser.getUid()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                firebaseUser.delete();
            }
        });
    }

    public void setUser(){
        setIsSignIn();
        if (isSignIn.getValue()) {
            mUserCollection.document(firebaseUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    User user = new User(firebaseUser.getUid()
                            , task.getResult().get("name") != null ? task.getResult().get("name").toString() : new String()
                            , firebaseUser.getEmail()
                            , task.getResult().get("profileImage") != null ? task.getResult().get("profileImage").toString() : "");
                    setUser(user);
                }
            });
        } else {
            User user = new User(new String(), new String(), new String());
            setUser(user);
        }

    }

    private void setUser(User user){
        if (user.getProfileImage() == null) user.setProfileImage(new String());
        mUser.setValue(user);
    }

    public void forgotPass(String email){
        mAuth.sendPasswordResetEmail(email);
    }

}