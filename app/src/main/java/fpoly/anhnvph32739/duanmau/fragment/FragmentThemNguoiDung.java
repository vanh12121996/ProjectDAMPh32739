package fpoly.anhnvph32739.duanmau.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fpoly.duanmau.R;

import fpoly.anhnvph32739.duanmau.MainActivity;
import fpoly.anhnvph32739.duanmau.database.DbHelper;
import fpoly.anhnvph32739.duanmau.database.ThuThuDao;
import fpoly.anhnvph32739.duanmau.model.ThuThu;

public class FragmentThemNguoiDung extends Fragment {
    View view;
    Toolbar toolbar;
    EditText edtUserName, edtFullName, edtPassword, edtConfirmPassword;
    Button btnSave, btnCancel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_them_nguoi_dung, container, false);

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar = ((MainActivity) getActivity()).toolbar;
        toolbar.setTitle("Thêm người dùng");
        toolbar.setBackgroundColor(Color.parseColor("#CDBA14"));
        toolbar.setTitleTextColor(Color.WHITE);
        ThuThuDao thuThuDao = new ThuThuDao(getContext(), new DbHelper(getContext()));
        edtUserName = view.findViewById(R.id.edt_userName_addUser);
        edtFullName = view.findViewById(R.id.edt_fullName_addUser);
        edtPassword = view.findViewById(R.id.edt_passWord_addUser);
        edtConfirmPassword = view.findViewById(R.id.edt_confirmPass_addUser);

        btnSave = view.findViewById(R.id.btn_confirm_addUser);
        btnCancel = view.findViewById(R.id.btn_cancel_addUser);

        btnSave.setOnClickListener(v -> {
            String getUser = edtUserName.getText().toString().trim();
            String getFullName = edtFullName.getText().toString().trim();
            String getPassWord = edtPassword.getText().toString().trim();
            String getConfirmPass = edtConfirmPassword.getText().toString().trim();

            if (getUser.length() == 0) {
                Toast.makeText(getContext(), "Không được để trống tên đăng nhập !", Toast.LENGTH_SHORT).show();
            } else if (getFullName.length() == 0) {
                Toast.makeText(getContext(), "Không được để trống tên người dùng !", Toast.LENGTH_SHORT).show();
            } else if (getPassWord.length() == 0) {
                Toast.makeText(getContext(), "Không được để trống mật khẩu !", Toast.LENGTH_SHORT).show();
            } else if (getConfirmPass.length() == 0 || !getConfirmPass.equals(getPassWord)) {
                Toast.makeText(getContext(), "Xác nhận mật khẩu không đúng !", Toast.LENGTH_SHORT).show();
            } else {
                ThuThu thuThu = new ThuThu(getUser, getFullName, getPassWord);

               if (thuThuDao.insertUser(thuThu, "Thêm người dùng thành công !", "Tên đăng nhập đã tồn tại !")){
                   clearText();
               }
            }
        });

        btnCancel.setOnClickListener(v -> {


        });

    }

    public void clearText(){
        edtUserName.setText(" ");
        edtPassword.setText(" ");
        edtConfirmPassword.setText(" ");
        edtFullName.setText(" ");
    }
}