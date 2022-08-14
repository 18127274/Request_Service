package Request.model;

import java.time.LocalDate;
import java.util.List;

public class ListWFH {
	private String ID;
	private String MaNguoiDuyet;
	private String MaNhanVien;
	// @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
	private LocalDate NgayBatDau;
	private LocalDate NgayKetThuc;
	private String LyDo;
	private String LyDoTuChoi;
	private String TrangThai;

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

	public String getTrangThai() {
		return TrangThai;
	}

	public void setTrangThai(String trangThai) {
		TrangThai = trangThai;
	}

	public ListWFH(String iD, String maNguoiDuyet, String maNhanVien, LocalDate ngayBatDau, LocalDate ngayKetThuc,
			String lyDo, String lyDoTuChoi, String trangThai) {
		super();
		ID = iD;
		MaNguoiDuyet = maNguoiDuyet;
		MaNhanVien = maNhanVien;
		NgayBatDau = ngayBatDau;
		NgayKetThuc = ngayKetThuc;
		LyDo = lyDo;
		LyDoTuChoi = lyDoTuChoi;
		TrangThai = trangThai;
	}

	public ListWFH() {
		super();
		// TODO Auto-generated constructor stub
	}

}
