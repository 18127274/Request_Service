package Request.model;

import java.time.LocalDate;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "REQUEST_FIXCHECKOUT")
public class Request_FixCheckOut {
	@Id
	private long ID;
	private long MaRQ;
	private LocalDate GioChinhSua;
	private LocalDate NgayChinhSua;

	public long getID() {
		return ID;
	}

	public void setID(long iD) {
		ID = iD;
	}

	public long getMaRQ() {
		return MaRQ;
	}

	public void setMaRQ(long maRQ) {
		MaRQ = maRQ;
	}

	public LocalDate getGioChinhSua() {
		return GioChinhSua;
	}

	public void setGioChinhSua(LocalDate gioChinhSua) {
		GioChinhSua = gioChinhSua;
	}

	public LocalDate getNgayChinhSua() {
		return NgayChinhSua;
	}

	public void setNgayChinhSua(LocalDate ngayChinhSua) {
		NgayChinhSua = ngayChinhSua;
	}

	public Request_FixCheckOut() {
		super();
		// TODO Auto-generated constructor stub
	}

}
