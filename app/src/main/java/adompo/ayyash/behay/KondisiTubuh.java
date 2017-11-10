package adompo.ayyash.behay;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class KondisiTubuh extends Fragment {
    CardView cvBerat, cvTinggi, cvLemak;
    TextView txtBerat, txtTinggi, txtLemak;
    PrefManager pref;
    String email, berat,tinggi, lemak;


    public static KondisiTubuh newInstance() {
        KondisiTubuh fragment = new KondisiTubuh();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pref = new PrefManager(getActivity());
      email = pref.getActiveEmail().toString().trim();
        Log.d("ayyub",""+email);

//        getContent(ConfigUmum.GET_KONDISI_TUBUH+email);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_kondisi_tubuh, container, false);
        txtBerat = (TextView)rootView.findViewById(R.id.txtBerat);
        txtTinggi = (TextView)rootView.findViewById(R.id.txtTinggi);
        txtLemak = (TextView)rootView.findViewById(R.id.txtLemak);

        cvLemak = (CardView) rootView.findViewById(R.id.cvLT);
        cvLemak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(),FormLemak.class);
                getActivity().startActivity(i);
            }
        });
        cvTinggi = (CardView) rootView.findViewById(R.id.cvTB);
        cvTinggi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(),TinggiBadan.class);
                getActivity().startActivity(i);
            }
        });
        cvBerat = (CardView) rootView.findViewById(R.id.cvBB);
        cvBerat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(),BeratBadan.class);
                getActivity().startActivity(i);
            }
        });
  //      pref = new PrefManager(getActivity());

//        String email = "ayyub@tampan.com";

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getContent(ConfigUmum.GET_KONDISI_TUBUH+email);
    }

    private void getContent(String URL){
        RequestQueue queue = Volley.newRequestQueue(getContext());

        final JsonObjectRequest request = new JsonObjectRequest( URL,null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//                System.out.println("sabtu"+response.toString());
//
//
//                   Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();


                try {
//                    JSONObject result = response.getJSONObject("news");
//                    System.out.println("mingu :"+ result.toString());
                    if (response.getString("berat_badan").equals("null")){
                         berat = "0";
                    }else{
                        berat = response.getString("berat_badan");
                    }
                    if (response.getString("tinggi_badan").equals("null")){
                        tinggi = "0";
                    }else {
                        tinggi = response.getString("tinggi_badan");
                    }

                    if (response.getString("lemak_tubuh").equals("null")){
                        lemak = "0";
                    }else {
                        lemak = response.getString("lemak_tubuh");
                    }





                    txtBerat.setText(berat+" kg");
                    txtTinggi.setText(tinggi+" cm");
                    txtLemak.setText(lemak+" %");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Masalah pada koneksi, Silakan ulangi", Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(getApplicationContext(),TipsSehatDetail.class);
//                startActivity(intent);

            }
        });

        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        queue.add(request);
    }
}