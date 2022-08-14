package Request.model;

import java.time.LocalDate;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "THEODOI")
public class TheoDoi {
	@Id
	private long ID;
	private long Ma;
	private LocalDate NgayTheoDoi;

	public long getID() {
		return ID;
	}

	public void setID(long iD) {
		ID = iD;
	}

	public long getMa() {
		return Ma;
	}

	public void setMa(long ma) {
		Ma = ma;
	}

	public LocalDate getNgayTheoDoi() {
		return NgayTheoDoi;
	}

	public void setNgayTheoDoi(LocalDate ngayTheoDoi) {
		NgayTheoDoi = ngayTheoDoi;
	}

	public TheoDoi() {
		super();
		// TODO Auto-generated constructor stub
	}

}
