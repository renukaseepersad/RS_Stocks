package com.example.gsonlistview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().setTitle("Home");


        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item){
                switch (item.getItemId()) {
                    case R.id.action_home:
                        Toast.makeText(com.example.gsonlistview.HomeActivity.this, "Home", Toast.LENGTH_SHORT).show();
                        //Intent intentMain = new Intent (nav_bar.this, MainActivity.class);
                        // startActivity(intentMain);
                        //Intent intentMain = new Intent(HomeActivity.this, MainActivity.class);
                        //startActivity(intentMain);
                        Intent intentHome = new Intent(HomeActivity.this, MainActivity.class);
                        startActivity(intentHome);
                        break;
                    case R.id.action_favorites:
                        Toast.makeText(com.example.gsonlistview.HomeActivity.this, "Favorites", Toast.LENGTH_SHORT).show();
                        Intent intentFav = new Intent(HomeActivity.this, FavActivity.class);
                        startActivity(intentFav);
                        break;
                    case R.id.action_charts:
                        Toast.makeText(com.example.gsonlistview.HomeActivity.this, "Charts", Toast.LENGTH_SHORT).show();
                        Intent intentChart = new Intent(HomeActivity.this, ChartActivity.class);
                        startActivity(intentChart);
                        break;

                }
                return true;
            }

        });
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}