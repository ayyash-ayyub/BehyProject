package adompo.ayyash.behay;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.List;

import adompo.ayyash.behay.AktifitasFisikUser.AktifitasFisikUserModel;
import adompo.ayyash.behay.test.KaloriAsupanMakananList;
import adompo.ayyash.behay.test.KaloriAsupanMakananModel;

public class AsupanMakanan extends Fragment implements View.OnClickListener {

    private ProgressDialog progressDialog;
    private String ambilEmail;
    private TextView textMakanPagi;
    private TextView textMakanSiang;
    private TextView textMakanMalam;
    private TextView textSelinganPagi;
    private TextView textSelinganSiang;
    private TextView textSelinganMalam;

    public static AsupanMakanan newInstance() {
        AsupanMakanan fragment = new AsupanMakanan();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PrefManager prefManager = new PrefManager(getActivity());
        ambilEmail = prefManager.getActiveEmail();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_asupan_makanan, container, false);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Silahkan Tunggu...");

        rootView.findViewById(R.id.tombolSarapanPagi).setOnClickListener(this);
        rootView.findViewById(R.id.tombolSelinganPagi).setOnClickListener(this);
        rootView.findViewById(R.id.tombolMakanSiang).setOnClickListener(this);
        rootView.findViewById(R.id.tombolSelinganSiang).setOnClickListener(this);
        rootView.findViewById(R.id.tombolMakanMalam).setOnClickListener(this);
        rootView.findViewById(R.id.tombolSelinganMalam).setOnClickListener(this);

        textMakanPagi = (TextView) rootView.findViewById(R.id.tv_asupan_pagi1);
        textSelinganPagi = (TextView) rootView.findViewById(R.id.tv_asupan_pagi2);
        textMakanSiang = (TextView) rootView.findViewById(R.id.tv_asupan_siang1);
        textSelinganSiang = (TextView) rootView.findViewById(R.id.tv_asupan_siang2);
        textMakanMalam = (TextView) rootView.findViewById(R.id.tv_asupan_malam1);
        textSelinganMalam = (TextView) rootView.findViewById(R.id.tv_asupan_malam2);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        String url = "http://administrator.behy.co/User/getKaloriAsupan/" + ambilEmail;

        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                GsonBuilder builder = new GsonBuilder();
                Gson mGson = builder.create();
                KaloriAsupanMakananList resp = mGson.fromJson(response, KaloriAsupanMakananList.class);
                List<KaloriAsupanMakananModel> list = resp.kaloriAsupanMakanan;

                DecimalFormat df = new DecimalFormat("0");
                textMakanPagi.setText(String.format("%s kal", (list.get(0).kalori != null ? df.format(Double.valueOf(list.get(0).kalori)) : "0")));
                textMakanSiang.setText(String.format("%s kal", (list.get(2).kalori != null ? df.format(Double.valueOf(list.get(2).kalori)) : "0")));
                textMakanMalam.setText(String.format("%s kal", (list.get(4).kalori != null ? df.format(Double.valueOf(list.get(4).kalori)) : "0")));
                textSelinganPagi.setText(String.format("%s kal", (list.get(1).kalori != null ? df.format(Double.valueOf(list.get(1).kalori)) : "0")));
                textSelinganSiang.setText(String.format("%s kal", (list.get(3).kalori != null ? df.format(Double.valueOf(list.get(3).kalori)) : "0")));
                textSelinganMalam.setText(String.format("%s kal", (list.get(5).kalori != null ? df.format(Double.valueOf(list.get(5).kalori)) : "0")));

                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        queue.add(stringRequest);
    }

    @Override
    public void onClick(View v) {
        int value = 0;
        switch (v.getId()) {
            case R.id.tombolSarapanPagi:
                value = 1;
                break;
            case R.id.tombolSelinganPagi:
                value = 2;
                break;
            case R.id.tombolMakanSiang:
                value = 3;
                break;
            case R.id.tombolSelinganSiang:
                value = 4;
                break;
            case R.id.tombolMakanMalam:
                value = 5;
                break;
            case R.id.tombolSelinganMalam:
                value = 6;
                break;
        }

        Intent i = new Intent(getContext(), SarapanActivity.class);
        i.putExtra("id", value);
        startActivity(i);
    }
}