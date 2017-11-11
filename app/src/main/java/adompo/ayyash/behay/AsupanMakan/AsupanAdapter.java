package adompo.ayyash.behay.AsupanMakan;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import adompo.ayyash.behay.R;



public class AsupanAdapter extends RecyclerView.Adapter<AsupanAdapter.AsupanViewHolder> {

    private Context context;
    private List<Asupan> listAsupan;
    private AsupanDeleteClickHandler clickHandler;

    public AsupanAdapter(Context context, AsupanDeleteClickHandler clickHandler) {
        this.context = context;
        this.listAsupan = new ArrayList<>();
        this.clickHandler = clickHandler;
    }

    @Override
    public AsupanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_aktifitas_user, parent, false);
        return new AsupanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AsupanViewHolder holder, int position) {
        Asupan asupan = listAsupan.get(position);
        holder.tvMakanan.setText(asupan.makanan);
        holder.tvKalori.setText(String.format("%.2f", Float.valueOf(asupan.kalori)));
        holder.tvUrt.setText(String.format("%s %s", asupan.jumlah, asupan.besaranMakanan));
    }

    @Override
    public int getItemCount() {
        return listAsupan.size();
    }

    public void setData(List<Asupan> asupans) {
        listAsupan.clear();
        listAsupan.addAll(asupans);
        notifyDataSetChanged();
    }

    public interface AsupanDeleteClickHandler {
        void onClick(int asupanId);
    }

    class AsupanViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvMakanan;
        TextView tvKalori;
        TextView tvUrt;
        Button imgDelete;

        AsupanViewHolder(View itemView) {
            super(itemView);

            tvMakanan = (TextView) itemView.findViewById(R.id.tvAktifitas);
            tvKalori = (TextView) itemView.findViewById(R.id.tvKal);
            tvUrt = (TextView) itemView.findViewById(R.id.tvDurasi);
            imgDelete = (Button) itemView.findViewById(R.id.btnHapus);

            imgDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                // TODO ini harusnya bukan idMakanan, tapi idFoodRecord
                int id = Integer.valueOf(listAsupan.get(position).idMakanan);
                clickHandler.onClick(id);
            }
        }
    }
}
