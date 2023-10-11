package fpoly.anhnvph32739.duanmau.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fpoly.duanmau.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import fpoly.anhnvph32739.duanmau.MainActivity;
import fpoly.anhnvph32739.duanmau.database.DbHelper;
import fpoly.anhnvph32739.duanmau.database.LoaiSachDAO;
import fpoly.anhnvph32739.duanmau.database.SachDAO;
import fpoly.anhnvph32739.duanmau.fragment.FragmentSach;
import fpoly.anhnvph32739.duanmau.model.LoaiSach;
import fpoly.anhnvph32739.duanmau.model.Sach;

public class SachAdapter extends RecyclerView.Adapter<SachAdapter.Viewholder> {
    Activity atv;
    ArrayList<Sach> list;
    LoaiSachDAO loaiSachDAO;
    SachDAO sachDAO;
    Locale localeEN = new Locale("en", "EN");
    NumberFormat en = NumberFormat.getInstance(localeEN);

    public SachAdapter(Activity atv, ArrayList<Sach> list) {
        this.atv = atv;
        this.list = list;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_item_recycler_sach, parent, false);
        return new Viewholder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, @SuppressLint("RecyclerView") int position) {

        sachDAO = new SachDAO(atv, new DbHelper(atv));
        loaiSachDAO = new LoaiSachDAO(atv, new DbHelper(atv));
        Sach sach = list.get(position);
        String formatMoney = en.format(sach.getGiaThue());
        Sach sach1 = sachDAO.getSachWithName(sach.getTenSach());
        LoaiSach loaiSach = loaiSachDAO.getLoaiSachWithID(sach.getMaLoai());

        holder.tvMaSach.setText("Mã sách: " + sach1.getMaSach());
        holder.tvTenSach.setText("Tên sách: " + sach.getTenSach());
        holder.tvGiaThue.setText("Giá thuê: " + formatMoney + " VNĐ");
        holder.tvLoaiSach.setText("Loại sách: " + loaiSach.getTenLoai());

        holder.imgDeleteBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sachDAO.checkExistsNameBook(sach.getTenSach())) {
                    Toast.makeText(atv, "Sách đang có người thuê, không thể xoá lúc này", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(atv);
                    builder.setMessage("Bạn có muốn xoá quyển sách này không !");
                    builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            list.remove(position);
                            sachDAO.deleteSach(sach);
                            notifyItemRangeRemoved(position, list.size());
                            notifyItemRemoved(position);
                        }
                    });

                    builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
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
                showAlertDialogAddBook(loaiSach, sach, position);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class Viewholder extends RecyclerView.ViewHolder {
        TextView tvMaSach, tvTenSach, tvGiaThue, tvLoaiSach;
        ImageView imgDeleteBook;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            tvMaSach = itemView.findViewById(R.id.tv_maSach);
            tvTenSach = itemView.findViewById(R.id.tv_tenSach_quanLySach);
            tvGiaThue = itemView.findViewById(R.id.tv_giaThueSach);
            tvLoaiSach = itemView.findViewById(R.id.tv_loaiSach_quanLySach);
            imgDeleteBook = itemView.findViewById(R.id.image_deleteBook);
        }
    }

    EditText edtUpdateTen, edtUpdateGia;
    Button btnConfirmUpdate;
    Spinner spinnerLoaiSach;
    String getTenSachUpdate;
    int getGiaUpdate, getMaLoaiSachUpdate;

    public void showAlertDialogAddBook(LoaiSach loaiSach, Sach sach, int position) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(atv, R.style.Style_AlertDialog_Corner);
        LayoutInflater inflater = atv.getLayoutInflater();
        View view1 = inflater.inflate(R.layout.custom_alertdialog_addbook, null);
        builder.setView(view1);
        androidx.appcompat.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();


        ArrayList<LoaiSach> listLoaiSach = loaiSachDAO.getAllData();
        SpinnerLoaiSachAdapter spinerLoaiSachAdapter = new SpinnerLoaiSachAdapter(atv, listLoaiSach);

        spinnerLoaiSach = view1.findViewById(R.id.sp_loaiSach);

        spinnerLoaiSach.setAdapter(spinerLoaiSachAdapter);
        edtUpdateTen = view1.findViewById(R.id.edt_themTenSach);
        edtUpdateGia = view1.findViewById(R.id.edt_themGiaThue);
        btnConfirmUpdate = view1.findViewById(R.id.btn_confirmAddBook);
        setDefaultSpinnerLoaiSach(listLoaiSach, loaiSach.getTenLoai());
        edtUpdateTen.setText(sach.getTenSach());
        edtUpdateGia.setText(String.valueOf(sach.getGiaThue()));
        spinnerLoaiSach.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getMaLoaiSachUpdate = listLoaiSach.get(position).getMaLoai();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                getMaLoaiSachUpdate = sach.getMaLoai();
            }
        });

        btnConfirmUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTenSachUpdate = edtUpdateTen.getText().toString().trim();
                getGiaUpdate = Integer.parseInt(edtUpdateGia.getText().toString().trim());


                if (getTenSachUpdate.length() == 0) {
                    Toast.makeText(atv, "Không được để trống tên !", Toast.LENGTH_SHORT).show();
                } else if (edtUpdateGia.getText().toString().trim().length() == 0) {
                    Toast.makeText(atv, "Không được để trống giá thuê !", Toast.LENGTH_SHORT).show();
                } else if (checkPrice()) {

                } else {
                    Sach sach1 = new Sach(sach.getMaSach(), getTenSachUpdate, getGiaUpdate, getMaLoaiSachUpdate);
                    list.set(position, sach1);
                    sachDAO.updateSach(sach1, "Update sách thành công !", "Update sách thất bại !");
                    notifyItemChanged(position);
                    notifyItemRangeChanged(position, list.size());
                    alertDialog.dismiss();
                }
            }
        });

    }

    public boolean checkPrice() {
        try {
            int gia = Integer.parseInt(edtUpdateGia.getText().toString().trim());

            return false;
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(atv, "Giá phải là số!", Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    public String[] getStringArray(ArrayList<LoaiSach> listLoaiSachToString) {
        String getName[] = new String[listLoaiSachToString.size()];

        for (int i = 0; i < listLoaiSachToString.size(); i++) {
            getName[i] = listLoaiSachToString.get(i).getTenLoai();
        }
        return getName;
    }

    public void setDefaultSpinnerLoaiSach(ArrayList<LoaiSach> list, String tenLoaiSach) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getTenLoai().equals(tenLoaiSach)) {
                spinnerLoaiSach.setSelection(i);
            }
        }
    }

}
