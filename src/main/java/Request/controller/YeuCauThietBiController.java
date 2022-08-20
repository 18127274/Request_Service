package Request.controller;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;

import java.util.List;
import java.util.Optional;
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
import Request.model.DuAn;
import Request.model.List_Staff;
import Request.model.List_ThamGiaDuAn;
import Request.model.NghiPhep;
import Request.model.NghiPhep_Response;
import Request.model.User;
import Request.model.NhanVien;
import Request.model.YeuCauThietBi_Response;
import Request.model.approve_ot;
import Request.model.OT;
import Request.model.OT_Response;
import Request.model.ThamGiaDuAn;
import Request.model.YeuCauThietBi;
import Request.model.WFH;
import Request.repository.NghiPhepRepository;
import Request.repository.NhanVienRepository;
import Request.repository.OTRepository;
import Request.repository.ThamGiaDuAnRepository;
import Request.repository.WFHRepository;
import Request.repository.YeuCauThietBiRepository;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/api")

public class YeuCauThietBiController {
	@Autowired
	YeuCauThietBiRepository repoYCTB;
	@Autowired
	MongoTemplate mongoTemplate;
	@Autowired
	MongoOperations mongoOperation;

	// lay ra danh sach tat ca yeu cau thiet bi
	@GetMapping("/get_all_list_request_yeucauthietbi_of_staff/{MaNV_input}")
	public ResponseEntity<ApiResponse<List<YeuCauThietBi>>> get_all_list_request_yeucauthietbi_of_staff(
			@PathVariable(value = "MaNV_input") String MaNV_input) {
		try {
			List<YeuCauThietBi> wfhlst = new ArrayList<YeuCauThietBi>();
			Query q = new Query();
			q.addCriteria(Criteria.where("MaNhanVien").is(MaNV_input));

			wfhlst = mongoTemplate.find(q, YeuCauThietBi.class);
			if (wfhlst.isEmpty()) {
				ApiResponse<List<YeuCauThietBi>> resp = new ApiResponse<List<YeuCauThietBi>>(1, "Request is empty!",
						null);
				return new ResponseEntity<>(resp, HttpStatus.OK);
				// return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			ApiResponse<List<YeuCauThietBi>> resp = new ApiResponse<List<YeuCauThietBi>>(0, "Success", wfhlst);
			return new ResponseEntity<>(resp, HttpStatus.OK);
		} catch (Exception e) {
			ApiResponse<List<YeuCauThietBi>> resp = new ApiResponse<List<YeuCauThietBi>>(1, "Request is empty!", null);
			return new ResponseEntity<>(resp, HttpStatus.OK);
		}
	}

	// lấy ra đơn yêu cầu wfh thông qua mã nhân viên.
	@GetMapping("/get_yctb_by_staff_id/{MaNV_input}")
	public ResponseEntity<ApiResponse<List<YeuCauThietBi>>> Get_yctb_by_staff_id(
			@PathVariable(value = "MaNV_input") String MaNV_input) {

		try {
			List<YeuCauThietBi> wfhlst = new ArrayList<YeuCauThietBi>();
			Query q = new Query();

			q.addCriteria(Criteria.where("MaNhanVien").is(MaNV_input));
			wfhlst = mongoTemplate.find(q, YeuCauThietBi.class);
			if (wfhlst.isEmpty()) {
				ApiResponse<List<YeuCauThietBi>> resp = new ApiResponse<List<YeuCauThietBi>>(1,
						"Id staff wrong or this device order is not available!", null);
				return new ResponseEntity<>(resp, HttpStatus.OK);
			}
			ApiResponse<List<YeuCauThietBi>> resp = new ApiResponse<List<YeuCauThietBi>>(0, "Success", wfhlst);
			return new ResponseEntity<>(resp, HttpStatus.OK);
		} catch (Exception e) {
			ApiResponse<List<YeuCauThietBi>> resp = new ApiResponse<List<YeuCauThietBi>>(1, "Request is empty or id staff wrong!", null);
			return new ResponseEntity<>(resp, HttpStatus.OK);
		}
	}

	// lay ra danh sach nhung don yeu cau thiet bi dc duyet theo role
	@GetMapping("/get_all_list_yctb_by_role")
	public ResponseEntity<ApiResponse<List<YeuCauThietBi_Response>>> Get_all_list_yctb_by_role(
			@RequestParam("id_reviewer") String id_reviewer, @RequestParam("status") int status_input,
			@RequestParam("role") int role) {
		try {
			if (role == 1 && status_input == 1) { // phong it dc duyet
				// check xem thang reviwer nay co ton tai va co role la 1 hay khong?

				System.out.println("vao 1");

				final String uri = "https://userteam07.herokuapp.com/api/staff_nghiphep/" + id_reviewer;
				System.out.println("api: " + uri);
				RestTemplate restTemplate2 = new RestTemplate();
				User result = restTemplate2.getForObject(uri, User.class);

				if (result.getID() != "" && result.getChucVu() == 1) {
					List<YeuCauThietBi> otlst = new ArrayList<YeuCauThietBi>();
					Query q = new Query();
					q.addCriteria(Criteria.where("TrangThai").is(1));
					otlst = mongoTemplate.find(q, YeuCauThietBi.class);
					if (otlst.isEmpty()) {
						ApiResponse<List<YeuCauThietBi_Response>> resp = new ApiResponse<>(0, "Empty data", null);
						return new ResponseEntity<>(resp, HttpStatus.OK);
					}
					// tra ve them ten nhan vien
					List<YeuCauThietBi> result1 = new ArrayList<YeuCauThietBi>();
					repoYCTB.findAll().forEach(result1::add);
					System.out.println("size cua yctb: " + result1.size());

					List<YeuCauThietBi_Response> resp = new ArrayList<YeuCauThietBi_Response>();
					for (YeuCauThietBi i : result1) {
						if (i.getTrangThai() == 1) {
							String uri1 = "https://gatewayteam07.herokuapp.com/api/staff_nghiphep/" + i.getMaNhanVien();
							System.out.println("api1: " + uri1);
							RestTemplate restTemplate1 = new RestTemplate();
							User staff1 = restTemplate1.getForObject(uri1, User.class);
							YeuCauThietBi_Response temp = new YeuCauThietBi_Response(i, staff1);
							resp.add(temp);
						}

					}

					ApiResponse<List<YeuCauThietBi_Response>> resp1 = new ApiResponse<>(0, "Success", resp);
					return new ResponseEntity<>(resp1, HttpStatus.OK);
				}
				ApiResponse<List<YeuCauThietBi_Response>> resp = new ApiResponse<>(1, "id_reviewer or status is wrong!",
						null);
				return new ResponseEntity<>(resp, HttpStatus.OK);
			} else if (role == 4 && status_input == 4) {
				System.out.println("vao 4");
				String uri = "https://gatewayteam07.herokuapp.com/api/list_staff_manager1/" + id_reviewer;
				RestTemplate restTemplate = new RestTemplate();
				List_Staff call = restTemplate.getForObject(uri, List_Staff.class);
				List<String> staff = call.getListstaff();

				if (status_input < 0 || status_input > 5) {
					ApiResponse<List<YeuCauThietBi_Response>> resp = new ApiResponse<>(1, "invalid status", null);
					return new ResponseEntity<>(resp, HttpStatus.OK);
				}

				List<YeuCauThietBi> otlst = new ArrayList<YeuCauThietBi>();
				Query q = new Query();
				q.addCriteria(Criteria.where("TrangThai").is(4));
				otlst = mongoTemplate.find(q, YeuCauThietBi.class);
				System.out.println("query tim ra nhung don co trang thai 1: " + otlst.isEmpty());
				if (otlst.isEmpty()) {
					System.out.println("empty tu trong check status bang yeu cau thiet bi");
					ApiResponse<List<YeuCauThietBi_Response>> resp = new ApiResponse<>(0, "Empty data", null);
					return new ResponseEntity<>(resp, HttpStatus.OK);
				}
				List<YeuCauThietBi> result = new ArrayList<YeuCauThietBi>();
				for (YeuCauThietBi i : otlst) {
					for (String y : staff) {
						System.out.println("vao dc so sanh");
						if (i.getMaNhanVien().equals(y)) {
							System.out.println("co data");
							result.add(i);
						}
					}
				}
				if (result.isEmpty()) {
					System.out.println("result empty");
					ApiResponse<List<YeuCauThietBi_Response>> resp = new ApiResponse<>(0, "Empty data", null);
					return new ResponseEntity<>(resp, HttpStatus.OK);
				}
				// them ten
				List<YeuCauThietBi_Response> resp = new ArrayList<YeuCauThietBi_Response>();
				for (YeuCauThietBi i : result) {
					String uri1 = "https://gatewayteam07.herokuapp.com/api/staff_nghiphep/" + i.getMaNhanVien();
					System.out.println("api1: " + uri1);
					RestTemplate restTemplate1 = new RestTemplate();
					User staff1 = restTemplate1.getForObject(uri1, User.class);
					YeuCauThietBi_Response temp = new YeuCauThietBi_Response(i, staff1);
					resp.add(temp);
				}

				ApiResponse<List<YeuCauThietBi_Response>> resp1 = new ApiResponse<>(0, "Success", resp);
				return new ResponseEntity<>(resp1, HttpStatus.OK);
			} else if (role == 5 && status_input == 5) {
				System.out.println("vao 1");

				final String uri = "https://userteam07.herokuapp.com/api/staff_nghiphep/" + id_reviewer;
				System.out.println("api: " + uri);
				RestTemplate restTemplate2 = new RestTemplate();
				User result = restTemplate2.getForObject(uri, User.class);

				if (result.getID() != "" && result.getChucVu() == 5) {
					List<YeuCauThietBi> otlst = new ArrayList<YeuCauThietBi>();
					Query q = new Query();
					q.addCriteria(Criteria.where("TrangThai").is(5));
					otlst = mongoTemplate.find(q, YeuCauThietBi.class);
					if (otlst.isEmpty()) {
						ApiResponse<List<YeuCauThietBi_Response>> resp = new ApiResponse<>(0, "Empty data", null);
						return new ResponseEntity<>(resp, HttpStatus.OK);
					}
					// tra ve them ten nhan vien
					List<YeuCauThietBi> result1 = new ArrayList<YeuCauThietBi>();
					repoYCTB.findAll().forEach(result1::add);
					System.out.println("size cua yctb: " + result1.size());

					List<YeuCauThietBi_Response> resp = new ArrayList<YeuCauThietBi_Response>();
					for (YeuCauThietBi i : result1) {
						if (i.getTrangThai() == 5) {
							String uri1 = "https://gatewayteam07.herokuapp.com/api/staff_nghiphep/" + i.getMaNhanVien();
							System.out.println("api1: " + uri1);
							RestTemplate restTemplate1 = new RestTemplate();
							User staff1 = restTemplate1.getForObject(uri1, User.class);
							YeuCauThietBi_Response temp = new YeuCauThietBi_Response(i, staff1);
							resp.add(temp);
						}

					}

					ApiResponse<List<YeuCauThietBi_Response>> resp1 = new ApiResponse<>(0, "Success", resp);
					return new ResponseEntity<>(resp1, HttpStatus.OK);
				}
				ApiResponse<List<YeuCauThietBi_Response>> resp = new ApiResponse<>(1, "id_reviewer or status is wrong!",
						null);
				return new ResponseEntity<>(resp, HttpStatus.OK);
			} else if (role == 3 && status_input == 3) {

				final String uri = "https://userteam07.herokuapp.com/api/staff_nghiphep/" + id_reviewer;
				System.out.println("api: " + uri);
				RestTemplate restTemplate2 = new RestTemplate();
				User result = restTemplate2.getForObject(uri, User.class);

				if (result.getID() != "" && result.getChucVu() == 3) {
					List<YeuCauThietBi> otlst = new ArrayList<YeuCauThietBi>();
					Query q = new Query();
					q.addCriteria(Criteria.where("TrangThai").is(3));
					otlst = mongoTemplate.find(q, YeuCauThietBi.class);
					if (otlst.isEmpty()) {
						ApiResponse<List<YeuCauThietBi_Response>> resp = new ApiResponse<>(0, "Empty data", null);
						return new ResponseEntity<>(resp, HttpStatus.OK);
					}
					// tra ve them ten nhan vien
					List<YeuCauThietBi> result1 = new ArrayList<YeuCauThietBi>();
					repoYCTB.findAll().forEach(result1::add);
					System.out.println("size cua yctb: " + result1.size());

					List<YeuCauThietBi_Response> resp = new ArrayList<YeuCauThietBi_Response>();
					for (YeuCauThietBi i : result1) {
						if (i.getTrangThai() == 3) {
							String uri1 = "https://gatewayteam07.herokuapp.com/api/staff_nghiphep/" + i.getMaNhanVien();
							System.out.println("api1: " + uri1);
							RestTemplate restTemplate1 = new RestTemplate();
							User staff1 = restTemplate1.getForObject(uri1, User.class);
							YeuCauThietBi_Response temp = new YeuCauThietBi_Response(i, staff1);
							resp.add(temp);
						}

					}

					ApiResponse<List<YeuCauThietBi_Response>> resp1 = new ApiResponse<>(0, "Success", resp);
					return new ResponseEntity<>(resp1, HttpStatus.OK);
				}
				ApiResponse<List<YeuCauThietBi_Response>> resp = new ApiResponse<>(1,
						"ID reviewer does not exist or status is wrong!", null);
				return new ResponseEntity<>(resp, HttpStatus.OK);
			}
			System.out.println("ra ngoai");
			ApiResponse<List<YeuCauThietBi_Response>> resp = new ApiResponse<>(1,
					"ID reviewer does not exist or status is wrong!", null);
			return new ResponseEntity<>(resp, HttpStatus.OK);
		} catch (Exception e) {
			ApiResponse<List<YeuCauThietBi_Response>> resp = new ApiResponse<>(1,
					"ID reviewer does not exist or status is wrong!", null);
			return new ResponseEntity<>(resp, HttpStatus.OK);
		}
	}


	// lấy ra đơn yêu cầu wfh theo trangthai (trangthai = 0: chờ xét duyệt, 1: đã
	// xét duyệt, 2: từ chối).
	@GetMapping("/get_yctb_by_status/{status}")
	public ResponseEntity<ApiResponse<List<YeuCauThietBi>>> Get_yctb_by_status(
			@PathVariable(value = "status") String status) {

		try {
			List<YeuCauThietBi> wfhlst = new ArrayList<YeuCauThietBi>();
			Query q = new Query();
			q.addCriteria(Criteria.where("TrangThai").is(status));
			wfhlst = mongoTemplate.find(q, YeuCauThietBi.class);
			if (wfhlst.isEmpty()) {
				ApiResponse<List<YeuCauThietBi>> resp = new ApiResponse<List<YeuCauThietBi>>(1,
						"Status format wrong or device order does not exist!", null);
				return new ResponseEntity<>(resp, HttpStatus.OK);
			}
			ApiResponse<List<YeuCauThietBi>> resp = new ApiResponse<List<YeuCauThietBi>>(0, "Success", wfhlst);
			return new ResponseEntity<>(resp, HttpStatus.OK);
		} catch (Exception e) {
			ApiResponse<List<YeuCauThietBi>> resp = new ApiResponse<List<YeuCauThietBi>>(1, "Request is empty or status wrong!", null);
			return new ResponseEntity<>(resp, HttpStatus.OK);
		}
	}

	// nhân viên yêu cầu thiết bị làm việc....
	// nếu nhân viên đã request đơn mà chưa dc duyệt thì k thể request thêm 1
	// đơn mới.
	@PostMapping("/request_yctb")
	public ResponseEntity<ApiResponse<YeuCauThietBi>> Request_yctb(@RequestBody YeuCauThietBi wfh) {
		try {
			// check xem nhân viên này có đơn nào đang chưa được duyệt hay không?
			List<YeuCauThietBi> wfhlst = new ArrayList<YeuCauThietBi>();
			Query q = new Query();
			// q.addCriteria(Criteria.where("MaNhanVien").is(wfh.getMaNhanVien()));

			// q.addCriteria(Criteria.where("MaNhanVien").is(wfh.getMaNhanVien())).addCriteria(Criteria.where("TrangThai").is("IT
			// Department is reviewing"));
			q.addCriteria(Criteria.where("MaNhanVien").is(wfh.getMaNhanVien()))
					.addCriteria(Criteria.where("").orOperator(Criteria.where("TrangThai").is(1),
							Criteria.where("TrangThai").is(4), Criteria.where("TrangThai").is(5),
							Criteria.where("TrangThai").is(3)));
			// criteria.orOperator(Criteria.where("A").is(10),Criteria.where("B").is(20));
			wfhlst = mongoTemplate.find(q, YeuCauThietBi.class);

			System.out.println(wfh.getMaNhanVien());
			System.out.println(wfhlst.isEmpty());
			if (wfhlst.isEmpty() == true) {
				System.out.println("khong co thang nhan vien nay");
				wfh.setID(UUID.randomUUID().toString());
				YeuCauThietBi _wfh = repoYCTB.save(new YeuCauThietBi(wfh.getID(), wfh.getMaNhanVien(), wfh.getMoTa(),
						wfh.getChiPhi(), wfh.getSoLuong(), "", "", 1));
				ApiResponse<YeuCauThietBi> resp = new ApiResponse<YeuCauThietBi>(0, "Success", _wfh);
				return new ResponseEntity<>(resp, HttpStatus.CREATED);
			}
			ApiResponse<YeuCauThietBi> resp = new ApiResponse<YeuCauThietBi>(1,
					"Can't request because you have petition", null);
			return new ResponseEntity<>(resp, HttpStatus.CREATED);
		}

		catch (Exception e) {
			ApiResponse<YeuCauThietBi> resp = new ApiResponse<YeuCauThietBi>(1, "Can't request because you have petition", null);
			return new ResponseEntity<>(resp, HttpStatus.OK);
		}
	}

	// hàm xét duyệt đơn yêu cầu thiết bị ( phòng it, manager 1, manager 2, phòng kế
	// toán)
	@PutMapping("/approve_request_yctb")
	public ResponseEntity<ApiResponse<List<YeuCauThietBi_Response>>> Approve_request_yctb(
			@RequestBody approve_ot approve) {
		try {
			List<YeuCauThietBi> result = new ArrayList<YeuCauThietBi>();
			if (!approve.getAction().toUpperCase().equals("ACCEPT")
					&& !(approve.getAction().toUpperCase().equals("REJECT"))) {
				ApiResponse<List<YeuCauThietBi_Response>> resp = new ApiResponse<>(1, "Action only accept or reject",
						null);
				return new ResponseEntity<>(resp, HttpStatus.CREATED);
			}
			if (approve.getAction().toUpperCase().equals("REJECT")
					&& (approve.getReason() == null || approve.getReason().equals(""))) {
				ApiResponse<List<YeuCauThietBi_Response>> resp = new ApiResponse<>(1, "Invalid reason", null);
				return new ResponseEntity<>(resp, HttpStatus.CREATED);
			}

			

			
			// check mang list
			List<YeuCauThietBi> result4 = new ArrayList<YeuCauThietBi>();
			repoYCTB.findAll().forEach(result4::add);
			
			System.out.println("size yctb: " + result4.size());
			for (String request_id : approve.getList_id_request()) {
				Query q = new Query();
				q.addCriteria(Criteria.where("ID").is(request_id));
				YeuCauThietBi ot = mongoTemplate.findOne(q, YeuCauThietBi.class);
				if(ot.getID() == "") {
					ApiResponse<List<YeuCauThietBi_Response>> resp = new ApiResponse<>(1,"Invalid reviewer id or of list_id request not valid", null);
					return new ResponseEntity<>(resp, HttpStatus.OK);
				}
			}
			
			
			// CHẤP THUẬN DUYỆT ĐƠN
			if (approve.getAction().toUpperCase().equals("ACCEPT")) { // check xem loai action la reject hay accept
				// duyệt list id_đơn sau đó lấy các mã đơn này duyệt trong bảng yêu cầu thiết bị
				// trạng thái của các đơn nếu đơn có trạng thái = 0 mới cho duyệt
				
				
				System.out.println("do dai list: " + approve.getList_id_request().size());
				for (String request_id : approve.getList_id_request()) {

					// Find request
					Query q = new Query();
					q.addCriteria(Criteria.where("ID").is(request_id));
					YeuCauThietBi ot = mongoTemplate.findOne(q, YeuCauThietBi.class);
					// Validate manager authority

					// kiểm tra xem chức vụ của người duyệt đơn này, (phòng it: 1, Team leader: 4,
					// ban giám đốc: 5, phòng kế toán: 3)
					final String uri1 = "https://userteam07.herokuapp.com/api/staff_nghiphep/" + approve.getId_lead();

					RestTemplate restTemplate1 = new RestTemplate();
					User result1 = restTemplate1.getForObject(uri1, User.class);
					
				
					if (ot.getTrangThai() == 1 && result1.getChucVu() == 1) {
						ot.setTrangThai(4);
						ot.setMaNguoiDuyet(approve.getId_lead());
						ApiResponse<YeuCauThietBi> resp = new ApiResponse<YeuCauThietBi>(0, "Success",
								repoYCTB.save(ot));
						repoYCTB.save(ot);

					}
					else if (ot.getTrangThai() == 4 && result1.getChucVu() == 4) {
						ot.setTrangThai(5);
						ot.setMaNguoiDuyet(approve.getId_lead());
						ApiResponse<YeuCauThietBi> resp = new ApiResponse<YeuCauThietBi>(0, "Success",
								repoYCTB.save(ot));
						repoYCTB.save(ot);

					}
					else if (ot.getTrangThai() == 5 && result1.getChucVu() == 5) {
						ot.setTrangThai(3);
						ot.setMaNguoiDuyet(approve.getId_lead());
						ApiResponse<YeuCauThietBi> resp = new ApiResponse<YeuCauThietBi>(0, "Success",
								repoYCTB.save(ot));
						repoYCTB.save(ot);

					}
					
					else if (ot.getTrangThai() == 3 && result1.getChucVu() == 3) {
						ot.setTrangThai(2);
						ot.setMaNguoiDuyet(approve.getId_lead());
						ApiResponse<YeuCauThietBi> resp = new ApiResponse<YeuCauThietBi>(0, "Success",
								repoYCTB.save(ot));
						repoYCTB.save(ot);

					}

					else {
						System.out.println("vao 2");
						ApiResponse<List<YeuCauThietBi_Response>> resp = new ApiResponse<>(1,
								"Invalid reviewer id or of list_id request not valid", null);
						return new ResponseEntity<>(resp, HttpStatus.OK);
					}

				}
			}
			// TỪ CHỐI DUYỆT ĐƠN

			else if (approve.getAction().toUpperCase().equals("REJECT")) { // check xem loai action la reject hay accept

				// duyệt list id_đơn sau đó lấy các mã đơn này duyệt trong bảng yêu cầu thiết bị
				// trạng thái của các đơn nếu đơn có trạng thái = 0 mới cho duyệt
				
				
				for (String request_id : approve.getList_id_request()) {
					// Find request
					Query q = new Query();
					q.addCriteria(Criteria.where("ID").is(request_id));
					YeuCauThietBi ot = mongoTemplate.findOne(q, YeuCauThietBi.class);
					// Validate manager authority

					// kiểm tra xem chức vụ của người duyệt đơn này, (phòng it: 1, ban giám đốc: 5,
					// phòng kế toán: 3)
					final String uri1 = "https://userteam07.herokuapp.com/api/staff_nghiphep/" + approve.getId_lead();
					System.out.println("api: " + uri1);
					RestTemplate restTemplate1 = new RestTemplate();
					User result1 = restTemplate1.getForObject(uri1, User.class);
					
					
					System.out.println("chuc vu: " + result1.getChucVu());
					System.out.println("trang thai: " + ot.getTrangThai());
					if (ot.getTrangThai() == 1 && result1.getChucVu() == 1) {
						ot.setMaNguoiDuyet(approve.getId_lead());
						ot.setTrangThai(0);
						ot.setLyDoTuChoi(approve.getReason());
						ApiResponse<YeuCauThietBi> resp = new ApiResponse<YeuCauThietBi>(0, "Success",
								repoYCTB.save(ot));
						repoYCTB.save(ot);

					}
					else if (ot.getTrangThai() == 4 && result1.getChucVu() == 4) {
						ot.setMaNguoiDuyet(approve.getId_lead());
						ot.setTrangThai(0);
						ot.setLyDoTuChoi(approve.getReason());
						ApiResponse<YeuCauThietBi> resp = new ApiResponse<YeuCauThietBi>(0, "Success",
								repoYCTB.save(ot));
						repoYCTB.save(ot);

					}
					else if (ot.getTrangThai() == 5 && result1.getChucVu() == 5) {
						ot.setMaNguoiDuyet(approve.getId_lead());
						ot.setTrangThai(0);
						ot.setLyDoTuChoi(approve.getReason());
						ApiResponse<YeuCauThietBi> resp = new ApiResponse<YeuCauThietBi>(0, "Success",
								repoYCTB.save(ot));
						repoYCTB.save(ot);

					}
					else if (ot.getTrangThai() == 3 && result1.getChucVu() == 3) {
						ot.setMaNguoiDuyet(approve.getId_lead());
						ot.setTrangThai(0);
						ot.setLyDoTuChoi(approve.getReason());
						ApiResponse<YeuCauThietBi> resp = new ApiResponse<YeuCauThietBi>(0, "Success",
								repoYCTB.save(ot));
						repoYCTB.save(ot);

					}

					else {
						System.out.println("vao 1");
						ApiResponse<List<YeuCauThietBi_Response>> resp = new ApiResponse<>(1,
								"Invalid reviewer id or of list_id request not valid", null);
						return new ResponseEntity<>(resp, HttpStatus.OK);
					}

				}

			}

			ApiResponse<List<YeuCauThietBi_Response>> resp = new ApiResponse<>(0, "Success", null);
			return new ResponseEntity<>(resp, HttpStatus.OK);

		} catch (Exception e) {
			System.out.println("vao 2");
			ApiResponse<List<YeuCauThietBi_Response>> resp = new ApiResponse<>(1,
					"Invalid reviwer id or of list_id request not valid", null);
			return new ResponseEntity<>(resp, HttpStatus.CREATED);
		}

	}

}
