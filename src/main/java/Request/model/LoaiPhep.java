package Request.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "LOAIPHEP")
public class LoaiPhep {
	@Id
	private String ID;
	private String TenLoai;

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getTenLoai() {
		return TenLoai;
	}

	public void setTenLoai(String tenLoai) {
		TenLoai = tenLoai;
	}

	public LoaiPhep(String iD, String tenLoai) {
		super();
		ID = iD;
		TenLoai = tenLoai;
	}

	public LoaiPhep() {
		super();
		// TODO Auto-generated constructor stub
	}

}
