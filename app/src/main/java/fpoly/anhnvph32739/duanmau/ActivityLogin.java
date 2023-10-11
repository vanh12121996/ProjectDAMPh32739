package fpoly.anhnvph32739.duanmau;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fpoly.duanmau.R;

import java.util.prefs.Preferences;

import fpoly.anhnvph32739.duanmau.database.DbHelper;
import fpoly.anhnvph32739.duanmau.database.ThuThuDao;
import fpoly.anhnvph32739.duanmau.model.ThuThu;

public class ActivityLogin extends AppCompatActivity {

    Button btnLogin;
    ThuThuDao thuThuDao;
    EditText edtUserName, edtPassWord;
    public CheckBox chkRememberPass;
    TextView tvForgotPass;

    public void rememberAccount(String user, String pass, boolean isChecked) {
        SharedPreferences pref = getSharedPreferences("ACCOUNT_FILE", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        if (!isChecked) {
            editor.clear();
        } else {
            editor.putString("USER", user);
            editor.putString("PASS", pass);
            editor.putBoolean("REMEMBER", isChecked);

        }

        editor.commit();
    }

    public void showAccount() {
        SharedPreferences pref = getSharedPreferences("ACCOUNT_FILE", MODE_PRIVATE);

        boolean getStatus = pref.getBoolean("REMEMBER", false);

        if (getStatus == true) {
            edtUserName.setText(pref.getString("USER", ""));
            edtPassWord.setText(pref.getString("PASS", ""));
            chkRememberPass.setChecked(true);
        } else {
            edtUserName.setText("");
            edtPassWord.setText("");
            chkRememberPass.setChecked(false);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        RelativeLayout relativeLayout = findViewById(R.id.mainLayout);

        AnimationDrawable animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(1500);
        animationDrawable.setExitFadeDuration(2000);
        animationDrawable.start();
        chkRememberPass = findViewById(R.id.chk_rememberPassword);

        edtUserName = findViewById(R.id.edt_userName);
        edtPassWord = findViewById(R.id.edt_passWord);
        tvForgotPass = findViewById(R.id.tv_forgot_passWord);
        btnLogin = findViewById(R.id.btnLogin);
        thuThuDao = new ThuThuDao(ActivityLogin.this, new DbHelper(getApplicationContext()));
        chkRememberPass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String getUser = edtUserName.getText().toString().trim();
                String getPass = edtPassWord.getText().toString().trim();
                if (isChecked) {
                    rememberAccount(getUser, getPass, true);
                } else {
                    rememberAccount(getUser, getPass, false);
                }
            }
        });

        showAccount();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getUserName = edtUserName.getText().toString().trim();
                String getPassWord = edtPassWord.getText().toString().trim();

                if (thuThuDao.checkAccount(getUserName, getPassWord)) {
                    ThuThu thuThu = thuThuDao.getThuThuWithID(getUserName);
                    Intent intent = new Intent(ActivityLogin.this, MainActivity.class);
                    intent.putExtra("ThuThu", thuThu);
                    startActivity(intent);
                    Toast.makeText(ActivityLogin.this, "Đăng nhập thành công !", Toast.LENGTH_SHORT).show();
                    finish();
                } else if (getUserName.length() == 0) {
                    Toast.makeText(ActivityLogin.this, "Không được để trống tài khoản", Toast.LENGTH_SHORT).show();
                } else if (getPassWord.length() == 0) {
                    Toast.makeText(ActivityLogin.this, "Không được để trống mật khẩu ", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ActivityLogin.this, "Tài khoản hoặc mật khẩu không chính xác", Toast.LENGTH_SHORT).show();

                }
            }
        });


        tvForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityLogin.this, ActivityForgotPass.class);
                startActivity(intent);
            }
        });
    }


}