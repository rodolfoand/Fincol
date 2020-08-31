package com.fatec.fincol;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fatec.fincol.model.User;
import com.fatec.fincol.viewmodel.UserViewModel;

public class ProfileActivity extends AppCompatActivity {

    private UserViewModel mUserViewModel;

    TextView profileNameTextView;
    TextView profileEmailTextView;
    Button editNameButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        profileNameTextView = findViewById(R.id.profileNameTextView);
        profileEmailTextView = findViewById(R.id.profileEmailTextView);
        editNameButton = findViewById(R.id.editNameButton);

        mUserViewModel.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                profileNameTextView.setText(user.getName());
                profileEmailTextView.setText(user.getEmail());
            }
        });

        editNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.dialog_edit_name, null);
                EditText nameTextInput = alertLayout.findViewById(R.id.nameTextInput);

                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setTitle(R.string.name_edit);
                alert.setView(alertLayout);
                alert.setCancelable(false);
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        nameTextInput.getText().toString();
                        mUserViewModel.updateProfile(nameTextInput.getText().toString());
                    }
                });
                AlertDialog dialog = alert.create();
                dialog.show();
            }
        });

    }
}