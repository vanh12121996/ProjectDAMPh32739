package fpoly.anhnvph32739.duanmau.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fpoly.duanmau.R;

import fpoly.anhnvph32739.duanmau.MainActivity;
import fpoly.anhnvph32739.duanmau.database.DbHelper;
import fpoly.anhnvph32739.duanmau.database.PhieuMuonDAO;
import fpoly.anhnvph32739.duanmau.database.SachDAO;
import fpoly.anhnvph32739.duanmau.database.ThanhVienDAO;
import fpoly.anhnvph32739.duanmau.fragment.FragmentPhieuMuon;
import fpoly.anhnvph32739.duanmau.model.PhieuMuon;
import fpoly.anhnvph32739.duanmau.model.Sach;
import fpoly.anhnvph32739.duanmau.model.ThanhVien;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class PhieuMuonAdapter extends RecyclerView.Adapter<PhieuMuonAdapter.Viewholder> {
    ArrayList<PhieuMuon> list;
    Activity atv;
    ThanhVienDAO thanhVienDAO;
    SachDAO sachDAO;
    ThanhVien thanhVien;
    PhieuMuonDAO phieuMuonDAO;
    Locale localeEN = new Locale("en", "EN");
    NumberFormat en = NumberFormat.getInstance(localeEN);

    public PhieuMuonAdapter(ArrayList<PhieuMuon> list, Activity atv) {
        this.list = list;
        this.atv = atv;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_adapter_phieumuon, parent, false);
        return new Viewholder(view);
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, @SuppressLint("RecyclerView") int position) {
        DbHelper dbHelper = new DbHelper(atv);

        thanhVienDAO = new ThanhVienDAO(atv, dbHelper);
        sachDAO = new SachDAO(atv, dbHelper);
        PhieuMuon phieuMuon = list.get(position);
        phieuMuonDAO = new PhieuMuonDAO(atv, new DbHelper(atv));
        thanhVien = thanhVienDAO.getThanhVienWithID(phieuMuon.getMaTV());
        Sach sach = sachDAO.getSachWithID(phieuMuon.getMaSach());
        String moneyFormat = en.format(sach.getGiaThue());
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ThanhVien thanhVien1 = thanhVienDAO.getThanhVienByID(phieuMuon.getMaTV());
                showAlerUpdatePhieuMuon(position, thanhVien1.getHoTen(), sach.getTenSach());
                return false;
            }
        });
        holder.tvMaPhieuMuon.setText(String.format("Mã phiếu: %d", phieuMuon.getMaPM()));
        holder.tvHoTenTV.setText(String.format("Tên thành viên: %s", thanhVien.getHoTen()));
        holder.tvTenSach.setText(String.format("Tên sách: %s", sach.getTenSach()));
        holder.tvTienThue.setText(String.format("Giá thuê: %s VNĐ", moneyFormat));
        holder.tvNgayMuon.setText(String.format("Ngày mượn: %s", phieuMuon.getNgayMuon()));
        if (phieuMuon.getTraSach() == 1) {
            holder.tvTrangThaiTraSach.setText("Đã trả sách");
            holder.tvTrangThaiTraSach.setTextColor(Color.BLUE);
        } else {
            holder.tvTrangThaiTraSach.setText("Chưa trả sách");
            holder.tvTrangThaiTraSach.setTextColor(Color.RED);
        }

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(atv);
                builder.setMessage("Bạn có muốn xoá phiếu mượn này không !");
                builder.setPositiveButton("Xoá", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (phieuMuon.getTraSach() == 0) {
                            Toast.makeText(atv, "Chưa trả sách mà đòi xoá phiếu à !", Toast.LENGTH_SHORT).show();
                        } else if (phieuMuonDAO.deletePhieuMuon(phieuMuon, "Xoá phiếu thành công !", "Xoá phiêu thất bại !")
                        ) {
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
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class Viewholder extends RecyclerView.ViewHolder {

        TextView tvMaPhieuMuon, tvHoTenTV, tvTenSach, tvTienThue, tvNgayMuon, tvTrangThaiTraSach;
        ImageView imgDelete;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            tvMaPhieuMuon = itemView.findViewById(R.id.tv_maPhieuMuon);
            tvHoTenTV = itemView.findViewById(R.id.tv_tenThanhVien);
            tvTenSach = itemView.findViewById(R.id.tv_tenSach);
            tvTienThue = itemView.findViewById(R.id.tv_tienThue);
            tvNgayMuon = itemView.findViewById(R.id.tv_ngayMuon);
            tvTrangThaiTraSach = itemView.findViewById(R.id.tv_trangThaiTraSach);

            imgDelete = itemView.findViewById(R.id.img_delete);
        }
    }

    TextView tvNgayThue, tvTienThue;
    Spinner spTenTV, spTenSach;
    Button btnSave, btnCancel;
    int updateTrangThai, giaThueUpdate, maSachUpdate, maThanhVienUpdate;
    CheckBox chkTraSach;


    public void showAlerUpdatePhieuMuon(int position, String tenThanhVien, String tenSach) {
        AlertDialog.Builder builder = new AlertDialog.Builder(atv, R.style.Style_AlertDialog_Corner);
        LayoutInflater inflater = atv.getLayoutInflater();
        View view1 = inflater.inflate(R.layout.custom_dialog_update_phieumuon, null);
        builder.setView(view1);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        chkTraSach = view1.findViewById(R.id.chk_trangThaiPhieu);
        chkTraSach.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    updateTrangThai = 1;
                } else {
                    updateTrangThai = 0;
                }
            }
        });
        tvNgayThue = view1.findViewById(R.id.tv_ngayMuon_phieuMuon_update);
        tvTienThue = view1.findViewById(R.id.tv_tienThue_phieuMuon_update);

        spTenTV = view1.findViewById(R.id.sp_tenTv_phieuMuon_update);
        spTenSach = view1.findViewById(R.id.sp_tenSach_phieuMuon_update);

        btnSave = view1.findViewById(R.id.btn_save_themPhieu_update);
        btnCancel = view1.findViewById(R.id.btn_cancel_themPhieu_update);

        PhieuMuon phieuMuon = list.get(position);

        ArrayList<ThanhVien> listThanhVien = thanhVienDAO.getAllData();
        ArrayList<Sach> listSach = sachDAO.getAllData();
        SpinnerThanhVienAdapter spThanhVienAdapter = new SpinnerThanhVienAdapter(listThanhVien, atv);
        SpinnerSachAdapter spSachAdapter = new SpinnerSachAdapter(listSach, atv);
        spTenSach.setAdapter(spSachAdapter);
        spTenTV.setAdapter(spThanhVienAdapter);
//      set default selection in spinner
        setSpinnerTextDefaultTenSach(listSach, tenSach);
        setSpinnerTextDefaultTenThanhVien(listThanhVien, tenThanhVien);

        spTenTV.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                maThanhVienUpdate = listThanhVien.get(position).getMaTV();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                maThanhVienUpdate = phieuMuon.getMaTV();
            }
        });

        spTenSach.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                maSachUpdate = listSach.get(position).getMaSach();

                Sach sach = sachDAO.getSachWithID(maSachUpdate);
                String formatMoney = en.format(sach.getGiaThue());
                tvTienThue.setText(String.format("Giá mượn: %s VNĐ", formatMoney));
                giaThueUpdate = sach.getGiaThue();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                maSachUpdate = phieuMuon.getMaSach();
            }
        });
        tvNgayThue.setText(String.format("Ngày mượn: %s", phieuMuon.getNgayMuon()));

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phieuMuonDAO.updatePhieuMuon(phieuMuon.getMaPM(), maThanhVienUpdate, maSachUpdate, giaThueUpdate, updateTrangThai);
                PhieuMuon updatePhieu = new PhieuMuon(phieuMuon.getMaPM(), phieuMuon.getMaTT(), maThanhVienUpdate, maSachUpdate, giaThueUpdate, updateTrangThai, phieuMuon.getNgayMuon());
                list.set(position, updatePhieu);
                notifyItemChanged(position);
                alertDialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    public void setSpinnerTextDefaultTenSach(ArrayList<Sach> list, String tenSach) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getTenSach().equals(tenSach)) {
                spTenSach.setSelection(i);
            }
        }
    }

    public void setSpinnerTextDefaultTenThanhVien(ArrayList<ThanhVien> list, String tenThanhVien) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getHoTen().equalsIgnoreCase(tenThanhVien)) {
                spTenTV.setSelection(i);
            }
        }
    }


}
