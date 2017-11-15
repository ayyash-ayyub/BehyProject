package adompo.ayyash.behay;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Locale;

import adompo.ayyash.behay.test.SummaryModel;

public class MyBehy extends Fragment {

    private ProgressDialog progressDialog;
    private TextView textStatusGizi;
    private TextView textBeratBadan;
    private TextView textLemakTubuh;
    private TextView textKaloriAktifitas;
    private TextView textPemenuhanGizi;
    private TextView textKebutuhan;
    private TextView textAsupan;
    private HorizontalBarChart mChart;

    public static MyBehy newInstance() {
        MyBehy fragment = new MyBehy();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_my_behy, container, false);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Silahkan Tunggu...");

        textStatusGizi = (TextView) rootView.findViewById(R.id.mybehy_status_gizi);
        textBeratBadan = (TextView) rootView.findViewById(R.id.mybehy_berat_badan);
        textLemakTubuh = (TextView) rootView.findViewById(R.id.mybehy_lemak_tubuh);
        textKaloriAktifitas = (TextView) rootView.findViewById(R.id.mybehy_kalori_aktifitas_fisik);
        textPemenuhanGizi = (TextView) rootView.findViewById(R.id.mybehy_pemenuhan_gizi);
        textKebutuhan = (TextView) rootView.findViewById(R.id.mybehy_kebutuhan);
        textAsupan = (TextView) rootView.findViewById(R.id.mybehy_asupan);

        mChart = (HorizontalBarChart) rootView.findViewById(R.id.mybehy_chart);
        mChart.setPinchZoom(false);
        mChart.getDescription().setEnabled(false);
        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(false);
        mChart.setHighlightFullBarEnabled(false);
        mChart.setBackgroundColor(Color.TRANSPARENT);

        mChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new PemenuhanGiziTabHost();

                getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_layout, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        CardView card = (CardView) rootView.findViewById(R.id.mybehy_card_status_gizi);
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new StatusGiziTabHost();

                getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_layout, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        LinearLayout linear = (LinearLayout) rootView.findViewById(R.id.mybehy_aktifitas_fisik);
        linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new GrafikAktifitasFisik();

                getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_layout, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    public void loadData() {
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        PrefManager prefManager = new PrefManager(getContext());
        String email = prefManager.getActiveEmail();

        String url = "http://administrator.behy.co/User/getMyBehy/" + email;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson mGson = new GsonBuilder().create();
                SummaryModel model = mGson.fromJson(response, SummaryModel.class);

                if (model.exist == 1) {
                    textPemenuhanGizi.setText(String.format(Locale.getDefault(), "%d %%", (int) (model.pemenuhanGizi.energi * 100)));
                    textStatusGizi.setText(model.statusGizi.toUpperCase());
                    textBeratBadan.setText(String.format(Locale.getDefault(), "%d", model.beratBadan));
                    textLemakTubuh.setText(String.format(Locale.getDefault(), "%d", model.lemakTubuh));
                    textKaloriAktifitas.setText(String.format(Locale.getDefault(), "%d", model.kaloriAktifitasFisik.intValue()));
                    textKebutuhan.setText(String.format(Locale.getDefault(), "%d", model.kebutuhanGizi.energi.intValue()));
                    textAsupan.setText(String.format(Locale.getDefault(), "%d", model.asupanGizi.kalori.intValue()));

                    setChartData(model);
                } else {
                    textPemenuhanGizi.setText("0 %");
                    textStatusGizi.setText("INVALID");
                    textBeratBadan.setText("0");
                    textLemakTubuh.setText("0");
                    textKaloriAktifitas.setText("0");
                    textKebutuhan.setText("0");
                    textAsupan.setText("0");
                }

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

    private void setChartData(SummaryModel model) {
        // raw values
        ArrayList<BarEntry> values = new ArrayList<>();
        values.add(new BarEntry(0, new float[]{
                model.asupanGizi.karbohidrat.floatValue(),
                model.asupanGizi.protein.floatValue(),
                model.asupanGizi.lemak.floatValue()
        }));
        values.add(new BarEntry(1, new float[]{
                model.kebutuhanGizi.karbohidrat.floatValue(),
                model.kebutuhanGizi.protein.floatValue(),
                model.kebutuhanGizi.lemak.floatValue()
        }));

        // dataset
        BarDataSet set1 = new BarDataSet(values, "");
        set1.setDrawIcons(false);
        set1.setColors(getColors());
        set1.setStackLabels(new String[]{"Karbohidrat", "Protein", "Lemak"});
        set1.setDrawValues(false);

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        // data
        BarData data = new BarData(dataSets);
        data.setBarWidth(0.5f);

        // set chart
        mChart.setData(data);
        mChart.setFitBars(true);

        setChartProperties();

        mChart.invalidate();
    }

    private void setChartProperties() {
        mChart.getAxisLeft().setEnabled(false);
        mChart.getAxisRight().setDrawGridLines(false);
        mChart.getAxisRight().setDrawLabels(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setXOffset(-15.0f);
        xAxis.setGranularity(1.0f);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                String formattedValue = "";
                if (value == 0) {
                    formattedValue = "Asupan";
                } else {
                    formattedValue = "Kebutuhan";
                }
                return formattedValue;
            }
        });

        Legend legend = mChart.getLegend();
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
    }

    private int[] getColors() {

        int stacksize = 3;

        // have as many colors as stack-values per entry
        int[] colors = new int[stacksize];

        for (int i = 0; i < colors.length; i++) {
            colors[i] = ColorTemplate.MATERIAL_COLORS[i];
        }

        return colors;
    }
}