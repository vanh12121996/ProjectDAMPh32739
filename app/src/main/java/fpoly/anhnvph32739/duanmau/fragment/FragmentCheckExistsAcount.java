package fpoly.anhnvph32739.duanmau.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.fpoly.duanmau.R;

import java.util.Date;
import java.util.Random;

import fpoly.anhnvph32739.duanmau.ActivityForgotPass;
import fpoly.anhnvph32739.duanmau.ActivityLogin;
import fpoly.anhnvph32739.duanmau.ConfigNotification;
import fpoly.anhnvph32739.duanmau.database.DbHelper;
import fpoly.anhnvph32739.duanmau.database.ThuThuDao;

public class FragmentCheckExistsAcount extends Fragment {
    View view;
    ImageView imgBack;
    EditText edtUser;
    Button btnConfirm;
    int OTP;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_check_exists_acount, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imgBack = view.findViewById(R.id.img_back);
        edtUser = view.findViewById(R.id.edt_checkAccount);
        btnConfirm = view.findViewById(R.id.btn_confirmForgotPass);
        OTP = new Random().nextInt(1000);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               getActivity().onBackPressed();
            }
        });
        ThuThuDao thuThuDao = new ThuThuDao(getContext(), new DbHelper(getContext()));
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getUserName = edtUser.getText().toString().trim().toLowerCase();

                if (thuThuDao.checkUserNameTT(getUserName)) {
                    setNotification(OTP);
                    showAlertOTP(OTP);

                    Bundle bundle = new Bundle();
                    bundle.putString("KEY_USER", getUserName); // Put anything what you want

                    getParentFragmentManager().setFragmentResult("key", bundle);
                } else {
                    Toast.makeText(getContext(), "Tài khoản này không tồn tại !", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    EditText edtOTP;
    int finalOTP;

    void showAlertOTP(int OTP) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.Style_AlertDialog_Corner);
        LayoutInflater inflater = getLayoutInflater();
        View view1 = inflater.inflate(R.layout.alert_forgot_pass_otp, null);
        builder.setView(view1);
        AlertDialog alertDialog = builder.create();

        Button btnConfirm = view1.findViewById(R.id.btnConfirmOTP);
        Button btnCancel = view1.findViewById(R.id.btnCancel);
        edtOTP = view1.findViewById(R.id.edt_OTP);


        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String parseOTP = edtOTP.getText().toString().trim();

                if (checkOTP()) {

                } else {
                    finalOTP = Integer.parseInt(parseOTP);
                }
                if (OTP == finalOTP) {
                    ((ActivityForgotPass) getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.frame_forgotpass, new FragmentChangePassForgot()).commit();
                    alertDialog.dismiss();

                } else {
                    Toast.makeText(getContext(), "OTP không đúng, vui lòng nhập lại !", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();


    }

    boolean checkOTP() {
        try {
            int checkOTP = Integer.parseInt(edtOTP.getText().toString());
            if (checkOTP < 0) {
                Toast.makeText(getContext(), "OTP phải lớn hơn 0 !", Toast.LENGTH_SHORT).show();
                return true;
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "OTP phải là số !", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    void setNotification(int otp) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(getContext(), ConfigNotification.CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_launcher_background);
        builder.setContentTitle("Chào mừng đến với FFPT Polytechnic");
        builder.setContentText("Mã OTP khôi phục tài khoản là: " + otp);

        NotificationManagerCompat com = NotificationManagerCompat.from(getContext());

        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.POST_NOTIFICATIONS)
                == PackageManager.PERMISSION_GRANTED
        ) {
            com.notify((int) new Date().getTime(), builder.build());
        } else {
            // xin quyen
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.POST_NOTIFICATIONS},
                    999); // 999  là mã tuỳ ý để nhận biết sự kiện người dùng đồng ý hay từ chối
        }

    }
}