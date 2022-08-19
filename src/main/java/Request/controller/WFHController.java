package Request.controller;

import java.time.LocalDate;
import java.time.LocalTime;
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
import Request.model.List_Staff;
import Request.model.List_ThamGiaDuAn;
import Request.model.NghiPhep;
import Request.model.NhanVien;
import Request.model.WFH_Reponse;
import Request.model.OT;
import Request.model.OT_Response;
import Request.model.ThamGiaDuAn;
import Request.model.User;
import Request.model.WFH;
import Request.model.approve_ot;
import Request.repository.NghiPhepRepository;
import Request.repository.NhanVienRepository;
import Request.repository.OTRepository;
import Request.repository.ThamGiaDuAnRepository;
import Request.repository.WFHRepository;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/api")

public class WFHController {
	@Autowired
	WFHRepository repoWFH;
	@Autowired
	MongoTemplate mongoTemplate;
	@Autowired
	MongoOperations mongoOperation;

	// lay ra danh sach tat ca yeu cau wfh @RequestBody WFH wfh
	@GetMapping("/get_all_list_request_wfh")
	public ResponseEntity<ApiResponse<List<WFH>>> View_all_list_request_wfh() {
		try {

			List<WFH> wfhlst = new ArrayList<WFH>();

			repoWFH.findAll().forEach(wfhlst::add);

			if (wfhlst.isEmpty()) {
				ApiResponse<List<WFH>> resp = new ApiResponse<List<WFH>>(1, "Request is empty!", null);
				return new ResponseEntity<>(resp, HttpStatus.OK);

			}

			ApiResponse<List<WFH>> resp = new ApiResponse<List<WFH>>(0, "Success", wfhlst);
			return new ResponseEntity<>(resp, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// lấy ra tat ca đơn yêu cầu wfh thông qua mã nhân viên.
	@GetMapping("/get_all_list_wfh_by_staff_id/{MaNV_input}")
	public ResponseEntity<ApiResponse<List<WFH>>> Get_all_list_wfh_by_staff_id(
			@PathVariable(value = "MaNV_input") String MaNV_input) {

		try {
			List<WFH> wfhlst = new ArrayList<WFH>();
			Query q = new Query();
			q.addCriteria(Criteria.where("MaNhanVien").is(MaNV_input));
			wfhlst = mongoTemplate.find(q, WFH.class);

			System.out.println(MaNV_input);
			System.out.println(wfhlst.isEmpty());
			if (wfhlst.isEmpty()) {
				ApiResponse<List<WFH>> resp = new ApiResponse<List<WFH>>(1, "Request is empty!", null);
				return new ResponseEntity<>(resp, HttpStatus.OK);
			}
			ApiResponse<List<WFH>> resp = new ApiResponse<List<WFH>>(0, "Success", wfhlst);
			return new ResponseEntity<>(resp, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// lấy ra đơn yêu cầu wfh thông qua mã đơn.
	@GetMapping("/get_wfh_id/{MaWFH_input}")
	public ResponseEntity<ApiResponse<List<WFH>>> Get_wfh_id(@PathVariable(value = "MaWFH_input") String MaWFH_input) {

		try {
			List<WFH> wfhlst = new ArrayList<WFH>();
			Query q = new Query();
			q.addCriteria(Criteria.where("ID").is(MaWFH_input));
			wfhlst = mongoTemplate.find(q, WFH.class);
			if (wfhlst.isEmpty()) {
				ApiResponse<List<WFH>> resp = new ApiResponse<List<WFH>>(1,
						"Id wfh wrong or this order_wfh is not available!", null);
				return new ResponseEntity<>(resp, HttpStatus.OK);
			}
			ApiResponse<List<WFH>> resp = new ApiResponse<List<WFH>>(0, "Success", wfhlst);
			return new ResponseEntity<>(resp, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// lấy ra đơn yêu cầu wfh thông qua mã nhân viên.
	@GetMapping("/get_wfh_by_staff_id/{MaNV_input}")
	public ResponseEntity<ApiResponse<List<WFH>>> Get_wfh_by_staff_id(
			@PathVariable(value = "MaNV_input") String MaNV_input) {

		try {
			List<WFH> wfhlst = new ArrayList<WFH>();
			Query q = new Query();
			q.addCriteria(Criteria.where("MaNhanVien").is(MaNV_input));
			wfhlst = mongoTemplate.find(q, WFH.class);
			if (wfhlst.isEmpty()) {
				ApiResponse<List<WFH>> resp = new ApiResponse<List<WFH>>(1,
						"Id staff wrong or this order_wfh is not available!", null);
				return new ResponseEntity<>(resp, HttpStatus.OK);
			}
			ApiResponse<List<WFH>> resp = new ApiResponse<List<WFH>>(0, "Success", wfhlst);
			return new ResponseEntity<>(resp, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// lấy ra đơn yêu cầu wfh theo trangthai (trangthai = 0: chờ xét duyệt, 1: đã
	// xét duyệt, 2: từ chối).
	@GetMapping("/get_wfh_by_status/{status}")
	public ResponseEntity<ApiResponse<List<WFH>>> Get_wfh_by_status(@PathVariable(value = "status") int status) {

		try {
			List<WFH> wfhlst = new ArrayList<WFH>();
			Query q = new Query();
			q.addCriteria(Criteria.where("TrangThai").is(status));
			wfhlst = mongoTemplate.find(q, WFH.class);

			System.out.println(status);
			System.out.println(wfhlst.isEmpty());
			if (wfhlst.isEmpty()) {
				ApiResponse<List<WFH>> resp = new ApiResponse<List<WFH>>(1,
						"Status format wrong or order wfh does not exist!", null);
				return new ResponseEntity<>(resp, HttpStatus.OK);
			}
			ApiResponse<List<WFH>> resp = new ApiResponse<List<WFH>>(0, "Success", wfhlst);
			return new ResponseEntity<>(resp, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// nhân viên yêu cầu làm việc tại nhà
	// nếu nhân viên đã request đơn wfh mà chưa dc duyệt thì k thể request thêm 1
	// đơn mới.
	@PostMapping("/request_wfh")
	public ResponseEntity<ApiResponse<WFH>> Request_wfh(@RequestBody WFH wfh) {
		try {
			// check xem nhân viên này có đơn nào đang chưa được duyệt hay không?
			
			LocalDate localDate = LocalDate.now();
			System.out.println(localDate);
			
			System.out.println("ss nbd: " + wfh.getNgayBatDau().compareTo(localDate));
			System.out.println("ss nkt: " + wfh.getNgayKetThuc().compareTo(localDate));
			LocalTime localTime = LocalTime.now();
			
			if(wfh.getNgayBatDau().compareTo(localDate) >= 0 && wfh.getNgayKetThuc().compareTo(localDate) >=0 ) {
				List<WFH> wfhlst = new ArrayList<WFH>();
				Query q = new Query();
				// q.addCriteria(Criteria.where("MaNhanVien").is(wfh.getMaNhanVien()));
				q.addCriteria(Criteria.where("MaNhanVien").is(wfh.getMaNhanVien()))
						.addCriteria(Criteria.where("TrangThai").is(0));
				wfhlst = mongoTemplate.find(q, WFH.class);

				System.out.println(wfh.getMaNhanVien());
				System.out.println(wfhlst.isEmpty());
				if (wfhlst.isEmpty() == true) {
					System.out.println("khong co thang nhan vien nay");
					wfh.setID(UUID.randomUUID().toString());
					WFH _wfh = repoWFH.save(new WFH(wfh.getID(), "", wfh.getMaNhanVien(), wfh.getNgayBatDau(),
							wfh.getNgayKetThuc(), wfh.getLyDo(), "", 0));
					ApiResponse<WFH> resp = new ApiResponse<WFH>(0, "Success", _wfh);
					return new ResponseEntity<>(resp, HttpStatus.CREATED);
				}
			}
			
			ApiResponse<WFH> resp = new ApiResponse<WFH>(1, "You was have petition or input date wrong!", null);
			return new ResponseEntity<>(resp, HttpStatus.CREATED);
		}

		catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/get_all_list_wfh_of_manager1")
	public ResponseEntity<ApiResponse<List<WFH_Reponse>>> Get_all_list_wfh_of_manager1(
			@RequestParam("id_lead") String id_lead_input, @RequestParam("status") int status_input) {
		try {
			String uri = "https://gatewayteam07.herokuapp.com/api/list_staff_manager1/" + id_lead_input;
			RestTemplate restTemplate = new RestTemplate();
			List_Staff call = restTemplate.getForObject(uri, List_Staff.class);
			List<String> staff = call.getListstaff();

			if (status_input < 0 || status_input > 2) {
				ApiResponse<List<WFH_Reponse>> resp = new ApiResponse<List<WFH_Reponse>>(0, "invalid status", null);
				return new ResponseEntity<>(resp, HttpStatus.OK);
			}

			List<WFH> otlst = new ArrayList<WFH>();
			Query q = new Query();
			q.addCriteria(Criteria.where("TrangThai").is(status_input));
			otlst = mongoTemplate.find(q, WFH.class);
			if (otlst.isEmpty()) {
				ApiResponse<List<WFH_Reponse>> resp = new ApiResponse<>(0, "Empty data", null);
				return new ResponseEntity<>(resp, HttpStatus.OK);
			}

			List<WFH> result = new ArrayList<WFH>();
			for (WFH i : otlst) {
				for (String y : staff) {
					if (i.getMaNhanVien().equals(y)) {
						result.add(i);
					}
				}
			}

			List<WFH_Reponse> resp = new ArrayList<WFH_Reponse>();
			for (WFH i : result) {
				String uri1 = "https://gatewayteam07.herokuapp.com/api/staff_nghiphep/" + i.getMaNhanVien();
				RestTemplate restTemplate1 = new RestTemplate();
				User staff1 = restTemplate1.getForObject(uri1, User.class);
				System.out.println(staff1.getID());
				System.out.println(staff1.getHoTen());
				WFH_Reponse temp = new WFH_Reponse(i, staff1);
				resp.add(temp);
				System.out.println(resp.isEmpty());
			}

			ApiResponse<List<WFH_Reponse>> resp1 = new ApiResponse<>(0, "Success", resp);
			return new ResponseEntity<>(resp1, HttpStatus.OK);
		} catch (Exception e) {
			ApiResponse<List<WFH_Reponse>> resp = new ApiResponse<>(1, "ID lead not exist", null);
			return new ResponseEntity<>(resp, HttpStatus.OK);
		}
	}

	// manager cấp 1/ manager cấp 2 chấp thuận đơn yêu cầu.
//	@PutMapping("/approve_request_wfh")
//	public ResponseEntity<ApiResponse<WFH>> Approve_request_wfh(@RequestParam(value = "id", required = false) String id,
//			@RequestParam(value = "manguoiduyet", required = false) String manguoiduyet) {
//
//		try {
//			Query q = new Query();
//			q.addCriteria(Criteria.where("ID").is(id));
//			WFH wfh = mongoTemplate.findOne(q, WFH.class);
//			// kiểm tra xem mã người duyệt đơn này có phải là manager cấp 1 của nhân viên
//			// này hay không?
//			final String uri = "https://duanteam07.herokuapp.com/api/get_manager1_of_staff/" + wfh.getMaNhanVien();
//			System.out.println("api: " + uri);
//			RestTemplate restTemplate = new RestTemplate();
//			ThamGiaDuAn result = restTemplate.getForObject(uri, ThamGiaDuAn.class);
//			System.out.println(result);
//			System.out.println(result.getMaTL());
//			System.out.println(manguoiduyet);
//
//			if (result != null && result.getMaTL().equals(manguoiduyet)) {
//				wfh.setTrangThai(1);
//				wfh.setMaNguoiDuyet(manguoiduyet);
//				ApiResponse<WFH> resp = new ApiResponse<WFH>(0, "Success", repoWFH.save(wfh));
//				// ApiResponse<OT> resp = new ApiResponse<OT>(0,"Success",repoOT.save(ot));
//				return new ResponseEntity<>(resp, HttpStatus.CREATED);
//			} else {
//				ApiResponse<WFH> resp = new ApiResponse<WFH>(1, "Order_id wrong or manager level 1 id wrong", null);
//				return new ResponseEntity<>(resp, HttpStatus.CREATED);
//			}
//		} catch (Exception e) {
//			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//	}

	@PutMapping("/approve_request_wfh")
	public ResponseEntity<ApiResponse<List<WFH_Reponse>>> Approve_request_wfh(@RequestBody approve_ot approve) {
		try {
			List<WFH> result = new ArrayList<WFH>();
			if (!approve.getAction().toUpperCase().equals("ACCEPT")
					&& !(approve.getAction().toUpperCase().equals("REJECT"))) {
				ApiResponse<List<WFH_Reponse>> resp = new ApiResponse<>(1, "Action only accept or reject", null);
				return new ResponseEntity<>(resp, HttpStatus.CREATED);
			}
			if (approve.getAction().toUpperCase().equals("REJECT")
					&& (approve.getReason() == null || approve.getReason().equals(""))) {
				ApiResponse<List<WFH_Reponse>> resp = new ApiResponse<>(1, "Invalid reason", null);
				return new ResponseEntity<>(resp, HttpStatus.CREATED);
			}
			System.out.println("list id: " + approve.getList_id_request());
			System.out.println("list id: " + approve.getAction());

			for (String request_id : approve.getList_id_request()) {
				// Find request
				Query q = new Query();
				q.addCriteria(Criteria.where("ID").is(request_id));
				WFH ot = mongoTemplate.findOne(q, WFH.class);
				// Validate manager authority
				String uri = "https://gatewayteam07.herokuapp.com/api/get_manager1_of_staff/" + ot.getMaNhanVien();
				RestTemplate restTemplate = new RestTemplate();
				ThamGiaDuAn manager = restTemplate.getForObject(uri, ThamGiaDuAn.class);

				if (manager.getID() != null && manager.getMaTL().equals(approve.getId_lead())) {
					ot.setLyDoTuChoi(approve.getReason());
					if (approve.getAction().toUpperCase().equals("ACCEPT")) {
						ot.setTrangThai(1);
						ot.setLyDoTuChoi("");
					} else {
						ot.setTrangThai(2);
					}
					ot.setMaNguoiDuyet(approve.getId_lead());
					repoWFH.save(ot);
					result.add(ot);
				} else {
					ApiResponse<List<WFH_Reponse>> resp = new ApiResponse<>(1,
							"invalid input or Leader don't have permission", null);
					return new ResponseEntity<>(resp, HttpStatus.OK);
				}
			}
			List<WFH_Reponse> resp = new ArrayList<WFH_Reponse>();
			for (WFH i : result) {
				String uri = "https://gatewayteam07.herokuapp.com/api/staff_nghiphep/" + i.getMaNhanVien();
				RestTemplate restTemplate = new RestTemplate();
				User staff = restTemplate.getForObject(uri, User.class);
				WFH_Reponse temp = new WFH_Reponse(i, staff);
				resp.add(temp);
				System.out.println("qua dc add");
			}

			ApiResponse<List<WFH_Reponse>> resp1 = new ApiResponse<>(0, "Success", resp);
			return new ResponseEntity<>(resp1, HttpStatus.OK);
//			ApiResponse<List<OT>> resp = new ApiResponse<>(0, "Success", result);
//			return new ResponseEntity<>(resp, HttpStatus.OK);
		} catch (Exception e) {
			ApiResponse<List<WFH_Reponse>> resp = new ApiResponse<>(1, "Invalid OT id", null);
			return new ResponseEntity<>(resp, HttpStatus.CREATED);
		}
	}

	// team leader từ chối đơn yêu cầu .
//	@PutMapping("/reject_request_wfh")
//	public ResponseEntity<ApiResponse<WFH>> Reject_request_wfh(@RequestParam(value = "id", required = false) String id,
//			@RequestParam(value = "manguoiduyet", required = false) String manguoiduyet,
//			@RequestParam(value = "lydotuchoi", required = false) String lydotuchoi) {
//		try {
//			Query q = new Query();
//			q.addCriteria(Criteria.where("ID").is(id));
//			WFH wfh = mongoTemplate.findOne(q, WFH.class);
//			// kiểm tra xem mã người duyệt đơn này có phải là manager cấp 1 của nhân viên
//			// này hay không?
//			final String uri = "https://duanteam07.herokuapp.com/api/get_manager1_of_staff/" + wfh.getMaNhanVien();
//			System.out.println("api: " + uri);
//			RestTemplate restTemplate = new RestTemplate();
//			ThamGiaDuAn result = restTemplate.getForObject(uri, ThamGiaDuAn.class);
//			if (result != null && result.getMaTL().equals(manguoiduyet)) {
//				wfh.setTrangThai(2);
//				wfh.setMaNguoiDuyet(manguoiduyet);
//				wfh.setLyDoTuChoi(lydotuchoi);
//				ApiResponse<WFH> resp = new ApiResponse<WFH>(0, "Success", repoWFH.save(wfh));
//				// ApiResponse<OT> resp = new ApiResponse<OT>(0,"Success",repoOT.save(ot));
//				return new ResponseEntity<>(resp, HttpStatus.CREATED);
//			} else {
//				ApiResponse<WFH> resp = new ApiResponse<WFH>(1, "Order_id wrong or manager level 1 id wrong", null);
//				return new ResponseEntity<>(resp, HttpStatus.CREATED);
//			}
//
//		} catch (Exception e) {
//			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//	}
}
