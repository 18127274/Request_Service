package Request.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "NHANVIEN")
public class NhanVien {
	@Id
	private String ID;
	private String HoTen;
	private String GioiTinh;
	private String ChucVu;
	private String DiaChi;
	private String Email;
	private String SDT;
	private int SoPhepConLai;
	private int TrangThaiLamViec;
	private String Username;
	private String Password;

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getHoTen() {
		return HoTen;
	}

	public void setHoTen(String hoTen) {
		HoTen = hoTen;
	}

	public String getGioiTinh() {
		return GioiTinh;
	}

	public void setGioiTinh(String gioiTinh) {
		GioiTinh = gioiTinh;
	}

	public String getChucVu() {
		return ChucVu;
	}

	public void setChucVu(String chucVu) {
		ChucVu = chucVu;
	}

	public String getDiaChi() {
		return DiaChi;
	}

	public void setDiaChi(String diaChi) {
		DiaChi = diaChi;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getSDT() {
		return SDT;
	}

	public void setSDT(String sDT) {
		SDT = sDT;
	}

	public int getSoPhepConLai() {
		return SoPhepConLai;
	}

	public void setSoPhepConLai(int soPhepConLai) {
		SoPhepConLai = soPhepConLai;
	}

	public int getTrangThaiLamViec() {
		return TrangThaiLamViec;
	}

	public void setTrangThaiLamViec(int trangThaiLamViec) {
		TrangThaiLamViec = trangThaiLamViec;
	}

	public String getUsername() {
		return Username;
	}

	public void setUsername(String username) {
		Username = username;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		Password = password;
	}

	public NhanVien(String iD, String hoTen, String gioiTinh, String chucVu, String diaChi, String email, String sDT,
			int soPhepConLai, int trangThaiLamViec, String username, String password) {
		super();
		ID = iD;
		HoTen = hoTen;
		GioiTinh = gioiTinh;
		ChucVu = chucVu;
		DiaChi = diaChi;
		Email = email;
		SDT = sDT;
		SoPhepConLai = soPhepConLai;
		TrangThaiLamViec = trangThaiLamViec;
		Username = username;
		Password = password;
	}

	public NhanVien() {
		super();
		// TODO Auto-generated constructor stub
	}

}
