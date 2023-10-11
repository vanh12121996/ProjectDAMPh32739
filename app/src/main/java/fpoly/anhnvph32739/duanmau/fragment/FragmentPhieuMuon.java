package fpoly.anhnvph32739.duanmau.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fpoly.duanmau.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import fpoly.anhnvph32739.duanmau.MainActivity;
import fpoly.anhnvph32739.duanmau.adapter.PhieuMuonAdapter;
import fpoly.anhnvph32739.duanmau.adapter.SpinnerSachAdapter;
import fpoly.anhnvph32739.duanmau.adapter.SpinnerThanhVienAdapter;
import fpoly.anhnvph32739.duanmau.database.DbHelper;
import fpoly.anhnvph32739.duanmau.database.PhieuMuonDAO;
import fpoly.anhnvph32739.duanmau.database.SachDAO;
import fpoly.anhnvph32739.duanmau.database.ThanhVienDAO;
import fpoly.anhnvph32739.duanmau.model.PhieuMuon;
import fpoly.anhnvph32739.duanmau.model.Sach;
import fpoly.anhnvph32739.duanmau.model.ThanhVien;
import fpoly.anhnvph32739.duanmau.model.ThuThu;


public class FragmentPhieuMuon extends Fragment {

    ImageView imgMagicBook;
    TextView tvHello, tvName, tvGiaMuon;
    View view;
    ArrayList<PhieuMuon> listPhieuMuon;
    ArrayList<ThanhVien> listThanhVien;
    RecyclerView recyclerView;
    SachDAO sachDAO;
    ThuThu thuThu;
    ThanhVienDAO thanhVienDAO;
    PhieuMuonAdapter adapter;
    ArrayList<Sach> listSach;
    PhieuMuonDAO phieuMuonDAO;
    FloatingActionButton btnAdd;
    String[] listTenSach, listTenTV;
    Spinner spHotenTV, spTenSach;
    TextView tvNgayMuon;
    String getTenTV, getTenSach, getNgayMuon;
    int getTrangThaiTraSach, getTienThue;
    Button btnSave;
    ThanhVien thanhVien;
    Sach sach;
    CheckBox chkTrangThaiTra;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_fragmen_phieu_muon, container, false);
        return view;
    }

    public void setAnimation() {
        imgMagicBook = view.findViewById(R.id.img_magicBook);
        tvHello = view.findViewById(R.id.tv_hello);
        tvName = view.findViewById(R.id.tv_nameOfMember);

        Animation animation = new TranslateAnimation(0, 0, 0, 50);
        animation.setDuration(2500);
        animation.setRepeatMode(Animation.REVERSE);
        animation.setRepeatCount(Animation.INFINITE);
        imgMagicBook.setAnimation(animation);

        Animation animation1 = new TranslateAnimation(-200, 0, 0, 0);
        animation1.setDuration(1000);
        animation1.setRepeatMode(Animation.RELATIVE_TO_SELF);
        animation1.setRepeatCount(0);
        tvHello.setAnimation(animation1);
    }

    void animationTypeWord() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tvName.setText("N");
            }
        }, 300);

        new Handler().postDelayed(() -> tvName.append("g"), 500);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tvName.append("u");
            }
        }, 700);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tvName.append("y");
            }
        }, 900);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tvName.append("ễ");
            }
        }, 1100);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tvName.append("n");
            }
        }, 1300);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tvName.append(" ");
            }
        }, 1500);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tvName.append("V");
            }
        }, 1700);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tvName.append("i");
            }
        }, 1900);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tvName.append("ệ");
            }
        }, 2100);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tvName.append("t");
            }
        }, 2300);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tvName.append(" ");
            }
        }, 2500);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tvName.append("A");
            }
        }, 2700);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tvName.append("n");
            }
        }, 2900);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tvName.append("h");
            }
        }, 3100);
    }

    Locale localeEN;
    NumberFormat en;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerPhieuMuon);
        btnAdd = view.findViewById(R.id.fab_themPhieuMuon);
        setAnimation();
        ((MainActivity) getActivity()).toolbar.setTitle("Quản lý phiếu mượn");
        Intent intent = getActivity().getIntent();
        thuThu = (ThuThu) intent.getSerializableExtra("ThuThu");
        localeEN = new Locale("en", "EN");
        en = NumberFormat.getInstance(localeEN);
        animationTypeWord();


        sachDAO = new SachDAO(getContext(), new DbHelper(getContext()));
        thanhVienDAO = new ThanhVienDAO(getContext(), new DbHelper(getContext()));
        phieuMuonDAO = new PhieuMuonDAO(getContext(), new DbHelper(getContext()));

        listSach = sachDAO.getAllData();
        listPhieuMuon = phieuMuonDAO.getAllData();
        listThanhVien = thanhVienDAO.getAllData();

        adapter = new PhieuMuonAdapter(listPhieuMuon, getActivity());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertThemPhieu();
            }
        });

    }


    int getIdThanhVien, getIdSach;

    // create dialog
    public void showAlertThemPhieu() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Style_AlertDialog_Corner);
        LayoutInflater inflater = getLayoutInflater();
        View view1 = inflater.inflate(R.layout.custom_alertdialog_themphieumuon, null);
        builder.setView(view1);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        spHotenTV = view1.findViewById(R.id.sp_tenTv_phieuMuon);
        spTenSach = view1.findViewById(R.id.sp_tenSach_phieuMuon);
        btnSave = view1.findViewById(R.id.btn_save_themPhieu);
        tvGiaMuon = view1.findViewById(R.id.tv_tienThue_phieuMuon);
        tvNgayMuon = view1.findViewById(R.id.tv_ngayMuon_phieuMuon);
        listTenSach = getStringArray(listSach);
        listTenTV = getTenThanhVien(listThanhVien);

        chkTrangThaiTra = view1.findViewById(R.id.chk_trangThaiPhieu);

        chkTrangThaiTra.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    getTrangThaiTraSach = 1;

                } else {
                    getTrangThaiTraSach = 0;

                }
            }
        });
        ArrayList<ThanhVien> list = thanhVienDAO.getAllData();
        SpinnerThanhVienAdapter spTv = new SpinnerThanhVienAdapter(list, getActivity());
        spHotenTV.setAdapter(spTv);
        spHotenTV.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getIdThanhVien = list.get(position).getMaTV();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spHotenTV.setSelection(0);
            }
        });
        // Spinner tên sách
        ArrayList<Sach> listSach = sachDAO.getAllData();
        SpinnerSachAdapter spTenSachAdapter = new SpinnerSachAdapter(listSach, getActivity());
        spTenSach.setAdapter(spTenSachAdapter);

        spTenSach.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                getIdSach = listSach.get(position).getMaSach();
                sach = sachDAO.getSachWithID(getIdSach);
                String formatMoney = en.format(sach.getGiaThue());
                tvGiaMuon.setText("Giá mượn: " + formatMoney + " VNĐ");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spTenSach.setSelection(0);
            }
        });

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();

        getNgayMuon = dateFormat.format(cal.getTime());
        tvNgayMuon.setText("Ngày: " + getNgayMuon);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTienThue = sach.getGiaThue();
                String getMaTT = thuThu.getMaThuThu();

                PhieuMuon phieuMuon = new PhieuMuon(getMaTT,getIdThanhVien, getIdSach, getTienThue, getTrangThaiTraSach, getNgayMuon);
                if (phieuMuonDAO.insertPhieuMuon(phieuMuon, "Thêm phiếu thành công", "Thêm phiếu thất bại")) {

//                    ((MainActivity) getActivity()).reloadFragment(new FragmentPhieuMuon());
                    alertDialog.dismiss();
                    ((MainActivity)getActivity()).reloadWhenAddPM(new FragmentPhieuMuon(), recyclerView, list.size()-1);
                }


            }
        });

    }

    public String[] getStringArray(ArrayList<Sach> listSachToString) {
        String[] getName = new String[listSachToString.size()];

        for (int i = 0; i < listSachToString.size(); i++) {
            getName[i] = listSachToString.get(i).getTenSach();
        }
        return getName;
    }

    public String[] getTenThanhVien(ArrayList<ThanhVien> listTV) {
        String[] getName = new String[listTV.size()];

        for (int i = 0; i < listTV.size(); i++) {
            getName[i] = listTV.get(i).getHoTen();
        }
        return getName;
    }

}