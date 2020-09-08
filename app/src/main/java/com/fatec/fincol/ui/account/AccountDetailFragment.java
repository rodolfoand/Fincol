package com.fatec.fincol.ui.account;

import androidx.lifecycle.ViewModelProviders;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.fatec.fincol.R;
import com.fatec.fincol.model.AccountVersion2;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AccountDetailFragment extends Fragment {

    private AccountViewModel mAccountViewModel;

    private TextInputLayout accountNameTextInputLayout;
    private TextInputEditText accountNameEditText;
    private Button saveAccountButton;
    private AccountVersion2 mAccount;

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

}