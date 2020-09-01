package com.fatec.fincol.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.fatec.fincol.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
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
    private CollectionReference mUserCollection;
    public MutableLiveData<User> mUser;

    public UserRepository() {
        this.mAuth = FirebaseAuth.getInstance();
        this.isSignIn = new MutableLiveData<>();
        this.isSignIn.setValue(isSignedIn());
        this.mUserCollection = FirebaseFirestore.getInstance().collection("user");
        this.mUser = new MutableLiveData<>();
        authStateListener();
        setUser();
    }

    public void signUp(String email, String password, String name){
        mAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                firebaseUser = authResult.getUser();
                updateProfile(name);
                mUser.setValue(new User(firebaseUser.getUid(), name, firebaseUser.getEmail()));
            }
        });
    }

    public void signIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener((result) -> {
            isSignIn.setValue(isSignedIn());
            setUser();
        }).addOnFailureListener((exception) -> {
            isSignIn.setValue(isSignedIn());
            exception.printStackTrace();
        });
    }

    private boolean isSignedIn(){
        firebaseUser = mAuth.getCurrentUser();
        return (firebaseUser != null);
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
        isSignIn.setValue(isSignedIn());
    }

    public void deleteUser(){

        mUserCollection.document(firebaseUser.getUid()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                firebaseUser.delete();
            }
        });

        isSignIn.setValue(isSignedIn());
    }

    public void updateProfile(String name){
        mUserCollection.document(firebaseUser.getUid()).set(new User(firebaseUser.getUid(), name, firebaseUser.getEmail()));
        mUser.setValue(new User(firebaseUser.getUid(), name, firebaseUser.getEmail()));
    }

    public void updateProfile(String name, String profileImage){
        mUserCollection.document(firebaseUser.getUid()).set(new User(firebaseUser.getUid(), name, firebaseUser.getEmail(), profileImage));
        mUser.setValue(new User(firebaseUser.getUid(), name, firebaseUser.getEmail(), profileImage));
    }

    private void setUser(){
        if (isSignedIn()) mUserCollection.document(firebaseUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    mUser.setValue(new User(firebaseUser.getUid()
                            , task.getResult().get("name").toString()
                            , firebaseUser.getEmail()
                            , task.getResult().get("profileImage").toString()));
            }
        });
    }

}