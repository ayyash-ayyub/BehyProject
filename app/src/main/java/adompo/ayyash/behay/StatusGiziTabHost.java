package adompo.ayyash.behay;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class StatusGiziTabHost extends Fragment {
    public static BukuHarian newInstance() {
        BukuHarian fragment = new BukuHarian();
        return fragment;
    }

    private FragmentTabHost tabHost;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        tabHost = new FragmentTabHost(getActivity());
        tabHost.setup(getActivity(), getChildFragmentManager(), R.id.frame_layout);

        Bundle arg1 = new Bundle();
        arg1.putInt("StatusGiziType", 1);
        tabHost.addTab(tabHost.newTabSpec("Tab1").setIndicator("IMT"),
                StatusGizi.class, arg1);


        Bundle arg2 = new Bundle();
        arg2.putInt("StatusGiziType", 2);
        tabHost.addTab(tabHost.newTabSpec("Tab2").setIndicator("Berat Badan"),
                StatusGizi.class, arg2);




        Bundle arg3 = new Bundle();
        arg3.putInt("StatusGiziType", 3);
        tabHost.addTab(tabHost.newTabSpec("Tab3").setIndicator("Tinggi Badan"),
                StatusGizi.class, arg3);

        Bundle arg4 = new Bundle();
        arg4.putInt("StatusGiziType", 4);
        tabHost.addTab(tabHost.newTabSpec("Tab4").setIndicator("Lemak Tubuh"),
                StatusGizi.class, arg4);


        Bundle arguments = getArguments();
        int tabIndex = arguments.getInt("ACTIVE_TAB", -1);
        tabHost.setCurrentTab(tabIndex);

        return tabHost;
    }



}
