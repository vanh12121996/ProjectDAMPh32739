package fpoly.anhnvph32739.duanmau.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fpoly.duanmau.R;

import java.util.ArrayList;

import fpoly.anhnvph32739.duanmau.model.Top10;

public class Top10Adapter extends RecyclerView.Adapter<Top10Adapter.Viewholder> {
    ArrayList<Top10> list;
    Activity atv;

    public Top10Adapter(ArrayList<Top10> list, Activity atv) {
        this.list = list;
        this.atv = atv;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_item_top10, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        Top10 top10 = list.get(position);
        holder.tvTenSach.setText("Tên sách: " + top10.getTenSach());
        holder.tvSoLuong.setText("Số lượng: " + top10.getSoLuong());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class Viewholder extends RecyclerView.ViewHolder {

        TextView tvTenSach, tvSoLuong;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            tvTenSach = itemView.findViewById(R.id.tv_tenSachTop10);
            tvSoLuong = itemView.findViewById(R.id.tv_soLuong);
        }
    }
}
