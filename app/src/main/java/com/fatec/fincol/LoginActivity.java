package com.fatec.fincol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private Button signInButton;
    private Button googleButton;
    private Button facebookButton;

    private TextView forgotTextView;
    private TextView createAccountTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signInButton = (Button) findViewById(R.id.signInButton);
        googleButton = (Button) findViewById(R.id.googleButton);
        facebookButton = (Button) findViewById(R.id.facebookButton);

        forgotTextView = (TextView) findViewById(R.id.forgotTextView);
        createAccountTextView = (TextView) findViewById(R.id.createAccountTextView);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), R.string.sign_in, Toast.LENGTH_SHORT).show();
            }
        });

        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), R.string.log_in_with_google, Toast.LENGTH_SHORT).show();
            }
        });

        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), R.string.log_in_with_the_facebook, Toast.LENGTH_SHORT).show();
            }
        });

        forgotTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), R.string.forgot_password, Toast.LENGTH_SHORT).show();
            }
        });

        createAccountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), R.string.create_an_account, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(v.getContext(), CreateAccountActivity.class));
            }
        });



    }
}