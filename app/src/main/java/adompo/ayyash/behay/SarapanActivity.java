package adompo.ayyash.behay;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import adompo.ayyash.behay.AsupanMakan.AsupanAdapter;
import adompo.ayyash.behay.AsupanMakan.AsupanModel;

public class SarapanActivity extends AppCompatActivity implements AsupanAdapter.AsupanDeleteClickHandler {

    FloatingActionButton fbAdd;
    ProgressDialog progressDialog;
    AsupanAdapter adapter;
    RecyclerView rvAsupan;
    int idJenisMakan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sarapan);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Silahkan Tunggu...");

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        adapter = new AsupanAdapter(this, this);
        rvAsupan = (RecyclerView) findViewById(R.id.rv_asupan_makanan);
        rvAsupan.setLayoutManager(llm);
        rvAsupan.setAdapter(adapter);

        fbAdd = (FloatingActionButton) findViewById(R.id.fb_add);
        fbAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ListSarapan.class);
                i.putExtra("id", idJenisMakan);
                startActivity(i);
                //   Toast.makeText(SarapanActivity.this, "memilih sarapan", Toast.LENGTH_LONG).show();
            }
        });

        Intent receiveIntent = getIntent();
        idJenisMakan = receiveIntent.getExtras().getInt("id", 0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getTitle(idJenisMakan));
        setSupportActionBar(toolbar);
    }

    private String getTitle(int id) {
        String title = "";
        switch (idJenisMakan) {
            case 1:
                title = "Makan Pagi";
                break;
            case 2:
                title = "Selingan Pagi";
                break;
            case 3:
                title = "Makan Siang";
                break;
            case 4:
                title = "Selingan Siang";
                break;
            case 5:
                title = "Makan Malam";
                break;
            case 6:
                title = "Selingan Malam";
                break;
        }
        return title;
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        PrefManager prefManager = new PrefManager(this);
        String email = prefManager.getActiveEmail();
        String url = "http://administrator.behy.co/User/getAsupanMakanan/" + email + "/" + idJenisMakan;
        System.out.println(url);

        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                Gson gson = new GsonBuilder().create();
                AsupanModel asupanModel = gson.fromJson(response, AsupanModel.class);
                adapter.setData(asupanModel.listAsupan);

                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
            }
        });

        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        queue.add(stringRequest);
    }

    @Override
    public void onClick(final int asupanId, String namaAsupan) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Konfirmasi");
        builder.setMessage("Apakah anda yakin ingin menghapus "+getTitle(idJenisMakan)+"\n" + namaAsupan + " ?");
        builder.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                deleteData("http://administrator.behy.co/User/deleteFoodRecord/" + asupanId);  // TODO Change this url to Config
            }
        });
        builder.setNegativeButton("Batalkan", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // do nothing
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void deleteData(String url) {
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                loadData();
                Log.d("uye jadi hapus", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e("uye gagal hapus", error.toString());
            }
        });
        queue.add(stringRequest);
    }
}
