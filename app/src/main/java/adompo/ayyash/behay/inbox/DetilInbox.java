package adompo.ayyash.behay.inbox;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

import adompo.ayyash.behay.ConfigUmum;
import adompo.ayyash.behay.NewMessage;
import adompo.ayyash.behay.PrefManager;
import adompo.ayyash.behay.R;

public class DetilInbox extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private ItemObjectDetil.ObjectDetil objectDetilInbox;
    private MainAdapterDetil adapter;
    private RecyclerView rv_item;
    String ambilEmail;
    TextView tvSubjek;
    public int id;
    EditText txtPesan;
    Button btnSend;

    public static final String KEY_EMAIL = "email";
    public static final String KEY_JUDUL = "subjek";
    public static final String KEY_PESAN = "pesan";
    public static final String KEY_REPLY = "id_reply";
    public static final String KEY_PENERIMA= "receiver";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detil_inbox);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Silahkan Tunggu...");

        PrefManager prefManager = new PrefManager(this);
        ambilEmail = prefManager.getActiveEmail();



        Intent i = getIntent();
        Bundle a = i.getExtras();
//        id = i.getIntExtra("id", 0);
        id = (int)a.get("id");
        String subjek = i.getStringExtra("subject");
        tvSubjek = (TextView) findViewById(R.id.tvSubjek);
        tvSubjek.setText(subjek);


        rv_item = (RecyclerView) findViewById(R.id.rvDetilInbox);
        GetData(ConfigUmum.URL_INBOX_DETIL+""+ambilEmail+"/"+id);


        txtPesan = (EditText)findViewById(R.id.txtPesan);


        btnSend = (Button)findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtPesan.getText().equals("") ){
                    Toast.makeText(getApplicationContext(), "Pesan tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }else{
                    save();
                }

            }
        });
    }

    public void GetData(String URL) {

        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            ;

            @Override
            public void onResponse(String response) {
                GsonBuilder builder = new GsonBuilder();
                Gson mGson = builder.create();
                objectDetilInbox = mGson.fromJson(response, ItemObjectDetil.ObjectDetil.class);
                System.out.println("Respond "+ response);
                LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                rv_item.setLayoutManager(llm);

                System.out.println("size: " + objectDetilInbox.pesan.size());
                adapter = new MainAdapterDetil(getApplicationContext(), objectDetilInbox.pesan);
                rv_item.setAdapter(adapter);

                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Gagal Konek ke server, periksa jaringan anda :(", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });

        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        queue.add(stringRequest);
    }

    private void save() {
        progressDialog.show();
        final String subjek = "";
        final String pesan = txtPesan.getText().toString().trim();
        final String id_reply = Integer.toString(id).trim();
        final String receiver = "1";
        final String email = ambilEmail;





        StringRequest sR = new StringRequest(Request.Method.POST, ConfigUmum.NEWMESSAGE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();


                        progressDialog.dismiss();
                        finish();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(KEY_EMAIL, email);
                params.put(KEY_REPLY, id_reply);
                params.put(KEY_PENERIMA, receiver);
                params.put(KEY_JUDUL, subjek);
                params.put(KEY_PESAN, pesan);

                return params;
            }

        };
        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        sR.setRetryPolicy(policy);
        requestQueue.add(sR);
    }
}
