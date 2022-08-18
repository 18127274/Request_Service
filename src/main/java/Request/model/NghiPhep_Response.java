package Request.model;

import java.time.LocalDate;
import java.time.Period;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "NGHIPHEP")
public class NghiPhep_Response {
	@Id
	private String ID;
	private String MaNguoiDuyet;
	private String MaNhanVien;
	private String TenNhanVien;
	private int LoaiNghiPhep;
	private LocalDate NgayBatDau;
	private LocalDate NgayKetThuc;
	private String LyDo;
	private String LyDoTuChoi;
	private int TrangThai;

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getMaNguoiDuyet() {
		return MaNguoiDuyet;
	}

	public void setMaNguoiDuyet(String maNguoiDuyet) {
		MaNguoiDuyet = maNguoiDuyet;
	}

	public String getMaNhanVien() {
		return MaNhanVien;
	}

	public void setMaNhanVien(String maNhanVien) {
		MaNhanVien = maNhanVien;
	}

	
	public String getTenNhanVien() {
		return TenNhanVien;
	}

	public void setTenNhanVien(String tenNhanVien) {
		TenNhanVien = tenNhanVien;
	}

	public int getLoaiNghiPhep() {
		return LoaiNghiPhep;
	}

	public void setLoaiNghiPhep(int loaiNghiPhep) {
		LoaiNghiPhep = loaiNghiPhep;
	}

	public LocalDate getNgayBatDau() {
		return NgayBatDau;
	}

	public void setNgayBatDau(LocalDate ngayBatDau) {
		NgayBatDau = ngayBatDau;
	}

	public LocalDate getNgayKetThuc() {
		return NgayKetThuc;
	}

	public void setNgayKetThuc(LocalDate ngayKetThuc) {
		NgayKetThuc = ngayKetThuc;
	}

	public String getLyDo() {
		return LyDo;
	}

	public void setLyDo(String lyDo) {
		LyDo = lyDo;
	}

	public String getLyDoTuChoi() {
		return LyDoTuChoi;
	}

	public void setLyDoTuChoi(String lyDoTuChoi) {
		LyDoTuChoi = lyDoTuChoi;
	}

	public int getTrangThai() {
		return TrangThai;
	}

	public void setTrangThai(int trangThai) {
		TrangThai = trangThai;
	}


	

	public NghiPhep_Response(String iD, String maNguoiDuyet, String maNhanVien, String tenNhanVien, int loaiNghiPhep,
			LocalDate ngayBatDau, LocalDate ngayKetThuc, String lyDo, String lyDoTuChoi, int trangThai) {
		super();
		ID = iD;
		MaNguoiDuyet = "";
		MaNhanVien = maNhanVien;
		TenNhanVien = tenNhanVien;
		LoaiNghiPhep = loaiNghiPhep;
		NgayBatDau = ngayBatDau;
		NgayKetThuc = ngayKetThuc;
		LyDo = lyDo;
		LyDoTuChoi = "";
		TrangThai = 0;
	}
	
	public NghiPhep_Response(NghiPhep request, User NhanVien) {
		super();
		ID = request.getID();
		MaNguoiDuyet = request.getMaNguoiDuyet();
		MaNhanVien = request.getMaNhanVien();
		TenNhanVien = NhanVien.getHoTen();
		LoaiNghiPhep = request.getLoaiNghiPhep();
		NgayBatDau = request.getNgayBatDau();
		NgayKetThuc = request.getNgayKetThuc();
		LyDo = request.getLyDo();
		LyDoTuChoi = request.getLyDoTuChoi();
		TrangThai = request.getTrangThai();
		
	}
	
	
	public NghiPhep_Response() {
		super();
		// TODO Auto-generated constructor stub
	}

}
