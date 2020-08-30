package com.fatec.fincol.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.fatec.fincol.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserRepository {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    public MutableLiveData<Boolean> isSignIn;

    public UserRepository() {

        this.mAuth = FirebaseAuth.getInstance();
        this.currentUser = mAuth.getCurrentUser();
        this.isSignIn = new MutableLiveData<>();
        this.isSignIn.setValue(isSignedIn());
        authStateListener();

    }

    public void signUp(String email, String password, String name){
        mAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                currentUser = authResult.getUser();
            }
        });
    }

    public void signIn(String email, String password){

        mAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener((result) -> {
            isSignIn.setValue(isSignedIn());
        }).addOnFailureListener((exception) -> {
            exception.printStackTrace();
            isSignIn.setValue(isSignedIn());
        });
    }

    private boolean isSignedIn(){
        currentUser = mAuth.getCurrentUser();
        return (currentUser != null);
    }

    public void authStateListener(){
        FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                isSignIn.setValue(isSignedIn());
            }
        };
        mAuth.getInstance().addAuthStateListener(authStateListener);
    }

    public void signOut(){
        mAuth.signOut();
    }

    public void deleteUser(){
        isSignedIn();
        currentUser.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            isSignIn.setValue(isSignedIn());
                        }
                    }
                });
    }

    public String getEmail(){
        return currentUser.getEmail();
    }


    private void updateProfile(String name){

    }

}
