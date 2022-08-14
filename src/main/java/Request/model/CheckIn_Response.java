package Request.model;

import java.util.List;

public class CheckIn_Response {
	private List<Check_in_out> MonthList;
	private Long MonthTime;
	private int Status;
	private Long YearTime;
	private int MonthIdeal;

	public List<Check_in_out> getMonthList() {
		return MonthList;
	}

	public void setMonthList(List<Check_in_out> monthList) {
		MonthList = monthList;
	}

	public Long getMonthTime() {
		return MonthTime;
	}

	public void setMonthTime(Long monthTime) {
		MonthTime = monthTime;
	}

	public int getStatus() {
		return Status;
	}

	public void setStatus(int status) {
		Status = status;
	}

	public Long getYearTime() {
		return YearTime;
	}

	public void setYearTime(Long yearTime) {
		YearTime = yearTime;
	}

	public int getMonthIdeal() {
		return MonthIdeal;
	}

	public void setMonthIdeal(int monthIdeal) {
		MonthIdeal = monthIdeal;
	}

	public CheckIn_Response(List<Check_in_out> monthList, Long monthTime, Long yearTime) {
		super();
		MonthList = monthList;
		MonthTime = monthTime;
		Status = 1;
		YearTime = yearTime;
		MonthIdeal = 200;
	}

	public CheckIn_Response() {
		super();
		// TODO Auto-generated constructor stub
	}

}
