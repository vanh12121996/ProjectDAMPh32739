package fpoly.anhnvph32739.duanmau.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fpoly.duanmau.R;

import fpoly.anhnvph32739.duanmau.ActivityLogin;
import fpoly.anhnvph32739.duanmau.database.DbHelper;
import fpoly.anhnvph32739.duanmau.database.ThuThuDao;
import fpoly.anhnvph32739.duanmau.model.ThuThu;

public class FragmentChangePassForgot extends Fragment {

    View view;
    EditText edtNewPass, edtConfirmNewPass;
    Button btnConfirm, btnCancel;
    String getUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_change_pass_forgot, container, false);
        getParentFragmentManager().setFragmentResultListener("key", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                getUser = result.getString("KEY_USER");

            }
        });
        return view;
    }

    ThuThu thuThu;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        edtNewPass = view.findViewById(R.id.edt_newPass_accountForgot);
        edtConfirmNewPass = view.findViewById(R.id.edt_confirmNewPass_accountForgot);
        btnConfirm = view.findViewById(R.id.btn_confirmNewPass_accountforgot);
        btnCancel = view.findViewById(R.id.btn_cancelNewPass_accountforgot);
        ThuThuDao thuThuDao = new ThuThuDao(getContext(), new DbHelper(getContext()));
        Intent intent = getActivity().getIntent();
        thuThu = (ThuThu) intent.getSerializableExtra("ThuThu");

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String getNewPass = edtNewPass.getText().toString().trim();
                String getConfirmPass = edtConfirmNewPass.getText().toString().trim();

                if (getNewPass.length() == 0) {
                    Toast.makeText(getContext(), "Không được để trống mật khẩu !", Toast.LENGTH_SHORT).show();
                } else if (getConfirmPass.length() == 0) {
                    Toast.makeText(getContext(), "Bạn chưa xác nhận lại mật khẩu !", Toast.LENGTH_SHORT).show();
                } else if (!getConfirmPass.equals(getNewPass)) {
                    Toast.makeText(getContext(), "Xác nhận mật khẩu mới chưa đúng !", Toast.LENGTH_SHORT).show();
                } else {
                    thuThuDao.updateThuThu(getNewPass, getUser, "Lấy lại mật khẩu thành công !", "Lấy lại mật khẩu thất bại !");
                    getActivity().onBackPressed();

                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }
}