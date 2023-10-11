package fpoly.anhnvph32739.duanmau.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fpoly.duanmau.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import fpoly.anhnvph32739.duanmau.MainActivity;
import fpoly.anhnvph32739.duanmau.adapter.LoaiSachAdapter;
import fpoly.anhnvph32739.duanmau.database.DbHelper;
import fpoly.anhnvph32739.duanmau.database.LoaiSachDAO;
import fpoly.anhnvph32739.duanmau.model.LoaiSach;

public class FragmentLoaiSach extends Fragment {
    View view;
    RecyclerView recyclerView;
    ArrayList<LoaiSach> list;
    Toolbar toolbar;
    FloatingActionButton fab;
    LoaiSachAdapter adapter;
    LoaiSachDAO loaiSachDAO;
    DbHelper dbHelper;
    EditText edtThemTenLoai;
    Button btnAddLoaiSach;

    TextView tvTittle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_loai_sach, container, false);
        return view;
    }

    public void setAdapter() {
        adapter = new LoaiSachAdapter(getActivity(), list);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerLoaiSach);
        dbHelper = new DbHelper(getContext());
        loaiSachDAO = new LoaiSachDAO(getContext(), dbHelper);
        list = loaiSachDAO.getAllData();
      toolbar =  ((MainActivity) getActivity()).toolbar;
        toolbar.setTitle("Quản lý loại sách");
        toolbar.setBackgroundColor(Color.parseColor("#C1943E"));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        setAdapter();
        fab = view.findViewById(R.id.fabAddNewLoaiSach);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertdialogAddLoaiSach();

            }
        });


    }


    public void showAlertdialogAddLoaiSach() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Style_AlertDialog_Corner);
        LayoutInflater inflater = getLayoutInflater();
        View view1 = inflater.inflate(R.layout.custom_alertdialog_themloaisach, null);
        builder.setView(view1);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        edtThemTenLoai = view1.findViewById(R.id.edt_themLoaiSach);
        btnAddLoaiSach = view1.findViewById(R.id.btnConfirmAddLoaiSach);

        tvTittle = view1.findViewById(R.id.tv_titleAlertLoaiSach);
        tvTittle.setText("Thêm loại sách");
        btnAddLoaiSach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getTenSach = edtThemTenLoai.getText().toString().trim();
                if (getTenSach.length() == 0) {
                    Toast.makeText(getContext(), "Không được để trống tên loại sách", Toast.LENGTH_SHORT).show();
                } else {
                    LoaiSach loaiSach = new LoaiSach(getTenSach);
                    if (loaiSachDAO.checkLoaiSachExists(getTenSach)) {
                        Toast.makeText(getContext(), "Loại sách này đã tồn lại, không thể thêm !", Toast.LENGTH_SHORT).show();
                    } else if (loaiSachDAO.insertLoaiSach(loaiSach)) {
                        list.add(loaiSach);

                        alertDialog.dismiss();
                        ((MainActivity) getContext()).reloadFragment(new FragmentLoaiSach());
                        recyclerView.scrollToPosition(list.size());
                    }
                }

            }
        });
    }
}