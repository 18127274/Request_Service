package Request.model;

import java.util.List;

public class NghiPhepResponse {
	private List<NghiPhep> NP_History;
	private int sophepconlai;

	public List<NghiPhep> getNP_History() {
		return NP_History;
	}

	public void setNP_History(List<NghiPhep> nP_History) {
		NP_History = nP_History;
	}

	public int getSophepconlai() {
		return sophepconlai;
	}

	public void setSophepconlai(int sophepconlai) {
		this.sophepconlai = sophepconlai;
	}

	public NghiPhepResponse(List<NghiPhep> nP_History, int sophepconlai) {
		super();
		NP_History = nP_History;
		this.sophepconlai = sophepconlai;
	}

	public NghiPhepResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

}
