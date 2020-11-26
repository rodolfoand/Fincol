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
import com.fatec.fincol.model.Collaborator;
import com.fatec.fincol.util.BitmapUtil;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class AccountListAdapter extends RecyclerView.Adapter<AccountListAdapter.MyAccountsViewHolder> {


    public class MyAccountsViewHolder extends RecyclerView.ViewHolder {

        private final TextView accountNameCardTextView;
        private final MaterialCardView accountsCardView;
        private final ImageView accountCardimageView;
        private final TextView accountTypeCardTextView;
        private final TextView accountStatusCardTextView;
        private final MaterialButton acceptCollabButton;
        private final MaterialButton rejectCollabButton;
        private final ImageView accountActiveimageView;
        private final ImageView accountInactiveimageView;



        public MyAccountsViewHolder(@NonNull View itemView) {
            super(itemView);
            accountNameCardTextView = itemView.findViewById(R.id.accountNameCardTextView);
            accountsCardView = itemView.findViewById(R.id.accountsCardView);
            accountCardimageView = itemView.findViewById(R.id.accountCardimageView);
            accountTypeCardTextView = itemView.findViewById(R.id.accountTypeCardTextView);
            accountStatusCardTextView = itemView.findViewById(R.id.accountStatusCardTextView);
            acceptCollabButton = itemView.findViewById(R.id.acceptCollabButton);
            rejectCollabButton = itemView.findViewById(R.id.rejectCollabButton);
            accountActiveimageView = itemView.findViewById(R.id.accountActiveimageView);
            accountInactiveimageView = itemView.findViewById(R.id.accountInactiveimageView);
        }
    }

    private final LayoutInflater mInflater;
    private List<AccountVersion2> mMyAccounts; // Cached copy of words
    private CallFragNavigation mCallFragNavigation;
    private CallDeleteAccount mDeleteAccount;
    private CallSetActiveAccount mSetActiveAccount;
    private CallSetInactiveAccount mSetInactiveAccount;
    private CallRejectAccount mRejectAccount;
    private List<Collaborator> mCollabs;

    public AccountListAdapter(Context context,
                              CallFragNavigation callFragNavigation,
                              CallDeleteAccount callDeleteAccount,
                              CallSetActiveAccount callSetActiveAccount,
                              CallSetInactiveAccount callSetInactiveAccount,
                              CallRejectAccount callRejectAccount) {
        mInflater = LayoutInflater.from(context);
        this.mCallFragNavigation = callFragNavigation;
        this.mDeleteAccount = callDeleteAccount;
        this.mSetActiveAccount = callSetActiveAccount;
        this.mSetInactiveAccount = callSetInactiveAccount;
        this.mRejectAccount = callRejectAccount;
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


        }
        if (mCollabs != null) {
            Collaborator collabCurrent = mCollabs.get(position);
            holder.accountTypeCardTextView.setText(collabCurrent.getType().getName());
            holder.accountStatusCardTextView.setText(collabCurrent.getStatus().getName());

            if (collabCurrent.getStatus() == Collaborator.StatusColl.PENDING){
                holder.acceptCollabButton.setVisibility(View.VISIBLE);
                holder.rejectCollabButton.setVisibility(View.VISIBLE);
                holder.accountActiveimageView.setVisibility(View.INVISIBLE);
                holder.accountInactiveimageView.setVisibility(View.INVISIBLE);
            }
            if (collabCurrent.getStatus() == Collaborator.StatusColl.ACTIVE){
                holder.accountActiveimageView.setVisibility(View.VISIBLE);
                holder.acceptCollabButton.setVisibility(View.INVISIBLE);
                holder.rejectCollabButton.setVisibility(View.INVISIBLE);
                holder.accountInactiveimageView.setVisibility(View.INVISIBLE);
            }
            if (collabCurrent.getStatus() == Collaborator.StatusColl.INACTIVE){
                holder.accountInactiveimageView.setVisibility(View.VISIBLE);
                holder.acceptCollabButton.setVisibility(View.INVISIBLE);
                holder.rejectCollabButton.setVisibility(View.INVISIBLE);
                holder.accountActiveimageView.setVisibility(View.INVISIBLE);
            }
        }

        if (mMyAccounts != null && mCollabs != null){
            AccountVersion2 current = mMyAccounts.get(position);
            Collaborator collabCurrent = mCollabs.get(position);

            holder.accountsCardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {


                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

                    builder.setTitle(R.string.active_account);
                    builder.setMessage(R.string.are_you_sure);

                    builder.setPositiveButton(R.string.active, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing but close the dialog

                            mSetActiveAccount.setActive(current.getId());

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

                    if (collabCurrent.getStatus() == Collaborator.StatusColl.INACTIVE)
                        alert.show();


//
//                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
//
//                    builder.setTitle(R.string.delete_account);
//                    builder.setMessage(R.string.are_you_sure);
//
//                    builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
//
//                        public void onClick(DialogInterface dialog, int which) {
//                            // Do nothing but close the dialog
//
//                            if (!current.getName().equals("Main"))
//                                mDeleteAccount.deleteAccount(current.getId());
//                            else Toast.makeText(v.getContext(), R.string.delete_main_account, Toast.LENGTH_LONG).show();
//
//                            dialog.dismiss();
//                        }
//                    });
//
//                    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//
//                            // Do nothing
//                            dialog.dismiss();
//                        }
//                    });
//
//                    AlertDialog alert = builder.create();
//                    alert.show();
                    return false;
                }
            });

            holder.acceptCollabButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSetInactiveAccount.setInactive(current.getId());
                }
            });

            holder.rejectCollabButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mRejectAccount.reject(current.getId());
                }
            });
        }
    }

    public void setMyAccounts(List<AccountVersion2> accounts){
        mMyAccounts = accounts;
        notifyDataSetChanged();
    }


    public void setCollabs(List<Collaborator> collabs){
        mCollabs = collabs;
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

    public interface CallSetActiveAccount{
        void setActive(String account_id);
    }

    public interface CallSetInactiveAccount{
        void setInactive(String account_id);
    }

    public interface CallRejectAccount{
        void reject(String account_id);
    }
}
