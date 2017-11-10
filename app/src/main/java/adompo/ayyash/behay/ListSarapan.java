package adompo.ayyash.behay;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
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

import adompo.ayyash.behay.AsupanMakan.AktifitasSarapanAdapter;
import adompo.ayyash.behay.AsupanMakan.AktifitasSarapanModel;

public class ListSarapan extends AppCompatActivity implements AktifitasSarapanAdapter.AktifitasSarapanClickHandler {

    private TextView activityEditText;
    private Button activityButton;

    private ProgressDialog progressDialog;
    private RecyclerView rv_item;
    private AktifitasSarapanAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_sarapan);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Silahkan Tunggu...");


        activityEditText = (TextView) findViewById(R.id.sarapanEditText);
        activityEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                mAdapter.filter(text);
            }
        });

        activityButton = (Button) findViewById(R.id.activitySarapanButt);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mAdapter = new AktifitasSarapanAdapter(this, this);
        rv_item = (RecyclerView) findViewById(R.id.rv_aktifitas_sarapan);
        rv_item.setLayoutManager(llm);
        rv_item.setAdapter(mAdapter);

        PrefManager prefManager = new PrefManager(this);
        String ambilEmail = prefManager.getActiveEmail();

        Intent receiveIntent = getIntent();
        int idJenisMakanan = receiveIntent.getExtras().getInt("id", 0);

     //   System.out.println("http://administrator.behy.co/User/getListAsupanMakanan/" + ambilEmail + "/" + idJenisMakanan);
        getListSarapan("http://administrator.behy.co/User/getListAsupanMakanan/" + ambilEmail + "/" + idJenisMakanan);
    }


    public void getListSarapan(String url) {
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                GsonBuilder builder = new GsonBuilder();
                Gson mGson = builder.create();
                System.out.println("oo : " + response);
                AktifitasSarapanModel model = mGson.fromJson(response, AktifitasSarapanModel.class);

                mAdapter.setData(model.listAktifitasSarapan);
                Log.d("AAA",response.toString());
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ListSarapan.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        queue.add(stringRequest);
    }

    @Override
    public void onClick(int id) {
        Intent receiveIntent = getIntent();
        int idJenisMakanan = receiveIntent.getExtras().getInt("id", 0);

        Intent i = new Intent(ListSarapan.this, DetailMakanPagi.class);
        i.putExtra("id_waktu_makan", idJenisMakanan);
        i.putExtra("id_makanan", id);
        startActivity(i);
    }
}
