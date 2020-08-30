package com.fatec.fincol.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserRepository {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private MutableLiveData<Boolean> isSignIn;

    public UserRepository() {
        this.mAuth = FirebaseAuth.getInstance();
        this.isSignIn = new MutableLiveData<>();
    }

    public void createUser(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password);
    }

    public void signIn(String email, String password){

        mAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener((result) -> {
            isSignIn.setValue(true);
        }).addOnFailureListener((exception) -> {
            exception.printStackTrace();
            isSignIn.setValue(false);
        });
    }

    public LiveData<Boolean> isSignedIn(){
        currentUser = mAuth.getCurrentUser();
        isSignIn.setValue(currentUser != null);
        return isSignIn;
    }

    public String getName(){
        currentUser = mAuth.getCurrentUser();
        return currentUser.getEmail();
    }

    public void signOut(){
        mAuth.signOut();
    }
}
