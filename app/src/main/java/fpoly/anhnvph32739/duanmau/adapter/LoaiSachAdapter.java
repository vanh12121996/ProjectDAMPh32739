package fpoly.anhnvph32739.duanmau.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fpoly.duanmau.R;

import java.util.ArrayList;

import fpoly.anhnvph32739.duanmau.MainActivity;
import fpoly.anhnvph32739.duanmau.database.DbHelper;
import fpoly.anhnvph32739.duanmau.database.LoaiSachDAO;
import fpoly.anhnvph32739.duanmau.database.SachDAO;
import fpoly.anhnvph32739.duanmau.fragment.FragmentLoaiSach;
import fpoly.anhnvph32739.duanmau.model.LoaiSach;
import fpoly.anhnvph32739.duanmau.model.Sach;

public class LoaiSachAdapter extends RecyclerView.Adapter<LoaiSachAdapter.Viewholder> {

    Activity atv;
    ArrayList<LoaiSach> list;
    LoaiSachDAO loaiSachDAO;
    SachDAO sachDAO;

    public LoaiSachAdapter(Activity atv, ArrayList<LoaiSach> list) {
        this.atv = atv;
        this.list = list;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_recycler_loaisach, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, @SuppressLint("RecyclerView") int position) {
        loaiSachDAO = new LoaiSachDAO(atv, new DbHelper(atv));
        sachDAO = new SachDAO(atv, new DbHelper(atv));
        LoaiSach loaiSach = list.get(position);
//
//        LoaiSach loaiSach1 = loaiSachDAO.getLoaiSachWithName(loaiSach.getTenLoai());
        holder.tvMaLoaiSach.setText("Mã loại: " + loaiSach.getMaLoai());
        holder.tvTenLoaiSach.setText("Tên loại: " + loaiSach.getTenLoai());

        boolean countSach = sachDAO.countSachOfLoaiSach(loaiSach.getMaLoai());
        holder.imgDeleteLoaiSach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (countSach) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(atv);
                    builder.setTitle("Cảnh báo !");
                    builder.setIcon(R.mipmap.warning);
                    builder.setMessage("Loại sách này đang chứa nhiều sách, không thế xoá lúc này !");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(atv);
                    builder.setMessage("Bạn có muốn xoá loại sách này không");
                    builder.setPositiveButton("Xoá", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (loaiSachDAO.deleteLoaiSach(loaiSach.getMaLoai())) {
                                list.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeRemoved(position, list.size());
                            }
                        }
                    });
                    builder.setNegativeButton("Huỷ", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    builder.show();
                }
            }

        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showDialogUpdateLoaiSach(loaiSach, position);
                return false;
            }
        });
    }

    EditText edtThemTenLoai;
    Button btnAddLoaiSach;

    TextView tvTittle;

    public void showDialogUpdateLoaiSach(LoaiSach loaiSach, int position) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(atv, R.style.Style_AlertDialog_Corner);
        LayoutInflater inflater = atv.getLayoutInflater();
        View view1 = inflater.inflate(R.layout.custom_alertdialog_themloaisach, null);
        builder.setView(view1);
        androidx.appcompat.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
        edtThemTenLoai = view1.findViewById(R.id.edt_themLoaiSach);
        btnAddLoaiSach = view1.findViewById(R.id.btnConfirmAddLoaiSach);
        edtThemTenLoai.setHint("Tên sách ");
        tvTittle = view1.findViewById(R.id.tv_titleAlertLoaiSach);
        tvTittle.setText("Update loại sách");
        edtThemTenLoai.setText(loaiSach.getTenLoai());
        btnAddLoaiSach.setText("Update");
        btnAddLoaiSach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getTenLoaiSach = edtThemTenLoai.getText().toString().trim();
                if (getTenLoaiSach.length() == 0) {
                    Toast.makeText(atv, "Không được để trống tên loại sách", Toast.LENGTH_SHORT).show();
                } else {
                    if (loaiSachDAO.updateLoaiSach(getTenLoaiSach, String.valueOf(loaiSach.getMaLoai()))) {
                        alertDialog.dismiss();
                        LoaiSach loaiSach1 = new LoaiSach(list.get(position).getMaLoai(), getTenLoaiSach);
                        notifyItemChanged(position);
                        list.set(position, loaiSach1);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class Viewholder extends RecyclerView.ViewHolder {

        TextView tvTenLoaiSach, tvMaLoaiSach;
        ImageView imgDeleteLoaiSach;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            tvTenLoaiSach = itemView.findViewById(R.id.tv_tenLoaiSach);
            tvMaLoaiSach = itemView.findViewById(R.id.tv_maLoaiSach);
            imgDeleteLoaiSach = itemView.findViewById(R.id.img_deleteLoaiSach);
        }
    }
}
