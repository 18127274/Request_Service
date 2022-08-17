package Request.model;

import java.util.List;

public class List_request {
	private String id_lead;
	private int Status;


	public List_request() {
		super();
		// TODO Auto-generated constructor stub
	}


	public String getId_lead() {
		return id_lead;
	}


	public void setId_lead(String id_lead) {
		this.id_lead = id_lead;
	}


	public int getStatus() {
		return Status;
	}


	public void setStatus(int status) {
		Status = status;
	}


	public List_request(String id_lead, int status) {
		super();
		this.id_lead = id_lead;
		Status = status;
	}
	

}
