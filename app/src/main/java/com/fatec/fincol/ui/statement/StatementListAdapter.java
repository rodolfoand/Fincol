package com.fatec.fincol.ui.statement;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.fatec.fincol.R;
import com.fatec.fincol.model.AccountVersion2;
import com.fatec.fincol.model.Transaction;
import com.fatec.fincol.ui.account.AccountListAdapter;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class StatementListAdapter extends RecyclerView.Adapter<StatementListAdapter.StatementViewHolder> {

    public class StatementViewHolder extends RecyclerView.ViewHolder {

        private final TextView dateCardTransactTextView;
        private final TextView descCardTransactTextView;
        private final TextView valueCardTransactTextView;
        private final CardView expenseCardView;
        private final CardView incomeCardView;

        public StatementViewHolder(@NonNull View itemView) {
            super(itemView);
            dateCardTransactTextView = itemView.findViewById(R.id.dateCardTransactTextView);
            descCardTransactTextView = itemView.findViewById(R.id.descCardTransactTextView);
            valueCardTransactTextView = itemView.findViewById(R.id.valueCardTransactTextView);
            expenseCardView = itemView.findViewById(R.id.expenseCardView);
            incomeCardView = itemView.findViewById(R.id.incomeCardView);
        }
    }

    private final LayoutInflater mInflater;
    private List<Transaction> mTransactionList; // Cached copy of words

    public StatementListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        mTransactionList = new ArrayList<>();
    }

    @NonNull
    @Override
    public StatementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.card_transaction, parent, false);
        return new StatementViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StatementViewHolder holder, int position) {
        if (mTransactionList != null) {

            Transaction current = mTransactionList.get(position);

            SimpleDateFormat dfDate = new SimpleDateFormat("dd/MM/yyyy");
            String date = dfDate.format(current.getDate());

            holder.dateCardTransactTextView.setText(date);

            if ((position - 1 >= 0 && date.equals(dfDate.format(mTransactionList.get(position-1).getDate())))){
                holder.dateCardTransactTextView.setVisibility(View.INVISIBLE);
            } else {
                holder.dateCardTransactTextView.setVisibility(View.VISIBLE);
            }

            if (current.getType().equals(Transaction.Type.Expense)) {
                holder.incomeCardView.setVisibility(View.INVISIBLE);
                holder.expenseCardView.setVisibility(View.VISIBLE);
            } else {
                holder.expenseCardView.setVisibility(View.INVISIBLE);
                holder.incomeCardView.setVisibility(View.VISIBLE);
            }

            holder.descCardTransactTextView.setText(current.getDescription());

            String formatted = NumberFormat.getCurrencyInstance(Locale.getDefault()).format(current.getValue());

            String replaceable = String.format("[%s\\s]", NumberFormat.getCurrencyInstance(Locale.getDefault()).getCurrency().getSymbol());
            String cleanString = formatted.replaceAll(replaceable, "");
            //symbolCurrencyTextView.setText(NumberFormat.getCurrencyInstance(Locale.getDefault()).getCurrency().getSymbol() + " ");
            String symbolCurrency = NumberFormat.getCurrencyInstance(Locale.getDefault()).getCurrency().getSymbol() + " ";
            holder.valueCardTransactTextView.setText(symbolCurrency + " " +cleanString);
        }
    }

    @Override
    public int getItemCount() {
        if (mTransactionList != null)
            return mTransactionList.size();
        else return 0;
    }


    public void setTransactionList(List<Transaction> t){
        mTransactionList.clear();
        Collections.sort(t);
        Collections.reverse(t);
        mTransactionList = t;
        notifyDataSetChanged();
    }


}
