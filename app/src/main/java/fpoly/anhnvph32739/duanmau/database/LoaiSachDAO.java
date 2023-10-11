package fpoly.anhnvph32739.duanmau.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import fpoly.anhnvph32739.duanmau.model.LoaiSach;

import java.util.ArrayList;

public class LoaiSachDAO {
    Context context;
    DbHelper dbHelper;

    public LoaiSachDAO(Context context, DbHelper dbHelper) {
        this.context = context;
        this.dbHelper = dbHelper;
    }

    public boolean insertLoaiSach(LoaiSach loaiSach) {
        SQLiteDatabase sql = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("TenLoai", loaiSach.getTenLoai());

        long result = sql.insert("LoaiSach", null, values);
        if (result > 0) {
            Toast.makeText(context, "Thêm loại sách thành công!", Toast.LENGTH_SHORT).show();
        return true;
        } else {
            Toast.makeText(context, "Thêm loại sách thất bại!", Toast.LENGTH_SHORT).show();
       return false;
        }
    }

    public boolean updateLoaiSach(String tenLoai, String clause) {
        SQLiteDatabase sql = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("TenLoai", tenLoai);

        long result = sql.update("LoaiSach", values, "MaLoaiSach = ?", new String[]{clause});
        if (result > 0) {
            Toast.makeText(context, "Update loại sách thành công!", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            Toast.makeText(context, "Update loại sách thất bại!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    public boolean deleteLoaiSach(int maLoaiSach){
        SQLiteDatabase sql = dbHelper.getWritableDatabase();
        long result = sql.delete("LoaiSach","MaLoaiSach = ?", new String[]{String.valueOf(maLoaiSach)});

        if (result > 0){
            Toast.makeText(context, "Xoá loại sách thành công !", Toast.LENGTH_SHORT).show();
            return true;
        }else {

            Toast.makeText(context, "Xoá loại sách thất bại !", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public LoaiSach getLoaiSachWithName(String maLoaiSach){
        String sql = "SELECT * FROM LoaiSach WHERE TenLoai = ?";
        ArrayList<LoaiSach> list = getData(sql, maLoaiSach);
        return list.get(0);
    }
    public LoaiSach getLoaiSachWithID(int maLoaiSach){
        String sql = "SELECT * FROM LoaiSach WHERE MaloaiSach = ?";
        ArrayList<LoaiSach> list = getData(sql, String.valueOf(maLoaiSach));
        return list.get(0);
    }

    public ArrayList<LoaiSach> getAllData(){
        String sql = "SELECT * FROM LoaiSach";
        return getData(sql);
    }

    public boolean checkLoaiSachDelete(int maLoaiSach ){

        String sqlQuery = "SELECT * FROM LoaiSach WHERE  MaLoaiSach  = ? ";
        SQLiteDatabase sql = dbHelper.getReadableDatabase();
        sql.beginTransaction();
        try {
            Cursor cursor = sql.rawQuery(sqlQuery, new String[]{String.valueOf(maLoaiSach)});
            if (cursor.getCount() > 0) {
           return true;
            }
        } catch (Exception e) {

        } finally {
            sql.endTransaction();
        }
        return false;
    }

    public ArrayList<LoaiSach> getData(String query, String...agrs){
        ArrayList<LoaiSach> list = new ArrayList<>();
        SQLiteDatabase sql = dbHelper.getReadableDatabase();
        sql.beginTransaction();
        try {
            Cursor cursor = sql.rawQuery(query, agrs);
            if (cursor.getCount() > 0 ){
                cursor.moveToFirst();
                do {
                    int getMaLoaiSach = cursor.getInt(0);
                    String getTenLoaiSach = cursor.getString(1);
                  list.add(new LoaiSach(getMaLoaiSach, getTenLoaiSach));

                }while (cursor.moveToNext());
                sql.setTransactionSuccessful();
            }else {
                Toast.makeText(context, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){

        }finally {
            sql.endTransaction();
        }

        return list;
    }

    public boolean checkLoaiSachExists(String tenLoaiSach){
        String sqlQuery = "SELECT * FROM LoaiSach WHERE  TenLoai  = ? ";
        SQLiteDatabase sql = dbHelper.getReadableDatabase();
        sql.beginTransaction();
        try {
            Cursor cursor = sql.rawQuery(sqlQuery, new String[]{tenLoaiSach});
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
