package com.fatec.fincol.ui.account;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fatec.fincol.R;
import com.fatec.fincol.model.AccountVersion2;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class AccountListAdapter extends RecyclerView.Adapter<AccountListAdapter.MyAccountsViewHolder> {


    public class MyAccountsViewHolder extends RecyclerView.ViewHolder {

        private final TextView accountNameCardTextView;
        private final MaterialCardView accountsCardView;

        public MyAccountsViewHolder(@NonNull View itemView) {
            super(itemView);
            accountNameCardTextView = itemView.findViewById(R.id.accountNameCardTextView);
            accountsCardView = itemView.findViewById(R.id.accountsCardView);
        }
    }

    private final LayoutInflater mInflater;
    private List<AccountVersion2> mMyAccounts; // Cached copy of words
    private CallFragNavigation mCallFragNavigation;

    public AccountListAdapter(Context context, CallFragNavigation callFragNavigation) {
        mInflater = LayoutInflater.from(context);
        this.mCallFragNavigation = callFragNavigation;
    }

    @NonNull
    @Override
    public MyAccountsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.card_my_accounts, parent, false);
        return new MyAccountsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAccountsViewHolder holder, int position) {
        if (mMyAccounts != null) {
            AccountVersion2 current = mMyAccounts.get(position);
            holder.accountNameCardTextView.setText(current.getName());
            holder.accountsCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallFragNavigation.navEdidAccountFrag(current.getId(), current.getName(), current.getAccountImage());
                }
            });
        }
    }

    public void setMyAccounts(List<AccountVersion2> accounts){
        mMyAccounts = accounts;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mMyAccounts != null)
            return mMyAccounts.size();
        else return 0;
    }

    public interface CallFragNavigation{
        void navEdidAccountFrag(String account_id, String account_name, String account_image);
    }

}
