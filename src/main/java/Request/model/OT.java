package Request.model;

import java.time.LocalDate;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "OT")
public class OT {
	@Id
	private String ID;
	private String MaNguoiDuyet;
	private String MaNhanVien;
	private LocalDate NgayOT;
	private float SoGio;
	private String LyDoOT;
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

	public LocalDate getNgayOT() {
		return NgayOT;
	}

	public void setNgayOT(LocalDate ngayOT) {
		NgayOT = ngayOT;
	}

	public float getSoGio() {
		return SoGio;
	}

	public void setSoGio(float soGio) {
		SoGio = soGio;
	}

	public String getLyDoOT() {
		return LyDoOT;
	}

	public void setLyDoOT(String lyDoOT) {
		LyDoOT = lyDoOT;
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

	public OT(String iD, String maNhanVien, LocalDate ngayOT, float soGio, String lyDoOT) {
		super();
		ID = iD;
		MaNguoiDuyet = "";
		MaNhanVien = maNhanVien;
		NgayOT = ngayOT;
		SoGio = soGio;
		LyDoOT = lyDoOT;
		LyDoTuChoi = "";
		TrangThai = "0";
	}

	public OT() {
		super();
		// TODO Auto-generated constructor stub
	}

}
