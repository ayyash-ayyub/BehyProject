package adompo.ayyash.behay;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AsupanMakanan extends Fragment implements View.OnClickListener {

    public static AsupanMakanan newInstance() {
        AsupanMakanan fragment = new AsupanMakanan();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_asupan_makanan, container, false);

        rootView.findViewById(R.id.tombolSarapanPagi).setOnClickListener(this);
        rootView.findViewById(R.id.tombolSelinganPagi).setOnClickListener(this);
        rootView.findViewById(R.id.tombolMakanSiang).setOnClickListener(this);
        rootView.findViewById(R.id.tombolSelinganSiang).setOnClickListener(this);
        rootView.findViewById(R.id.tombolMakanMalam).setOnClickListener(this);
        rootView.findViewById(R.id.tombolSelinganMalam).setOnClickListener(this);

        return rootView;
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