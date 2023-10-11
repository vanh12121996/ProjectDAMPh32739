package fpoly.anhnvph32739.duanmau.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.fpoly.duanmau.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import fpoly.anhnvph32739.duanmau.MainActivity;
import fpoly.anhnvph32739.duanmau.adapter.SachAdapter;
import fpoly.anhnvph32739.duanmau.database.DbHelper;
import fpoly.anhnvph32739.duanmau.database.LoaiSachDAO;
import fpoly.anhnvph32739.duanmau.database.SachDAO;
import fpoly.anhnvph32739.duanmau.model.LoaiSach;
import fpoly.anhnvph32739.duanmau.model.Sach;


public class FragmentSach extends Fragment {
    View view;
    FloatingActionButton btnAddSach;
    SachAdapter sachAdapter;
    ArrayList<Sach> list;
    SachDAO sachDAO;
    LoaiSachDAO loaiSachDAO;
    ArrayList<LoaiSach> listLoaisach;
    RecyclerView recyclerView;
    Button btnConfirmAdd;
    String getLoaiSach;
    Toolbar toolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_sach, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnAddSach = view.findViewById(R.id.fabAddNewSach);
        toolbar = ((MainActivity) getActivity()).toolbar;
        toolbar.setTitle("Quản lý sách");
        toolbar.setBackgroundColor(Color.parseColor("#0C84C1"));
        recyclerView = view.findViewById(R.id.recyclerSach);
        sachDAO = new SachDAO(getContext(), new DbHelper(getContext()));
        loaiSachDAO = new LoaiSachDAO(getContext(), new DbHelper(getContext()));
        list = sachDAO.getAllData();
        sachAdapter = new SachAdapter(getActivity(), list);
        listLoaisach = loaiSachDAO.getAllData();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(sachAdapter);

        btnAddSach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialogAddBook();
            }
        });

    }

    EditText edtAddTen, edtAddGiaThue;

    public void showAlertDialogAddBook() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Style_AlertDialog_Corner);
        LayoutInflater inflater = getLayoutInflater();
        View view1 = inflater.inflate(R.layout.custom_alertdialog_addbook, null);
        builder.setView(view1);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        String listLoaiSachSpiner[] = getStringArray(listLoaisach);
        Spinner spinner = view1.findViewById(R.id.sp_loaiSach);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, listLoaiSachSpiner);
        spinner.setAdapter(spinnerAdapter);
        edtAddTen = view1.findViewById(R.id.edt_themTenSach);
        edtAddGiaThue = view1.findViewById(R.id.edt_themGiaThue);
        btnConfirmAdd = view1.findViewById(R.id.btn_confirmAddBook);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getLoaiSach = listLoaiSachSpiner[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnConfirmAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getTen = edtAddTen.getText().toString().trim();
                String getGiaThue = edtAddGiaThue.getText().toString().trim();

                if (getTen.length() == 0) {
                    Toast.makeText(getContext(), "Không được để trống tên !", Toast.LENGTH_SHORT).show();
                } else if (getGiaThue.length() == 0) {
                    Toast.makeText(getContext(), "Không được để trống giá thuê !", Toast.LENGTH_SHORT).show();
                } else {
                    LoaiSach getIdOfLoaiSach = loaiSachDAO.getLoaiSachWithName(getLoaiSach);
                    Sach sach = new Sach(getTen, Integer.parseInt(getGiaThue), getIdOfLoaiSach.getMaLoai());

                 if (checkNameSach(list, getTen)){
                     Toast.makeText(getContext(), "Sách đã tồn tại không thể thêm !", Toast.LENGTH_SHORT).show();
                 }else {
                     if (sachDAO.insertSach(sach, "Thêm sách thành công ", "Thêm sách thất bại")) {
                         list.add(sach);
                         sachAdapter.notifyItemInserted(list.size());
                         alertDialog.dismiss();
                         ((MainActivity) getActivity()).reloadFragment(new FragmentSach());
                     }
                 }

                }
            }
        });

    }

    public String[] getStringArray(ArrayList<LoaiSach> listLoaiSachToString) {
        String getName[] = new String[listLoaiSachToString.size()];

        for (int i = 0; i < listLoaiSachToString.size(); i++) {
            getName[i] = listLoaiSachToString.get(i).getTenLoai();
        }
        return getName;
    }

    public boolean checkNameSach(ArrayList<Sach> list, String name){
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getTenSach().equals(name)){
                return true;
            }
        }
        return false;
    }
}