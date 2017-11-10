package adompo.ayyash.behay;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class DetilAktifitas extends AppCompatActivity {
    int id, durasi;
    TextView txtAktifitas, txtKal;
    ImageView img;
    String nmaktifitas,kal,gambar, user;
    Button btnPlus, btnMin, btnSave;
    EditText txtDurasi;
    int x;
    ProgressDialog progressDialog;
    PrefManager pref;

    public static final String KEY_AKTIFITAS = "aktifitas";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_DURASI= "durasi";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detil_aktifitas);

        pref = new PrefManager(getApplicationContext());
        user = pref.getActiveEmail().toString().trim();


        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Silahkan Tunggu...");

        Intent i = getIntent();
        Bundle a = i.getExtras();
//        id = i.getIntExtra("id", 0);
        id = (int)a.get("id");
        nmaktifitas = (String)a.get("nama");
        gambar = (String)a.get("gambar");
        kal = (String)a.get("kalori");
//        aktifitas = i.getStringExtra("nama");
//        gambar = i.getStringExtra("gambar");
//        kal = i.getStringExtra("kalori");

        txtAktifitas = (TextView)findViewById(R.id.txtJudul);
        txtKal = (TextView)findViewById(R.id.txtKalori);
        img = (ImageView)findViewById(R.id.imgTumb);

        txtAktifitas.setText(nmaktifitas);
        txtKal.setText(kal);
        Picasso.with(getApplicationContext()).load("http://"+ConfigUmum.IP+"/public/assets/images/"+gambar).into(img);

        txtDurasi = (EditText)findViewById(R.id.txtDurasi);
        btnMin = (Button)findViewById(R.id.btnMin);
        btnMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                x = Integer.parseInt(txtDurasi.getText().toString());

                if (x == 0){

                    Snackbar snackbar = Snackbar
                            .make(view, "Minimal lebih dari 1 menit", Snackbar.LENGTH_LONG)
                            .setAction("RETRY", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                }
                            });

                    snackbar.show();
                }
                else{
                    x=x-1;
                    txtDurasi.setText(""+x);
                }
            }
        });
        btnPlus = (Button)findViewById(R.id.btnPlus);
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                x = Integer.parseInt(txtDurasi.getText().toString());

                if (x >= 0){

                    x=x+1;
                    txtDurasi.setText(""+x);
                }
                else{

                }
            }
        });
        btnSave = (Button)findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save(view);
            }
        });

    }

    private void save(final View view) {
        progressDialog.show();

        final String aktifitas = String.valueOf(id).trim();
        final String email = user.toString().trim();
        final String durasi = txtDurasi.getText().toString().trim();


        StringRequest sR = new StringRequest(Request.Method.POST, ConfigUmum.SAVE_AKTIFITAS_FISIK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();


                        progressDialog.dismiss();
                        //  signinhere.setText(response);
                       // Intent i = new Intent(getApplicationContext(), UtamaActivity.class);
                      //  startActivity(i);
                    //    finish();
                       // Fragment selectedFragment = new Fragment();
                      //  selectedFragment.getActivity().recreate();


                       finish();
                      //



                      // BukuHarian.instantiate()
                      // startActivity(getIntent());










                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Snackbar snackbar = Snackbar
                                .make(view, "Minimal lebih dari 1 menit", Snackbar.LENGTH_LONG)
                                .setAction("RETRY", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                    }
                                });
// Changing message text color
//                    snackbar.setActionTextColor(Color.RED);

// Changing action button text color
//                    View sbView = snackbar.getView();
//                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
//                    textView.setTextColor(Color.YELLOW);
                        snackbar.show();
                        progressDialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(KEY_AKTIFITAS, aktifitas);
                params.put(KEY_EMAIL, email);
                params.put(KEY_DURASI, durasi);


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
