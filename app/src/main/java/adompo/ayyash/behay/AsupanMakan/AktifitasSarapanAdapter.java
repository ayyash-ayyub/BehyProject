package adompo.ayyash.behay.AsupanMakan;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import adompo.ayyash.behay.R;


public class AktifitasSarapanAdapter extends RecyclerView.Adapter<AktifitasSarapanAdapter.AktifitasSarapanViewHolder> {

    private Context context;
    private List<AktifitasSarapanModel.Results> data;
    private List<AktifitasSarapanModel.Results> dataOriginal;
    private AktifitasSarapanClickHandler clickHandler;

    public AktifitasSarapanAdapter(Context context, AktifitasSarapanClickHandler clickHandler) {
        this.context = context;
        this.clickHandler = clickHandler;
        this.data = new ArrayList<>();
        this.dataOriginal = new ArrayList<>();
    }

    public void setData(List<AktifitasSarapanModel.Results> data) {
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
            for (AktifitasSarapanModel.Results obj : dataOriginal) {
                if (obj.nama_makanan.toLowerCase().contains(search)) {
                    data.add(obj);
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public AktifitasSarapanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_aktifitas, parent, false);
        AktifitasSarapanViewHolder viewHolder = new AktifitasSarapanViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AktifitasSarapanViewHolder holder, final int pilihanAktifitas) {
        final String text = data.get(pilihanAktifitas).nama_makanan;
        final String kalorii = data.get(pilihanAktifitas).kalori;
        final String besmak = data.get(pilihanAktifitas).besaran_makanan;

        holder.tv_item_aktifitas_fisik.setText(text);
        holder.tv_kal.setText(kalorii + " kalori" + " / " + besmak);
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public interface AktifitasSarapanClickHandler {
        void onClick(int id);
    }

    class AktifitasSarapanViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv_item_aktifitas_fisik, tv_kal;

        public AktifitasSarapanViewHolder(View itemView) {
            super(itemView);
            tv_kal = (TextView) itemView.findViewById(R.id.txtKal);
            tv_item_aktifitas_fisik = (TextView) itemView.findViewById(R.id.txtLabel);
            tv_item_aktifitas_fisik.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            clickHandler.onClick(data.get(position).id);
        }
    }
}
