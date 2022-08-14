package Request.model;

import java.time.LocalDate;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "TINTUC")
public class TinTuc {
	@Id
	private long ID;
	private long MaTT;
	private String NoiDung;
	private LocalDate NgayDang;

	public long getID() {
		return ID;
	}

	public void setID(long iD) {
		ID = iD;
	}

	public long getMaTT() {
		return MaTT;
	}

	public void setMaTT(long maTT) {
		MaTT = maTT;
	}

	public String getNoiDung() {
		return NoiDung;
	}

	public void setNoiDung(String noiDung) {
		NoiDung = noiDung;
	}

	public LocalDate getNgayDang() {
		return NgayDang;
	}

	public void setNgayDang(LocalDate ngayDang) {
		NgayDang = ngayDang;
	}

	public TinTuc() {
		super();
		// TODO Auto-generated constructor stub
	}

}
