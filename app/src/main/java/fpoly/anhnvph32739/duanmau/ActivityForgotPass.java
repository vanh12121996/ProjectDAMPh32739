package fpoly.anhnvph32739.duanmau;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.fpoly.duanmau.R;

import fpoly.anhnvph32739.duanmau.fragment.FragmentCheckExistsAcount;

public class ActivityForgotPass extends AppCompatActivity {
FrameLayout frameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);
        frameLayout = findViewById(R.id.frame_forgotpass);

        FragmentCheckExistsAcount check = new FragmentCheckExistsAcount();

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_forgotpass, check).commit();
    }
}