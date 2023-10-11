package fpoly.anhnvph32739.duanmau.model;

public class ThanhVien {
    private int maTV;
    private String hoTen;
    private String ngaySinh;

    public ThanhVien(int maTV, String hoTen, String ngaySinh) {
        this.maTV = maTV;
        this.hoTen = hoTen;
        this.ngaySinh = ngaySinh;
    }

    public ThanhVien(String hoTen, String ngaySinh) {
        this.hoTen = hoTen;
        this.ngaySinh = ngaySinh;
    }

    public int getMaTV() {
        return maTV;
    }

    public void setMaTV(int maTV) {
        this.maTV = maTV;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
        this.ngaySinh = ngaySinh;
    }
}
