package com.fatec.fincol.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.fatec.fincol.R;
import com.fatec.fincol.model.User;

public class HomeFragment extends Fragment {

    private HomeFragViewModel mHomeViewModel;

    private TextView signInTextView;
    private TextView nameTextView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mHomeViewModel =
                ViewModelProviders.of(this).get(HomeFragViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        final TextView textView = root.findViewById(R.id.text_home);
        mHomeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });


        signInTextView = (TextView) root.findViewById(R.id.signInTextView);
        nameTextView = (TextView) root.findViewById(R.id.nameTextView);

        mHomeViewModel.getUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                nameTextView.setText(user.getName());
                signInTextView.setText(user.getEmail());
            }
        });

        return root;
    }
}