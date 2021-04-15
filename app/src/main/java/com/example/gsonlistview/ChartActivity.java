package com.example.gsonlistview;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ChartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        getSupportActionBar().setTitle("Charts");


        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item){
                switch (item.getItemId()) {
                    case R.id.action_home:
                        Toast.makeText(ChartActivity.this, "Home", Toast.LENGTH_SHORT).show();
                        //Intent intentMain = new Intent (nav_bar.this, MainActivity.class);
                        // startActivity(intentMain);
                        //Intent intentMain = new Intent(HomeActivity.this, MainActivity.class);
                        //startActivity(intentMain);
                        Intent intentHome = new Intent(ChartActivity.this, MainActivity.class);
                        startActivity(intentHome);
                        break;
                    case R.id.action_favorites:
                        Toast.makeText(ChartActivity.this, "Favorites", Toast.LENGTH_SHORT).show();
                        Intent intentFav = new Intent(ChartActivity.this, FavActivity.class);
                        startActivity(intentFav);
                        break;
                    case R.id.action_charts:
                        Toast.makeText(ChartActivity.this, "Charts", Toast.LENGTH_SHORT).show();
                        break;

                }
                return true;
            }

        });
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}