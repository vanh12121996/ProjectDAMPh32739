package fpoly.anhnvph32739.duanmau.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;

import fpoly.anhnvph32739.duanmau.model.Sach;
import fpoly.anhnvph32739.duanmau.model.ThanhVien;

public class SachDAO {
    Context context;
    DbHelper dbHelper;

    public SachDAO(Context context, DbHelper dbHelper) {
        this.context = context;
        this.dbHelper = dbHelper;
    }

    public boolean insertSach(Sach sach, String messagePositive, String messageNegative) {
        SQLiteDatabase sql = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MaLoaiSach", sach.getMaLoai());
        values.put("TenSach", sach.getTenSach());

        values.put("GiaThue", sach.getGiaThue());

        long result = sql.insert("Sach", null, values);
        if (result > 0) {
            Toast.makeText(context, messagePositive, Toast.LENGTH_SHORT).show();
            return true;
        } else {
            Toast.makeText(context, messageNegative, Toast.LENGTH_SHORT).show();
            return false;

        }
    }
    public boolean updateSach(Sach sach, String messagePositive, String messageNegative) {
        SQLiteDatabase sql = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("TenSach", sach.getTenSach());

        values.put("GiaThue", sach.getGiaThue());
        values.put("MaLoaiSach", sach.getMaLoai());

        long result = sql.update("Sach", values, "MaSach = ?", new String[]{String.valueOf(sach.getMaSach())});
        if (result > 0) {
            Toast.makeText(context, messagePositive, Toast.LENGTH_SHORT).show();
            return true;
        } else {
            Toast.makeText(context, messageNegative, Toast.LENGTH_SHORT).show();
            return false;

        }
    }

    public void deleteSach(Sach sach) {
        SQLiteDatabase sql = dbHelper.getWritableDatabase();
        long result = sql.delete("Sach", "MaSach = ?", new String[]{String.valueOf(sach.getMaSach())});

        if (result > 0) {
            Toast.makeText(context, "Delete book success !", Toast.LENGTH_SHORT).show();
        } else {

            Toast.makeText(context, "Delete book fail !", Toast.LENGTH_SHORT).show();
        }
    }

    public String[] getAllNameOfBook() {
        String[] listSach = new String[]{};
        SQLiteDatabase sql = dbHelper.getWritableDatabase();
        sql.beginTransaction();
        try {
            Cursor cursor = sql.rawQuery("SELECT TenSach FROM Sach", null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    String getTenSach = cursor.getString(0);
                    for (int i = 0; i < cursor.getCount(); i++) {
                        listSach[i] = getTenSach;
                    }
                } while (cursor.moveToNext());
                sql.setTransactionSuccessful();
            }
        } catch (Exception e) {

        } finally {
            sql.endTransaction();
        }

        return listSach;
    }

    public Sach getSachWithID(int maSach) {
        String sql = "SELECT * FROM Sach WHERE MaSach = ?";
        ArrayList<Sach> list = getData(sql, String.valueOf(maSach));
        return list.get(0);
    }


    public Sach getSachWithName(String tenSach) {
        String sql = "SELECT * FROM Sach WHERE TenSach = ?";
        ArrayList<Sach> list = getData(sql, tenSach);
        return list.get(0);
    }


    public boolean checkExistsNameBook(String tenSach) {
        String sqlQuery = "SELECT * FROM PhieuMuon WHERE MaSach IN (SELECT MaSach FROM Sach WHERE TenSach = ?)";
        SQLiteDatabase sql = dbHelper.getReadableDatabase();
        sql.beginTransaction();
        try {
            Cursor cursor = sql.rawQuery(sqlQuery, new String[]{tenSach});
            if (cursor.getCount() > 0) {

                return true;
            }
        } catch (Exception e) {

        } finally {
            sql.endTransaction();
        }
        return false;
    }

    public ArrayList<Sach> getData(String sqlQuery, String... args) {
        ArrayList<Sach> list = new ArrayList<>();
        SQLiteDatabase sql = dbHelper.getWritableDatabase();
        sql.beginTransaction();
        try {
            Cursor cursor = sql.rawQuery(sqlQuery, args);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    int getMaSach = cursor.getInt(0);
                    int getMaLoaiSach = cursor.getInt(1);
                    String getTenSach = cursor.getString(2);
                    int getGiaThue = cursor.getInt(3);
                    list.add(new Sach(getMaSach, getTenSach, getGiaThue, getMaLoaiSach));
                } while (cursor.moveToNext());
                sql.setTransactionSuccessful();
            }
        } catch (Exception e) {

        } finally {
            sql.endTransaction();
        }

        return list;
    }

    public ArrayList<Sach> getAllData() {
        ArrayList<Sach> list = new ArrayList<>();
        SQLiteDatabase sql = dbHelper.getWritableDatabase();
        sql.beginTransaction();
        try {
            Cursor cursor = sql.rawQuery("SELECT * FROM Sach", null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    int getMaSach = cursor.getInt(0);
                    int getMaLoaiSach = cursor.getInt(1);
                    String getTenSach = cursor.getString(2);
                    int getGiaThue = cursor.getInt(3);
                    list.add(new Sach(getMaSach, getTenSach, getGiaThue, getMaLoaiSach));
                } while (cursor.moveToNext());
                sql.setTransactionSuccessful();
            }else {
                Toast.makeText(context, "Ko co du lieu", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(context, "Không có dữ liệu list sách", Toast.LENGTH_SHORT).show();
        } finally {
            sql.endTransaction();
        }

        return list;
    }

    public boolean countSachOfLoaiSach(int maLoai) {
        SQLiteDatabase sql = dbHelper.getWritableDatabase();
        String sqlQuery = "SELECT MaLoaiSach FROM Sach WHERE  MaLoaiSach IN (SELECT MaLoaiSach FROM LoaiSach WHERE MaLoaiSach = ?)";

        Cursor cursor = sql.rawQuery(sqlQuery, new String[]{String.valueOf(maLoai)});
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
}
