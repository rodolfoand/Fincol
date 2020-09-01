package com.fatec.fincol;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import com.fatec.fincol.ui.signin.SignInActivity;

public class MainActivity extends AppCompatActivity {

    private MainViewModel mMainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mMainViewModel.isSignIn.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                isSignedIn(aBoolean);
            }
        });
    }

    protected void isSignedIn(boolean signIn){
        if (signIn) startActivity(new Intent(this, HomeActivity.class));
        else startActivity(new Intent(this, SignInActivity.class));
    }
}