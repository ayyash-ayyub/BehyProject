package adompo.ayyash.behay;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;
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
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.EntryXComparator;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import adompo.ayyash.behay.test.RiwayatAktifitasFisikList;
import adompo.ayyash.behay.test.RiwayatAktifitasFisikModel;
import adompo.ayyash.behay.test.RiwayatKondisiTubuhList;
import adompo.ayyash.behay.test.RiwayatKondisiTubuhModel;


public class GrafikAktifitasFisik extends Fragment {

    private ProgressDialog progressDialog;
    private TableLayout tl;
    private TableRow tr;
    private BarChart mChart;

    public static GrafikAktifitasFisik newInstance() {
        GrafikAktifitasFisik fragment = new GrafikAktifitasFisik();
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
        // no description text
        mChart.getDescription().setEnabled(false);
        // enable touch gestures
        mChart.setTouchEnabled(true);
        mChart.setDragDecelerationFrictionCoef(0.9f);
        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);
        mChart.setHighlightPerDragEnabled(true);
        // set an alternative background color
        mChart.setBackgroundColor(Color.WHITE);
        //mChart.setViewPortOffsets(0f, 0f, 0f, 0f);

        loadData();

        return rootView;
    }

    private void loadData() {
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        PrefManager prefManager = new PrefManager(getActivity());
        String email = prefManager.getActiveEmail();

        String url = "http://administrator.behy.co/User/getRiwayatAktifitasFisik/" + email;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson mGson = new GsonBuilder().create();
                RiwayatAktifitasFisikList list = mGson.fromJson(response, RiwayatAktifitasFisikList.class);

                setData(list);
                mChart.invalidate();
                formatLegend();

                tl.removeAllViews();
                addHeaders();
                addData(list.riwayatAktifitasFisik);

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

    private void setData(RiwayatAktifitasFisikList list) {
        ArrayList<BarEntry> values = new ArrayList<>();

        for (RiwayatAktifitasFisikModel model : list.riwayatAktifitasFisik) {
            float x = TimeUnit.SECONDS.toDays(Integer.valueOf(model.timestamp));
            float y = Float.valueOf(model.kalori);
            values.add(new BarEntry(x,y));
        }
        Collections.sort(values, new EntryXComparator());

        // create a dataset and give it a type
        BarDataSet set1 = new BarDataSet(values, "DataSet 1");
        set1.setDrawIcons(false);
//        set1.enableDashedLine(10f, 5f, 0f);
//        set1.enableDashedHighlightLine(10f, 5f, 0f);
        set1.setColor(0xFF20A6FA);
//        set1.setColors(ColorTemplate.MATERIAL_COLORS);
//        set1.setCircleColor(Color.BLACK);
//        set1.setLineWidth(1f);
//        set1.setCircleRadius(3f);
//        set1.setDrawCircleHole(false);
//        set1.setValueTextSize(9f);
        // TODO Conseder usage of setValueFormatter
//        set1.setDrawFilled(false);
//        set1.setFormLineWidth(1f);
//        set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
//        set1.setFormSize(15.f);

        // prepare dataSets
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        // create a data object with the datasets
        BarData data = new BarData(dataSets);
        data.setValueTextColor(Color.BLACK);
        data.setValueTextSize(9f);
        data.setBarWidth(0.9f);

        // set data
        mChart.setData(data);
        mChart.setAutoScaleMinMaxEnabled(true);
    }

    private void formatLegend() {
        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();
        l.setEnabled(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new IAxisValueFormatter() {

            private SimpleDateFormat mFormat = new SimpleDateFormat("d/M/yy");
            @Override
            public String getFormattedValue(float value, AxisBase axis) {

                long millis = TimeUnit.DAYS.toMillis((long) value);
                return mFormat.format(new Date(millis));
            }
        });

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
//        leftAxis.setAxisMaximum(3);
//        leftAxis.setAxisMinimum(-2);
        leftAxis.setDrawZeroLine(true);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);
    }

    private void addHeaders() {
        TextView tvTanggal, tvKalori;

        // create a table row dynamically //
        tr = new TableRow(getContext());
        tr.setLayoutParams(new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
        ));

        // Create textview to add to the row
        tvTanggal = new TextView(getContext());
        tvTanggal.setText("Tanggal");
        tvTanggal.setTextColor(Color.BLACK);
        tvTanggal.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tvTanggal.setPadding(5,5,5,0);
        tvTanggal.setGravity(Gravity.CENTER);
        tr.addView(tvTanggal);

        tvKalori = new TextView(getContext());
        tvKalori.setText("Total Aktifitas Fisik\n(Kalori)"); // TODO change the text of kalori
        tvKalori.setTextColor(Color.BLACK);
        tvKalori.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tvKalori.setPadding(5,5,5,0);
        tvKalori.setGravity(Gravity.CENTER);
        tr.addView(tvKalori);

        // Add the TableRow to the TableLayout
        tr.setBackgroundColor(Color.WHITE);
        tl.addView(tr, new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
        ));
    }

    private void addData(List<RiwayatAktifitasFisikModel> list) {
        TextView tvTanggal, tvKalori;

        int i = 0;
        for (RiwayatAktifitasFisikModel model : list) {
            // create a table row dynamically //
            tr = new TableRow(getContext());
            tr.setLayoutParams(new LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT
            ));

            SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat mFormat = new SimpleDateFormat("d/M/yy");
            Date d = null;
            try {
                d = sFormat.parse(model.tanggal);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            // Create textview to add to the row
            tvTanggal = new TextView(getContext());
            tvTanggal.setText(mFormat.format(d));
            tvTanggal.setTextColor(Color.BLACK);
            tvTanggal.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            tvTanggal.setPadding(5,20,5,20);
            tvTanggal.setGravity(Gravity.CENTER);
            tr.addView(tvTanggal);

            DecimalFormat df = new DecimalFormat("0");
            double kalori = Double.valueOf(model.kalori);

            tvKalori = new TextView(getContext());
            tvKalori.setText(df.format(kalori));
            tvKalori.setTextColor(Color.BLACK);
            tvKalori.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            tvKalori.setPadding(5,20,5,20);
            tvKalori.setGravity(Gravity.CENTER);
            tr.addView(tvKalori);

            // set row background
            if (i++ % 2 == 0){
                tr.setBackgroundColor(0xFFB3D9DE);
            } else {
                tr.setBackgroundColor(0xFFD8EBEE);
            }

            // Add the TableRow to the TableLayout
            tl.addView(tr, new LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT
            ));
        }
    }
}
