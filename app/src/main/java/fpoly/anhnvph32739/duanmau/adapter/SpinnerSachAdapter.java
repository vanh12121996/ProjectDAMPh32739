package fpoly.anhnvph32739.duanmau.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fpoly.duanmau.R;

import java.util.ArrayList;

import fpoly.anhnvph32739.duanmau.model.Sach;

public class SpinnerSachAdapter extends BaseAdapter {
    ArrayList<Sach> list;
    Activity atv;

    public SpinnerSachAdapter(ArrayList<Sach> list, Activity atv) {
        this.list = list;
        this.atv = atv;
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
    public View getView(int position, View view, ViewGroup parent) {
        view = atv.getLayoutInflater().inflate(R.layout.spinner_adapter_sach, parent, false);
        TextView tvTenSach = view.findViewById(R.id.tv_tenSachSpinner);
        tvTenSach.setText(list.get(position).getTenSach());
        return view;
    }
}
