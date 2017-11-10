package adompo.ayyash.behay;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {


    private TextView tombolDaftar;
    EditText email,password;
    Button login;
    private boolean loggedIn = false;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//            ulangIntro();

        Log.d("MAIN", "starting activity");

        // Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Silahkan Tunggu...");

        tombolDaftar = (TextView)findViewById(R.id.tmbDaftar);

        email = (EditText)findViewById(R.id.txtEmail);
        password = (EditText)findViewById(R.id.txtPasswordD);
        login = (Button) findViewById(R.id.btnLoginn);


        tombolDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Register.class);
                startActivity(i);
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "email dan password tidak boleh kosong", Toast.LENGTH_SHORT).show();
                } else {
                    // cek dari db
                    login();
                }
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

//        SharedPreferences sharedPreferences = getSharedPreferences(ConfigUmum.SHARED_PREF_NAME, Context.MODE_PRIVATE);
//        loggedIn = sharedPreferences.getBoolean(ConfigUmum.LOGGEDIN_SHARED_PREF, false);

        PrefManager pref = new PrefManager(this);
        loggedIn = pref.isLoggedIn();

        if (loggedIn) {
            Intent intent = new Intent(LoginActivity.this, UtamaActivity.class);
            startActivity(intent);
            finish();
        }
    }


    private void login() {
        progressDialog.show();
        final String nisA = email.getText().toString().trim();
        final String passwordA = password.getText().toString().trim();

//        Toast.makeText(Login.this, "hai: "+nisA +" "+passwordA,Toast.LENGTH_LONG).show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ConfigUmum.LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equalsIgnoreCase(ConfigUmum.LOGIN_SUCCESS)) {
                            PrefManager pref = new PrefManager(getApplicationContext());

                            pref.setLoggedIn(true);
                            pref.setActiveEmail(nisA);

                            Intent i = new Intent(LoginActivity.this, UtamaActivity.class);
                            startActivity(i);
                            finish();

                            progressDialog.dismiss();
                        } else {
                            Toast.makeText(LoginActivity.this, "username/password salah /masalah koneksi ke server", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("aaa", error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put(ConfigUmum.KEY_EMAIL, nisA);
                params.put(ConfigUmum.KEY_PASSWORD, passwordA);

                return params;
            }
        };

        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }


    protected void ulangIntro(){
        PrefManager prefManager = new PrefManager(getApplicationContext());

        // make first time launch TRUE
        prefManager.setFirstTimeLaunch(true);

        // startActivity(new Intent(LoginActivity.this, WelcomeActivity.class));
        // finish();

    }
}
