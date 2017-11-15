package adompo.ayyash.behay;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.EntryXComparator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import adompo.ayyash.behay.test.RiwayatKebutuhanGiziList;
import adompo.ayyash.behay.test.RiwayatKebutuhanGiziModel;



public class RiwayatKebutuhanGizi extends Fragment {

    private ProgressDialog progressDialog;
    private TableLayout tl;
    private TableRow tr;
    private BarChart mChart;

    public static RiwayatKebutuhanGizi newInstance() {
        RiwayatKebutuhanGizi fragment = new RiwayatKebutuhanGizi();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_grafik_riwayat_fisik, container, false);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Silahkan Tunggu...");

        tl = (TableLayout) rootView.findViewById(R.id.table_status_gizi);

        mChart = (BarChart) rootView.findViewById(R.id.chart_riwayat_fisik);
        mChart.setPinchZoom(false);
        mChart.getDescription().setEnabled(false);
        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(false);
        mChart.setHighlightFullBarEnabled(false);
        mChart.setBackgroundColor(Color.WHITE);

        loadData();

        return rootView;
    }

    private void loadData() {
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        PrefManager prefManager = new PrefManager(getActivity());
        String email = prefManager.getActiveEmail();

        String url = "http://administrator.behy.co/User/getRiwayatKebutuhanGizi/" + email;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson mGson = new GsonBuilder().create();
                RiwayatKebutuhanGiziList list = mGson.fromJson(response, RiwayatKebutuhanGiziList.class);

                setChartData(list.riwayatKebutuhanGizi);

                tl.removeAllViews();
                addHeaders();
                addData(list.riwayatKebutuhanGizi);

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

    private void setChartData(List<RiwayatKebutuhanGiziModel> list) {
        // raw values
        ArrayList<BarEntry> values = new ArrayList<>();

        for (RiwayatKebutuhanGiziModel model : list) {
            float x = TimeUnit.SECONDS.toDays(model.timestamp);
            values.add(new BarEntry(x, new float[]{
                    model.karbohidrat.floatValue(),
                    model.protein.floatValue(),
                    model.lemak.floatValue()
            }));
        }
        Collections.sort(values, new EntryXComparator());

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
        //data.setBarWidth(0.5f);

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
            private SimpleDateFormat mFormat = new SimpleDateFormat("d/M/yy");

            @Override
            public String getFormattedValue(float value, AxisBase axis) {

                long millis = TimeUnit.DAYS.toMillis((long) value);
                mFormat.setTimeZone(TimeZone.getTimeZone("Asia/Jakarta"));
                return mFormat.format(new Date(millis));
            }
        });

        Legend legend = mChart.getLegend();
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
    }

    private int[] getColors() {

        int stacksize = 3;

        // have as many colors as stack-values per entry
        int[] colors = new int[stacksize];

        System.arraycopy(ColorTemplate.MATERIAL_COLORS, 0, colors, 0, colors.length);

        return colors;
    }

    private void addHeaders() {
        TextView tvTanggal, tvEnergi, tvKarbohidrat, tvProtein, tvLemak;

        // create a table row dynamically //
        tr = new TableRow(getContext());
        tr.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT
        ));

        // Create textview to add to the row
        tvTanggal = new TextView(getContext());
        tvTanggal.setText("Tanggal");
        tvTanggal.setTextColor(Color.BLACK);
        tvTanggal.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tvTanggal.setPadding(5, 5, 5, 0);
        tvTanggal.setGravity(Gravity.CENTER);
        tr.addView(tvTanggal);

        tvEnergi = new TextView(getContext());
        tvEnergi.setText("Energi\n(Kalori)");
        tvEnergi.setTextColor(Color.BLACK);
        tvEnergi.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tvEnergi.setPadding(5, 5, 5, 0);
        tvEnergi.setGravity(Gravity.CENTER);
        tr.addView(tvEnergi);

        tvKarbohidrat = new TextView(getContext());
        tvKarbohidrat.setText("Karbohidrat\n(gram)");
        tvKarbohidrat.setTextColor(Color.BLACK);
        tvKarbohidrat.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tvKarbohidrat.setPadding(5, 5, 5, 0);
        tvKarbohidrat.setGravity(Gravity.CENTER);
        tr.addView(tvKarbohidrat);

        tvProtein = new TextView(getContext());
        tvProtein.setText("Protein\n(gram)");
        tvProtein.setTextColor(Color.BLACK);
        tvProtein.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tvProtein.setPadding(5, 5, 5, 0);
        tvProtein.setGravity(Gravity.CENTER);
        tr.addView(tvProtein);

        tvLemak = new TextView(getContext());
        tvLemak.setText("Lemak\n(gram)");
        tvLemak.setTextColor(Color.BLACK);
        tvLemak.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tvLemak.setPadding(5, 5, 5, 0);
        tvLemak.setGravity(Gravity.CENTER);
        tr.addView(tvLemak);

        // Add the TableRow to the TableLayout
        tr.setBackgroundColor(Color.WHITE);
        tl.addView(tr, new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT
        ));
    }

    private void addData(List<RiwayatKebutuhanGiziModel> list) {
        TextView tvTanggal, tvEnergi, tvKarbohidrat, tvProtein, tvLemak;

        int i = 0;
        for (RiwayatKebutuhanGiziModel model : list) {
            // create a table row dynamically //
            tr = new TableRow(getContext());
            tr.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT
            ));

            SimpleDateFormat mFormat = new SimpleDateFormat("d/M/yy");

            // Create textview to add to the row
            tvTanggal = new TextView(getContext());
            tvTanggal.setText(mFormat.format(new Date((long)model.timestamp * 1000)));
            tvTanggal.setTextColor(Color.BLACK);
            tvTanggal.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            tvTanggal.setPadding(5, 20, 5, 20);
            tvTanggal.setGravity(Gravity.CENTER);
            tr.addView(tvTanggal);

            tvEnergi = new TextView(getContext());
            tvEnergi.setText(String.format(Locale.getDefault(), "%.0f", model.energi));
            tvEnergi.setTextColor(Color.BLACK);
            tvEnergi.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            tvEnergi.setPadding(5, 20, 5, 20);
            tvEnergi.setGravity(Gravity.RIGHT);
            tr.addView(tvEnergi);

            tvKarbohidrat = new TextView(getContext());
            tvKarbohidrat.setText(String.format(Locale.getDefault(), "%.0f", model.karbohidrat));
            tvKarbohidrat.setTextColor(Color.BLACK);
            tvKarbohidrat.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            tvKarbohidrat.setPadding(5, 20, 5, 20);
            tvKarbohidrat.setGravity(Gravity.RIGHT);
            tr.addView(tvKarbohidrat);

            tvProtein = new TextView(getContext());
            tvProtein.setText(String.format(Locale.getDefault(), "%.0f", model.protein));
            tvProtein.setTextColor(Color.BLACK);
            tvProtein.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            tvProtein.setPadding(5, 20, 5, 20);
            tvProtein.setGravity(Gravity.RIGHT);
            tr.addView(tvProtein);

            tvLemak = new TextView(getContext());
            tvLemak.setText(String.format(Locale.getDefault(), "%.0f", model.lemak));
            tvLemak.setTextColor(Color.BLACK);
            tvLemak.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            tvLemak.setPadding(5, 20, 5, 20);
            tvLemak.setGravity(Gravity.RIGHT);
            tr.addView(tvLemak);

            // set row background
            if (i++ % 2 == 0) {
                tr.setBackgroundColor(0xFFB3D9DE);
            } else {
                tr.setBackgroundColor(0xFFD8EBEE);
            }

            // Add the TableRow to the TableLayout
            tl.addView(tr, new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT
            ));
        }
    }
}
