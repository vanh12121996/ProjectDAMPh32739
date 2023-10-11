package fpoly.anhnvph32739.duanmau.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {

    public DbHelper(@Nullable Context context) {
        super(context, "PNLib", null, 1);
    }

    String createTableThuThu = "CREATE TABLE ThuThu(MaTT TEXT PRIMARY KEY," +
            " HoTen TEXT NOT NULL," +
            " MatKhau TEXT NOT NULL)";
    String createTableThanhVien = "CREATE TABLE ThanhVien(MaTV INTEGER PRIMARY KEY AUTOINCREMENT, HoTen TEXT NOT NULL,NgaySinh TEXT NOT NULL)";

    String createTableLoaiSach = "CREATE TABLE LoaiSach(MaLoaiSach INTEGER PRIMARY KEY AUTOINCREMENT, TenLoai TEXT NOT NULL)";

    String createTableSach = "CREATE TABLE Sach(MaSach INTEGER PRIMARY KEY AUTOINCREMENT," +
            "MaLoaiSach REFERENCES LoaiSach(MaLoaiSach)," +
            " TenSach TEXT  NOT NULL," +
            " GiaThue INTEGER  NOT NULL)";
    String createTablePhieuMuon = "CREATE TABLE PhieuMuon(MaPhieuMuon INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "MaTV REFERENCES ThanhVien(MaTV)," +
            "MaSach REFERENCES Sach(MaSach)," +
            "MaTT REFERENCES ThuThu(MaTT)," +
            "Ngay DATE NOT NULL, " +
            "TraSach INTEGER NOT NULL," +
            "TienThue INTEGER NOT NULL)";

    String insertAdmin = "INSERT INTO ThuThu VALUES ('admin','Nguyễn Việt Anh', 'admin' )";
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createTableThuThu);
        db.execSQL(createTableThanhVien);
        db.execSQL(createTableLoaiSach);
        db.execSQL(createTableSach);
        db.execSQL(createTablePhieuMuon);
        db.execSQL(insertAdmin);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
