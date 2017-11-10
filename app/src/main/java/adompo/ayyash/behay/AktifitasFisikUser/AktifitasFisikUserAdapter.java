package adompo.ayyash.behay.AktifitasFisikUser;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;

import adompo.ayyash.behay.ConfigUmum;
import adompo.ayyash.behay.DetilAktifitas;
import adompo.ayyash.behay.R;


public class AktifitasFisikUserAdapter extends RecyclerView.Adapter<AktifitasFisikUserAdapter.AktifitasFisikViewHolder> {
    ProgressDialog progressDialog;
    private Context context;
    private List<AktifitasFisikUserModel.Results> data;
    private List<AktifitasFisikUserModel.Results> dataOriginal;

    public AktifitasFisikUserAdapter(Context context) {
        this.context = context;
        this.data = new ArrayList<AktifitasFisikUserModel.Results>();
        this.dataOriginal = new ArrayList<AktifitasFisikUserModel.Results>();
    }

    public void setData(List<AktifitasFisikUserModel.Results> data) {
        this.data.clear();
        this.dataOriginal.clear();
        this.data.addAll(data);
        this.dataOriginal.addAll(data);
        notifyDataSetChanged();
    }

    public void filter(String search) {

        this.data.clear();
        if (search.isEmpty()) {
            Log.d("ayyub-cari", "kosong");

            data.addAll(dataOriginal);
        } else {

            search = search.toLowerCase();
            for (AktifitasFisikUserModel.Results obj : dataOriginal) {
                if (obj.aktifitas.toLowerCase().contains(search)) {
                    data.add(obj);
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public AktifitasFisikViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_aktifitas_user, parent, false);
        AktifitasFisikViewHolder viewHolder = new AktifitasFisikViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AktifitasFisikViewHolder holder, final int pilihanAktifitas) {
        final String text = data.get(pilihanAktifitas).aktifitas;
        final Integer id = data.get(pilihanAktifitas).id;

        holder.tvAktifitas.setText(text);
        holder.tvdurasi.setText(data.get(pilihanAktifitas).durasi+" menit");
        holder.tvkalori.setText(data.get(pilihanAktifitas).kalori+" kalori");

//        holder.tvAktifitas.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent i = new Intent(context, DetilAktifitas.class);
//                v.getContext().startActivity(i);
//
//            }
//        });

        holder.btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                progressDialog = new ProgressDialog(context);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Silahkan Tunggu...");


                //  DeleteData(ConfigUmum.URL_DELETE_ACTIVITY+idd);
                // Intent i = new Intent(context, Pengalih.class);
                //  view.getContext().startActivity(i);


                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Konfirmasi");
                builder.setMessage("Apakah anda yakin ingin menghapus Aktifitas\n" +
                        text + " ?");
                builder.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //  http://103.43.45.237/recfon/api/delete_activity.php?id=13
                        DeleteData(ConfigUmum.URL_DELETE_ACTIVITY+id);
//                        Activity activity = (Activity)view.getContext();
//                        activity.finish();
//                        view.getContext().startActivity(activity.getIntent());

                    }
                });
                builder.setNegativeButton("Batalkan", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();


                //   Toast.makeText(context,"ID nya: "+resultsList.get(position).nama_makanan, Toast.LENGTH_LONG).show();
            }
        });



    }

    public void DeleteData(String Url) {

        RequestQueue queue = Volley.newRequestQueue(context.getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Url,
                new Response.Listener<String>() {;
                    @Override
                    public void onResponse(String response) {
                        Log.d("uye jadi hapus", response);


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("uye gagal hapus", error.toString());

            }
        });
        queue.add(stringRequest);
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

//    public class AktifitasFisikViewHolder {
//    }

    public class AktifitasFisikViewHolder extends RecyclerView.ViewHolder {
        TextView tvAktifitas, tvkalori, tvdurasi;
        Button btnHapus;
        public AktifitasFisikViewHolder(View itemView) {
            super(itemView);
            tvAktifitas = (TextView) itemView.findViewById(R.id.tvAktifitas);
            tvkalori  = (TextView)itemView.findViewById(R.id.tvKal);
            tvdurasi  = (TextView)itemView.findViewById(R.id.tvDurasi);
            btnHapus = (Button)itemView.findViewById(R.id.btnHapus);

        }
    }
}
