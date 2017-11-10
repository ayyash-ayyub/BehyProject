package adompo.ayyash.behay;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BukuHarian extends Fragment {
    public static BukuHarian newInstance() {
        BukuHarian fragment = new BukuHarian();
        return fragment;
    }

    private FragmentTabHost tabHost;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        tabHost = new FragmentTabHost(getActivity());
        tabHost.setup(getActivity(), getChildFragmentManager(), R.id.frame_layout);

        Bundle arg1 = new Bundle();
        arg1.putInt("Arg for Frag1", 1);
        tabHost.addTab(tabHost.newTabSpec("Tab1").setIndicator("Asupan Makanan"),
                AsupanMakanan.class, arg1);


        Bundle arg2 = new Bundle();
        arg2.putInt("Arg for Frag2", 2);
        tabHost.addTab(tabHost.newTabSpec("Tab2").setIndicator("Aktivitas Fisik"),
                AktivitasFisik.class, arg2);

        Bundle arg3 = new Bundle();
        arg3.putInt("Arg for Frag2", 3);
        tabHost.addTab(tabHost.newTabSpec("Tab3").setIndicator("Kondisi Tubuh"),
                KondisiTubuh.class, arg3);

        return tabHost;
    }
}
