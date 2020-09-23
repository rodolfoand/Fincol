package com.fatec.fincol.ui.account;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.fatec.fincol.R;
import com.fatec.fincol.model.AccountVersion2;
import com.fatec.fincol.util.BitmapUtil;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class AccountListAdapter extends RecyclerView.Adapter<AccountListAdapter.MyAccountsViewHolder> {


    public class MyAccountsViewHolder extends RecyclerView.ViewHolder {

        private final TextView accountNameCardTextView;
        private final MaterialCardView accountsCardView;
        private final ImageView accountCardimageView;

        public MyAccountsViewHolder(@NonNull View itemView) {
            super(itemView);
            accountNameCardTextView = itemView.findViewById(R.id.accountNameCardTextView);
            accountsCardView = itemView.findViewById(R.id.accountsCardView);
            accountCardimageView = itemView.findViewById(R.id.accountCardimageView);
        }
    }

    private final LayoutInflater mInflater;
    private List<AccountVersion2> mMyAccounts; // Cached copy of words
    private CallFragNavigation mCallFragNavigation;
    private CallDeleteAccount mDeleteAccount;

    public AccountListAdapter(Context context, CallFragNavigation callFragNavigation, CallDeleteAccount callDeleteAccount) {
        mInflater = LayoutInflater.from(context);
        this.mCallFragNavigation = callFragNavigation;
        this.mDeleteAccount = callDeleteAccount;
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
            holder.accountCardimageView.setImageBitmap(BitmapUtil.base64ToBitmap(current.getAccountImage()));
            holder.accountsCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallFragNavigation.navEdidAccountFrag(current.getId(), current.getName(), current.getAccountImage());
                }
            });

            holder.accountsCardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

                    builder.setTitle(R.string.delete_account);
                    builder.setMessage(R.string.are_you_sure);

                    builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing but close the dialog

                            if (!current.getName().equals("Main"))
                                mDeleteAccount.deleteAccount(current.getId());
                            else Toast.makeText(v.getContext(), R.string.delete_main_account, Toast.LENGTH_LONG).show();

                            dialog.dismiss();
                        }
                    });

                    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            // Do nothing
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();
                    return false;
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

    public interface CallDeleteAccount{
        void deleteAccount(String account_id);
    }

}
