package com.fatec.fincol.ui.signup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.fatec.fincol.R;
import com.google.android.material.textfield.TextInputLayout;

public class SignUpActivity extends AppCompatActivity {

    private SignUpViewModel mSignUpViewModel;

    private EditText nameTextInput;
    private EditText emailTextInput;
    private EditText confirmEmailTextInput;
    private EditText passwordTextInput;
    private EditText confirmPasswordTextInput;

    private TextInputLayout nameLabel;
    private TextInputLayout emailLabel;
    private TextInputLayout confirmEmailLabel;
    private TextInputLayout passwordLabel;
    private TextInputLayout confirmPasswordLabel;

    private Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mSignUpViewModel = new ViewModelProvider(this).get(SignUpViewModel.class);

        nameTextInput = (EditText) findViewById(R.id.nameTextInput);
        emailTextInput = (EditText) findViewById(R.id.emailTextInput);
        confirmEmailTextInput = (EditText) findViewById(R.id.confirmEmailTextInput);
        passwordTextInput = (EditText) findViewById(R.id.passwordTextInput);
        confirmPasswordTextInput = (EditText) findViewById(R.id.confirmPasswordTextInput);

        nameLabel = (TextInputLayout) findViewById(R.id.nameLabel);
        emailLabel = (TextInputLayout) findViewById(R.id.emailLabel);
        confirmEmailLabel = (TextInputLayout) findViewById(R.id.confirmEmailLabel);
        passwordLabel = (TextInputLayout) findViewById(R.id.passwordLabel);
        confirmPasswordLabel = (TextInputLayout) findViewById(R.id.confirmPasswordLabel);

        signUpButton = (Button) findViewById(R.id.signUpButton);

        emailTextInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if (!emailIsValid())
                    emailLabel.setError(getString(R.string.e_mail_invalid));
                else
                    emailLabel.setError(null);
            }
        });

        confirmEmailTextInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if (!confirmEmail())
                    confirmEmailLabel.setError(getString(R.string.e_mail_mismatch));
                else
                    confirmEmailLabel.setError(null);
            }
        });
        confirmPasswordTextInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if (!confirmPassword())
                    confirmPasswordLabel.setError(getString(R.string.password_mismatch));
                else
                    confirmPasswordLabel.setError(null);
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (nameIsEmpty())
                    nameLabel.setError(getString(R.string.name_is_required));
                else
                    nameLabel.setError(null);

                if (emailIsEmpty())
                    emailLabel.setError(getString(R.string.e_mail_is_required));
                else if (!emailIsValid())
                    emailLabel.setError(getString(R.string.e_mail_invalid));
                else emailLabel.setError(null);

                if (confirmEmailIsEmpty())
                    confirmEmailLabel.setError(getString(R.string.e_mail_is_required));
                else
                    confirmEmailLabel.setError(null);

                if (passwordIsEmpty())
                    passwordLabel.setError(getString(R.string.password_is_required));
                else
                    passwordLabel.setError(null);

                if (confirmPasswordIsEmpty())
                    confirmPasswordLabel.setError(getString(R.string.password_is_required));
                else
                    confirmPasswordLabel.setError(null);

                if (!confirmEmail())
                    confirmEmailLabel.setError(getString(R.string.e_mail_mismatch));

                if (!confirmPassword())
                    confirmPasswordLabel.setError(getString(R.string.password_mismatch));

                if (validateSignUp()) {
                    mSignUpViewModel.signUp(emailTextInput.getText().toString()
                            , passwordTextInput.getText().toString()
                            , nameTextInput.getText().toString());
                    finish();
                }

            }
        });
    }

    private boolean nameIsEmpty(){
        return nameTextInput.getText().toString().isEmpty();
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

    private boolean confirmEmailIsEmpty(){
        return confirmEmailTextInput.getText().toString().isEmpty();
    }

    private boolean passwordIsEmpty(){
        return passwordTextInput.getText().toString().isEmpty();
    }

    private boolean confirmPasswordIsEmpty(){
        return confirmPasswordTextInput.getText().toString().isEmpty();
    }

    private boolean confirmEmail(){
        return emailTextInput.getText().toString().equals(confirmEmailTextInput.getText().toString());
    }

    private boolean confirmPassword(){
        return passwordTextInput.getText().toString().equals(confirmPasswordTextInput.getText().toString());
    }

    private boolean validateSignUp(){
        return !nameIsEmpty()
                && !emailIsEmpty()
                && emailIsValid()
                && !confirmEmailIsEmpty()
                && !passwordIsEmpty()
                && !confirmPasswordIsEmpty()
                && confirmEmail()
                && confirmPassword();
    }
}