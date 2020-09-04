package com.fatec.fincol;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

public class TransactionActivity extends AppCompatActivity {

    String[] TRANSACTIONS = new String[] {"Income", "Expense"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                        R.layout.dropdown_menu_popup_item,
                        TRANSACTIONS);

        AutoCompleteTextView editTextFilledExposedDropdown = findViewById(R.id.filled_exposed_dropdown);
        editTextFilledExposedDropdown.setAdapter(adapter);
    }


}