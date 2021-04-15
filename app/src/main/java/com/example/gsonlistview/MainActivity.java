package com.example.gsonlistview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private final String[] stocks = {"IBM","AAPL","GOOGL","AMZN","TSLA"};
    private ArrayList<Stock> theStocks;
    RecyclerView example;
    StockRecyclerAdapter exampleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("MainActivity");
        theStocks = new ArrayList<Stock>();
        example = findViewById(R.id.recycler);


        for(String s : stocks) {
            String url = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol={{REPLACE_KEY}}&apikey= {}".replace("{{REPLACE_KEY}}",s);
            RequestQueue queue = Volley.newRequestQueue(this);


            GsonRequest<GlobalQuote> req = new GsonRequest<GlobalQuote>(url, GlobalQuote.class, null, new Response.Listener<GlobalQuote>() {
                @Override
                public void onResponse(GlobalQuote response) {

                    System.out.println("Successful Response");
                    System.out.println(response.getGlobalQuote().getSymbol());
                    theStocks.add(response.getGlobalQuote());

                    if(theStocks.size() == stocks.length){
                        doView();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("That didn't work!");
                }
            });

            queue.add(req);
        }
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem item){
            switch (item.getItemId()) {
                case R.id.action_home:
                    Toast.makeText(com.example.gsonlistview.MainActivity.this, "Home", Toast.LENGTH_SHORT).show();
                    //Intent intentMain = new Intent (nav_bar.this, MainActivity.class);
                    // startActivity(intentMain);
                    Intent intentHome = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intentHome);
                    break;
                case R.id.action_favorites:
                    Toast.makeText(com.example.gsonlistview.MainActivity.this, "Favorites", Toast.LENGTH_SHORT).show();
                    //Intent intentHome = new Intent(MainActivity.this, HomeActivity.class);
                    //startActivity(intentHome);
                    Intent intentFav = new Intent(MainActivity.this, FavActivity.class);
                    startActivity(intentFav);

                    break;
                case R.id.action_charts:
                    Toast.makeText(com.example.gsonlistview.MainActivity.this, "Charts", Toast.LENGTH_SHORT).show();

                    Intent intentChart = new Intent(MainActivity.this, ChartActivity.class);
                    startActivity(intentChart);
            }
            return true;
        }
        });
    }

    private void doView(){
        exampleAdapter = new StockRecyclerAdapter(theStocks);
        example.setAdapter(exampleAdapter);
        example.setLayoutManager(new LinearLayoutManager(this));
    }



}