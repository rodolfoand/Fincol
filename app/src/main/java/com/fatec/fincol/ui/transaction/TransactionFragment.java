package com.fatec.fincol.ui.transaction;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.fatec.fincol.R;
import com.fatec.fincol.model.Transaction;
import com.fatec.fincol.ui.account.AccountListAdapter;
import com.fatec.fincol.ui.account.AccountViewModel;
import com.fatec.fincol.ui.category.CategoryListAdapter;
import com.fatec.fincol.ui.home.HomeFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TransactionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TransactionFragment extends Fragment
        implements CategoryListAdapter.AddSelectedCategory,
        CategoryListAdapter.RemoveSelectedCategory {

    private Spinner transactionSpinner;
    private EditText valueTransactEditText;
    private EditText dateTransactEditText;
    private TextView dateTransactTextView;
    private Spinner repeatTransactSpinner;
    private CheckBox repeatTransactCheckBox;
    private CheckBox fixedTransactCheckBox;
    private Button saveTransactButton;
    private TextInputLayout valueTransactTextInputLayout;
    private EditText locationTransactEditText;
    private EditText descTransactEditText;
    private MaterialButton addCategoryTransactButton;

    private TransactionViewModel mTransactionViewModel;

    private String account;

    private List<String> categoryList;
    private List<String> selectedCategoryList;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TransactionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TransactionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TransactionFragment newInstance(String param1, String param2) {
        TransactionFragment fragment = new TransactionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root = inflater.inflate(R.layout.fragment_transaction, container, false);
        mTransactionViewModel = ViewModelProviders.of(this).get(TransactionViewModel.class);

        categoryList = new ArrayList<>();
        selectedCategoryList = new ArrayList<>();


        saveTransactButton = root.findViewById(R.id.saveTransactButton);
        valueTransactTextInputLayout = root.findViewById(R.id.valueTransactTextInputLayout);
        locationTransactEditText = root.findViewById(R.id.locationTransactEditText);
        descTransactEditText = root.findViewById(R.id.descTransactEditText);
        addCategoryTransactButton = root.findViewById(R.id.addCategoryTransactButton);

        transactionSpinner = root.findViewById(R.id.transactionSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.transaction_list, android.R.layout.simple_spinner_dropdown_item);
        transactionSpinner.setAdapter(adapter);

        repeatTransactSpinner = root.findViewById(R.id.repeatTransactSpinner);
        ArrayAdapter<CharSequence> adapterRepeat = ArrayAdapter.createFromResource(getActivity(), R.array.repeat_list, android.R.layout.simple_spinner_dropdown_item);
        repeatTransactSpinner.setAdapter(adapterRepeat);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        account = preferences.getString(getString(R.string.saved_account_id_key), "");

        mTransactionViewModel.getCategories(account);
        mTransactionViewModel.mCategoryList.observe(getActivity(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                categoryList = strings;
                Log.d("catlist", strings.toString());
            }
        });

        repeatTransactCheckBox = root.findViewById(R.id.repeatTransactCheckBox);
        repeatTransactCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    repeatTransactSpinner.setVisibility(View.VISIBLE);
                    fixedTransactCheckBox.setChecked(false);
                }
                else repeatTransactSpinner.setVisibility(View.INVISIBLE);
            }
        });


        fixedTransactCheckBox = root.findViewById(R.id.fixedTransactCheckBox);
        fixedTransactCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) repeatTransactCheckBox.setChecked(false);
            }
        });


        dateTransactTextView = root.findViewById(R.id.dateTransactTextView);
        Calendar date;
        date = Calendar.getInstance();
        SimpleDateFormat dfDate = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        String formatted = dfDate.format(date.getTime());
        dateTransactTextView.setText(formatted);
        dateTransactTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar currentDate = Calendar.getInstance();
                Calendar date;
                date = Calendar.getInstance();
                new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        date.set(year, monthOfYear, dayOfMonth);

                        date.set(Calendar.HOUR_OF_DAY, 0);
                        date.set(Calendar.MINUTE, 0);
                        date.set(Calendar.SECOND, 0);
                        date.set(Calendar.MILLISECOND, 0);

                        if (date.before(Calendar.getInstance())){
                            Toast.makeText(getContext(), R.string.select_future_date, Toast.LENGTH_SHORT).show();
                        } else{
                            SimpleDateFormat dfDate = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
                            String formatted = dfDate.format(date.getTime());
                            dateTransactTextView.setText(formatted);
                        }
//                        new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
//                            @Override
//                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                                date.set(Calendar.HOUR_OF_DAY, hourOfDay);
//                                date.set(Calendar.MINUTE, minute);
//                                //Log.v(TAG, "The choosen one " + date.getTime());
//                                SimpleDateFormat dfDate = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
//                                String formatted = dfDate.format(date.getTime());
//                                dateTransactTextView.setText(formatted);
//                            }
//                        }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
                    }
                }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
            }
        });




        valueTransactEditText = root.findViewById(R.id.valueTransactEditText);
        valueTransactEditText.setText("0,00");
        valueTransactEditText.addTextChangedListener(new TextWatcher() {
            private WeakReference<EditText> editTextWeakReference = new WeakReference<>(valueTransactEditText);
            private Locale locale = Locale.getDefault();

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                EditText editText = editTextWeakReference.get();
                if (editText == null) return;
                editText.removeTextChangedListener(this);

                BigDecimal parsed = parseToBigDecimal(s.toString());
                String formatted = NumberFormat.getCurrencyInstance(locale).format(parsed);

                String replaceable = String.format("[%s\\s]", NumberFormat.getCurrencyInstance(Locale.getDefault()).getCurrency().getSymbol());
                String cleanString = formatted.replaceAll(replaceable, "");

                editText.setText(cleanString);
                editText.setSelection(cleanString.length());
                editText.addTextChangedListener(this);

            }

        });

        saveTransactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!valueTransactEditText.getText().toString().equals("0,00")){
                    valueTransactTextInputLayout.setError(null);
                    BigDecimal valueBig = parseToBigDecimal(valueTransactEditText.getText().toString());
                    Double value = valueBig.doubleValue();
                    Transaction.Type type = Transaction.Type.get(transactionSpinner.getSelectedItem().toString());
                    if (type == Transaction.Type.Expense) value *= -1;

                    Calendar dateTransact = Calendar.getInstance();

                    try {
                        dateTransact.setTime(dfDate.parse(dateTransactTextView.getText().toString()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Timestamp timestamp = new Timestamp(dateTransact.getTimeInMillis());

                    int repeatTime = repeatTransactSpinner.getSelectedItemPosition() + 2;

                    Transaction transaction = new Transaction(
                            type,
                            timestamp,
                            value,
                            locationTransactEditText.getText().toString(),
                            descTransactEditText.getText().toString(),
                            fixedTransactCheckBox.isChecked(),
                            repeatTransactCheckBox.isChecked(),
                            repeatTime
                    );


                    mTransactionViewModel.addTransaction(account, transaction, selectedCategoryList).observe(getActivity(), new Observer<String>() {
                        @Override
                        public void onChanged(String s) {
                            NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                            navController.popBackStack();
                            Toast.makeText(getActivity(), getString(R.string.transaction_inserted), Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    valueTransactTextInputLayout.setError(getString(R.string.value_is_required));
                }


            }
        });

        addCategoryTransactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                // Get the layout inflater
                LayoutInflater inflater = getActivity().getLayoutInflater();

                View dialogLayout = inflater.inflate(R.layout.dialog_category, null);


                RecyclerView categoryRecyclerView = dialogLayout.findViewById(R.id.categoryRecyclerView);
                categoryRecyclerView.setHasFixedSize(true);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                categoryRecyclerView.setLayoutManager(layoutManager);

                final CategoryListAdapter adapterCategory = new CategoryListAdapter(getActivity(),
                        TransactionFragment.this::addSelectedCategory,
                        TransactionFragment.this::removeSelectedCategory);

                categoryRecyclerView.setAdapter(adapterCategory);
                adapterCategory.setCategoryList(categoryList, selectedCategoryList);
                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                builder.setView(dialogLayout)
                        // Add action buttons
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                // sign in the user ...
                                adapterCategory.setCategoryList(categoryList, selectedCategoryList);
                            }
                        });
                builder.create().show();

                MaterialButton addCategoryButton = dialogLayout.findViewById(R.id.addCategoryButton);
                TextInputEditText nameCategoryTextInputEditText = dialogLayout.findViewById(R.id.nameCategoryTextInputEditText);

                TextInputLayout nameCategoryTextInputLayout = dialogLayout.findViewById(R.id.nameCategoryTextInputLayout);

                addCategoryButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (nameCategoryTextInputEditText.getText().toString().isEmpty()){
                            nameCategoryTextInputLayout.setError(getString(R.string.category_required));
                        } else {
                            nameCategoryTextInputLayout.setError(null);
                            categoryList.add(nameCategoryTextInputEditText.getText().toString());
                            adapterCategory.setCategoryList(categoryList, selectedCategoryList);
                        }
                    }
                });


            }
        });

        return root;
    }

    private BigDecimal parseToBigDecimal(String value) {
        String replaceable = String.format("[%s,.\\s]", NumberFormat.getCurrencyInstance(Locale.getDefault()).getCurrency().getSymbol());

        String cleanString = value.replaceAll(replaceable, "");

        try {
            return new BigDecimal(cleanString).setScale(
                    2, BigDecimal.ROUND_FLOOR).divide(new BigDecimal(100), BigDecimal.ROUND_FLOOR);
        } catch (NumberFormatException e) {
            //ao apagar todos valores de uma só vez dava erro
            //Com a exception o valor retornado é 0.00
            return new BigDecimal(0);

        }
    }

    @Override
    public void addSelectedCategory(String category) {
        selectedCategoryList.add(category);
    }

    @Override
    public void removeSelectedCategory(String category) {
        selectedCategoryList.remove(category);
    }
}