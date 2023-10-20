package com.mobdeve.s13.kok.james.gueryandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.mobdeve.s13.kok.james.gueryandroid.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnItemSelectedListener {

    Fragment home = new HomeFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityHomeBinding binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize the default fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, new HomeFragment())
                    .commit();
        }

        BottomNavigationView bottomNavigationView = binding.navbar;
        bottomNavigationView.setOnItemSelectedListener(this);
        }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.nav_home){
            replaceFragment(home);
            return true;
        }
        else if(item.getItemId() == R.id.nav_add){
            return true;
        }
        else if(item.getItemId() == R.id.nav_community){
            return true;
        }
        else if(item.getItemId() == R.id.nav_profile){
            return true;
        }
        else if(item.getItemId() == R.id.nav_notification){
            return true;
        }
        else return false;
    }

    private void replaceFragment (Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, fragment).commit();
    }
}
