package fpoly.anhnvph32739.duanmau.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;

import fpoly.anhnvph32739.duanmau.model.LoaiSach;
import fpoly.anhnvph32739.duanmau.model.ThuThu;

public class ThuThuDao {
    Context context;
    DbHelper dbHelper;

    public ThuThuDao(Context context, DbHelper dbHelper) {
        this.context = context;
        this.dbHelper = dbHelper;
    }

    public boolean insertUser(ThuThu thuThu, String messPositive, String messNegative) {
        SQLiteDatabase sql = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MaTT", thuThu.getMaThuThu());
        values.put("HoTen", thuThu.getHoTen());
        values.put("MatKhau", thuThu.getMatKhau());
        long kq = sql.insert("ThuThu", null, values);
        if (kq > 0) {
            Toast.makeText(context, messPositive, Toast.LENGTH_SHORT).show();
            return true;
        } else {
            Toast.makeText(context, messNegative,
                    Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public boolean updateThuThu(String passWordNew,String clause, String messPositive, String messNegative) {
        SQLiteDatabase sql = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MatKhau", passWordNew);
        long kq = sql.update("ThuThu", values, "MaTT = ?", new String[]{clause});
        if (kq > 0) {
            Toast.makeText(context, messPositive, Toast.LENGTH_SHORT).show();
            return true;
        } else {
            Toast.makeText(context, messNegative,
                    Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public boolean checkAccount(String tk, String mk) {

        SQLiteDatabase sql = dbHelper.getReadableDatabase();
        sql.beginTransaction();
        try {
            Cursor cursor = sql.rawQuery("SELECT * FROM ThuThu where MaTT = ? and MatKhau = ?", new String[]{tk, mk});
            if (cursor.getCount() > 0) {

                return true;
            }
        } catch (Exception e) {

        } finally {
            sql.endTransaction();
        }
        return false;
    }

    public ThuThu getThuThuWithID(String maTT) {
        String sql = "SELECT * FROM ThuThu WHERE MaTT = ?";
        ArrayList<ThuThu> list = getData(sql, maTT);
        return list.get(0);
    }

    public ArrayList<ThuThu> getAllData() {
        String sql = "SELECT * FROM ThuThu";
        return getData(sql);
    }

    public ArrayList<ThuThu> getData(String query, String... agrs) {
        ArrayList<ThuThu> list = new ArrayList<>();
        SQLiteDatabase sql = dbHelper.getReadableDatabase();
        sql.beginTransaction();
        try {
            Cursor cursor = sql.rawQuery(query, agrs);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    String getMaTT = cursor.getString(0);
                    String getHoTen = cursor.getString(1);
                    String getMatKhau = cursor.getString(2);
                    list.add(new ThuThu(getMaTT, getHoTen, getMatKhau));

                } while (cursor.moveToNext());
                sql.setTransactionSuccessful();
            }
        } catch (Exception e) {

        } finally {
            sql.endTransaction();
        }

        return list;
    }

    public boolean checkUserNameTT(String maTT){

        SQLiteDatabase sql = dbHelper.getReadableDatabase();
        sql.beginTransaction();
        try {
            Cursor cursor = sql.rawQuery("SELECT * FROM ThuThu where MaTT = ? ", new String[]{maTT});
            if (cursor.getCount() > 0) {

                return true;
            }
        } catch (Exception e) {

        } finally {
            sql.endTransaction();
        }
        return false;
    }
}
