package com.fatec.fincol.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.fatec.fincol.R;
import com.fatec.fincol.model.User;
import com.fatec.fincol.viewmodel.UserViewModel;

public class HomeFragment extends Fragment {

    private HomeFragViewModel homeViewModel;

    private UserViewModel mUserViewModel;
    private TextView signInTextView;
    private TextView nameTextView;
    private Button signOutButton;
    private Button deleteButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeFragViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        signInTextView = (TextView) root.findViewById(R.id.signInTextView);
        nameTextView = (TextView) root.findViewById(R.id.nameTextView);
        signOutButton = (Button) root.findViewById(R.id.signOutButton);
        deleteButton = (Button) root.findViewById(R.id.deleteButton);


        mUserViewModel.getUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                nameTextView.setText(user.getName());
                signInTextView.setText(user.getEmail());
            }
        });

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserViewModel.signOut();
                getActivity().finish();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserViewModel.deleteUser();
            }
        });

        return root;
    }
}