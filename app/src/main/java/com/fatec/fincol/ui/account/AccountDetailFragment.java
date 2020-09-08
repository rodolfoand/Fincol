package com.fatec.fincol.ui.account;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.fatec.fincol.R;
import com.fatec.fincol.model.AccountVersion2;
import com.fatec.fincol.ui.profile.ProfileActivity;
import com.fatec.fincol.util.BitmapUtil;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;

public class AccountDetailFragment extends Fragment {

    private AccountViewModel mAccountViewModel;

    private TextInputLayout accountNameTextInputLayout;
    private TextInputEditText accountNameEditText;
    private CardView accountImageCardView;
    private ImageView accountImageView;
    private Button saveAccountButton;
    private AccountVersion2 mAccount;
    private View mLayout;


    static final int REQUEST_IMAGE = 1;
    static final int PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE = 2;
    static final int REQUEST_CODE_CROP = 3;

    public static AccountDetailFragment newInstance() {
        return new AccountDetailFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_account_detail, container, false);

        saveAccountButton = root.findViewById(R.id.saveAccountButton);
        accountNameEditText = root.findViewById(R.id.accountNameEditText);
        accountNameTextInputLayout = root.findViewById(R.id.accountNameTextInputLayout);
        accountImageCardView = root.findViewById(R.id.accountImageCardView);
        accountImageView = root.findViewById(R.id.accountImageView);

        mLayout = root.findViewById(R.id.account_detail_layout);


        if (getArguments() != null
                && getArguments().containsKey("account_id")
                && getArguments().containsKey("account_name")){

            String account_id = getArguments().getString("account_id");
            String account_name = getArguments().getString("account_name");

            mAccount = new AccountVersion2(account_id, account_name);

            accountNameEditText.setText(mAccount.getName());

            if (getArguments().containsKey("account_image")
            && getArguments().getString("account_image") != null){

                String account_image = getArguments().getString("account_image");
                mAccount.setAccountImage(account_image);
                accountImageView.setImageBitmap(BitmapUtil.base64ToBitmap(mAccount.getAccountImage()));
            }
        }


        saveAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameIsEmpty()) accountNameTextInputLayout.setError(getString(R.string.name_is_required));

                if (validateSaveAccount()){
                    Toast.makeText(v.getContext(), accountNameEditText.getText().toString(), Toast.LENGTH_SHORT).show();


                    if (mAccount == null) {
                        mAccountViewModel.updateAccount(accountNameEditText.getText().toString());
                    } else {
                        mAccount.setName(accountNameEditText.getText().toString());
                        mAccountViewModel.updateAccount(mAccount);
                    }
                    NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                    navController.popBackStack();
                }
            }
        });

        accountImageCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyPermissions();
            }
        });

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAccountViewModel = ViewModelProviders.of(this).get(AccountViewModel.class);


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String uid_user = preferences.getString(getString(R.string.saved_uid_user_key), "");

        mAccountViewModel.initialize(uid_user);
        // TODO: Use the ViewModel
    }

    private boolean nameIsEmpty(){
        return accountNameEditText.getText().toString().isEmpty();
    }


    private boolean validateSaveAccount(){
        return !nameIsEmpty();
    }


    private void verifyPermissions() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // request the permission
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE);
        }
        else {
            // has the permission.
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE) {
            selectImage();
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                cropUri(BitmapUtil.getImageUri(getActivity(), bitmap));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == REQUEST_CODE_CROP) {
            if (resultCode != RESULT_OK) {
                Toast.makeText(getActivity(), getString(R.string.crop_image_failure_message), Toast.LENGTH_LONG).show();
                return;
            }


            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                String bitmapString = BitmapUtil.bitmapToBase64(bitmap);

                if (mAccount != null){
                    mAccount.setAccountImage(bitmapString);
                }
                accountImageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }
}