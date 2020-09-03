package com.fatec.fincol;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fatec.fincol.model.User;
import com.fatec.fincol.ui.profile.ProfileActivity;
import com.fatec.fincol.util.BitmapUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class HomeActivity extends AppCompatActivity {

    private TextView nameUserTextView;
    private TextView emailUserTextView;
    private ImageView imageView;
    private ProgressBar homeProgressBar;

    private AppBarConfiguration mAppBarConfiguration;
    private HomeViewModel mHomeViewModel;
    public static final int PROFILE_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);





        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        mHomeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mHomeViewModel.isSignIn.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (!aBoolean) finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);

        mHomeViewModel.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                nameUserTextView = findViewById(R.id.nameUserTextView);
                emailUserTextView = findViewById(R.id.emailUserTextView);
                imageView = findViewById(R.id.imageView);
                homeProgressBar = findViewById(R.id.homeProgressBar);

                homeProgressBar.setVisibility(View.INVISIBLE);

                nameUserTextView.setText(user.getName());
                emailUserTextView.setText(user.getEmail());
                if (!user.getProfileImage().isEmpty())
                    imageView.setImageBitmap(BitmapUtil.base64ToBitmap(user.getProfileImage()));
            }
        });

        LinearLayout profileLinearLayout = findViewById(R.id.profileLinearLayout);
        profileLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(v.getContext(), ProfileActivity.class), PROFILE_REQUEST_CODE);
            }
        });

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PROFILE_REQUEST_CODE){
            finish();
        }
    }
}