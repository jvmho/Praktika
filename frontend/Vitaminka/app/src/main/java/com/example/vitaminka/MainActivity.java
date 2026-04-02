package com.example.vitaminka;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.vitaminka.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    public static final String PREFS_ISLOGGED = "Is logged";
    private ActivityMainBinding binding;
    public static SharedPreferences SP_SETTINGS;
    public static SharedPreferences.Editor SP_EDITOR;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        SP_SETTINGS = getSharedPreferences("Account", Context.MODE_PRIVATE);
        SP_EDITOR = SP_SETTINGS.edit();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_catalog, R.id.navigation_catalog_orthopedic, R.id.navigation_catalog_orthopedic_massagers, R.id.navigation_catalog_orthopedic_massagers_filters, R.id.navigation_profile, R.id.navigation_profile_likes, R.id.navigation_profile_login, R.id.navigation_profile_registration)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }
}