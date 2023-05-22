package com.example.apprecetas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.apprecetas.Fragments.FavoritesFragment;
import com.example.apprecetas.Fragments.HomeFragment;
import com.example.apprecetas.Fragments.SettingsFragment;
import com.example.apprecetas.Login.LoginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        SharedPreferences sharedPref = getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        boolean estaLogeado = sharedPref.getBoolean("estaLogeado", false);

        if (!estaLogeado) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment fragmento = null;

            switch (item.getItemId()) {
                case R.id.menu_home:
                    fragmento = new HomeFragment();
                    break;
                case R.id.menu_favorites:
                    fragmento = new FavoritesFragment();
                    break;
                case R.id.menu_settings:
                    fragmento = new SettingsFragment();
                    break;
            }

            if (fragmento != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, fragmento).commit();
            }
            return true;
        });
        // para elegir el fragmento principal
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new HomeFragment()).commit();
    }
}
