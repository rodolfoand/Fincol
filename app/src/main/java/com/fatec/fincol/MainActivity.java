package com.fatec.fincol;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fatec.fincol.viewmodel.UserViewModel;

public class MainActivity extends AppCompatActivity {

    private UserViewModel mUserViewModel;
    private TextView signInTextView;
    private Button signOutButton;
    private Button deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        signInTextView = (TextView) findViewById(R.id.signInTextView);
        signOutButton = (Button) findViewById(R.id.signOutButton);
        deleteButton = (Button) findViewById(R.id.deleteButton);

        mUserViewModel.isSignIn.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                isSignedIn(aBoolean);
            }
        });

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserViewModel.signOut();
                startActivity(new Intent(v.getContext(), SignInActivity.class));
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserViewModel.deleteUser();
            }
        });

    }

    protected void isSignedIn(boolean signIn){
        if (signIn) signInTextView.setText("Sign in as: " + mUserViewModel.getEmail());
        else startActivity(new Intent(this, SignInActivity.class));
    }
}