package com.fatec.fincol;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import com.fatec.fincol.ui.signin.SignInActivity;

public class MainActivity extends AppCompatActivity {

    private MainViewModel mMainViewModel;

    public static final int SIGN_IN_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        mMainViewModel.isSignIn.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                isSignedIn(aBoolean);
            }
        });
    }

    protected void isSignedIn(boolean signIn){
        if (signIn) startActivity(new Intent(this, HomeActivity.class));
        else startActivityForResult(new Intent(this, SignInActivity.class), SIGN_IN_REQUEST_CODE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMainViewModel.setIsSignIn();
    }
}