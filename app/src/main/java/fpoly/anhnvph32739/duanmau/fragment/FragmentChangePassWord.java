package fpoly.anhnvph32739.duanmau.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fpoly.duanmau.R;

import fpoly.anhnvph32739.duanmau.MainActivity;
import fpoly.anhnvph32739.duanmau.database.DbHelper;
import fpoly.anhnvph32739.duanmau.database.ThuThuDao;
import fpoly.anhnvph32739.duanmau.model.ThuThu;

public class FragmentChangePassWord extends Fragment {
    View view;
    EditText edtPassOld, edtPassNew, edtConfirmPassNew;
    Button btnConfirm;
    TextView tvTitle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_change_pass_word, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edtPassOld = view.findViewById(R.id.edt_passOld);
        edtPassNew = view.findViewById(R.id.edt_newPass);
        tvTitle = view.findViewById(R.id.tv_titleChangePass);
        edtConfirmPassNew = view.findViewById(R.id.edt_confirmNewPass);
        btnConfirm = view.findViewById(R.id.btn_confirmChangePass);
        ThuThuDao thuThuDao = new ThuThuDao(getContext(), new DbHelper(getContext()));
        Intent intent = getActivity().getIntent();
        ThuThu thuThu = (ThuThu) intent.getSerializableExtra("ThuThu");

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getPassOld = edtPassOld.getText().toString().trim();
                String getPassNew = edtPassNew.getText().toString().trim();
                String getConfirmPass = edtConfirmPassNew.getText().toString().trim();

                if (getPassOld.length() == 0) {
                    Toast.makeText(getContext(), "Không được để trống mật khẩu cũ !", Toast.LENGTH_SHORT).show();
                } else if (getPassNew.length() == 0) {
                    Toast.makeText(getContext(), "Không được để trống mật khẩu mới !", Toast.LENGTH_SHORT).show();
                } else if (getConfirmPass.length() == 0) {
                    Toast.makeText(getContext(), "Bạn chưa xác nhận lại mật khẩu !", Toast.LENGTH_SHORT).show();
                } else if (!getConfirmPass.equals(getPassNew)) {
                    Toast.makeText(getContext(), "Xác nhận mật khẩu mới chưa đúng !", Toast.LENGTH_SHORT).show();
                } else {
                    if (thuThuDao.checkAccount(thuThu.getMaThuThu(), getPassOld)) {
                        thuThuDao.updateThuThu(getPassNew, thuThu.getMaThuThu(), "Đổi mật khẩu thành công !", "Đổi mật khẩu thất bại");

                    } else {
                        Toast.makeText(getContext(), "Mật khẩu cũ chưa đúng !", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        TextPaint paint = tvTitle.getPaint();
        float width = paint.measureText("Tianjin, China");

        Shader textShader = new LinearGradient(0, 0, width, tvTitle.getTextSize(),
                new int[]{
                        Color.parseColor("#F97C3C"),
                        Color.parseColor("#FDB54E"),
                        Color.parseColor("#64B678"),
                        Color.parseColor("#478AEA"),
                        Color.parseColor("#8446CC"),
                }, null, Shader.TileMode.CLAMP);
        tvTitle.getPaint().setShader(textShader);

    }

    public void clearText() {
        edtPassOld.setText(" ");
        edtPassNew.setText(" ");
        edtConfirmPassNew.setText(" ");
    }
}