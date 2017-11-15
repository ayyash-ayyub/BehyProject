package adompo.ayyash.behay;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class PemenuhanGiziTabHost extends Fragment {

    private FragmentTabHost tabHost;

    public static BukuHarian newInstance() {
        BukuHarian fragment = new BukuHarian();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        tabHost = new FragmentTabHost(getActivity());
        tabHost.setup(getActivity(), getChildFragmentManager(), R.id.frame_layout);

        tabHost.addTab(tabHost.newTabSpec("Tab1").setIndicator("Kebutuhan Gizi"),
                RiwayatKebutuhanGizi.class, null);
        tabHost.addTab(tabHost.newTabSpec("Tab2").setIndicator("Asupan Gizi"),
                RiwayatAsupanGizi.class, null);
        tabHost.addTab(tabHost.newTabSpec("Tab3").setIndicator("Pemenuhan Gizi"),
                RiwayatPemenuhanGizi.class, null);

        return tabHost;
    }
}
