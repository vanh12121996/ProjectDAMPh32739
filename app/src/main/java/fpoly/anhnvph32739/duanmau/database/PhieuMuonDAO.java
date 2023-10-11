package fpoly.anhnvph32739.duanmau.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import fpoly.anhnvph32739.duanmau.model.LoaiSach;
import fpoly.anhnvph32739.duanmau.model.PhieuMuon;
import fpoly.anhnvph32739.duanmau.model.Sach;
import fpoly.anhnvph32739.duanmau.model.Top10;

public class PhieuMuonDAO {
    Context context;
    DbHelper dbHelper;

    public PhieuMuonDAO(Context context, DbHelper dbHelper) {
        this.context = context;
        this.dbHelper = dbHelper;
    }

    public boolean insertPhieuMuon(PhieuMuon phieuMuon, String messPositive, String messNegative) {
        String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar
                .getInstance().getTime());
        SQLiteDatabase sql = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MaTV", phieuMuon.getMaTV());
        values.put("MaSach", phieuMuon.getMaSach());
        values.put("MaTT", phieuMuon.getMaTT());
        values.put("Ngay", date);
        values.put("TraSach", phieuMuon.getTraSach());
        values.put("TienThue", phieuMuon.getTienThue());
        long result = sql.insert("PhieuMuon", null, values);
        if (result > 0) {
            Toast.makeText(context, messPositive, Toast.LENGTH_SHORT).show();
            return true;
        } else {
            Toast.makeText(context, messNegative, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public boolean deletePhieuMuon(PhieuMuon phieuMuon, String messPositive, String messNegative) {
        SQLiteDatabase sql = dbHelper.getWritableDatabase();
        long result = sql.delete("PhieuMuon", "MaPhieuMuon = ?", new String[]{String.valueOf(phieuMuon.getMaPM())});
        if (result > 0) {
            Toast.makeText(context, messPositive, Toast.LENGTH_SHORT).show();
            return true;
        } else {
            Toast.makeText(context, messNegative, Toast.LENGTH_SHORT).show();
            return false;
        }
    }


    public PhieuMuon getPhieuMuonWithMa(int maPhieu) {
        String sql = "SELECT * FROM PhieuMuon WHERE MaPhieuMuon = ?";
        ArrayList<PhieuMuon> list = getData(sql, String.valueOf(maPhieu));
        return list.get(0);
    }

    public ArrayList<PhieuMuon> getAllData() {
        String sql = "SELECT * FROM PhieuMuon";
        return getData(sql);
    }




    public ArrayList<PhieuMuon> getData(String query, String... agrs) {
        ArrayList<PhieuMuon> list = new ArrayList<>();
        SQLiteDatabase sql = dbHelper.getReadableDatabase();
        sql.beginTransaction();
        try {
            Cursor cursor = sql.rawQuery(query, agrs);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    int getMaPhieu = cursor.getInt(0);
                    int getMaTV = cursor.getInt(1);
                    int getMaSach = cursor.getInt(2);
                    String getMaTT = cursor.getString(3);
                    String getNgay = cursor.getString(4);
                    int getTrangThaiTraSach = cursor.getInt(5);
                    int getTienThue = cursor.getInt(6);

                    list.add(new PhieuMuon(getMaPhieu, getMaTT, getMaTV, getMaSach, getTienThue, getTrangThaiTraSach, getNgay));

                } while (cursor.moveToNext());
                sql.setTransactionSuccessful();
            }
        } catch (Exception e) {

        } finally {
            sql.endTransaction();
        }

        return list;
    }

    public ArrayList<Top10> getTop10() {
        ArrayList<Top10> list = new ArrayList<>();
        String sqlQuery = "SELECT MaSach, COUNT(MaSach) AS SoLuong FROM PhieuMuon GROUP BY MaSach ORDER BY SoLuong LIMIT 10";
        SQLiteDatabase sql = dbHelper.getWritableDatabase();
        Cursor cursor = sql.rawQuery(sqlQuery, null);
        SachDAO sachDAO = new SachDAO(context, dbHelper);
        while (cursor.moveToNext()) {
            @SuppressLint("Range") Sach sach = sachDAO.getSachWithID(Integer.parseInt(cursor.getString(cursor.getColumnIndex("MaSach"))));
            @SuppressLint("Range") int soLuong = Integer.parseInt(cursor.getString(cursor.getColumnIndex("SoLuong")));

            list.add(new Top10(sach.getTenSach(), soLuong));
        }
        return list;
    }

    @SuppressLint("Range")
    public int getDoanhThu(String from, String to) {
        SQLiteDatabase sql = dbHelper.getWritableDatabase();
        ArrayList<Integer> list = new ArrayList<>();
        Cursor cursor = sql.rawQuery("SELECT SUM(TienThue) AS DoanhThu FROM PhieuMuon WHERE Ngay BETWEEN ? AND ?", new String[]{from, to});
        while (cursor.moveToNext()) {
            try {
                int doanhThu = Integer.parseInt(cursor.getString(cursor.getColumnIndex("DoanhThu")));
                list.add(doanhThu);
            } catch (Exception e) {
                list.add(0);
            }

        }
        return list.get(0);
    }

    public void updatePhieuMuon(int maPM, int maThanhVien, int maSach, int tienThue, int trangThai){
        SQLiteDatabase sql = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MaTV", maThanhVien);
        values.put("MaSach", maSach);
        values.put("TienThue", tienThue);
        values.put("TraSach", trangThai);
        long result = sql.update("PhieuMuon", values, "MaPhieuMuon = ? ", new String[]{String.valueOf(maPM)});

        if (result > 0){
            Toast.makeText(context, "Update phiếu mượn thành công !", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Update phiếu mượn thất bại !", Toast.LENGTH_SHORT).show();

        }

    }

}
