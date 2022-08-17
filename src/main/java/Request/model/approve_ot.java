package Request.model;

import java.util.List;

public class approve_ot {
	private String id_lead;
	private List<String> list_id_request;
	private String action;
	private String reason;
	public String getId_lead() {
		return id_lead;
	}
	public void setId_lead(String id_lead) {
		this.id_lead = id_lead;
	}
	public List<String> getList_id_request() {
		return list_id_request;
	}
	public void setList_id_request(List<String> list_id_request) {
		this.list_id_request = list_id_request;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public approve_ot(String id_lead, List<String> list_id_request, String action, String reason) {
		super();
		this.id_lead = id_lead;
		this.list_id_request = list_id_request;
		this.action = action;
		this.reason = reason;
	}
	public approve_ot() {
		super();
		// TODO Auto-generated constructor stub
	}

	

}
