package com.example.gsonlistview;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FavActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav);
        getSupportActionBar().setTitle("Favorites");


        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item){
                switch (item.getItemId()) {
                    case R.id.action_home:
                        Toast.makeText(FavActivity.this, "Home", Toast.LENGTH_SHORT).show();
                        //Intent intentMain = new Intent (nav_bar.this, MainActivity.class);
                        // startActivity(intentMain);
                        //Intent intentMain = new Intent(HomeActivity.this, MainActivity.class);
                        //startActivity(intentMain);
                        Intent intentHome = new Intent(FavActivity.this, MainActivity.class);
                        startActivity(intentHome);
                        break;
                    case R.id.action_favorites:
                        Toast.makeText(FavActivity.this, "Favorites", Toast.LENGTH_SHORT).show();
                        //Intent intentFav = new Intent(FavActivity.this, FavActivity.class);
                        //startActivity(intentFav);
                        break;
                    case R.id.action_charts:
                        Toast.makeText(FavActivity.this, "Charts", Toast.LENGTH_SHORT).show();
                        Intent intentChart = new Intent(FavActivity.this, ChartActivity.class);
                        startActivity(intentChart);
                        break;

                }
                return true;
            }

        });
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}