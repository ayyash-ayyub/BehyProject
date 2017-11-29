package adompo.ayyash.behay;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import adompo.ayyash.behay.AktifitasFisikUser.AktifitasFisikUserAdapter;
import adompo.ayyash.behay.inbox.ItemObjectInbox;
import adompo.ayyash.behay.inbox.MainAdapterInbox;
import adompo.ayyash.behay.news.ItemObjectNews;
import adompo.ayyash.behay.news.MainAdapterNews;

/**
 * A simple {@link Fragment} subclass.
 */
public class InboxFragment extends Fragment {
    FloatingActionButton fbAdd;
    private ProgressDialog progressDialog;
    private ItemObjectInbox.ObjectInbox objectInbox;
    private MainAdapterInbox adapter;
    private RecyclerView rv_item;
    String ambilEmail;


    public static InboxFragment newInstance() {
        InboxFragment fragment = new InboxFragment();
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PrefManager prefManager = new PrefManager(getActivity());
        ambilEmail = prefManager.getActiveEmail();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_inbox, container, false);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Silahkan Tunggu...");


        rv_item = (RecyclerView) rootView.findViewById(R.id.rvInbox);
        GetData(ConfigUmum.URL_INBOX+""+ambilEmail);

        fbAdd = (FloatingActionButton)rootView.findViewById(R.id.fb_add);
        fbAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),NewMessage.class);
                startActivity(i);

                //   getActivity().startActivity(i);
                // getActivity().startActivity(i);
                //   getActivity().recreate();
                //   getActivity().getLastCustomNonConfigurationInstance();




            }
        });
        // Inflate the layout for this fragment

        fbAdd.setVisibility(View.GONE);

        return rootView;
    }

    public void GetData(String URL) {

        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(getContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            ;

            @Override
            public void onResponse(String response) {
                GsonBuilder builder = new GsonBuilder();
                Gson mGson = builder.create();
                objectInbox = mGson.fromJson(response, ItemObjectInbox.ObjectInbox.class);
                System.out.println("Respond "+ response);
                LinearLayoutManager llm = new LinearLayoutManager(getContext());
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                rv_item.setLayoutManager(llm);

                System.out.println("size: " + objectInbox.list_inbox.size());
                adapter = new MainAdapterInbox(getContext(), objectInbox.list_inbox);
                rv_item.setAdapter(adapter);

                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Gagal Konek ke server, periksa jaringan anda :(", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });

        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        queue.add(stringRequest);
    }

}
