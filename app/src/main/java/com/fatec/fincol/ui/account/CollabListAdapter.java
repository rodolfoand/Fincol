package com.fatec.fincol.ui.account;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fatec.fincol.R;
import com.fatec.fincol.model.Collaborator;
import com.fatec.fincol.model.User;

import java.util.List;


public class CollabListAdapter extends RecyclerView.Adapter<CollabListAdapter.CollabViewHolder> {


    public class CollabViewHolder extends RecyclerView.ViewHolder {
        private final TextView collabNameCardTextView;
        private final ImageView collabCardimageView;
        private final TextView collabTypeCardTextView;
        private final TextView collabStatusCardTextView;
        private final TextView collabEmailCardTextView;


        public CollabViewHolder(@NonNull View itemView) {
            super(itemView);
            collabNameCardTextView = itemView.findViewById(R.id.collabNameCardTextView);
            collabCardimageView = itemView.findViewById(R.id.collabCardimageView);
            collabTypeCardTextView = itemView.findViewById(R.id.collabTypeCardTextView);
            collabStatusCardTextView = itemView.findViewById(R.id.collabStatusCardTextView);
            collabEmailCardTextView = itemView.findViewById(R.id.collabEmailCardTextView);
        }
    }

    private final LayoutInflater mInflater;
    private List<User> mUsers; // Cached copy of words
    private List<Collaborator> mCollabs;

    public CollabListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public CollabListAdapter.CollabViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.card_collabs, parent, false);
        return new CollabListAdapter.CollabViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CollabListAdapter.CollabViewHolder holder, int position) {
        Log.d("collabList", "position: " + position);
        if (mUsers != null) {
            Log.d("CollabU", "mUsers: " + mUsers.toString());
            Log.d("CollabU", "mUsers.size: " + mUsers.size());
            User current = mUsers.get(position);
            holder.collabNameCardTextView.setText(current.getName());
            holder.collabEmailCardTextView.setText(current.getEmail());

        }
        if (mCollabs != null){
            Log.d("CollabU", "mCollabs: " + mCollabs.toString());
            Log.d("CollabU", "mCollabs.size: " + mCollabs.size());
            Collaborator collabCurrent = mCollabs.get(position);
            holder.collabTypeCardTextView.setText(collabCurrent.getType().getName());
            holder.collabStatusCardTextView.setText(collabCurrent.getStatus().getName());

        }

    }

    public void setUsers(List<User> users){
        Log.d("collabList", "users: " + users.size());
        mUsers = users;
        notifyDataSetChanged();
    }

    public void setCollabs(List<Collaborator> collabs){
        mCollabs = collabs;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mUsers != null) {
            Log.d("collabList", "mUsers.size(): " + mUsers.size());
            return mUsers.size();
        }
        else return 0;
    }
}
