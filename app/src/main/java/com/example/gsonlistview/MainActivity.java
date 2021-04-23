package com.example.gsonlistview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final String[] stocks = {"IBM","AAPL","GOOGL","AMZN","TSLA"};
    private ArrayList<Stock> theStocks;
    //RecyclerView example;
    ListView example;
    //StockRecyclerAdapter exampleAdapter;
    StockListAdapter exampleAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Home");
        theStocks = new ArrayList<Stock>();
        example = findViewById(R.id.recycler);


        for(String s : stocks) {
            String url = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol={{REPLACE_KEY}}&apikey=KN687CW0J75UU9M1".replace("{{REPLACE_KEY}}",s);
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


    /*
    private void doView(){
        exampleAdapter = new StockRecyclerAdapter(theStocks);
        example.setAdapter(exampleAdapter);
        example.setLayoutManager(new LinearLayoutManager(this));
    }*/

    private void doView(){
        exampleAdapter = new StockListAdapter(this,theStocks);
        example.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Add code here to do what you want when an Item is clicked
                Toast.makeText(MainActivity.this, theStocks.get(i).getSymbol().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        example.setAdapter(exampleAdapter);

    }


}