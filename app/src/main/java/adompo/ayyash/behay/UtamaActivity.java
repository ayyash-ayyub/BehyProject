package adompo.ayyash.behay;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class UtamaActivity extends AppCompatActivity {

    private CollapsingToolbarLayout ctb;
    private int mutedColor;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private String getEmail;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utama);

        final PrefManager pref = new PrefManager(this);
        Log.d("WelcomeActivity", "pref in Utama: firstTimeLaunch = " + pref.isFirstTimeLaunch());
        Log.d("WelcomeActivity", "pref in Utama: loggedIn = " + pref.isLoggedIn());
        Log.d("WelcomeActivity", "pref in Utama: activeEmail = " + pref.getActiveEmail());

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);

        BukuHarian fragmenttab = new BukuHarian();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.frame_layout, fragmenttab).commit();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setTitle("Behy");

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.action_item1:
                                actionBar.setTitle("Behy");
                                selectedFragment = MyBehy.newInstance();
                                break;
                            case R.id.action_item2:

                                actionBar.setTitle("Buku Harian");
                                selectedFragment = BukuHarian.newInstance();
                                break;
                            case R.id.action_item3:
                                actionBar.setTitle("Tips Sehat");
                                selectedFragment = News.newInstance();
                                break;
                            case R.id.action_item4:
                                actionBar.setTitle("Info Sehat");
                                selectedFragment = InfoSehat.newInstance();
                                break;
                            case R.id.action_item5:
                                actionBar.setTitle("Inbox");
                                selectedFragment = InboxFragment.newInstance();
                                break;
                        }
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_layout, selectedFragment);
                        transaction.commit();
                        return true;
                    }
                });

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, MyBehy.newInstance());
        transaction.commit();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        // On click of menu icon on toolbar
        toolbar.setNavigationIcon(R.drawable.ic_menu_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

// On click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                // Set item in checked state
                menuItem.setChecked(true);

                //TODO: handle navigation
                String title = menuItem.getTitle().toString();
                if (title.equals("Logout")) {
                    pref.setActiveEmail("");
                    pref.setLoggedIn(false);
                    pref.setFirstTimeLaunch(true);

                    Intent i = new Intent(UtamaActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }else if (title.equals("Profile")){
                    Intent i = new Intent(UtamaActivity.this, Profile.class);
                    startActivity(i);
                }else if (title.equals("Feedback")){
                    Fragment selectedFragment = null;
                    selectedFragment = InboxFragment.newInstance();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame_layout, selectedFragment);
                    transaction.commit();
                }else if (title.equals("Support")){
                    Intent i = new Intent(UtamaActivity.this, Support.class);
                    startActivity(i);
                }else if (title.equals("Syarat dan Ketentuan")){
                    Intent i = new Intent(UtamaActivity.this, Syarat.class);
                    startActivity(i);
                }else if (title.equals("Panduan")){
                    Intent i = new Intent(UtamaActivity.this, Panduan.class);
                    startActivity(i);
                }else if (title.equals("Share Behy")){

                }

                //Closing drawer on item click
                drawerLayout.closeDrawers();
                return true;
            }
        });

        //Used to select an item programmatically
        //bottomNavigationView.getMenu().getItem(2).setChecked(true);
            getEmail = pref.getActiveEmail();
        getMyBehy(ConfigUmum.GET_MY_BEHY+getEmail);
        
    }


    private void getMyBehy(String URL){
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        final JsonObjectRequest request = new JsonObjectRequest( URL,null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("My Behy data"+response.toString());
//
//
//                   Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Masalah pada koneksi, Silakan ulangi", Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(getApplicationContext(),TipsSehatDetail.class);
//                startActivity(intent);

            }
        });

        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        queue.add(request);
    }
}
