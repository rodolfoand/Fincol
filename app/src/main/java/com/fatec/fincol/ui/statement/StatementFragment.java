package com.fatec.fincol.ui.statement;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fatec.fincol.R;
import com.fatec.fincol.model.AccountVersion2;
import com.fatec.fincol.model.Transaction;
import com.fatec.fincol.ui.account.AccountListAdapter;

import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StatementFragment extends Fragment {

    private StatementViewModel statementViewModel;
    private Spinner statementSpinner;

    private RecyclerView statementRecyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private TextView balanceStateTextView;
    private TextView symbolCurrencyStateTextView;

    private String account;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        statementViewModel =
                ViewModelProviders.of(this).get(StatementViewModel.class);
        View root = inflater.inflate(R.layout.fragment_statement, container, false);

        balanceStateTextView = root.findViewById(R.id.balanceStateTextView);
        symbolCurrencyStateTextView = root.findViewById(R.id.symbolCurrencyStateTextView);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String user = preferences.getString(getString(R.string.saved_uid_user_key), "");
        account = preferences.getString(getString(R.string.saved_account_id_key), "");

        statementViewModel.initialize(user);

        statementSpinner = root.findViewById(R.id.statementSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.period_list, android.R.layout.simple_spinner_dropdown_item);
        statementSpinner.setAdapter(adapter);
        statementSpinner.setSelection(2);

        statementRecyclerView = root.findViewById(R.id.statementRecyclerView);

        statementRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        statementRecyclerView.setLayoutManager(layoutManager);

        final StatementListAdapter statementListAdapter = new StatementListAdapter(getActivity());
        statementRecyclerView.setAdapter(statementListAdapter);

        updateListStatement(30);

        statementViewModel.mTransactionList.observe(getActivity(), new Observer<List<Transaction>>() {
            @Override
            public void onChanged(List<Transaction> transactions) {
                statementListAdapter.setTransactionList(transactions);
            }
        });

        statementViewModel.mActiveAccount.observe(getActivity(), new Observer<AccountVersion2>() {
            @Override
            public void onChanged(AccountVersion2 accountVersion2) {
                String formatted = NumberFormat.getCurrencyInstance(Locale.getDefault()).format(accountVersion2.getBalance());

                String replaceable = String.format("[%s\\s]", NumberFormat.getCurrencyInstance(Locale.getDefault()).getCurrency().getSymbol());
                String cleanString = formatted.replaceAll(replaceable, "");
                symbolCurrencyStateTextView.setText(NumberFormat.getCurrencyInstance(Locale.getDefault()).getCurrency().getSymbol() + " ");
                balanceStateTextView.setText(cleanString);
            }
        });

        statementSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), position + "", Toast.LENGTH_SHORT).show();

                int days;

                switch (position){
                    case 0:
                        days = 7;
                        break;
                    case 1:
                        days = 15;
                        break;
                    case 2:
                        days = 30;
                        break;
                    case 3:
                        days = 60;
                        break;
                    case 4:
                        days = 90;
                        break;
                    default:
                        days = 0;
                        break;
                }

                updateListStatement(days);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return root;
    }

    private void updateListStatement(int days){

        Calendar filterDate = Calendar.getInstance();

        filterDate.add(Calendar.DATE, -days);
        filterDate.set(Calendar.HOUR_OF_DAY, 0);
        filterDate.set(Calendar.MINUTE, 0);
        filterDate.set(Calendar.SECOND, 0);
        filterDate.set(Calendar.MILLISECOND, 0);

        Timestamp filter = new Timestamp(filterDate.getTimeInMillis());

        statementViewModel.getTransactions(account, filter);
    }
}