package Request.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "CHUDE")
public class ChuDe {
	@Id
	private long ID;
	private long MaCD;
	private String TenChuDe;

	public long getID() {
		return ID;
	}

	public void setID(long iD) {
		ID = iD;
	}

	public long getMaCD() {
		return MaCD;
	}

	public void setMaCD(long maCD) {
		MaCD = maCD;
	}

	public String getTenChuDe() {
		return TenChuDe;
	}

	public void setTenChuDe(String tenChuDe) {
		TenChuDe = tenChuDe;
	}

	public ChuDe() {
		super();
		// TODO Auto-generated constructor stub
	}

}
