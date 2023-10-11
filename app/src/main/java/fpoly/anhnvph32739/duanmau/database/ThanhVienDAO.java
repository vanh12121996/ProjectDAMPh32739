package fpoly.anhnvph32739.duanmau.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import fpoly.anhnvph32739.duanmau.model.LoaiSach;
import fpoly.anhnvph32739.duanmau.model.Sach;
import fpoly.anhnvph32739.duanmau.model.ThanhVien;
import fpoly.anhnvph32739.duanmau.model.Top10;

import java.util.ArrayList;

public class ThanhVienDAO {
    Context context;
    DbHelper dbHelper;

    public ThanhVienDAO(Context context, DbHelper dbHelper) {
        this.context = context;
        this.dbHelper = dbHelper;
    }

    public boolean insertThanhVien(ThanhVien thanhVien) {
        SQLiteDatabase sql = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("HoTen", thanhVien.getHoTen());
        values.put("NgaySinh", thanhVien.getNgaySinh());

        long result = sql.insert("ThanhVien", null, values);
        if (result > 0) {
            Toast.makeText(context, "Đã thêm thành viên  !", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            Toast.makeText(context, "Thêm thành viên thất bại  !", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public boolean updateThanhVien(ThanhVien thanhVien) {
        SQLiteDatabase sql = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("HoTen", thanhVien.getHoTen());
        values.put("NgaySinh", thanhVien.getNgaySinh());

        long result = sql.update("ThanhVien", values, "MaTV = ?", new String[]{String.valueOf(thanhVien.getMaTV())});
        if (result > 0) {
            Toast.makeText(context, "Đã update thành viên  !", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            Toast.makeText(context, "Update thành viên thất bại  !", Toast.LENGTH_SHORT).show();
            return false;
        }
    }



    public ThanhVien getThanhVienWithID(int maThanhVien) {
        String sql = "SELECT * FROM ThanhVien WHERE MaTV = ?";
        ArrayList<ThanhVien> list = getData(sql, String.valueOf(maThanhVien));
        return list.get(0);
    }

    public void deleteThanhVien(ThanhVien thanhVien) {
        SQLiteDatabase sql = dbHelper.getWritableDatabase();
        long result = sql.delete("ThanhVien", "MaTV = ?", new String[]{String.valueOf(thanhVien.getMaTV())});

        if (result > 0) {
            Toast.makeText(context, "Đã xoá thành viên !", Toast.LENGTH_SHORT).show();
        } else {

            Toast.makeText(context, "Không xoá thành viên được, vui lòng thử lại sau !", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean checkThanhVienDelete(int maTv) {
        SQLiteDatabase sql = dbHelper.getWritableDatabase();

        String sqlQuery = "SELECT MaTV FROM PhieuMuon WHERE  MaTV IN (SELECT MaTV FROM ThanhVien WHERE MaTV = ?)";

        Cursor cursor = sql.rawQuery(sqlQuery, new String[]{String.valueOf(maTv)});
        sql.beginTransaction();
        try {

            if (cursor.getCount() > 0) {
                 return true;
            }
        } catch (Exception e) {

        } finally {
            sql.endTransaction();
            cursor.close();
        }

        return false;
    }

    public ArrayList<ThanhVien> getData(String sqlQuery, String... args) {
        ArrayList<ThanhVien> list = new ArrayList<>();
        SQLiteDatabase sql = dbHelper.getWritableDatabase();
        sql.beginTransaction();
        try {
            Cursor cursor = sql.rawQuery(sqlQuery, args);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    int getMaThanhVien = cursor.getInt(0);
                    String getTenThanhVien = cursor.getString(1);
                    String getNgaySinh = cursor.getString(2);
                    list.add(new ThanhVien(getMaThanhVien, getTenThanhVien, getNgaySinh));
                } while (cursor.moveToNext());
                sql.setTransactionSuccessful();
            }
        } catch (Exception e) {

        } finally {
            sql.endTransaction();
        }

        return list;
    }

    public ArrayList<ThanhVien> getAllData() {
        ArrayList<ThanhVien> list = new ArrayList<>();
        SQLiteDatabase sql = dbHelper.getReadableDatabase();
        sql.beginTransaction();
        try {
            Cursor cursor = sql.rawQuery("SELECT * FROM ThanhVien", null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    int getMaThanhVien = cursor.getInt(0);
                    String getTenThanhVien = cursor.getString(1);
                    String getNgaySinh = cursor.getString(2);
                    list.add(new ThanhVien(getMaThanhVien, getTenThanhVien, getNgaySinh));
                } while (cursor.moveToNext());
                sql.setTransactionSuccessful();
            }
        } catch (Exception e) {

        } finally {
            sql.endTransaction();
        }

        return list;
    }

    public ThanhVien getThanhVienByID(int maTV) {
        ArrayList<ThanhVien> list = new ArrayList<>();
        String sqlQuery = "SELECT * FROM ThanhVien WHERE MaTV = ?";
        SQLiteDatabase sql = dbHelper.getWritableDatabase();
        Cursor cursor = sql.rawQuery(sqlQuery, new String[]{String.valueOf(maTV)});

        while (cursor.moveToNext()) {
            @SuppressLint("Range") int getMaTV = Integer.parseInt(cursor.getString(cursor.getColumnIndex("MaTV")));
            @SuppressLint("Range") String getHoTen = cursor.getString(cursor.getColumnIndex("HoTen"));
            @SuppressLint("Range") String getNgaySinh = cursor.getString(cursor.getColumnIndex("NgaySinh"));
            list.add(new ThanhVien(getMaTV, getHoTen, getNgaySinh));
        }
        return list.get(0);
    }

}
