package Request.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "YEUCAUTHIETBI")

public class YeuCauThietBi {
	@Id
	private String ID;
	private String MaNhanVien;
	private String MoTa;
	private float ChiPhi;
	private float SoLuong;
	private String MaNguoiDuyet;
	private String LyDoTuChoi;
	private String TrangThai;




	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	
	public String getMaNhanVien() {
		return MaNhanVien;
	}

	public void setMaNhanVien(String maNhanVien) {
		MaNhanVien = maNhanVien;
	}
	

	public String getMoTa() {
		return MoTa;
	}

	public void setMoTa(String moTa) {
		MoTa = moTa;
	}

	public float getSoLuong() {
		return SoLuong;
	}

	public void setSoLuong(float soLuong) {
		SoLuong = soLuong;
	}

	public float getChiPhi() {
		return ChiPhi;
	}

	public void setChiPhi(float chiPhi) {
		ChiPhi = chiPhi;
	}

	
	public String getMaNguoiDuyet() {
		return MaNguoiDuyet;
	}

	public void setMaNguoiDuyet(String maNguoiDuyet) {
		MaNguoiDuyet = maNguoiDuyet;
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
	
	



	public YeuCauThietBi(String iD, String maNhanVien, String moTa, float chiPhi, float soLuong, String maNguoiDuyet,
			String lyDoTuChoi, String trangThai) {
		super();
		ID = iD;
		MaNhanVien = maNhanVien;
		MoTa = moTa;
		ChiPhi = chiPhi;
		SoLuong = soLuong;
		MaNguoiDuyet = maNguoiDuyet;
		LyDoTuChoi = lyDoTuChoi;
		TrangThai = trangThai;
	}

	public YeuCauThietBi() {
		super();
		// TODO Auto-generated constructor stub
	}

}
