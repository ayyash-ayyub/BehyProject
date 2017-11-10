package adompo.ayyash.behay.AktifitasFisik;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import adompo.ayyash.behay.DetilAktifitas;
import adompo.ayyash.behay.R;



public class AktifitasFisikAdapter extends RecyclerView.Adapter<AktifitasFisikAdapter.AktifitasFisikViewHolder> {

    private Context context;
    private List<AktifitasFisikModel.Results> data;
    private List<AktifitasFisikModel.Results> dataOriginal;

    public AktifitasFisikAdapter(Context context) {
        this.context = context;
        this.data = new ArrayList<AktifitasFisikModel.Results>();
        this.dataOriginal = new ArrayList<AktifitasFisikModel.Results>();
    }

    public void setData(List<AktifitasFisikModel.Results> data) {
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
            for (AktifitasFisikModel.Results obj : dataOriginal) {
                if (obj.aktifitas.toLowerCase().contains(search)) {
                    data.add(obj);
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public AktifitasFisikViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_aktifitas, parent, false);
        AktifitasFisikViewHolder viewHolder = new AktifitasFisikViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AktifitasFisikViewHolder holder, final int pilihanAktifitas) {
        final String text = data.get(pilihanAktifitas).aktifitas;
        final String gambar = data.get(pilihanAktifitas).gambar;
        final int id = data.get(pilihanAktifitas).id;

        holder.tv_item_aktifitas_fisik.setText(text);
        holder.tv_kal.setText((double) data.get(pilihanAktifitas).mets+" kal/mnt");
        holder.tv_item_aktifitas_fisik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context, DetilAktifitas.class);
                i.putExtra("id", id);
                i.putExtra("nama", text);
                i.putExtra("gambar", gambar);
                i.putExtra("kalori", (double) data.get(pilihanAktifitas).mets+" kal/mnt");

                v.getContext().startActivity(i);

                ((Activity)context).finish();

                //  Toast.makeText(context, "jadii " + pilihanAktifitas + ": " + text, Toast.LENGTH_SHORT).show();
//                Toast.makeText(context, "jadii: " + id + "" + text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    class AktifitasFisikViewHolder extends RecyclerView.ViewHolder {
        TextView tv_item_aktifitas_fisik, tv_kal;
        public AktifitasFisikViewHolder(View itemView) {
            super(itemView);
            tv_item_aktifitas_fisik = (TextView) itemView.findViewById(R.id.txtLabel);
            tv_kal  = (TextView)itemView.findViewById(R.id.txtKal);
        }
    }
}
