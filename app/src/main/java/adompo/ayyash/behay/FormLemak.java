package adompo.ayyash.behay;

import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.text.UnicodeSetSpanner;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FormLemak extends AppCompatActivity {
    EditText inputLemak;
    Button simpanTuhLemak;



    public static final String KEY_EMAIL = "email";
    public static final String KEY_ALAT_LEMAK = "alat_lemak";
    public static final String KEY_LEMAK = "lemak_tubuh";
    Spinner spinner;
    PrefManager pref;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_lemak);

        pref = new PrefManager(this);
        // Log.d("bakso"," " + pref.getActiveEmail());


        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Silahkan Tunggu...");

        // Spinner element
        spinner = (Spinner) findViewById(R.id.spinner);

        inputLemak   = (EditText)findViewById(R.id.editTextLemak);
        simpanTuhLemak = (Button)findViewById(R.id.button3SimpanLemak);



        // Spinner Drop down elements
        final List<String> categories = new ArrayList<String>();
        categories.add("Tanita BC-418 Segmental Body Composition Analyzer");
        categories.add("seca mBCA 515");
        categories.add("Other");


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);



        simpanTuhLemak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(inputLemak.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Input data lemak", Toast.LENGTH_LONG).show();
                }else {
                    save();


                }


            }
        });


    }



    private void save() {
        progressDialog.show();

        final String email = pref.getActiveEmail();
        final String alat_lemak = spinner.getSelectedItem().toString().trim();
        final String lemak_tubuh = inputLemak.getText().toString().trim();




        StringRequest sR = new StringRequest(Request.Method.POST, ConfigUmum.SAVE_LEMAK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();


                        progressDialog.dismiss();
                        //  signinhere.setText(response);
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
                params.put(KEY_EMAIL, email);
                params.put(KEY_ALAT_LEMAK, alat_lemak);
                params.put(KEY_LEMAK, lemak_tubuh);


                return params;
            }

        };
//        Toast.makeText(getApplicationContext(), txt_email + " makanan = " + makanan, Toast.LENGTH_LONG).show();
        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        sR.setRetryPolicy(policy);
        requestQueue.add(sR);
    }
}
