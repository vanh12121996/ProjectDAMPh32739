package fpoly.anhnvph32739.duanmau;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.fpoly.duanmau.R;
import com.google.android.material.navigation.NavigationView;

import fpoly.anhnvph32739.duanmau.fragment.FragmentChangePassWord;
import fpoly.anhnvph32739.duanmau.fragment.FragmentDoanhThu;
import fpoly.anhnvph32739.duanmau.fragment.FragmentPhieuMuon;
import fpoly.anhnvph32739.duanmau.fragment.FragmentLoaiSach;
import fpoly.anhnvph32739.duanmau.fragment.FragmentSach;
import fpoly.anhnvph32739.duanmau.fragment.FragmentThanhVien;
import fpoly.anhnvph32739.duanmau.fragment.FragmentThemNguoiDung;
import fpoly.anhnvph32739.duanmau.fragment.FragmentTop10;
import fpoly.anhnvph32739.duanmau.model.ThuThu;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    public Toolbar toolbar;
    NavigationView navigationView;
    ThuThu thuThu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigation);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Fragment fragment = new FragmentPhieuMuon();
        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, fragment).commit();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(
                MainActivity.this,
                drawerLayout,
                toolbar,
                R.string.nav_open,
                R.string.nav_close
        );

        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerToggle.syncState();
        drawerLayout.addDrawerListener(drawerToggle);

        Intent intent = getIntent();
        thuThu = (ThuThu) intent.getSerializableExtra("ThuThu");
        Toast.makeText(this, "" + thuThu.getMaThuThu(), Toast.LENGTH_SHORT).show();
        NavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setItemIconTintList(null);

        if (!thuThu.getMaThuThu().equals("admin")) {
            navigationView.getMenu().findItem(R.id.menu_themNguoiDung).setVisible(false);
        }
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                if (item.getItemId() == R.id.menu_quanLyPhieuMuon) {
                    Fragment fragment = new FragmentPhieuMuon();
                    getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, fragment).commit();
                    drawerLayout.close();
                    return true;
                } else if (item.getItemId() == R.id.menu_quanLyLoaiSach) {
                    Fragment fragment = new FragmentLoaiSach();
                    getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, fragment).commit();
                    drawerLayout.close();
                    return true;
                } else if (item.getItemId() == R.id.menu_quanLySach) {
                    Fragment fragment = new FragmentSach();
                    getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, fragment).commit();
                    drawerLayout.close();
                    return true;
                } else if (item.getItemId() == R.id.menu_quanLyThanhVien) {
                    Fragment fragment = new FragmentThanhVien();
                    getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, fragment).commit();
                    drawerLayout.close();
                    return true;
                } else if (item.getItemId() == R.id.menu_topTen) {
                    Fragment fragment = new FragmentTop10();
                    getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, fragment).commit();
                    drawerLayout.close();
                    return true;
                } else if (item.getItemId() == R.id.menu_doanhThu) {
                    Fragment fragment = new FragmentDoanhThu();
                    getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, fragment).commit();
                    drawerLayout.close();
                    return true;
                } else if (item.getItemId() == R.id.menu_themNguoiDung) {
                    Fragment fragment = new FragmentThemNguoiDung();
                    getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, fragment).commit();
                    drawerLayout.close();
                    return true;
                } else if (item.getItemId() == R.id.menu_doiMatKhau) {
                    Fragment fragment = new FragmentChangePassWord();
                    getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, fragment).commit();
                    drawerLayout.close();
                    return true;
                } else if (item.getItemId() == R.id.menu_logOut) {
                    Intent intent1 = new Intent(MainActivity.this, ActivityLogin.class);
                    startActivity(intent1);
                    finish();
                }

                return false;
            }
        });

    }


    public void reloadFragment(Fragment fragment){

        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, fragment).commit();
    }

    public void reloadWhenAddPM(Fragment fragment, RecyclerView recyclerView,int position){
recyclerView.scrollToPosition(position);
        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, fragment).commit();
    }

}