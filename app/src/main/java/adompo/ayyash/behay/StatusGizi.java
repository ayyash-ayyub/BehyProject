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
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.EntryXComparator;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import adompo.ayyash.behay.test.RiwayatKondisiTubuhList;
import adompo.ayyash.behay.test.RiwayatKondisiTubuhModel;



public class StatusGizi extends Fragment {

    private ProgressDialog progressDialog;
    private TableLayout tl;
    private TableRow tr;
    private LineChart mChart;
    private int type;

    public static StatusGizi newInstance() {
        StatusGizi fragment = new StatusGizi();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_status_gizi, container, false);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Silahkan Tunggu...");

        tl = (TableLayout) rootView.findViewById(R.id.table_status_gizi);
        tl.setColumnShrinkable(4, true);

        mChart = (LineChart) rootView.findViewById(R.id.chart_status_gizi);
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

        // get argument from tabhost
        Bundle bundle = getArguments();
        type = bundle.getInt("StatusGiziType", 0);

        loadData();

        return rootView;
    }

    private void loadData() {
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        PrefManager prefManager = new PrefManager(getActivity());
        String email = prefManager.getActiveEmail();

        String url = "http://administrator.behy.co/User/getRiwayatKondisiTubuh/" + email;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson mGson = new GsonBuilder().create();
                RiwayatKondisiTubuhList list = mGson.fromJson(response, RiwayatKondisiTubuhList.class);

                if (list.getRiwayatKondisiTubuh().size() > 0){
                    setData(list);
                    mChart.invalidate();
                    formatLegend();
                }

                tl.removeAllViews();
                addHeaders();
                addData(list.getRiwayatKondisiTubuh());

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

    private void setData(RiwayatKondisiTubuhList list) {
        ArrayList<Entry> values = new ArrayList<Entry>();

        for (RiwayatKondisiTubuhModel model : list.getRiwayatKondisiTubuh()) {
            float x = TimeUnit.SECONDS.toDays(model.timestamp);
            float y = 0;
            switch (type) {
                case 1: // IMT
                    y = model.imt.floatValue();
                    break;
                case 2: // Berat Badan
                    y = Float.valueOf(model.beratBadan);
                    break;
                case 3: // Tinggi Badan
                    y = model.tinggiBadan.floatValue();
                    break;
                case 4: // Lemak Tubuh
                    y = Float.valueOf(model.lemakTubuh);
                    break;
            }
            values.add(new Entry(x,y));
        }
        Collections.sort(values, new EntryXComparator());

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(values, "DataSet 1");
        set1.enableDashedLine(10f, 5f, 0f);
        set1.enableDashedHighlightLine(10f, 5f, 0f);
        set1.setColor(Color.BLACK);
        set1.setCircleColor(Color.BLACK);
        set1.setLineWidth(1f);
        set1.setCircleRadius(3f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);
        set1.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                DecimalFormat df = null;
                switch (type) {
                    case 1: // IMT
                        df = new DecimalFormat("0.00");
                        break;
                    case 2: // Berat Badan
                        df = new DecimalFormat("0.0");
                        break;
                    case 3: // Tinggi Badan
                        df = new DecimalFormat("0.00");
                        break;
                    case 4: // Lemak Tubuh
                        df = new DecimalFormat("0");
                        break;
                }
                return df.format(value);
            }
        });
        set1.setDrawFilled(false);
        set1.setFormLineWidth(1f);
        set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
        set1.setFormSize(15.f);

        // create a data object with the datasets
        LineData data = new LineData(set1);
        data.setValueTextColor(Color.BLACK);
        data.setValueTextSize(9f);

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
        TextView tvTanggal, tvUmur, tvIMT, tvZScore, tvStatus;

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

        tvUmur = new TextView(getContext());
        tvUmur.setText("Umur\n(Tahun,Bulan)");
        tvUmur.setTextColor(Color.BLACK);
        tvUmur.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tvUmur.setPadding(5,5,5,0);
        tvUmur.setGravity(Gravity.CENTER);
        tr.addView(tvUmur);

        tvIMT = new TextView(getContext());
        switch (type) {
            case 1: // IMT
                tvIMT.setText("IMT");
                break;
            case 2: // Berat Badan
                tvIMT.setText("Berat Badan");
                break;
            case 3: // Tinggi Badan
                tvIMT.setText("Tinggi Badan");
                break;
            case 4: // Lemak Tubuh
                tvIMT.setText("Lemak Tubuh");
                break;
        }
        tvIMT.setTextColor(Color.BLACK);
        tvIMT.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tvIMT.setPadding(5,5,5,0);
        tvIMT.setGravity(Gravity.CENTER);
        tr.addView(tvIMT);

        tvZScore = new TextView(getContext());
        tvZScore.setText("Z-Score");
        tvZScore.setTextColor(Color.BLACK);
        tvZScore.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tvZScore.setPadding(5,5,5,0);
        tvZScore.setGravity(Gravity.CENTER);
        tr.addView(tvZScore);

        tvStatus = new TextView(getContext());
        tvStatus.setText("Status");
        tvStatus.setTextColor(Color.BLACK);
        tvStatus.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        tvStatus.setPadding(5,5,5,0);
        tvStatus.setGravity(Gravity.CENTER);
        tr.addView(tvStatus);

        // Add the TableRow to the TableLayout
        tr.setBackgroundColor(Color.WHITE);
        tl.addView(tr, new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
        ));
    }

    private void addData(List<RiwayatKondisiTubuhModel> list) {
        TextView tvTanggal, tvUmur, tvIMT, tvZScore, tvStatus;

        int i = 0;
        for (RiwayatKondisiTubuhModel model : list) {
            // create a table row dynamically //
            tr = new TableRow(getContext());
            tr.setLayoutParams(new LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT
            ));

            SimpleDateFormat mFormat = new SimpleDateFormat("d/M/yy");

            // Create textview to add to the row
            tvTanggal = new TextView(getContext());
            tvTanggal.setText(mFormat.format(new Date((long)model.timestamp * 1000)));
            tvTanggal.setTextColor(Color.BLACK);
            tvTanggal.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            tvTanggal.setPadding(5,20,5,20);
            tvTanggal.setGravity(Gravity.CENTER);
            tr.addView(tvTanggal);

            tvUmur = new TextView(getContext());
            tvUmur.setText(String.format("%s,%s", model.umur.getTahun(), model.umur.getBulan()));
            tvUmur.setTextColor(Color.BLACK);
            tvUmur.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            tvUmur.setPadding(5,20,5,20);
            tvUmur.setGravity(Gravity.CENTER);
            tr.addView(tvUmur);

            tvIMT = new TextView(getContext());
            switch (type) {
                case 1: // IMT
                    tvIMT.setText(String.format(Locale.getDefault(), "%.2f", model.imt.floatValue()));
                    break;
                case 2: // Berat Badan
                    tvIMT.setText(String.format(Locale.getDefault(), "%.1f", Float.valueOf(model.beratBadan)));
                    break;
                case 3: // Tinggi Badan
                    tvIMT.setText(String.format(Locale.getDefault(), "%.2f", model.tinggiBadan.floatValue()));
                    break;
                case 4: // Lemak Tubuh
                    tvIMT.setText(String.format(Locale.getDefault(), "%.0f", Float.valueOf(model.lemakTubuh)));
                    break;
            }
            tvIMT.setTextColor(Color.BLACK);
            tvIMT.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            tvIMT.setPadding(5,20,5,20);
            tvIMT.setGravity(Gravity.CENTER);
            tr.addView(tvIMT);

            tvZScore = new TextView(getContext());
            tvZScore.setText(model.zScore.toString());
            tvZScore.setTextColor(Color.BLACK);
            tvZScore.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            tvZScore.setPadding(5,20,5,20);
            tvZScore.setGravity(Gravity.CENTER);
            tr.addView(tvZScore);

            tvStatus = new TextView(getContext());
            tvStatus.setText(model.statusGizi);
            tvStatus.setTextColor(Color.BLACK);
            tvStatus.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            tvStatus.setPadding(5,20,5,20);
            tvStatus.setGravity(Gravity.CENTER);
            tr.addView(tvStatus);

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
