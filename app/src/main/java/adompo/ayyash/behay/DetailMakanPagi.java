package adompo.ayyash.behay;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adompo.ayyash.behay.test.DetailMakan;
import adompo.ayyash.behay.test.Makanan;
import adompo.ayyash.behay.test.Urt;

public class DetailMakanPagi extends AppCompatActivity {

    public static final String KEY_EMAIL = "email";
    public static final String KEY_ID_WAKTU_MAKAN = "id_waktu_makan";
    public static final String KEY_ID_BAHAN_MAKANAN = "id_bahan_makanan";
    public static final String KEY_JUMLAH = "jumlah";
    public static final String KEY_ID_BESARAN_MAKANAN = "id_besaran_makanan";
    String namaMakanan, kal;
    TextView namanya, kalory;
    ImageView imageView;
    Button buttonSaveMakanan;
    List<Urt> listUrt;
    List<String> listSpinner;
    Makanan makanan;
    PrefManager pref;
    EditText txtDurasi;
    ProgressDialog progressDialog;
    Button btnPlus, btnMin;
    Urt urt;
    int x;
    int idWaktuMakan, idMakanan;
    String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_makan_pagi);

        pref = new PrefManager(getApplicationContext());
        user = pref.getActiveEmail().toString().trim();

        listSpinner = new ArrayList<>();

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Silahkan Tunggu...");

        namanya = (TextView) findViewById(R.id.txtJudul);
        kalory = (TextView) findViewById(R.id.txtKalori);
        imageView = (ImageView) findViewById(R.id.imgTumb);
        txtDurasi = (EditText) findViewById(R.id.txtDurasiMakan);

        btnMin = (Button) findViewById(R.id.btnMinMakan);


        btnMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                x = Integer.parseInt(txtDurasi.getText().toString());

                if (x == 0) {

                    Snackbar snackbar = Snackbar
                            .make(view, "Input angka yang benar", Snackbar.LENGTH_LONG)
                            .setAction("RETRY", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                }
                            });

                    snackbar.show();
                } else {
                    x = x - 1;
                    txtDurasi.setText("" + x);
                }
            }
        });
        btnPlus = (Button) findViewById(R.id.btnPlusMakan);
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                x = Integer.parseInt(txtDurasi.getText().toString());

                if (x >= 0) {

                    x = x + 1;
                    txtDurasi.setText("" + x);
                } else {

                }
            }
        });


        buttonSaveMakanan = (Button) findViewById(R.id.btnSave);

        buttonSaveMakanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spinner mySpinner = (Spinner) findViewById(R.id.urt);
                int position = mySpinner.getSelectedItemPosition();

                urt = listUrt.get(position);

//                String idMakanan = makanan.getId();
//                String namaMakanan = makanan.getNamaMakanan();
//                String kalori = urt.getKalori();
                //  String idUrt = urt.getId();
//                String besarUrt = urt.getBesaranMakanan();


//                Toast.makeText(DetailMakanPagi.this,"emailnya : "+ user.trim().toString() + "\n" +
//
//                        "waktu makan : " + 1 + "\n"
//                                + "bahan makan: " + idMakanan + "\n"
//                                + "jumlah: " + txtDurasi.getText().toString() + "\n"
//                                + "idUrt: " + idUrt+ "\n",
//                             //   + "besarUrt: " + besarUrt + "\n",
//                        Toast.LENGTH_SHORT).show();


                save(v);
                //finish();
            }
        });

        Intent i = getIntent();
        Bundle a = i.getExtras();
        idWaktuMakan = i.getIntExtra("id_waktu_makan", 0);
        idMakanan = i.getIntExtra("id_makanan", 0);

        namaMakanan = (String) a.get("nama");
        kal = (String) a.get("kalori");

        namanya.setText(namaMakanan);
        kalory.setText(kal);

        loadUrt(idMakanan);
    }


    private void ekoGila(){
        System.out.println();
        int gila=100;
    }
    private void loadUrt(int id) {
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = "http://administrator.behy.co/User/getDetilMakanan/" + id;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new GsonBuilder().create();
                DetailMakan detailMakan = gson.fromJson(response, DetailMakan.class);

                namanya.setText(detailMakan.getMakanan().getNamaMakanan());
                makanan = detailMakan.getMakanan();
                listUrt = detailMakan.getUrt();
                listSpinner.clear();
                for (Urt urt : listUrt) {
                    listSpinner.add(urt.getBesaranMakanan());
                }
                setSpinnerData();

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

    private void setSpinnerData() {
        // Locate the spinner in activity_main.xml
        Spinner mySpinner = (Spinner) findViewById(R.id.urt);

        // Spinner adapter
        mySpinner.setAdapter(new ArrayAdapter<String>(DetailMakanPagi.this,
                android.R.layout.simple_spinner_dropdown_item,
                listSpinner));

        // Spinner on item click listener
        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Urt urt = listUrt.get(position);

                kalory.setText(urt.getKalori());
                if (urt.getGambar() != null) {
                    Picasso.with(DetailMakanPagi.this)
                            .load("http://administrator.behy.co/assets/images/urt/" + urt.getGambar())
                            .error(R.drawable.no_images)
                            .into(imageView);
                } else {
                    Picasso.with(DetailMakanPagi.this)
                            .load(R.drawable.no_images)
                            .into(imageView);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void save(final View view) {
        progressDialog.show();

        //  final String aktifitas = String.valueOf(id).trim();
        final String email = user.trim().toString();
        final String id_waktu_makan = String.format("%d", idWaktuMakan);
        final String id_bahan_makanan = makanan.getId();
        final String jumlah = txtDurasi.getText().toString();
        final String id_besaran_makanan = urt.getId();


        StringRequest sR = new StringRequest(Request.Method.POST, "http://administrator.behy.co/User/setAsupanMakanan",
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
                                .make(view, "inputkan data valid", Snackbar.LENGTH_LONG)
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

                params.put(KEY_EMAIL, email);
                params.put(KEY_ID_WAKTU_MAKAN, id_waktu_makan);
                params.put(KEY_ID_BAHAN_MAKANAN, id_bahan_makanan);
                params.put(KEY_JUMLAH, jumlah);
                params.put(KEY_ID_BESARAN_MAKANAN, id_besaran_makanan);


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
