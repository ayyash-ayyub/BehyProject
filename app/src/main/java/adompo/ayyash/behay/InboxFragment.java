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

import adompo.ayyash.behay.AktifitasFisikUser.AktifitasFisikUserAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class InboxFragment extends Fragment {
    FloatingActionButton fbAdd;
    private ProgressDialog progressDialog;
    private RecyclerView rv_item;
    private AktifitasFisikUserAdapter mAdapter;
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

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mAdapter = new AktifitasFisikUserAdapter(getActivity());
        rv_item = (RecyclerView) rootView.findViewById(R.id.rvInbox);
        rv_item.setLayoutManager(llm);
        rv_item.setAdapter(mAdapter);

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

        return rootView;
    }

}
