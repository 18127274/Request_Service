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
import Request.model.NghiPhep;
import Request.model.User;
import Request.model.NhanVien;
import Request.model.OT;
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
	public ResponseEntity<ApiResponse<List<YeuCauThietBi>>> get_all_list_request_yeucauthietbi_of_staff(@PathVariable(value = "MaNV_input") String MaNV_input) {
		try {
			List<YeuCauThietBi> wfhlst = new ArrayList<YeuCauThietBi>();
			Query q = new Query();
			q.addCriteria(Criteria.where("MaNhanVien").is(MaNV_input));

			wfhlst = mongoTemplate.find(q, YeuCauThietBi.class);
			if (wfhlst.isEmpty()) {
				ApiResponse<List<YeuCauThietBi>> resp = new ApiResponse<List<YeuCauThietBi>>(1, "Request is empty!", null);
				return new ResponseEntity<>(resp, HttpStatus.OK);
				// return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			ApiResponse<List<YeuCauThietBi>> resp = new ApiResponse<List<YeuCauThietBi>>(0, "Success", wfhlst);
			return new ResponseEntity<>(resp, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
//
//	// lấy ra đơn yêu cầu wfh thông qua mã đơn.
//	@GetMapping("/get_wfh_id/{MaWFH_input}")
//	public ResponseEntity<ApiResponse<List<YeuCauThietBi>>> Get_wfh_id(@PathVariable(value = "MaWFH_input") String MaWFH_input) {
//
//		try {
//			List<YeuCauThietBi> wfhlst = new ArrayList<YeuCauThietBi>();
//			Query q = new Query();
//			q.addCriteria(Criteria.where("ID").is(MaWFH_input));
//			wfhlst = mongoTemplate.find(q, YeuCauThietBi.class);
//			if (wfhlst.isEmpty()) {
//				ApiResponse<List<YeuCauThietBi>> resp = new ApiResponse<List<YeuCauThietBi>>(1, "Id wfh wrong or this order_wfh is not available!", null);
//				return new ResponseEntity<>(resp, HttpStatus.OK);
//			}
//			ApiResponse<List<YeuCauThietBi>> resp = new ApiResponse<List<YeuCauThietBi>>(0, "Success", wfhlst);
//			return new ResponseEntity<>(resp, HttpStatus.OK);
//		} catch (Exception e) {
//			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//	}

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
				ApiResponse<List<YeuCauThietBi>> resp = new ApiResponse<List<YeuCauThietBi>>(1, "Id staff wrong or this device order is not available!", null);
				return new ResponseEntity<>(resp, HttpStatus.OK);
			}
			ApiResponse<List<YeuCauThietBi>> resp = new ApiResponse<List<YeuCauThietBi>>(0, "Success", wfhlst);
			return new ResponseEntity<>(resp, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// lấy ra đơn yêu cầu wfh theo trangthai (trangthai = 0: chờ xét duyệt, 1: đã
	// xét duyệt, 2: từ chối).
	@GetMapping("/get_yctb_by_status/{status}")
	public ResponseEntity<ApiResponse<List<YeuCauThietBi>>> Get_yctb_by_status(@PathVariable(value = "status") String status) {

		try {
			List<YeuCauThietBi> wfhlst = new ArrayList<YeuCauThietBi>();
			Query q = new Query();
			q.addCriteria(Criteria.where("TrangThai").is(status));
			wfhlst = mongoTemplate.find(q, YeuCauThietBi.class);
			if (wfhlst.isEmpty()) {
				ApiResponse<List<YeuCauThietBi>> resp = new ApiResponse<List<YeuCauThietBi>>(1, "Status format wrong or device order does not exist!", null);
				return new ResponseEntity<>(resp, HttpStatus.OK);
			}
			ApiResponse<List<YeuCauThietBi>> resp = new ApiResponse<List<YeuCauThietBi>>(0, "Success", wfhlst);
			return new ResponseEntity<>(resp, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
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
			
			//q.addCriteria(Criteria.where("MaNhanVien").is(wfh.getMaNhanVien())).addCriteria(Criteria.where("TrangThai").is("IT Department is reviewing"));
			q.addCriteria(Criteria.where("MaNhanVien").is(wfh.getMaNhanVien())).addCriteria(Criteria.where("").orOperator(Criteria.where("TrangThai").is(1),Criteria.where("TrangThai").is(4), Criteria.where("TrangThai").is(5), Criteria.where("TrangThai").is(3)));
			//criteria.orOperator(Criteria.where("A").is(10),Criteria.where("B").is(20));
			wfhlst = mongoTemplate.find(q, YeuCauThietBi.class);

			System.out.println(wfh.getMaNhanVien());
			System.out.println(wfhlst.isEmpty());
			if (wfhlst.isEmpty() == true) {
				System.out.println("khong co thang nhan vien nay");
				wfh.setID(UUID.randomUUID().toString());
				YeuCauThietBi _wfh = repoYCTB.save(new YeuCauThietBi(wfh.getID(), wfh.getMaNhanVien(), wfh.getMoTa(), wfh.getChiPhi(), wfh.getSoLuong(), "" , "", 1));
				ApiResponse<YeuCauThietBi> resp = new ApiResponse<YeuCauThietBi>(0, "Success", _wfh);
				return new ResponseEntity<>(resp, HttpStatus.CREATED);
			}
			ApiResponse<YeuCauThietBi> resp = new ApiResponse<YeuCauThietBi>(0, "Can't request because you have petition", null);
			return new ResponseEntity<>(resp, HttpStatus.CREATED);
		}

		catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

//	public long Find(LocalDate startday, LocalDate enddate) {
//		Period period = Period.between(startday, enddate);
//		long daysDiff = Math.abs(period.getDays());
//		System.out.println(" The number of days between dates: " + daysDiff);
//		return daysDiff;
//	}

	// các phòng như phòng it, cấp quản lý, ban giám đốc, phòng kế toán thực hiện xem xét đơn yêu cầu.
	@PutMapping("/approve_request_yctb")
	public ResponseEntity<ApiResponse<YeuCauThietBi>> Approve_request_yctb(@RequestParam(value = "id", required = false) String id,
			@RequestParam(value = "manguoiduyet", required = false) String manguoiduyet) {

		try {
			Query q = new Query();
			q.addCriteria(Criteria.where("ID").is(id));
			YeuCauThietBi wfh = mongoTemplate.findOne(q, YeuCauThietBi.class);
			// kiểm tra xem mã người duyệt đơn này có phải là manager cấp 1 của nhân viên
			// này hay không?
			final String uri = "https://duanteam07.herokuapp.com/api/get_manager1_of_staff/" + wfh.getMaNhanVien();
			System.out.println("api: " + uri);
			RestTemplate restTemplate = new RestTemplate();
			ThamGiaDuAn result = restTemplate.getForObject(uri, ThamGiaDuAn.class);
			System.out.println(result);
			System.out.println(result.getMaTL());
			System.out.println(manguoiduyet);
			
			// kiểm tra xem chức vụ của người duyệt đơn này, (phòng it: 1, ban giám đốc: 5, phòng kế toán: 3)
			final String uri1 = "https://userteam07.herokuapp.com/api/staff_nghiphep/" + manguoiduyet;
			System.out.println("api: " + uri1);
			RestTemplate restTemplate1 = new RestTemplate();
			User result1 = restTemplate.getForObject(uri1, User.class);
			
			System.out.println(result1.getChucVu());
			
			
			if(wfh.getTrangThai() == 1 && result1.getChucVu() == 1) {
				wfh.setTrangThai(4);
				wfh.setMaNguoiDuyet(manguoiduyet);
				ApiResponse<YeuCauThietBi> resp = new ApiResponse<YeuCauThietBi>(0, "Success", repoYCTB.save(wfh));
				// ApiResponse<OT> resp = new ApiResponse<OT>(0,"Success",repoOT.save(ot));
				return new ResponseEntity<>(resp, HttpStatus.CREATED);
			}
			else if(wfh.getTrangThai() == 4 && result.getMaTL().equals(manguoiduyet)) {
				wfh.setTrangThai(5);
				wfh.setMaNguoiDuyet(manguoiduyet);
				ApiResponse<YeuCauThietBi> resp = new ApiResponse<YeuCauThietBi>(0, "Success", repoYCTB.save(wfh));
				// ApiResponse<OT> resp = new ApiResponse<OT>(0,"Success",repoOT.save(ot));
				return new ResponseEntity<>(resp, HttpStatus.CREATED);
			}
			else if(wfh.getTrangThai() == 5  && result1.getChucVu() == 5 ) {
				wfh.setTrangThai(3);
				wfh.setMaNguoiDuyet(manguoiduyet);
				ApiResponse<YeuCauThietBi> resp = new ApiResponse<YeuCauThietBi>(0, "Success", repoYCTB.save(wfh));
				// ApiResponse<OT> resp = new ApiResponse<OT>(0,"Success",repoOT.save(ot));
				return new ResponseEntity<>(resp, HttpStatus.CREATED);
			}
			else if(wfh.getTrangThai() == 3 && result1.getChucVu() == 3) {
				wfh.setTrangThai(2);
				wfh.setMaNguoiDuyet(manguoiduyet);
				ApiResponse<YeuCauThietBi> resp = new ApiResponse<YeuCauThietBi>(0, "Success", repoYCTB.save(wfh));
				// ApiResponse<OT> resp = new ApiResponse<OT>(0,"Success",repoOT.save(ot));
				return new ResponseEntity<>(resp, HttpStatus.CREATED);
			}
			else {
				ApiResponse<YeuCauThietBi> resp = new ApiResponse<YeuCauThietBi>(1, "Order_id wrong or Reviewer_id not valid", null);
				return new ResponseEntity<>(resp, HttpStatus.CREATED);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// team leader từ chối đơn yêu cầu .
	@PutMapping("/reject_request_yctb")
	public ResponseEntity<ApiResponse<YeuCauThietBi>> Reject_request_yctb(@RequestParam(value = "id", required = false) String id,
			@RequestParam(value = "manguoiduyet", required = false) String manguoiduyet,
			@RequestParam(value = "lydotuchoi", required = false) String lydotuchoi) {
		try {
			Query q = new Query();
			q.addCriteria(Criteria.where("ID").is(id));
			YeuCauThietBi wfh = mongoTemplate.findOne(q, YeuCauThietBi.class);
			// kiểm tra xem mã người duyệt đơn này có phải là manager cấp 1 của nhân viên
			// này hay không?
			final String uri = "https://duanteam07.herokuapp.com/api/get_manager1_of_staff/" + wfh.getMaNhanVien();
			System.out.println("api: " + uri);
			RestTemplate restTemplate = new RestTemplate();
			ThamGiaDuAn result = restTemplate.getForObject(uri, ThamGiaDuAn.class);
			
			// kiểm tra xem chức vụ của người duyệt đơn này, (phòng it: 1, ban giám đốc: 5, phòng kế toán: 3)
			final String uri1 = "https://userteam07.herokuapp.com/api/staff_nghiphep/" + manguoiduyet;
			System.out.println("api: " + uri1);
			RestTemplate restTemplate1 = new RestTemplate();
			User result1 = restTemplate.getForObject(uri1, User.class);
			
			System.out.println(result1.getChucVu());
			System.out.println(result.getMaTL());
			System.out.println(wfh.getTrangThai());
			
			//Xét các trường hợp các phòng ban duyệt đơn
			if(wfh.getTrangThai() == 1  && result1.getChucVu() == 1) {
				wfh.setTrangThai(0);
				wfh.setMaNguoiDuyet(manguoiduyet);
				wfh.setLyDoTuChoi(lydotuchoi);
				ApiResponse<YeuCauThietBi> resp = new ApiResponse<YeuCauThietBi>(0, "Success", repoYCTB.save(wfh));
				// ApiResponse<OT> resp = new ApiResponse<OT>(0,"Success",repoOT.save(ot));
				return new ResponseEntity<>(resp, HttpStatus.CREATED);
			}
			else if(wfh.getTrangThai() == 4 && result.getMaTL().equals(manguoiduyet)) {
				wfh.setTrangThai(0);
				wfh.setMaNguoiDuyet(manguoiduyet);
				wfh.setLyDoTuChoi(lydotuchoi);
				ApiResponse<YeuCauThietBi> resp = new ApiResponse<YeuCauThietBi>(0, "Success", repoYCTB.save(wfh));
				// ApiResponse<OT> resp = new ApiResponse<OT>(0,"Success",repoOT.save(ot));
				return new ResponseEntity<>(resp, HttpStatus.CREATED);
			}
			else if(wfh.getTrangThai() == 5  && result1.getChucVu() == 5) {
				wfh.setTrangThai(0);
				wfh.setMaNguoiDuyet(manguoiduyet);
				ApiResponse<YeuCauThietBi> resp = new ApiResponse<YeuCauThietBi>(0, "Success", repoYCTB.save(wfh));
				// ApiResponse<OT> resp = new ApiResponse<OT>(0,"Success",repoOT.save(ot));
				return new ResponseEntity<>(resp, HttpStatus.CREATED);
			}
			else if(wfh.getTrangThai() == 3  && result1.getChucVu() == 3) {
				wfh.setTrangThai(0);
				wfh.setMaNguoiDuyet(manguoiduyet);
				wfh.setLyDoTuChoi(lydotuchoi);
				ApiResponse<YeuCauThietBi> resp = new ApiResponse<YeuCauThietBi>(0, "Success", repoYCTB.save(wfh));
				// ApiResponse<OT> resp = new ApiResponse<OT>(0,"Success",repoOT.save(ot));
				return new ResponseEntity<>(resp, HttpStatus.CREATED);
			}
			else {
				ApiResponse<YeuCauThietBi> resp = new ApiResponse<YeuCauThietBi>(1, "Order_id wrong or Reviewer_id not valid", null);
				return new ResponseEntity<>(resp, HttpStatus.CREATED);
			}

		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
