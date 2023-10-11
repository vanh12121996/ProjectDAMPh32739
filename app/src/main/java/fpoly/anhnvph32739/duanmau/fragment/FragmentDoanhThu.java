package fpoly.anhnvph32739.duanmau.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fpoly.duanmau.R;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import fpoly.anhnvph32739.duanmau.MainActivity;
import fpoly.anhnvph32739.duanmau.database.DbHelper;
import fpoly.anhnvph32739.duanmau.database.PhieuMuonDAO;

public class FragmentDoanhThu extends Fragment {

    View view;
    EditText edtFromDay, edtToDay;
    Button btnShowDoanhThu;
    TextView tvDoanhThu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_doanh_thu, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        edtFromDay = view.findViewById(R.id.edt_fromDay);
        edtToDay = view.findViewById(R.id.edt_toDay);
        tvDoanhThu = view.findViewById(R.id.tv_doanhThu);
        btnShowDoanhThu = view.findViewById(R.id.btn_showDoanhThu);
        ((MainActivity) getActivity()).toolbar.setTitle("Quản lý doanh thu");
        edtFromDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(edtFromDay);
            }
        });

        edtToDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(edtToDay);
            }
        });
        Locale localeEN = new Locale("en", "EN");
        NumberFormat en = NumberFormat.getInstance(localeEN);
        PhieuMuonDAO phieuMuonDAO = new PhieuMuonDAO(getActivity(), new DbHelper(getActivity()));
        btnShowDoanhThu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fromDay = edtFromDay.getText().toString().trim();
                String toDay = edtToDay.getText().toString().trim();
                if (fromDay.length() == 0 || toDay.length() == 0){
                    Toast.makeText(getContext(), "Hãy nhập ngày để xem doanh thu", Toast.LENGTH_SHORT).show();
                }else if (!checkDate(fromDay)) {
                    Toast.makeText(getContext(), "Khác định dạng", Toast.LENGTH_SHORT).show();
                } else {
                    int getDoanhThu = phieuMuonDAO.getDoanhThu(fromDay, toDay);
                    String moneyFormat = en.format(getDoanhThu);
                    tvDoanhThu.setText("Doanh thu: " + moneyFormat + " VND");
                }
            }
        });
    }

    public void showDatePicker(EditText editText) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        if (month < 9){
                            editText.setText(year + "-" + "0" + (month + 1) + "-" + dayOfMonth);
                        }else if (dayOfMonth < 10){

                            editText.setText(year + "-" + (month + 1) + "-0" + dayOfMonth);
                        }else {
                            editText.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                        }

                    }

                }, year, month, day);

        datePickerDialog.show();
    }

    public boolean checkDate(String input) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            dateFormat.parse(input);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}