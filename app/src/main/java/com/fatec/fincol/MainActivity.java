package com.fatec.fincol;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import com.fatec.fincol.viewmodel.UserViewModel;

public class MainActivity extends AppCompatActivity {

    private UserViewModel mUserViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        mUserViewModel.isSignIn.observe(this, new Observer<Boolean>() {
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