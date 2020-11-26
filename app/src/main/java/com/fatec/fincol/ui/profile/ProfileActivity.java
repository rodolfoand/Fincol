package com.fatec.fincol.ui.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fatec.fincol.HomeActivity;
import com.fatec.fincol.R;
import com.fatec.fincol.model.User;
import com.fatec.fincol.util.BitmapUtil;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;

public class ProfileActivity extends AppCompatActivity {

    private ProfileViewModel mProfileViewModel;

    private TextView profileNameTextView;
    private TextView profileEmailTextView;

    private ImageView profileImageView;
    private ImageView editNameImageView;

    private Button signOutButton;

    private ProgressBar profileProgressBar;
    private CardView profileCardView;

    private View mLayout;

    static final int REQUEST_IMAGE = 1;
    static final int PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE = 2;
    static final int REQUEST_CODE_CROP = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mLayout = findViewById(R.id.profile_layout);

        mProfileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        profileNameTextView = findViewById(R.id.profileNameTextView);
        profileEmailTextView = findViewById(R.id.profileEmailTextView);
        profileImageView = findViewById(R.id.profileImageView);
        editNameImageView = findViewById(R.id.editNameImageView);
        signOutButton = findViewById(R.id.signOutButton);
        profileProgressBar = findViewById(R.id.profileProgressBar);
        profileCardView = findViewById(R.id.profileCardView);

        mProfileViewModel.mUser.observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {

                profileProgressBar.setVisibility(View.INVISIBLE);
                profileCardView.setVisibility(View.VISIBLE);

                profileNameTextView.setText(user.getName());
                profileEmailTextView.setText(user.getEmail());
                if (!user.getProfileImage().isEmpty())
                    profileImageView.setImageBitmap(BitmapUtil.base64ToBitmap(user.getProfileImage()));
            }
        });

        editNameImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editName();
            }
        });

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProfileViewModel.signOut();

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();

                Intent replyIntent = new Intent();
                setResult(HomeActivity.SIGN_OUT_REQUEST_CODE, replyIntent);
                finish();
            }
        });

        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyPermissions();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_delete_user:
                mProfileViewModel.deleteUser();
                Intent replyIntent = new Intent();
                setResult(HomeActivity.DELETE_REQUEST_CODE, replyIntent);
                finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void editName(){
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.dialog_edit_name, null);
        EditText nameTextInput = alertLayout.findViewById(R.id.nameTextInput);
        nameTextInput.setText(profileNameTextView.getText().toString());

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
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
                profileNameTextView.setText(nameTextInput.getText().toString());
                mProfileViewModel.updateProfile(profileNameTextView.getText().toString());
            }
        });
        AlertDialog dialog = alert.create();
        dialog.show();
    }

    private void verifyPermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Snackbar.make(mLayout, R.string.allow_media_access,
                    Snackbar.LENGTH_INDEFINITE).setAction(R.string.ok, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Request the permission
                    ActivityCompat.requestPermissions(ProfileActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE);
                }
            }).show();

        } else {
            Snackbar.make(mLayout, R.string.permissions_not_granted, Snackbar.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Snackbar.make(mLayout, R.string.permissions_granted,
                        Snackbar.LENGTH_SHORT)
                        .show();
                selectImage();
            } else {
                Snackbar.make(mLayout, R.string.permissions_not_granted,
                        Snackbar.LENGTH_SHORT)
                        .show();
            }
        }
    }

    public void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_IMAGE);
    }

    private void cropUri(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scaleUpIfNeeded", true);
        intent.putExtra("scale", "true");
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.name());
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        startActivityForResult(intent, REQUEST_CODE_CROP);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                cropUri(BitmapUtil.getImageUri(this, bitmap));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == REQUEST_CODE_CROP) {
            if (resultCode != RESULT_OK) {
                Toast.makeText(this, getString(R.string.crop_image_failure_message), Toast.LENGTH_LONG).show();
                return;
            }


            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                String bitmapString = BitmapUtil.bitmapToBase64(bitmap);
                mProfileViewModel.updateProfile(profileNameTextView.getText().toString()
                        , bitmapString);
            } catch (IOException e) {
                e.printStackTrace();
            }
            profileImageView.setImageURI(data.getData());
        }
    }
}