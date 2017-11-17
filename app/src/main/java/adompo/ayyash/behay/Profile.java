package adompo.ayyash.behay;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import adompo.ayyash.behay.test.ProfileModel;
import adompo.ayyash.behay.test.RiwayatAsupanGiziList;

public class Profile extends AppCompatActivity {

    private TextView textViewTanggal;
    private TextView textViewNama;
    private Button buttonUpdate;
    private ProgressDialog progressDialog;
    private int mYear, mMonth, mDay;

    public static final String KEY_EMAIL = "email";
    public static final String KEY_NAMA = "nama";
    public static final String KEY_TANGGAL_LAHIR = "tanggal_lahir";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Silahkan Tunggu...");

        textViewNama = (TextView) findViewById(R.id.tvNama);
        textViewTanggal = (TextView) findViewById(R.id.tvTanggal);
        buttonUpdate = (Button) findViewById(R.id.btnUpdate);

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });

        textViewTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(Profile.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                textViewTanggal.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        load();
    }

    private void save() {
        progressDialog.show();

        PrefManager prefManager = new PrefManager(this);
        final String email = prefManager.getActiveEmail().trim();
        final String nama = textViewNama.getText().toString().trim();
        final String tanggal = textViewTanggal.getText().toString().trim();

        String url = "http://administrator.behy.co/User/setProfile";
        StringRequest sR = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("{\"message\":\"success\"}")) {
                            Toast.makeText(Profile.this, "Profile telah diupdate: " + response, Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(Profile.this, "Profile gagal diupdate", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Profile.this, error.toString(), Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(KEY_EMAIL, email);
                params.put(KEY_NAMA, nama);
                params.put(KEY_TANGGAL_LAHIR, tanggal);
                return params;
            }
        };

        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        sR.setRetryPolicy(policy);
        requestQueue.add(sR);
    }

    private void load() {
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(Profile.this);

        PrefManager prefManager = new PrefManager(Profile.this);
        String email = prefManager.getActiveEmail();

        String url = "http://administrator.behy.co/User/getProfile/" + email;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson mGson = new GsonBuilder().create();
                ProfileModel model = mGson.fromJson(response, ProfileModel.class);

                textViewNama.setText(model.profile.nama);
                textViewTanggal.setText(model.profile.tanggalLahir);

                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Profile.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                error.printStackTrace();
                progressDialog.dismiss();
            }
        });

        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        queue.add(stringRequest);
    }
}
