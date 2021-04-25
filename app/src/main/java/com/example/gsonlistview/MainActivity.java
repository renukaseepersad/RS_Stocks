package com.example.gsonlistview;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.auth.GoogleAuthProvider;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static List<String> cFavorites;
    private FirebaseAuth mAuth;
    private EditText pw;
    private EditText un;
    private Button login;
    private Button signup;
    private TextView error;
    static FirebaseUser mUser;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("RS Stocks");

        //Attach views
        signup = findViewById(R.id.signup);
        pw = findViewById(R.id.pw);
        un = findViewById(R.id.un);
        login = findViewById(R.id.login);
        error = findViewById(R.id.error);

        //Init firebase
        mAuth = FirebaseAuth.getInstance();


    }
    public static FirebaseUser getUser(){
        return mUser;
    }


    public void onLoginClick(View v){
        String email = un.getText().toString();
        String password = pw.getText().toString();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            mUser = mAuth.getCurrentUser();
                            error.setText("signInWithEmail:success " +  mUser.getEmail().toString());
                            Intent intent = new Intent(v.getContext(), BottomNavActivity.class);
                            cFavorites = new ArrayList<String>();
                            startActivity(intent);


                        } else {
                            // If sign in fails, display a message to the user.
                            error.setText("signInWithEmail:failure" +  task.getException().toString());

                        }
                    }
                });


    }

    public static List<String> getFavs(){
        return cFavorites;
    }

    public static void setFavs(List<String> s){
        cFavorites = s;
    }

    public static void addFav(String s){
        cFavorites.add(s);
    }

    public static boolean isFav(String s){
        for(String k : cFavorites){
            if(k.equals(s)){
                return true;
            }

        }
        return false;
    }

    public static void remFav(String s){
        if (isFav(s)){
            cFavorites.remove(s);
        }
        return;
    }


    public void onSignupClick(View v){
        String email = un.getText().toString();
        String password = pw.getText().toString();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            error.setText("createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();


                        } else {
                            // If sign in fails, display a message to the user.
                            error.setText("createUserWithEmail:failure" + task.getException().toString());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });


    }
}




/*
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
/*
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

 */