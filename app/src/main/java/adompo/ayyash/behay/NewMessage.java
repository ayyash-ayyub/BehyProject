package adompo.ayyash.behay;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
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

import java.util.HashMap;
import java.util.Map;

public class NewMessage extends AppCompatActivity {

    public static final String KEY_JUDUL = "subjek";
    public static final String KEY_PESAN = "pesan";
    public static final String KEY_REPLY = "id_reply";
    public static final String KEY_PENERIMA= "receiver";
    public static final String KEY_EMAIL = "email";

    EditText txtPesan, txtSubjek;


    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);



        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Silahkan Tunggu...");

        txtPesan = (EditText) findViewById(R.id.txtPesan);
        txtSubjek = (EditText)findViewById(R.id.txtSubject);

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }


    private void save() {
        progressDialog.show();
        final String subjek = txtSubjek.getText().toString().trim();
        final String pesan = txtPesan.getText().toString().trim();
        final String id_reply = "".trim();
        final String receiver = "1";
        final String email = "test".toString().trim();





        StringRequest sR = new StringRequest(Request.Method.POST, ConfigUmum.REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();


                        progressDialog.dismiss();
                        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(i);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(KEY_JUDUL, subjek);
                params.put(KEY_PESAN, pesan);
                params.put(KEY_REPLY, id_reply);
                params.put(KEY_PENERIMA, receiver);
                params.put(KEY_EMAIL, email);


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
