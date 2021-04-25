package com.example.gsonlistview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.firestore.core.View;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;


public class MainActivity extends AppCompatActivity {

    static List<String> cFavorites;
    private FirebaseAuth mAuth;
    private EditText pw;
    private EditText un;
    private Button login;
    private Button signup;
    private TextView error;
    static FirebaseUser mUser;

    static FirebaseFirestore DB;

    SignInButton signInButton;
    Button signOutButton;
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "SignInActivity";

    GoogleSignInClient mGoogleSignInClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("RS Stocks");

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // [END config_signin]
        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.sign_in_button:
                        signIn();
                    case R.id.signOutButton:
                        signOut();
                }
            }
        });

        //Attach views
        signup = findViewById(R.id.signup);
        pw = findViewById(R.id.pw);
        un = findViewById(R.id.un);
        login = findViewById(R.id.login);


        //Init firebase
        mAuth = FirebaseAuth.getInstance();

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

                        //change this to call favoritesFragment
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


        DB = FirebaseFirestore.getInstance();
        DocumentReference docRef = DB.collection("users").document(MainActivity.getUser().getEmail());



        Source source = Source.DEFAULT;

        // Get the document, forcing the SDK to use the offline cache
        docRef.get(source).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    // Document found in the offline cache
                    DocumentSnapshot document = task.getResult();
                    if (document.getData().get("Favorites") != null){
                        MainActivity.setFavs((List<String>)document.getData().get("Favorites"));
                    }
                    System.out.println("Cached document data: " + document.getData());
                } else {
                    System.out.println("GET FAILED");
                }
            }
        });

        WriteDBTask task = new WriteDBTask();
        Timer timer = new Timer("DB Timer");
        timer.schedule(task,60000L,60000L);

    }

    public static FirebaseFirestore getDB(){
        return DB;
    }




    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);

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
                            //need to change intent
                            Intent intent = new Intent(v.getContext(), HomeActivity.class);
                            cFavorites = new ArrayList<String>();
                            startActivity(intent);


                        } else {
                            // If sign in fails, display a message to the user.
                            error.setText("signInWithEmail:failure" +  task.getException().toString());

                        }
                    }
                });


    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                //Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                //Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithCredential:failure", task.getException());
                            updateUI(null);
                        }
                    }
                });
    }
    public void signOut() {
        FirebaseAuth.getInstance().signOut();
    }

    private void updateUI(FirebaseUser user) {
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