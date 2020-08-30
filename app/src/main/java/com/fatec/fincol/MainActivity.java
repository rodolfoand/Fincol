package com.fatec.fincol;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fatec.fincol.viewmodel.UserViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private UserViewModel mUserViewModel;
    private TextView signInTextView;
    private Button signOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        signInTextView = (TextView) findViewById(R.id.signInTextView);
        signOutButton = (Button) findViewById(R.id.signOutButton);

        isSignedIn();

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserViewModel.signOut();
                startActivity(new Intent(v.getContext(), LoginActivity.class));
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        isSignedIn();
    }

    protected void isSignedIn(){
        if (mUserViewModel.isSignedIn().getValue())
            signInTextView.setText("Sign in as: " + mUserViewModel.getName());
        else startActivity(new Intent(this, LoginActivity.class));
    }
}