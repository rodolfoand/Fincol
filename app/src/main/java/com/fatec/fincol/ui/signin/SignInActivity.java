package com.fatec.fincol.ui.signin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fatec.fincol.R;
import com.fatec.fincol.ui.signup.SignUpActivity;
import com.google.android.material.textfield.TextInputLayout;

public class SignInActivity extends AppCompatActivity {

    private Button signInButton;
    private Button googleButton;
    private Button facebookButton;

    private EditText emailTextInput;
    private EditText passTextInput;

    private TextInputLayout emailLabel;
    private TextInputLayout passLabel;

    private TextView forgotTextView;
    private TextView createAccountTextView;

    private SignInViewModel mSignInViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mSignInViewModel = new ViewModelProvider(this).get(SignInViewModel.class);

        signInButton = (Button) findViewById(R.id.signInButton);
        googleButton = (Button) findViewById(R.id.googleButton);
        facebookButton = (Button) findViewById(R.id.facebookButton);

        forgotTextView = (TextView) findViewById(R.id.forgotTextView);
        createAccountTextView = (TextView) findViewById(R.id.createAccountTextView);

        emailTextInput = (EditText) findViewById(R.id.emailTextInput);
        passTextInput = (EditText) findViewById(R.id.passTextInput);

        emailLabel = (TextInputLayout) findViewById(R.id.emailLabel);
        passLabel = (TextInputLayout) findViewById(R.id.passLabel);


        mSignInViewModel.isSignIn.observe( this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean)
                    finish();
                if (!aBoolean) {
                    Toast.makeText(SignInActivity.this, R.string.incorrect_e_mail_pass, Toast.LENGTH_LONG).show();
                }
            }
        });


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (emailIsEmpty())
                    emailLabel.setError(getString(R.string.e_mail_is_required));
                else if (!emailIsValid())
                    emailLabel.setError(getString(R.string.e_mail_invalid));
                else emailLabel.setError(null);

                if (passwordIsEmpty())
                    passLabel.setError(getString(R.string.password_is_required));
                else passLabel.setError(null);

                if (validateSignIn()) {
                    mSignInViewModel.signIn(emailTextInput.getText().toString()
                            , passTextInput.getText().toString());
                    InputMethodManager ims =
                            (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    ims.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
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
                startActivity(new Intent(v.getContext(), SignUpActivity.class));
            }
        });

    }

    private boolean emailIsEmpty(){
        return emailTextInput.getText().toString().isEmpty();
    }

    private boolean emailIsValid(){
        return android.util.Patterns
                .EMAIL_ADDRESS
                .matcher(emailTextInput.getText().toString())
                .matches();
    }

    private boolean passwordIsEmpty(){
        return passTextInput.getText().toString().isEmpty();
    }


    private boolean validateSignIn(){
        return !emailIsEmpty()
                && emailIsValid()
                && !passwordIsEmpty();
    }

}