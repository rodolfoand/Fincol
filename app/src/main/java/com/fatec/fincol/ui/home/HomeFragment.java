package com.fatec.fincol.ui.home;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.fatec.fincol.R;
import com.fatec.fincol.model.AccountVersion2;
import com.fatec.fincol.model.CategoryExpense;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private HomeFragViewModel mHomeViewModel;
    private TextView symbolCurrencyTextView;
    private TextView balanceValueTextView;
    private FloatingActionButton transactFloatingActionButton;
    private PieChart piechart;
    private AccountVersion2 accountObj;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mHomeViewModel =
                ViewModelProviders.of(this).get(HomeFragViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        balanceValueTextView = root.findViewById(R.id.balanceValueTextView);
        symbolCurrencyTextView = root.findViewById(R.id.symbolCurrencyTextView);
        transactFloatingActionButton = root.findViewById(R.id.transactFloatingActionButton);

        //ini

        PieChart pieChart = root.findViewById(R.id.piechart);


        //fim

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String user = preferences.getString(getString(R.string.saved_uid_user_key), "");
        String account = preferences.getString(getString(R.string.saved_account_id_key), "");


        mHomeViewModel.initialize(user);

        if (!account.equals("")) mHomeViewModel.getCategories(account);


        mHomeViewModel.mCategoryList.observe(getActivity(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                List<String> stringList = new ArrayList<>();
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH) + 1;
                String concateName = "-" + month + year;
                for (String categoryExpense : strings){
                    stringList.add(categoryExpense + concateName);
                    Log.d("cat2", categoryExpense + concateName);
                }


                mHomeViewModel.getCategoryExpenses(account, stringList);

            }
        });


        transactFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.action_nav_home_to_transactionFragment);
            }
        });

        pieChart.clear();
        pieChart.invalidate();
        mHomeViewModel.mActiveAccount.observe(getActivity(), new Observer<AccountVersion2>() {
            @Override
            public void onChanged(AccountVersion2 accountVersion2) {
                accountObj = accountVersion2;


                String formatted = NumberFormat.getCurrencyInstance(Locale.getDefault()).format(accountVersion2.getBalance());

                String replaceable = String.format("[%s\\s]", NumberFormat.getCurrencyInstance(Locale.getDefault()).getCurrency().getSymbol());
                String cleanString = formatted.replaceAll(replaceable, "");
                symbolCurrencyTextView.setText(NumberFormat.getCurrencyInstance(Locale.getDefault()).getCurrency().getSymbol() + " ");
                balanceValueTextView.setText(cleanString);
                //pieChart.invalidate();


                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(getString(R.string.saved_account_id_key),accountVersion2.getId());
                editor.apply();
                pieChart.clear();
                pieChart.invalidate();


                mHomeViewModel.mCategoryExpenseList.observe(getActivity(), new Observer<List<CategoryExpense>>() {
                    @Override
                    public void onChanged(List<CategoryExpense> categoryExpenses) {

                        pieChart.clear();
                        pieChart.invalidate();
                        List<PieEntry> entries = new ArrayList<>();
                        entries.clear();

                        Log.d("acc", account);

                        float total = 0;

                        for(CategoryExpense catExp : categoryExpenses){
                            total -= catExp.getValue();
                        }

                        if (accountObj != null && accountObj.getBalance() > 0
                        ){
                            total -= - accountObj.getBalance();
                            entries.add(new PieEntry(- 100 *  - accountObj.getBalance().floatValue() / total, getString(R.string.balance_now)));
                        }


                        for (CategoryExpense catExp : categoryExpenses){
                            //entries.add(new PieEntry(catExp.getValue(), catExp.getName()));
                            entries.add(new PieEntry(- 100 * catExp.getValue() / total, catExp.getName()));
                            //Log.d("cat2", catExp.getName() + catExp.getValue());
                        }
//
//                entries.add(new PieEntry(18.5f, "Green"));
//                entries.add(new PieEntry(26.7f, "Yellow"));
//                entries.add(new PieEntry(24.0f, "Red"));
//                entries.add(new PieEntry(30.8f, "Blue"));
                        ArrayList<Integer> colors = new ArrayList<>();
                        colors.add(Color.GRAY);
                        colors.add(Color.BLUE);
                        colors.add(Color.RED);
                        colors.add(Color.GREEN);
                        colors.add(Color.CYAN);
                        colors.add(Color.YELLOW);
                        colors.add(Color.DKGRAY);
                        colors.add(Color.MAGENTA);
                        colors.add(Color.WHITE);
                        colors.add(Color.BLACK);
                        colors.add(Color.LTGRAY);

                        PieDataSet set = new PieDataSet(entries, "");
                        PieData data = new PieData(set);
                        set.setColors(colors);
                        set.setValueTextSize(20);
                        set.setSliceSpace(2);
                        set.setLabel("Categories");
                        pieChart.setData(data);

                        pieChart.invalidate(); // refresh
                    }
                });

            }
        });

        return root;
    }
}