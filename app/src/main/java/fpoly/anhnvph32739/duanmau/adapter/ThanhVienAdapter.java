package fpoly.anhnvph32739.duanmau.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.fpoly.duanmau.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import fpoly.anhnvph32739.duanmau.MainActivity;
import fpoly.anhnvph32739.duanmau.database.DbHelper;
import fpoly.anhnvph32739.duanmau.database.ThanhVienDAO;
import fpoly.anhnvph32739.duanmau.model.ThanhVien;

public class ThanhVienAdapter extends RecyclerView.Adapter<ThanhVienAdapter.ViewholderThanhVien> {
    Activity atv;
    ArrayList<ThanhVien> list;
    ThanhVienDAO thanhVienDAO;
    ThanhVien thanhVien;
    public ThanhVienAdapter(Activity atv, ArrayList<ThanhVien> list) {
        this.atv = atv;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewholderThanhVien onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_adapter_thanhvien, parent, false);
        return new ViewholderThanhVien(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewholderThanhVien holder, @SuppressLint("RecyclerView") int position) {
         thanhVien = list.get(position);
        thanhVienDAO = new ThanhVienDAO(atv, new DbHelper(atv));
        holder.tvHoTen.setText("Họ tên: " + thanhVien.getHoTen());
        holder.tvNgaySinh.setText("Ngày sinh: " + thanhVien.getNgaySinh());
        holder.tvMaTV.setText("Mã thành viên: " + thanhVien.getMaTV());
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showAlerdialogUpdate(thanhVien, position);
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewholderThanhVien extends RecyclerView.ViewHolder {
        TextView tvHoTen, tvNgaySinh, tvMaTV;


        public ViewholderThanhVien(@NonNull View itemView) {
            super(itemView);

            tvHoTen = itemView.findViewById(R.id.tv_hoTenThanhVien);
            tvNgaySinh = itemView.findViewById(R.id.tv_ngaySinhThanhVien);
            tvMaTV = itemView.findViewById(R.id.tv_maThanhVien);
        }
    }

    EditText edtUpdateName, edtUpdateBirthDay;
    Button btnConfirmUpdateThanhVien;
    TextView tvTitle;

    public void showAlerdialogUpdate(ThanhVien thanhVien, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(atv, R.style.Style_AlertDialog_Corner);
        LayoutInflater inflater = atv.getLayoutInflater();
        View view1 = inflater.inflate(R.layout.custom_alertdialog, null);
        builder.setView(view1);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        edtUpdateName = view1.findViewById(R.id.edt_addHoTenTV);
        edtUpdateBirthDay = view1.findViewById(R.id.edt_addNgaySinhTV);
        btnConfirmUpdateThanhVien = view1.findViewById(R.id.btn_confirmAddMember);
        tvTitle = view1.findViewById(R.id.tv_titleAlertMember);
        tvTitle.setText("Update thành viên");
        btnConfirmUpdateThanhVien.setText("Update");
        edtUpdateName.setText(thanhVien.getHoTen());
        edtUpdateBirthDay.setText(thanhVien.getNgaySinh());

        btnConfirmUpdateThanhVien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getName = edtUpdateName.getText().toString().trim();
                String getBirthDay = edtUpdateBirthDay.getText().toString().trim();

                if (getName.length() == 0) {
                    Toast.makeText(atv, "Bạn không được để trống tên thanh viên", Toast.LENGTH_SHORT).show();
                } else if (getBirthDay.length() == 0) {
                    Toast.makeText(atv, "Bạn không được để trống ngày sinh của thanh viên", Toast.LENGTH_SHORT).show();
                } else if (!checkDate(getBirthDay)) {
                    Toast.makeText(atv, "Ngày sinh không đúng định dạng(\"dd-MM-yyyy\"), vui lòng nhập lại", Toast.LENGTH_SHORT).show();
                } else {
                    ThanhVien tv = new ThanhVien(thanhVien.getMaTV(), getName, getBirthDay);
                    if (thanhVienDAO.updateThanhVien(tv)) {
                        alertDialog.dismiss();
                        notifyItemChanged(position);
                        list.set(position, tv);
                    }

                }
            }
        });

    }

    public boolean checkDate(String input) {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        try {
            dateFormat.parse(input);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


}
