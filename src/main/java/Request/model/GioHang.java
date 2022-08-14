package Request.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
//import javax.persistence.Column;

@Document(collection = "GIOHANG")
public class GioHang {
	@Id
	private long ID;
	private long MaKH;
	private Float TongTien;

	public long getID() {
		return ID;
	}

	public void setID(long iD) {
		ID = iD;
	}

	public long getMaKH() {
		return MaKH;
	}

	public void setMaKH(long maKH) {
		MaKH = maKH;
	}

	public Float getTongTien() {
		return TongTien;
	}

	public void setTongTien(Float tongTien) {
		TongTien = tongTien;
	}

	public GioHang(long iD, long maKH, Float tongTien) {
		super();
		ID = iD;
		MaKH = maKH;
		TongTien = tongTien;
	}

	public GioHang() {
		super();
		// TODO Auto-generated constructor stub
	}

}
