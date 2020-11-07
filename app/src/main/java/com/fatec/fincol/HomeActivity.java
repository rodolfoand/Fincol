package com.fatec.fincol;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fatec.fincol.model.AccountVersion2;
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

    private AppBarConfiguration mAppBarConfiguration;
    private HomeViewModel mHomeViewModel;

    public static final int PROFILE_REQUEST_CODE = 1;
    public static final int SIGN_OUT_REQUEST_CODE = 2;
    public static final int DELETE_REQUEST_CODE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_account_list)
                .setDrawerLayout(drawer)
                .build();

        Bundle bundle = new Bundle();
        bundle.putString("amount", "1");

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        mHomeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                navController.navigate(R.id.action_nav_home_to_transactionFragment);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);

        TextView nameUserTextView = findViewById(R.id.nameUserTextView);
        TextView emailUserTextView = findViewById(R.id.emailUserTextView);
        ImageView imageView = findViewById(R.id.imageView);

        ProgressBar homeProgressBar = findViewById(R.id.homeProgressBar);

        mHomeViewModel.mUser.observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {

                homeProgressBar.setVisibility(View.INVISIBLE);

                nameUserTextView.setText(user.getName());
                emailUserTextView.setText(user.getEmail());
                if (!user.getProfileImage().isEmpty())
                    imageView.setImageBitmap(BitmapUtil.base64ToBitmap(user.getProfileImage()));

//                Context context = getApplicationContext();
//                SharedPreferences sharedPref = context.getSharedPreferences(
//                        getString(R.string.preference_file_key), Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPref.edit();
//                editor.putString(getString(R.string.saved_uid_user_key), user.getUid());
//                //editor.putString(user.getUid(), "Not found");
//                editor.commit();

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(getString(R.string.saved_uid_user_key),user.getUid());
                editor.apply();

                mHomeViewModel.initialize(user.getUid());
            }
        });

        mHomeViewModel.mActiveAccount.observe(this, new Observer<AccountVersion2>() {
            @Override
            public void onChanged(AccountVersion2 accountVersion2) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(getString(R.string.saved_account_id_key),accountVersion2.getId());
                editor.apply();
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
            if (resultCode == SIGN_OUT_REQUEST_CODE)
                Toast.makeText(this, R.string.come_back_soon, Toast.LENGTH_SHORT).show();

            if (resultCode == DELETE_REQUEST_CODE) {
                Toast.makeText(this, R.string.bye, Toast.LENGTH_SHORT).show();
                finishAffinity();
            }

            finish();
        }
    }

}