package com.fatec.fincol.ui.account;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fatec.fincol.R;
import com.fatec.fincol.model.AccountVersion2;

import java.util.List;

public class AccountListFragment extends Fragment implements AccountListAdapter.CallFragNavigation, AccountListAdapter.CallDeleteAccount {

    private AccountViewModel mAccountViewModel;
    private Button newAccountButton;
    private RecyclerView myAccountsRecyclerView;
    private RecyclerView.LayoutManager layoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mAccountViewModel =
                ViewModelProviders.of(this).get(AccountViewModel.class);
        View root = inflater.inflate(R.layout.fragment_account_list, container, false);


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String uid_user = preferences.getString(getString(R.string.saved_uid_user_key), "");

        mAccountViewModel.initialize(uid_user);

        newAccountButton = root.findViewById(R.id.newAccountButton);
        myAccountsRecyclerView = root.findViewById(R.id.myAccountsRecyclerView);

        myAccountsRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        myAccountsRecyclerView.setLayoutManager(layoutManager);


        final AccountListAdapter adapter = new AccountListAdapter(getActivity(), this::navEdidAccountFrag, this::deleteAccount);
        myAccountsRecyclerView.setAdapter(adapter);


//        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
//        String uid_user = sharedPref.getString(getString(R.string.preference_file_key), "Not found");
//        Log.d("User", uid_user);



        mAccountViewModel.mMyAccountList.observe(getActivity(), new Observer<List<AccountVersion2>>() {
            @Override
            public void onChanged(List<AccountVersion2> accountVersion2s) {
                Log.d("Account", accountVersion2s.size() + " T");
                adapter.setMyAccounts(accountVersion2s);
                for (AccountVersion2 account : accountVersion2s){
                    Log.d("AccountFrag", account.getName());
                }
            }
        });


        newAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navEditAccountFragment();
            }
        });



        return root;
    }


    public void navEditAccountFragment(){
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        navController.navigate(R.id.action_nav_account_to_nav_new_account);
    }

    public void navEditAccountFragment(String account_id, String account_name, String account_image){
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        Bundle bundle = new Bundle();
        bundle.putString("account_id", account_id);
        bundle.putString("account_name", account_name);
        //bundle.putString("account_image", account_image);
        navController.navigate(R.id.action_nav_account_to_nav_new_account, bundle);
    }

    @Override
    public void navEdidAccountFrag(String account_id, String account_name, String account_image) {
        navEditAccountFragment(account_id, account_name, account_image);
    }

    @Override
    public void deleteAccount(String account_id) {
        mAccountViewModel.deleteAccount(account_id);
        Toast.makeText(getActivity(), R.string.account_deleted, Toast.LENGTH_SHORT).show();
    }
}