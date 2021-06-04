package com.amarjeet.android.teckatproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class PaymentDetails extends AppCompatActivity {
    NavigationView nav;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;

    RecyclerView recyclerView;
    ArticleAdapter adaptor;
    ArrayList<Article> articles;
    SharedPreferences sharedPreferences;

    private static final String SHARED_PREF_NAME="mypref";
    private static final String KET_NAME="userID";
    private static final String KEY_PASS="password";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);

        Toolbar mToolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        nav=(NavigationView)findViewById(R.id.navmenu);
        drawerLayout=(DrawerLayout)findViewById(R.id.drawer);

        toggle=new ActionBarDrawerToggle(this,drawerLayout,mToolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
        String name = sharedPreferences.getString(KET_NAME,null);
        String pass = sharedPreferences.getString(KEY_PASS,null);

        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case R.id.menu_latesttrainings:
                        //Toast.makeText(PaymentDetails.this, "Home", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Intent trainings = new Intent(PaymentDetails.this,trainings_web.class);
                        startActivity(trainings);
                        break;
                    case R.id.menu_faq:
                        //Toast.makeText(PaymentDetails.this, "Call", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Intent faq = new Intent(PaymentDetails.this,web_faq.class);
                        startActivity(faq);
                        break;
                    case R.id.menu_contact:
                        //Toast.makeText(PaymentDetails.this, "Settings", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Intent contacts = new Intent(PaymentDetails.this,web_contacts.class);
                        startActivity(contacts);
                        break;
                    case R.id.menu_policy:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Intent policyI = new Intent(PaymentDetails.this,PrivacyPolicy.class);
                        startActivity(policyI);
                        break;
                    case R.id.menu_logout:
                        Toast.makeText(PaymentDetails.this, "Logged out Successfully", Toast.LENGTH_SHORT).show();
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.commit();
                        finish();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Intent intent = new Intent(PaymentDetails.this,Login_ui.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.menu_tsipreg:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Intent tsip = new Intent(PaymentDetails.this,web_tsip.class);
                        startActivity(tsip);
                        break;
                }

                return true;
            }
        });

        final String username=getIntent().getStringExtra("username");
        final SwipeRefreshLayout swipeRefreshLayout;
        swipeRefreshLayout=findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                articles.clear();
                getData(username);
                adaptor.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        recyclerView=findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adaptor = new ArticleAdapter();
        recyclerView.setAdapter(adaptor);
        articles=new ArrayList<>();
        getData(username);

    }

    private void getData(String username) {
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Connecting");
        progress.setMessage("Please wait while we fetch your data.....");
        progress.show();

        Runnable progressRunnable = new Runnable() {

            @Override
            public void run() {
                Toast.makeText(PaymentDetails.this, "Payment sheet will update automatically", Toast.LENGTH_SHORT).show();
                progress.cancel();
            }
        };

        Handler pdCanceller = new Handler();
        pdCanceller.postDelayed(progressRunnable, 4000);

        String UID=username;
        FetchAnActivity fetchAnActivity= new FetchAnActivity(recyclerView,adaptor,articles);
        fetchAnActivity.execute(UID);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu,menu);
        MenuItem item =menu.findItem(R.id.search_button);
        SearchView searchView=(SearchView) item.getActionView();
        searchView.setQueryHint("Search Name or Mobile...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adaptor.getFilter().filter(s);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}