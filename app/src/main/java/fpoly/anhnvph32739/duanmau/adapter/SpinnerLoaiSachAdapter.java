package fpoly.anhnvph32739.duanmau.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fpoly.duanmau.R;

import java.util.ArrayList;

import fpoly.anhnvph32739.duanmau.model.LoaiSach;

public class SpinnerLoaiSachAdapter extends BaseAdapter {
    Activity atv;
    ArrayList<LoaiSach> list;

    public SpinnerLoaiSachAdapter(Activity atv, ArrayList<LoaiSach> list) {
        this.atv = atv;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = atv.getLayoutInflater();
        convertView = inflater.inflate(R.layout.spinner_adapter_loaisach, parent, false);
        TextView tvLoaiSach = convertView.findViewById(R.id.tv_tenLoaiSach_Spinner);
        tvLoaiSach.setText(list.get(position).getTenLoai());
        return convertView;
    }
}
