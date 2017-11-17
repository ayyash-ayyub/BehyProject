package adompo.ayyash.behay.AktifitasFisikUser;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import adompo.ayyash.behay.R;

public class AktifitasFisikUserAdapter extends RecyclerView.Adapter<AktifitasFisikUserAdapter.AktifitasFisikViewHolder> {

    private Context context;
    private List<AktifitasFisikUserModel.Results> data;
    private AktifitasFisikUserDeleteClickHandler mHandler;



    public interface AktifitasFisikUserDeleteClickHandler {
        void onClick(int aktifitasId, String aktifitasText);
    }

    public AktifitasFisikUserAdapter(Context context, AktifitasFisikUserDeleteClickHandler handler) {
        this.context = context;
        this.mHandler = handler;
        this.data = new ArrayList<>();
    }

    public void setData(List<AktifitasFisikUserModel.Results> data) {
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public AktifitasFisikViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_aktifitas_user, parent, false);
        AktifitasFisikViewHolder viewHolder = new AktifitasFisikViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AktifitasFisikViewHolder holder, final int pilihanAktifitas) {
        AktifitasFisikUserModel.Results aktifitas = data.get(pilihanAktifitas);

        holder.tvAktifitas.setText(aktifitas.aktifitas);
        holder.tvdurasi.setText(String.format("%s menit", aktifitas.durasi));
        holder.tvkalori.setText(String.format("%s kalori", aktifitas.kalori));
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public class AktifitasFisikViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvAktifitas;
        TextView tvkalori;
        TextView tvdurasi;
        Button btnHapus;

        public AktifitasFisikViewHolder(View itemView) {
            super(itemView);
            tvAktifitas = (TextView) itemView.findViewById(R.id.tvAktifitas);
            tvkalori  = (TextView)itemView.findViewById(R.id.tvKal);
            tvdurasi  = (TextView)itemView.findViewById(R.id.tvDurasi);
            btnHapus = (Button)itemView.findViewById(R.id.btnHapus);

            btnHapus.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                mHandler.onClick(data.get(position).id, data.get(position).aktifitas);
            }
        }
    }
}
