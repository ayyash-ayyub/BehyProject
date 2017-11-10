package adompo.ayyash.behay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class TipsSehatDetail extends AppCompatActivity {
    String data1, data2, data3;
    TextView txtJudul, txtDetail, txtLabel, txtTgl;
    ImageView imgBanner;
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips_sehat_detail);

        txtJudul = (TextView)findViewById(R.id.txtJudul);
        txtDetail = (TextView)findViewById(R.id.txtDetail);
        txtLabel = (TextView)findViewById(R.id.txtLabel);
        txtTgl = (TextView)findViewById(R.id.txtTgl);

        imgBanner = (ImageView)findViewById(R.id.imgTumb);

        Intent i = getIntent();
        id = i.getIntExtra("id", 0);

        getContent(ConfigUmum.URL_NEWS_DETAIL+id);
    }

    private void getContent(String URL){
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        final JsonObjectRequest request = new JsonObjectRequest( URL,null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//                System.out.println("sabtu"+response.toString());
//
//
//                   Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();


                try {
                    JSONObject result = response.getJSONObject("news");
//                    System.out.println("mingu :"+ result.toString());
                    String judul = (String) result.get("judul");
                    String detail = (String) result.get("isi");
                    String label = (String) result.get("kategori");
                    String tanggal = (String) result.get("tanggal");
                    String gambar = (String) result.get("gambar");


                    txtJudul.setText(judul);
                    txtDetail.setText(detail);
                    txtLabel.setText(label);
                    txtTgl.setText(tanggal);
                    Picasso.with(getApplicationContext()).load("http://administrator.behy.co/public/assets/images/"+gambar).into(imgBanner);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Masalah pada koneksi, Silakan ulangi", Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(getApplicationContext(),TipsSehatDetail.class);
//                startActivity(intent);
                finish();
            }
        });

        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        queue.add(request);
    }
}
