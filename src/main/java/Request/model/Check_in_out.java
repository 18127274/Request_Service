package Request.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "CHECK_IN_OUT")
public class Check_in_out {
	@Id
	private String ID;
	private String MaNV;
	private LocalDateTime GioBatDau;
	private LocalDateTime GioKetThuc;

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getMaNV() {
		return MaNV;
	}

	public void setMaNV(String maNV) {
		MaNV = maNV;
	}

	public LocalDateTime getGioBatDau() {
		return GioBatDau;
	}

	public void setGioBatDau(LocalDateTime gioBatDau) {
		GioBatDau = gioBatDau;
	}

	public LocalDateTime getGioKetThuc() {
		return GioKetThuc;
	}

	public void setGioKetThuc(LocalDateTime gioKetThuc) {
		GioKetThuc = gioKetThuc;
	}

	public Check_in_out(String iD, String maNV) {
		super();
		ID = iD;
		MaNV = maNV;
		GioBatDau = LocalDateTime.now().plusHours(7);
		GioKetThuc = null;
	}

	public Check_in_out() {
		super();
		// TODO Auto-generated constructor stub
	}

}
