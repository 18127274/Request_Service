package Request.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import Request.model.ApiResponse;
import Request.model.Check_in_out;
import Request.model.DuAn;
import Request.model.User;
import Request.model.ListWFH;
import Request.model.List_Staff;
import Request.model.List_ThamGiaDuAn;
import Request.model.NghiPhep;
import Request.model.NghiPhepResponse;
import Request.model.OT;
import Request.model.OT_Response;
import Request.model.ThamGiaDuAn;
import Request.model.NghiPhep_Response;
import Request.model.WFH;
import Request.model.WFH_Reponse;
import Request.model.approve_ot;
import Request.repository.DuAnRepository;
import Request.repository.NghiPhepRepository;
import Request.repository.UserRepository;
import Request.repository.OTRepository;
import Request.repository.ThamGiaDuAnRepository;
import Request.repository.WFHRepository;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.time.Period;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/api")

public class NghiPhepController {
	@Autowired
	NghiPhepRepository repoNP;
	@Autowired
	UserRepository repoUser;
	@Autowired
	MongoTemplate mongoTemplate;
	@Autowired
	MongoOperations mongoOperation;

	// lay ra danh sach tat ca yeu cau nghi phep
	@GetMapping("/get_all_list_request_nghiphep")
	public ResponseEntity<ApiResponse<List<NghiPhep>>> View_all_list_request_nghiphep() {
		try {
			List<NghiPhep> wfhlst = new ArrayList<NghiPhep>();

			repoNP.findAll().forEach(wfhlst::add);

			if (wfhlst.isEmpty()) {
				ApiResponse<List<NghiPhep>> resp = new ApiResponse<List<NghiPhep>>(1, "Request is empty!", null);
				return new ResponseEntity<>(resp, HttpStatus.OK);

			}

			ApiResponse<List<NghiPhep>> resp = new ApiResponse<List<NghiPhep>>(0, "Success", wfhlst);
			return new ResponseEntity<>(resp, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// lay ra danh sach tat ca yeu cau nghi phep cua nhan vien
	@GetMapping("/get_all_list_request_nghiphep_of_staff/{MaNV_input}")
	public ResponseEntity<ApiResponse<List<NghiPhep>>> View_all_list_request_nghiphep_of_staff(
			@PathVariable(value = "MaNV_input") String MaNV_input) {
		try {
			System.out.println(MaNV_input);

			List<NghiPhep> wfhlst = new ArrayList<NghiPhep>();
			Query q = new Query();
			q.addCriteria(Criteria.where("MaNhanVien").is(MaNV_input));

			wfhlst = mongoTemplate.find(q, NghiPhep.class);
			if (wfhlst.isEmpty()) {
				ApiResponse<List<NghiPhep>> resp = new ApiResponse<List<NghiPhep>>(1, "Request is empty!", null);
				return new ResponseEntity<>(resp, HttpStatus.OK);
				// return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			ApiResponse<List<NghiPhep>> resp = new ApiResponse<List<NghiPhep>>(0, "Success", wfhlst);
			return new ResponseEntity<>(resp, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// lay ra nhung don nghi phep duyet duoc theo role (manager1, manager2)
	@GetMapping("/get_all_list_nghiphep_by_role")
	public ResponseEntity<ApiResponse<List<NghiPhep_Response>>> Get_all_list_nghiphep_by_role(
			@RequestParam("id_reviewer") String id_reviewer, @RequestParam("status") int status_input,
			@RequestParam("role") int role) {
		try {
			System.out.println(role);
			System.out.println(status_input);
			System.out.println(id_reviewer);
			if (role == 4) {
				String uri = "https://gatewayteam07.herokuapp.com/api/list_staff_manager1/" + id_reviewer;
				RestTemplate restTemplate = new RestTemplate();
				List_Staff call = restTemplate.getForObject(uri, List_Staff.class);
				List<String> staff = call.getListstaff();

				if (status_input < 0 || status_input > 2) {
					ApiResponse<List<NghiPhep_Response>> resp = new ApiResponse<>(1, "invalid status", null);
					return new ResponseEntity<>(resp, HttpStatus.OK);
				}

				List<NghiPhep> otlst = new ArrayList<NghiPhep>();
				Query q = new Query();
				q.addCriteria(Criteria.where("TrangThai").is(status_input));
				otlst = mongoTemplate.find(q, NghiPhep.class);
				if (otlst.isEmpty()) {
					ApiResponse<List<NghiPhep_Response>> resp = new ApiResponse<>(0, "Empty data", null);
					return new ResponseEntity<>(resp, HttpStatus.OK);
				}
				List<NghiPhep> result = new ArrayList<NghiPhep>();
				for (NghiPhep i : otlst) {
					for (String y : staff) {
						if (i.getMaNhanVien().equals(y)
								&& Caculatebetweentwoday(i.getNgayBatDau(), i.getNgayKetThuc()) <= 3) {
							result.add(i);
						}
					}
				}
				if (result.isEmpty()) {
					ApiResponse<List<NghiPhep_Response>> resp = new ApiResponse<>(0, "Empty data", null);
					return new ResponseEntity<>(resp, HttpStatus.OK);
				}

				List<NghiPhep_Response> resp = new ArrayList<NghiPhep_Response>();
				for (NghiPhep i : result) {
					String uri1 = "https://gatewayteam07.herokuapp.com/api/staff_nghiphep/" + i.getMaNhanVien();
					RestTemplate restTemplate1 = new RestTemplate();
					User staff1 = restTemplate1.getForObject(uri1, User.class);
					NghiPhep_Response temp = new NghiPhep_Response(i, staff1);
					resp.add(temp);
				}

				ApiResponse<List<NghiPhep_Response>> resp1 = new ApiResponse<>(0, "Success", resp);
				return new ResponseEntity<>(resp1, HttpStatus.OK);
			} else if (role == 5) {
				// gọi api lấy ra director của 1 thằng team leader

				String uri1 = "https://duanteam07.herokuapp.com/api/get_teamleader_manage_project_has_status_0/"
						+ id_reviewer;
				RestTemplate restTemplate1 = new RestTemplate();
				List_ThamGiaDuAn call1 = restTemplate1.getForObject(uri1, List_ThamGiaDuAn.class);
				List<ThamGiaDuAn> infor_tl = call1.getListstaff();

				ThamGiaDuAn[] array = infor_tl.toArray(new ThamGiaDuAn[0]);
				System.out.println("ma tl: " + array[0].getMaTL());

				String uri = "https://gatewayteam07.herokuapp.com/api/list_staff_manager1/" + array[0].getMaTL();
				System.out.println(uri);
				RestTemplate restTemplate = new RestTemplate();
				System.out.println("cac1");
				List_Staff call = restTemplate.getForObject(uri, List_Staff.class);
				List<String> staff = call.getListstaff();

				System.out.println("cac3");
				if (status_input < 0 || status_input > 2) {
					ApiResponse<List<NghiPhep_Response>> resp = new ApiResponse<>(1, "invalid status", null);
					return new ResponseEntity<>(resp, HttpStatus.OK);
				}
				System.out.println("cac4");
				List<NghiPhep> otlst = new ArrayList<NghiPhep>();
				Query q = new Query();
				q.addCriteria(Criteria.where("TrangThai").is(status_input));
				otlst = mongoTemplate.find(q, NghiPhep.class);
				System.out.println("cac5");

				if (otlst.isEmpty()) {
					ApiResponse<List<NghiPhep_Response>> resp = new ApiResponse<>(0, "Empty data", null);
					return new ResponseEntity<>(resp, HttpStatus.OK);
				}
				System.out.println("cac6");
				List<NghiPhep> result = new ArrayList<NghiPhep>();
				for (NghiPhep i : otlst) {
					for (String y : staff) {
						System.out.println("ngay bat dau: " + i.getNgayBatDau());
						System.out.println("ngay ket thuc: " + i.getNgayKetThuc());
						System.out.println(Caculatebetweentwoday(i.getNgayBatDau(), i.getNgayKetThuc()));
						if (i.getMaNhanVien().equals(y)
								&& Caculatebetweentwoday(i.getNgayBatDau(), i.getNgayKetThuc()) > 3) {
							System.out.println("co data");
							result.add(i);
						}
					}
				}
				if (result.isEmpty()) {
					ApiResponse<List<NghiPhep_Response>> resp = new ApiResponse<>(0, "Empty data", null);
					return new ResponseEntity<>(resp, HttpStatus.OK);
				}

				List<NghiPhep_Response> resp = new ArrayList<NghiPhep_Response>();
				for (NghiPhep i : result) {
					String uri2 = "https://gatewayteam07.herokuapp.com/api/staff_nghiphep/" + i.getMaNhanVien();
					RestTemplate restTemplate2 = new RestTemplate();
					User staff1 = restTemplate2.getForObject(uri2, User.class);
					NghiPhep_Response temp = new NghiPhep_Response(i, staff1);
					resp.add(temp);
				}

				ApiResponse<List<NghiPhep_Response>> resp1 = new ApiResponse<>(0, "Success", resp);
				return new ResponseEntity<>(resp1, HttpStatus.OK);
			}

			ApiResponse<List<NghiPhep_Response>> resp = new ApiResponse<>(0, "Empty data", null);
			return new ResponseEntity<>(resp, HttpStatus.OK);
		} catch (Exception e) {
			ApiResponse<List<NghiPhep_Response>> resp = new ApiResponse<>(1,
					"ID reviewer not exist or role input wrong!", null);
			return new ResponseEntity<>(resp, HttpStatus.OK);
		}
	}

	// lay ra danh sach nhung don yeu cau ma manager1 dc duyet
	@GetMapping("/get_all_list_nghiphep_of_manager1")
	public ResponseEntity<ApiResponse<List<NghiPhep>>> Get_all_list_nghiphep_of_manager1(
			@RequestParam("id_lead") String id_lead_input, @RequestParam("status") int status_input) {
		try {
			String uri = "https://gatewayteam07.herokuapp.com/api/list_staff_manager1/" + id_lead_input;
			RestTemplate restTemplate = new RestTemplate();
			List_ThamGiaDuAn call = restTemplate.getForObject(uri, List_ThamGiaDuAn.class);
			List<ThamGiaDuAn> staff = call.getListstaff();

			if (status_input < 0 || status_input > 2) {
				ApiResponse<List<NghiPhep>> resp = new ApiResponse<List<NghiPhep>>(0, "invalid status", null);
				return new ResponseEntity<>(resp, HttpStatus.OK);
			}

			List<NghiPhep> otlst = new ArrayList<NghiPhep>();
			Query q = new Query();
			q.addCriteria(Criteria.where("TrangThai").is(status_input));
			otlst = mongoTemplate.find(q, NghiPhep.class);
			if (otlst.isEmpty()) {
				ApiResponse<List<NghiPhep>> resp = new ApiResponse<List<NghiPhep>>(0, "Empty data", otlst);
				return new ResponseEntity<>(resp, HttpStatus.OK);
			}
			List<NghiPhep> result = new ArrayList<NghiPhep>();
			for (NghiPhep i : otlst) {
				for (ThamGiaDuAn y : staff) {
					if (i.getMaNhanVien().equals(y.getMaNV())
							&& Caculatebetweentwoday(i.getNgayBatDau(), i.getNgayKetThuc()) <= 3) {
						result.add(i);
					}
				}
			}
			if (result.isEmpty()) {
				ApiResponse<List<NghiPhep>> resp = new ApiResponse<List<NghiPhep>>(0, "Empty data", null);
				return new ResponseEntity<>(resp, HttpStatus.OK);
			}
			ApiResponse<List<NghiPhep>> resp = new ApiResponse<List<NghiPhep>>(0, "Success", result);
			return new ResponseEntity<>(resp, HttpStatus.OK);
		} catch (Exception e) {
			ApiResponse<List<NghiPhep>> resp = new ApiResponse<List<NghiPhep>>(1, "ID lead not exist", null);
			return new ResponseEntity<>(resp, HttpStatus.OK);
		}
	}

	// lay ra danh sach nhung don yeu cau ma manager2 dc duyet
	@GetMapping("/get_all_list_nghiphep_of_manager2")
	public ResponseEntity<ApiResponse<List<NghiPhep>>> Get_all_list_nghiphep_of_manager2(
			@RequestParam("id_director") String id_director, @RequestParam("status") int status_input) {
		try {

			// gọi api lấy ra director của 1 thằng team leader

			String uri1 = "https://duanteam07.herokuapp.com/api/get_teamleader_manage_project_has_status_0/"
					+ id_director;

			RestTemplate restTemplate1 = new RestTemplate();

			List_ThamGiaDuAn call1 = restTemplate1.getForObject(uri1, List_ThamGiaDuAn.class);

			List<ThamGiaDuAn> infor_tl = call1.getListstaff();

			ThamGiaDuAn[] array = infor_tl.toArray(new ThamGiaDuAn[0]);

			System.out.println(array[0].getMaTL());

			String uri = "https://gatewayteam07.herokuapp.com/api/list_staff_manager1/" + array[0].getMaTL();
			System.out.println(uri);
			RestTemplate restTemplate = new RestTemplate();
			System.out.println("cac1");
			List_ThamGiaDuAn call = restTemplate.getForObject(uri, List_ThamGiaDuAn.class);
			System.out.println("cac2");
			List<ThamGiaDuAn> staff = call.getListstaff();
			System.out.println("cac3");
			if (status_input < 0 || status_input > 2) {
				ApiResponse<List<NghiPhep>> resp = new ApiResponse<List<NghiPhep>>(0, "invalid status", null);
				return new ResponseEntity<>(resp, HttpStatus.OK);
			}
			System.out.println("cac4");
			List<NghiPhep> otlst = new ArrayList<NghiPhep>();
			Query q = new Query();
			q.addCriteria(Criteria.where("TrangThai").is(status_input));
			otlst = mongoTemplate.find(q, NghiPhep.class);
			System.out.println("cac5");

			if (otlst.isEmpty()) {
				ApiResponse<List<NghiPhep>> resp = new ApiResponse<List<NghiPhep>>(0, "Empty data", otlst);
				return new ResponseEntity<>(resp, HttpStatus.OK);
			}
			System.out.println("cac6");
			List<NghiPhep> result = new ArrayList<NghiPhep>();
			for (NghiPhep i : otlst) {
				for (ThamGiaDuAn y : staff) {
					System.out.println("ngay bat dau: " + i.getNgayBatDau());
					System.out.println("ngay ket thuc: " + i.getNgayKetThuc());
					System.out.println(Caculatebetweentwoday(i.getNgayBatDau(), i.getNgayKetThuc()));
					if (i.getMaNhanVien().equals(y.getMaNV())
							&& Caculatebetweentwoday(i.getNgayBatDau(), i.getNgayKetThuc()) > 3) {
						result.add(i);
					}
				}
			}
			if (result.isEmpty()) {
				ApiResponse<List<NghiPhep>> resp = new ApiResponse<List<NghiPhep>>(0, "Empty data", null);
				return new ResponseEntity<>(resp, HttpStatus.OK);
			}

			ApiResponse<List<NghiPhep>> resp = new ApiResponse<List<NghiPhep>>(0, "Success", result);
			return new ResponseEntity<>(resp, HttpStatus.OK);
		} catch (Exception e) {
			ApiResponse<List<NghiPhep>> resp = new ApiResponse<List<NghiPhep>>(1, "ID lead not exist", null);
			return new ResponseEntity<>(resp, HttpStatus.OK);
		}
	}

	// lay ra danh sach tat ca yeu cau nghi phep thông qua trạng thái
	@GetMapping("/get_all_list_request_nghiphep_by_status/{status}")
	public ResponseEntity<ApiResponse<List<NghiPhep>>> View_all_list_request_nghiphep_by_status(
			@PathVariable(value = "status") int status) {
		try {

			List<NghiPhep> wfhlst = new ArrayList<NghiPhep>();
			Query q = new Query();
			q.addCriteria(Criteria.where("TrangThai").is(status));

			wfhlst = mongoTemplate.find(q, NghiPhep.class);
			if (wfhlst.isEmpty()) {
				ApiResponse<List<NghiPhep>> resp = new ApiResponse<List<NghiPhep>>(1, "Status fomart wrong!", null);
				return new ResponseEntity<>(resp, HttpStatus.OK);
				// return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			ApiResponse<List<NghiPhep>> resp = new ApiResponse<List<NghiPhep>>(0, "Success", wfhlst);
			return new ResponseEntity<>(resp, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// lấy ra đơn yêu cầu nghi phép thông qua mã đơn.
	@GetMapping("/get_np_id/{MaNP_input}")
	public ResponseEntity<ApiResponse<List<NghiPhep>>> Get_np_id(
			@PathVariable(value = "MaNP_input") String MaNP_input) {

		try {
			List<NghiPhep> wfhlst = new ArrayList<NghiPhep>();
			Query q = new Query();
			q.addCriteria(Criteria.where("ID").is(MaNP_input));
			wfhlst = mongoTemplate.find(q, NghiPhep.class);
			if (wfhlst.isEmpty()) {
				ApiResponse<List<NghiPhep>> resp = new ApiResponse<List<NghiPhep>>(1, "Id of this leave does not exist",
						null);
				return new ResponseEntity<>(resp, HttpStatus.OK);
				// return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			ApiResponse<List<NghiPhep>> resp = new ApiResponse<List<NghiPhep>>(0, "Success", wfhlst);
			return new ResponseEntity<>(resp, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// lấy ra đơn yêu cầu nghi phép có trạng thái là Pending thông qua mã nhân viên.
	@GetMapping("/get_np_by_staff_id/{MaNV_input}")
	public ResponseEntity<ApiResponse<List<NghiPhep>>> Get_np_by_staff_id(
			@PathVariable(value = "MaNV_input") String MaNV_input) {
		try {
			System.out.println(MaNV_input);

			List<NghiPhep> wfhlst = new ArrayList<NghiPhep>();
			Query q = new Query();
			q.addCriteria(Criteria.where("MaNhanVien").is(MaNV_input));

			wfhlst = mongoTemplate.find(q, NghiPhep.class);
			if (wfhlst.isEmpty()) {
				ApiResponse<List<NghiPhep>> resp = new ApiResponse<List<NghiPhep>>(1,
						"Staff id wrong or staff haven't leave application", null);
				return new ResponseEntity<>(resp, HttpStatus.OK);
				// return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			ApiResponse<List<NghiPhep>> resp = new ApiResponse<List<NghiPhep>>(0, "Success", wfhlst);
			return new ResponseEntity<>(resp, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// lấy ra đơn yêu cầu nghỉ phép theo trangthai (trangthai = 0: chờ xét duyệt, 1:
	// đã xét duyệt, 2: từ chối).
	@GetMapping("/get_np_by_status/{status}")
	public ResponseEntity<ApiResponse<List<NghiPhep>>> Get_np_by_status(@PathVariable(value = "status") int status) {

		try {
			List<NghiPhep> wfhlst = new ArrayList<NghiPhep>();
			Query q = new Query();
			q.addCriteria(Criteria.where("TrangThai").is(status));
			wfhlst = mongoTemplate.find(q, NghiPhep.class);
			if (wfhlst.isEmpty()) {
				ApiResponse<List<NghiPhep>> resp = new ApiResponse<List<NghiPhep>>(1, "Wrong status format!", null);
				return new ResponseEntity<>(resp, HttpStatus.OK);
				// return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			ApiResponse<List<NghiPhep>> resp = new ApiResponse<List<NghiPhep>>(0, "Success", wfhlst);
			return new ResponseEntity<>(resp, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// hàm hỗ trợ tính khoảng cách giữa hai ngày
	public long Caculatebetweentwoday(LocalDate startday, LocalDate enddate) {
		Period period = Period.between(startday, enddate);
		long daysDiff = Math.abs(period.getDays());
		return daysDiff;
	}

	// nhân viên yêu cầu nghỉ phép
	@PostMapping("/request_nghiphep")
	public ResponseEntity<ApiResponse<NghiPhep>> Request_nghiphep(@RequestBody NghiPhep np) {
		try {
			LocalTime localTime = LocalTime.now();

			System.out.println(localTime);
			List<NghiPhep> wfhlst = new ArrayList<NghiPhep>();
			Query q = new Query();
			// q.addCriteria(Criteria.where("MaNhanVien").is(wfh.getMaNhanVien()));
			q.addCriteria(Criteria.where("MaNhanVien").is(np.getMaNhanVien()))
					.addCriteria(Criteria.where("TrangThai").is(0));
			wfhlst = mongoTemplate.find(q, NghiPhep.class);
			System.out.println("rong hay k tren: " + wfhlst.isEmpty());

			// goi service user de lay infor nhanvien
			final String uri = "https://userteam07.herokuapp.com/api/staff_nghiphep/" + np.getMaNhanVien();
			RestTemplate restTemplate = new RestTemplate();
			User result = restTemplate.getForObject(uri, User.class);

			System.out.println("uri: " + uri);
			System.out.println("result: " + result);
			System.out.println("rong hay k: " + wfhlst.isEmpty());
			System.out.println("so phep con lai: " + result.getSoPhepConLai());

			System.out.println(result.getID());
			System.out.println(wfhlst.isEmpty());
			if (wfhlst.isEmpty() == true && result.getSoPhepConLai() >= 1) {// can them dieu kien la so php con lai phai
																			// >=1
				System.out.println("khong co thang nhan vien nay");
				np.setID(UUID.randomUUID().toString());
				NghiPhep _np = repoNP.save(new NghiPhep(np.getID(), "", np.getMaNhanVien(), np.getLoaiNghiPhep(),
						np.getNgayBatDau(), np.getNgayKetThuc(), np.getLyDo(), "", 0));
				ApiResponse<NghiPhep> resp = new ApiResponse<NghiPhep>(0, "Success", _np);
				return new ResponseEntity<>(resp, HttpStatus.CREATED);
			}
			ApiResponse<NghiPhep> resp = new ApiResponse<NghiPhep>(1, "Can't request because you have petition", null);
			return new ResponseEntity<>(resp, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// hàm xét duyệt đơn nghỉ phép ( manager1, manager 2)
	@PutMapping("/approve_request_nghiphep")
	public ResponseEntity<ApiResponse<List<NghiPhep_Response>>> Approve_request_nghiphep(
			@RequestBody approve_ot approve) {
		try {
			List<NghiPhep> result = new ArrayList<NghiPhep>();
			if (!approve.getAction().toUpperCase().equals("ACCEPT")
					&& !(approve.getAction().toUpperCase().equals("REJECT"))) {
				ApiResponse<List<NghiPhep_Response>> resp = new ApiResponse<>(1, "Action only accept or reject", null);
				return new ResponseEntity<>(resp, HttpStatus.CREATED);
			}
			if (approve.getAction().toUpperCase().equals("REJECT")
					&& (approve.getReason() == null || approve.getReason().equals(""))) {
				ApiResponse<List<NghiPhep_Response>> resp = new ApiResponse<>(1, "Invalid reason", null);
				return new ResponseEntity<>(resp, HttpStatus.CREATED);
			}
			System.out.println("list id: " + approve.getList_id_request());
			System.out.println("list id: " + approve.getAction());

			int check = 0;
			// set điều kiện để manager cấp 1 hay là manager cấp 2 duyệt đơn
			// if (Caculatebetweentwoday(ot.getNgayBatDau(), ot.getNgayKetThuc()) <= 3) {

			if (approve.getAction().toUpperCase().equals("ACCEPT")) { // check xem loai action la reject hay accept
				// duyệt list id_đơn sau đó lấy các mã đơn này duyệt trong bảng nghỉ phép lấy ra
				// trạng thái của các đơn nếu đơn có trạng thái = 0 mới cho duyệt

				for (String request_id : approve.getList_id_request()) {
					// Find request
					Query q = new Query();
					q.addCriteria(Criteria.where("ID").is(request_id));
					NghiPhep ot = mongoTemplate.findOne(q, NghiPhep.class);
					// Validate manager authority
					System.out.println("deo vao dc ");
					if (ot.getTrangThai() == 0) { // kiểm tra trạng thái đơn hàng

						System.out.println("ngay bat dau: " + ot.getNgayBatDau());
						System.out.println("ngay bat dau: " + ot.getNgayKetThuc());

						if (Caculatebetweentwoday(ot.getNgayBatDau(), ot.getNgayKetThuc()) <= 3) {
							System.out.println("so ngay < 3");
							// lấy ra manager 1 của staff
							String uri = "https://gatewayteam07.herokuapp.com/api/get_manager1_of_staff/"
									+ ot.getMaNhanVien();
							RestTemplate restTemplate = new RestTemplate();
							ThamGiaDuAn manager = restTemplate.getForObject(uri, ThamGiaDuAn.class);

							if (manager.getID() != null && manager.getMaTL().equals(approve.getId_lead())) {
								ot.setLyDoTuChoi(approve.getReason());

								// goi service user de lay infor nhanvien
								final String uri1 = "https://userteam07.herokuapp.com/api/staff_nghiphep/"
										+ manager.getMaNV();
								RestTemplate restTemplate1 = new RestTemplate();
								User result_staff = restTemplate1.getForObject(uri1, User.class);

								ot.setTrangThai(1);
								ot.setLyDoTuChoi("");
								ot.setMaNguoiDuyet(approve.getId_lead());

								result_staff.setSoPhepConLai(result_staff.getSoPhepConLai() - 1); // cập nhật lại số
																									// ngày
																									// nghỉ
								// phép còn lại
								ApiResponse<NghiPhep> resp = new ApiResponse<NghiPhep>(0, "Success", repoNP.save(ot));
								System.out.println("mã nhân viên cần update: " + manager.getMaNV());
								// goi lai service user de cap nhat lai so phep con lai
								final String uri3 = "https://userteam07.herokuapp.com/api/update_sophepconlai/"
										+ manager.getMaNV();
								System.out.println("api update: " + uri3);
								RestTemplate restTemplate3 = new RestTemplate();
								User _nv = new User(result_staff.getID(), result_staff.getHoTen(),
										result_staff.getUserName(), result_staff.getPassWord(),
										result_staff.getGioiTinh(), result_staff.getChucVu(), result_staff.getDiaChi(),
										result_staff.getEmail(), result_staff.getSDT(), result_staff.getAvatar());
								User result3 = restTemplate3.postForObject(uri3, _nv, User.class);
								// return new ResponseEntity<>(resp, HttpStatus.CREATED);

								repoNP.save(ot);
								result.add(ot);
							} else {
								ApiResponse<List<NghiPhep_Response>> resp = new ApiResponse<>(1,
										"Id reviewer or order id not valid", null);
								return new ResponseEntity<>(resp, HttpStatus.OK);
							}

						} else if (ot.getTrangThai() != 0) {
							ApiResponse<List<NghiPhep_Response>> resp = new ApiResponse<>(1,
									"Invalid reviwer id or of list_id request not valid", null);
							return new ResponseEntity<>(resp, HttpStatus.OK);
						}

						else if (Caculatebetweentwoday(ot.getNgayBatDau(), ot.getNgayKetThuc()) > 3
								&& ot.getTrangThai() == 0) {

							System.out.println("so ngay > 3");
							// lấy ra manager 2 của staff
							String uri2 = "https://gatewayteam07.herokuapp.com/api/get_manager2_of_staff/"
									+ ot.getMaNhanVien();
							RestTemplate restTemplate2 = new RestTemplate();
							DuAn manager2 = restTemplate2.getForObject(uri2, DuAn.class);

							// lấy ra manager 1 của staff
							String uri = "https://gatewayteam07.herokuapp.com/api/get_manager1_of_staff/"
									+ ot.getMaNhanVien();
							RestTemplate restTemplate = new RestTemplate();
							ThamGiaDuAn manager = restTemplate.getForObject(uri, ThamGiaDuAn.class);

							System.out.println("mã pm update ne: " + manager2.getMaPM());
							System.out.println("ma pm input: " + approve.getId_lead());
							System.out.println(
									"so sanh ma pm voi in mapm: " + manager2.getMaPM().equals(approve.getId_lead()));

							if (manager2.getMaPM() != null && manager2.getMaPM().equals(approve.getId_lead())) {
								ot.setLyDoTuChoi(approve.getReason());
								System.out.println("cac 1");
								// goi service user de lay infor nhanvien
								final String uri4 = "https://userteam07.herokuapp.com/api/staff_nghiphep/"
										+ manager.getMaNV();
								RestTemplate restTemplate4 = new RestTemplate();
								User result_staff = restTemplate4.getForObject(uri4, User.class);

								System.out.println("cac 2");
								System.out.println(result_staff.getID());

								ot.setTrangThai(1);
								ot.setLyDoTuChoi("");
								ot.setMaNguoiDuyet(approve.getId_lead());

								result_staff.setSoPhepConLai(result_staff.getSoPhepConLai() - 1); // cập nhật lại số
																									// ngày
								System.out.println("cac 3"); // nghỉ
								// phép còn lại
								ApiResponse<NghiPhep> resp = new ApiResponse<NghiPhep>(0, "Success", repoNP.save(ot));
								System.out.println("mã nhân viên cần update: " + manager);
								// goi lai service user de cap nhat lai so phep con lai
								final String uri3 = "https://userteam07.herokuapp.com/api/update_sophepconlai/"
										+ manager.getMaNV();
								System.out.println("cac 4");
								System.out.println("api update: " + uri3);
								RestTemplate restTemplate3 = new RestTemplate();
								User _nv = new User(result_staff.getID(), result_staff.getHoTen(),
										result_staff.getUserName(), result_staff.getPassWord(),
										result_staff.getGioiTinh(), result_staff.getChucVu(), result_staff.getDiaChi(),
										result_staff.getEmail(), result_staff.getSDT(), result_staff.getAvatar());
								User result3 = restTemplate3.postForObject(uri3, _nv, User.class);
								// return new ResponseEntity<>(resp, HttpStatus.CREATED);
								repoNP.save(ot);
								System.out.println("cac 5");

							} else {
								ApiResponse<List<NghiPhep_Response>> resp = new ApiResponse<>(1,
										"Id reviewer or order id not valid", null);
								return new ResponseEntity<>(resp, HttpStatus.OK);
							}

						} else {
							ApiResponse<List<NghiPhep_Response>> resp = new ApiResponse<>(1,
									"Invalid reviwer id or of list_id request not valid", null);
							return new ResponseEntity<>(resp, HttpStatus.OK);
						}
					} else {
						ApiResponse<List<NghiPhep_Response>> resp = new ApiResponse<>(1,
								"Invalid reviwer id or of list_id request not valid", null);
						return new ResponseEntity<>(resp, HttpStatus.OK);
					}

				}
			}
			// TU CHOI DUYET DON

			else if (approve.getAction().toUpperCase().equals("REJECT")) { // check xem loai action la reject hay accept
				// duyệt list id_đơn sau đó lấy các mã đơn này duyệt trong bảng nghỉ phép lấy ra
				// trạng thái của các đơn nếu đơn có trạng thái = 0 mới cho duyệt
				System.out.println("tao vao trong reject roi nay");
				for (String request_id : approve.getList_id_request()) {
					// Find request

					Query q = new Query();

					q.addCriteria(Criteria.where("ID").is(request_id));
					NghiPhep ot = mongoTemplate.findOne(q, NghiPhep.class);
					// Validate manager authority

					System.out.println("ngay bat dau: " + ot.getNgayBatDau());
					System.out.println("ngay bat dau: " + ot.getNgayKetThuc());

					if (Caculatebetweentwoday(ot.getNgayBatDau(), ot.getNgayKetThuc()) <= 3 && ot.getTrangThai() == 0) {
						System.out.println("so ngay < 3");
						// lấy ra manager 1 của staff
						String uri = "https://gatewayteam07.herokuapp.com/api/get_manager1_of_staff/"
								+ ot.getMaNhanVien();
						RestTemplate restTemplate = new RestTemplate();
						ThamGiaDuAn manager = restTemplate.getForObject(uri, ThamGiaDuAn.class);

						if (manager.getID() != null && manager.getMaTL().equals(approve.getId_lead())) {
							ot.setLyDoTuChoi(approve.getReason());

							// goi service user de lay infor nhanvien
							final String uri1 = "https://userteam07.herokuapp.com/api/staff_nghiphep/"
									+ manager.getMaNV();
							RestTemplate restTemplate1 = new RestTemplate();
							User result_staff = restTemplate1.getForObject(uri1, User.class);

							ot.setTrangThai(2);
							ot.setLyDoTuChoi(approve.getReason());
							ot.setMaNguoiDuyet(approve.getId_lead());

							ApiResponse<NghiPhep> resp = new ApiResponse<NghiPhep>(0, "Success", repoNP.save(ot));
							System.out.println("mã nhân viên cần update: " + manager.getMaNV());
							repoNP.save(ot);
						} else {
							ApiResponse<List<NghiPhep_Response>> resp = new ApiResponse<>(1,
									"Id reviewer or order id not valid", null);
							return new ResponseEntity<>(resp, HttpStatus.OK);
						}

					} else if (ot.getTrangThai() != 0) {
						ApiResponse<List<NghiPhep_Response>> resp = new ApiResponse<>(1,
								"Invalid reviwer id or of list_id request not valid", null);
						return new ResponseEntity<>(resp, HttpStatus.OK);
					}

					else if (Caculatebetweentwoday(ot.getNgayBatDau(), ot.getNgayKetThuc()) > 3
							&& ot.getTrangThai() == 0) {

						System.out.println("so ngay > 3");
						// lấy ra manager 2 của staff
						String uri2 = "https://gatewayteam07.herokuapp.com/api/get_manager2_of_staff/"
								+ ot.getMaNhanVien();
						RestTemplate restTemplate2 = new RestTemplate();
						DuAn manager2 = restTemplate2.getForObject(uri2, DuAn.class);

						// lấy ra manager 1 của staff
						String uri = "https://gatewayteam07.herokuapp.com/api/get_manager1_of_staff/"
								+ ot.getMaNhanVien();
						RestTemplate restTemplate = new RestTemplate();
						ThamGiaDuAn manager = restTemplate.getForObject(uri, ThamGiaDuAn.class);

						System.out.println("mã pm update ne: " + manager2.getMaPM());
						if (manager2.getMaPM() != null && manager2.getMaPM().equals(approve.getId_lead())) {
							ot.setLyDoTuChoi(approve.getReason());

							// goi service user de lay infor nhanvien
							final String uri4 = "https://userteam07.herokuapp.com/api/staff_nghiphep/"
									+ manager.getMaNV();
							RestTemplate restTemplate4 = new RestTemplate();
							User result_staff = restTemplate4.getForObject(uri4, User.class);

							ot.setTrangThai(2);
							ot.setLyDoTuChoi(approve.getReason());
							ot.setMaNguoiDuyet(approve.getId_lead());

							ApiResponse<NghiPhep> resp = new ApiResponse<NghiPhep>(0, "Success", repoNP.save(ot));
							System.out.println("mã nhân viên cần update: " + manager.getMaNV());
							repoNP.save(ot);
						} else {
							ApiResponse<List<NghiPhep_Response>> resp = new ApiResponse<>(1,
									"Id reviewer or order id not valid", null);
							return new ResponseEntity<>(resp, HttpStatus.OK);
						}
					} else {
						ApiResponse<List<NghiPhep_Response>> resp = new ApiResponse<>(1,
								"Invalid reviwer id or of list_id request not valid", null);
						return new ResponseEntity<>(resp, HttpStatus.OK);
					}

				}
			}

			ApiResponse<List<NghiPhep_Response>> resp = new ApiResponse<>(0, "Success", null);
			return new ResponseEntity<>(resp, HttpStatus.OK);

		} catch (Exception e) {
			ApiResponse<List<NghiPhep_Response>> resp = new ApiResponse<>(1,
					"Invalid reviwer id or of list_id request not valid", null);
			return new ResponseEntity<>(resp, HttpStatus.CREATED);
		}

	}

	// nhân viên xem lại lịch sử cũng như số phép còn lại trong năm
	@GetMapping("/get_np_history/{MaNV_input}")
	public ResponseEntity<ApiResponse<NghiPhepResponse>> Get_np_history(
			@PathVariable(value = "MaNV_input") String MaNV_input) {
		try {
			System.out.println(MaNV_input);

			List<NghiPhep> wfhlst = new ArrayList<NghiPhep>();
			Query q = new Query();
			q.addCriteria(Criteria.where("MaNhanVien").is(MaNV_input));
			wfhlst = mongoTemplate.find(q, NghiPhep.class);

			// goi service user de lay infor nhanvien
			final String uri1 = "https://userteam07.herokuapp.com/api/staff_nghiphep/" + MaNV_input;
			RestTemplate restTemplate1 = new RestTemplate();
			User result_staff = restTemplate1.getForObject(uri1, User.class);

			if (wfhlst.isEmpty() == false) {
				NghiPhepResponse result = new NghiPhepResponse(wfhlst, result_staff.getSoPhepConLai());
				ApiResponse<NghiPhepResponse> resp = new ApiResponse<NghiPhepResponse>(0, "Success", result);
				return new ResponseEntity<>(resp, HttpStatus.OK);

			}
			ApiResponse<NghiPhepResponse> resp = new ApiResponse<NghiPhepResponse>(0, "no content", null);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
