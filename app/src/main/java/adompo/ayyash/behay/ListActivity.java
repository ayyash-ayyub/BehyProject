package adompo.ayyash.behay;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
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

import adompo.ayyash.behay.AktifitasFisik.AktifitasFisikAdapter;
import adompo.ayyash.behay.AktifitasFisik.AktifitasFisikModel;

public class ListActivity extends AppCompatActivity {

    private TextView activityEditText;
    private Button activityButton;

    private ProgressDialog progressDialog;
    private RecyclerView rv_item;
    private AktifitasFisikAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Silahkan Tunggu...");

        activityEditText = (TextView) findViewById(R.id.activityEditText);
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

        activityButton = (Button) findViewById(R.id.activityButton);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mAdapter = new AktifitasFisikAdapter(this);
        rv_item = (RecyclerView) findViewById(R.id.rv_aktifitas_fisik);
        rv_item.setLayoutManager(llm);
        rv_item.setAdapter(mAdapter);

        PrefManager prefManager = new PrefManager(this);

        String ambilEmail = prefManager.getActiveEmail();

        getAktifitasIhsan("http://administrator.behy.co/User/getAktifitasFisik/" + ambilEmail);
    }

    public void getAktifitasIhsan(String url) {
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                GsonBuilder builder = new GsonBuilder();
                Gson mGson = builder.create();
                System.out.println("aaaa : " + response);
                AktifitasFisikModel model = mGson.fromJson(response, AktifitasFisikModel.class);

                mAdapter.setData(model.listAktifitasFisik);

                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ListActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        queue.add(stringRequest);
    }

}
