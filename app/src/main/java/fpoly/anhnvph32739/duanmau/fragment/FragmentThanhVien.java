package fpoly.anhnvph32739.duanmau.fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.fpoly.duanmau.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import fpoly.anhnvph32739.duanmau.MainActivity;
import fpoly.anhnvph32739.duanmau.adapter.ThanhVienAdapter;
import fpoly.anhnvph32739.duanmau.database.DbHelper;
import fpoly.anhnvph32739.duanmau.database.ThanhVienDAO;
import fpoly.anhnvph32739.duanmau.model.ThanhVien;

public class FragmentThanhVien extends Fragment {
    View view;
    FloatingActionButton fab;
    ArrayList<ThanhVien> list;
    DbHelper dbHelper;
    ThanhVienDAO thanhVienDAO;
    ThanhVienAdapter adapter;
    RecyclerView recyclerView;
    EditText edtAddName, edtAddBirthday;
    Button btnConfirmAdd;
    RelativeLayout relativeLayout;
    Toolbar toolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_thanh_vien, container, false);

        return view;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       toolbar = ((MainActivity) getActivity()).toolbar;
        toolbar.setTitle("Quản lý thành viên");
        toolbar.setBackgroundColor(Color.parseColor("#ffffff"));
        toolbar.setTitleTextColor(Color.parseColor("#333333"));
        relativeLayout = view.findViewById(R.id.layoutThanhVien);
        fab = view.findViewById(R.id.floatingBtn);
        dbHelper = new DbHelper(getContext());
        thanhVienDAO = new ThanhVienDAO(getContext(), dbHelper);
        list = thanhVienDAO.getAllData();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView = view.findViewById(R.id.recyclerThanhVien);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ThanhVienAdapter(getActivity(), list);
        recyclerView.setAdapter(adapter);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlerdialogAdd();
            }
        });


        ItemTouchHelper.SimpleCallback itemCallBack = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                final int position = viewHolder.getAdapterPosition();
                ThanhVien thanhVien = list.get(position);

                if (thanhVienDAO.checkThanhVienDelete(thanhVien.getMaTV())) {
                    adapter.notifyItemChanged(position);
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Cảnh báo !");
                    builder.setIcon(R.mipmap.warning);
                    builder.setMessage("Thành viên đang mượn sách, không thể xoá");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            builder.setCancelable(true);
                        }
                    });
                    builder.show();
                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Bạn có muốn xoá thành viên này không");
                    builder.setPositiveButton("Xoá", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            list.remove(position);
                            adapter.notifyItemRemoved(position);
                            thanhVienDAO.deleteThanhVien(thanhVien);
                        }
                    });

                    builder.setNegativeButton("Huỷ", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            adapter.notifyItemChanged(position);
                        }
                    });

                    builder.show();
                }

            }
        };
        new ItemTouchHelper(itemCallBack).attachToRecyclerView(recyclerView);

    }

    public void showAlerdialogAdd() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Style_AlertDialog_Corner);
        LayoutInflater inflater = getLayoutInflater();
        View view1 = inflater.inflate(R.layout.custom_alertdialog, null);
        builder.setView(view1);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        edtAddName = view1.findViewById(R.id.edt_addHoTenTV);
        edtAddBirthday = view1.findViewById(R.id.edt_addNgaySinhTV);
        btnConfirmAdd = view1.findViewById(R.id.btn_confirmAddMember);


        btnConfirmAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getName = edtAddName.getText().toString().trim();
                String getBirthDay = edtAddBirthday.getText().toString().trim();

                if (getName.length() == 0) {
                    Toast.makeText(getContext(), "Bạn không được để trống tên thanh viên", Toast.LENGTH_SHORT).show();
                } else if (getBirthDay.length() == 0) {
                    Toast.makeText(getContext(), "Bạn không được để trống ngày sinh của thanh viên", Toast.LENGTH_SHORT).show();
                } else {
                    ThanhVien thanhVien = new ThanhVien(getName, getBirthDay);
                    if (thanhVienDAO.insertThanhVien(thanhVien)) {
                        list.add(thanhVien);
                        adapter.notifyItemInserted(list.size());
                        alertDialog.dismiss();
                        ((MainActivity)getActivity()).reloadFragment(new FragmentThanhVien());
                    }

                }
            }
        });

    }
}