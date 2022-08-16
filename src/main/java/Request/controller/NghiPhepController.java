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
import Request.model.NghiPhep;
import Request.model.NghiPhepResponse;
import Request.model.OT;
import Request.model.ThamGiaDuAn;
import Request.model.WFH;
import Request.repository.DuAnRepository;
import Request.repository.NghiPhepRepository;
import Request.repository.UserRepository;
import Request.repository.OTRepository;
import Request.repository.ThamGiaDuAnRepository;
import Request.repository.WFHRepository;

import java.text.ParseException;
import java.time.LocalDate;
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
				//return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			ApiResponse<List<NghiPhep>> resp = new ApiResponse<List<NghiPhep>>(0, "Success", wfhlst);
			return new ResponseEntity<>(resp, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	
	// lay ra danh sach tat ca yeu cau nghi phep thông qua trạng thái  
	@GetMapping("/get_all_list_request_nghiphep_by_status/{status}")
	public ResponseEntity<ApiResponse<List<NghiPhep>>> View_all_list_request_nghiphep_by_status(@PathVariable(value = "status") String status) {
		try {

			List<NghiPhep> wfhlst = new ArrayList<NghiPhep>();
			Query q = new Query();
			q.addCriteria(Criteria.where("TrangThai").is(status));

			wfhlst = mongoTemplate.find(q, NghiPhep.class);
			if (wfhlst.isEmpty()) {
				ApiResponse<List<NghiPhep>> resp = new ApiResponse<List<NghiPhep>>(1, "Status fomart wrong!", null);
				return new ResponseEntity<>(resp, HttpStatus.OK);
				//return new ResponseEntity<>(HttpStatus.NO_CONTENT);
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
				ApiResponse<List<NghiPhep>> resp = new ApiResponse<List<NghiPhep>>(1, "Id of this leave does not exist", null);
				return new ResponseEntity<>(resp, HttpStatus.OK);
				//return new ResponseEntity<>(HttpStatus.NO_CONTENT);
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
				ApiResponse<List<NghiPhep>> resp = new ApiResponse<List<NghiPhep>>(1, "Staff id wrong or staff haven't leave application", null);
				return new ResponseEntity<>(resp, HttpStatus.OK);
				//return new ResponseEntity<>(HttpStatus.NO_CONTENT);
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
	public ResponseEntity<ApiResponse<List<NghiPhep>>> Get_np_by_status(@PathVariable(value = "status") String status) {

		try {
			List<NghiPhep> wfhlst = new ArrayList<NghiPhep>();
			Query q = new Query();
			q.addCriteria(Criteria.where("TrangThai").is(status));
			wfhlst = mongoTemplate.find(q, NghiPhep.class);
			if (wfhlst.isEmpty()) {
				ApiResponse<List<NghiPhep>> resp = new ApiResponse<List<NghiPhep>>(1, "Wrong status format!", null);
				return new ResponseEntity<>(resp, HttpStatus.OK);
				//return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			ApiResponse<List<NghiPhep>> resp = new ApiResponse<List<NghiPhep>>(0, "Success", wfhlst);
			return new ResponseEntity<>(resp, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// lấy ra đơn yêu cầu nghỉ phép thuộc quyền của teamleader/PM xác định. vd: lấy
	// ra những đơn của teamleader có mald = ld001
//1 lấy ra những thằng nhân viên do 1 TL/PM quản lý 

//2 lấy ra những đơn nghỉ phép có số ngày < 3 và > 3 
//	@GetMapping("/get_list_dayoff_greater_than_3/{ngaybatdau}/{ngayketthuc}")
//	public ResponseEntity<ApiResponse<List<NghiPhep>>> Get_list_dayoff_greater_than_3(@PathVariable(value = "ngaybatdau") LocalDate ngaybatdau, @PathVariable(value = "ngayketthuc") LocalDate ngayketthuc) {
//		
	public long Caculatebetweentwoday(LocalDate startday, LocalDate enddate) {
		Period period = Period.between(startday, enddate);
		long daysDiff = Math.abs(period.getDays());
		return daysDiff;
	}

	@GetMapping("/get_list_dayoff_by_TLorPM")
	public ResponseEntity<ApiResponse<NghiPhep>> Get_list_dayoff_by_TLorPM() {
		try {
			List<NghiPhep> nplst = new ArrayList<NghiPhep>();
			// repoNP.findAll().forEach(wfhlst::add);

			// System.out.println(wfhlst.getClass().getName());
			// List<NghiPhep> list_nghiphep = new ArrayList<NghiPhep>();
			Query q = new Query();
			q.addCriteria(Criteria.where("TrangThai").is("Pending"));
			nplst = mongoTemplate.find(q, NghiPhep.class);

			if (nplst.isEmpty() == false) {
				for (NghiPhep i : nplst) {
					System.out.println("start day: " + i.getNgayBatDau().minusDays(1));
					System.out.println("end day: " + i.getNgayKetThuc().minusDays(1));

					if (Caculatebetweentwoday(i.getNgayBatDau(), i.getNgayKetThuc()) > 3) {
						// System.out.println(i.getClass().getName());
						// System.out.println("day: " + i.getNgayKetThuc());
//						i.getNgayBatDau().minusDays(1);
//						i.getNgayKetThuc().minusDays(1);

						ApiResponse<NghiPhep> resp = new ApiResponse<NghiPhep>(0, "Success", i);
						// return new ResponseEntity<>(resp, HttpStatus.OK);
						return new ResponseEntity<>(resp, HttpStatus.CREATED);
					} else {

					}
				}
			}
			ApiResponse<NghiPhep> resp = new ApiResponse<NghiPhep>(0, "No infor", null);
			return new ResponseEntity<>(resp, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// // nhân viên yêu cầu nghỉ phép
	@PostMapping("/request_nghiphep")
	public ResponseEntity<ApiResponse<NghiPhep>> Request_nghiphep(@RequestBody NghiPhep np) {
		try {
			List<NghiPhep> wfhlst = new ArrayList<NghiPhep>();
			Query q = new Query();
			// q.addCriteria(Criteria.where("MaNhanVien").is(wfh.getMaNhanVien()));
			q.addCriteria(Criteria.where("MaNhanVien").is(np.getMaNhanVien()))
					.addCriteria(Criteria.where("TrangThai").is("0"));
			wfhlst = mongoTemplate.find(q, NghiPhep.class);
			System.out.println("rong hay k tren: " + wfhlst.isEmpty());
			
			//goi service user de lay infor nhanvien 
			final String uri = "https://userteam07.herokuapp.com/api/staff_nghiphep/"+ np.getMaNhanVien();
			RestTemplate restTemplate = new RestTemplate();
			User result = restTemplate.getForObject(uri, User.class);
			
			System.out.println("uri: " + uri);
			System.out.println("result: " + result);
			System.out.println("rong hay k: " + wfhlst.isEmpty());
			System.out.println("so phep con lai: " + result.getSoPhepConLai());
			

			System.out.println(result.getID());
			System.out.println(wfhlst.isEmpty());
			if (wfhlst.isEmpty() == true && result.getSoPhepConLai() >= 1) {// can them dieu kien la so php con lai phai >=1
				System.out.println("khong co thang nhan vien nay");
				np.setID(UUID.randomUUID().toString());
				NghiPhep _np = repoNP.save(new NghiPhep(np.getID(), "", np.getMaNhanVien(), np.getLoaiNghiPhep(),
						np.getNgayBatDau().plusDays(1), np.getNgayKetThuc().plusDays(1), np.getLyDo(), "", 1));
				ApiResponse<NghiPhep> resp = new ApiResponse<NghiPhep>(0, "Success", _np);
				return new ResponseEntity<>(resp, HttpStatus.CREATED);
			}
			ApiResponse<NghiPhep> resp = new ApiResponse<NghiPhep>(1, "Can't request because you have petition", null);
			return new ResponseEntity<>(resp, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	@PutMapping("/approve_request_nghiphep")
	public ResponseEntity<ApiResponse<NghiPhep>> Approve_request_nghiphep(
			@RequestParam(value = "id", required = false) String id,
			@RequestParam(value = "manguoiduyet", required = false) String manguoiduyet) {
		try {
			// duyệt table nghỉ phép theo id
			Query q = new Query();
			q.addCriteria(Criteria.where("ID").is(id));
			NghiPhep np = mongoTemplate.findOne(q, NghiPhep.class);
			
			//goi service user de lay infor nhanvien 
			final String uri1 = "https://userteam07.herokuapp.com/api/staff_nghiphep/"+ np.getMaNhanVien();
			RestTemplate restTemplate1 = new RestTemplate();
			User result_staff = restTemplate1.getForObject(uri1, User.class);

			System.out.println("trang thai: " + np.getTrangThai());
			System.out.println("So Phep con lai: " + result_staff.getSoPhepConLai());
			System.out.println(np.getTrangThai() == 0);
			if (np.getTrangThai() == 0) { // kiểm tra xem đơn nghỉ phép có trạng thái là pending
				System.out.println(
						"so ngay nghi phep: " + Caculatebetweentwoday(np.getNgayBatDau(), np.getNgayKetThuc()));

				if (Caculatebetweentwoday(np.getNgayBatDau(), np.getNgayKetThuc()) > 3) {
					// kiểm tra xem mã người duyệt đơn này có phải là manager cấp 2 của nhân viên
					// này hay không?

					final String uri = "https://duanteam07.herokuapp.com/api/get_manager2_of_staff/"
							+ np.getMaNhanVien();
					System.out.println("api: " + uri);
					RestTemplate restTemplate2 = new RestTemplate();
					DuAn result = restTemplate2.getForObject(uri, DuAn.class);

					System.out.println("result: " + result);

					System.out.println("mapm api : " + result.getMaPM());
					System.out.println("mapm input: " + manguoiduyet);
					if (result != null && result.getMaPM().equals(manguoiduyet)) { // kiểm tra xem data trả về từ api
																					// tìm manager cấp 2 theo id_staff
																					// có tồn tại k và mã người duyệt
																					// đúng là manager cấp 2 của staff
																					// đó hay k?
						// result.getMaTL().equals(manguoiduyet)
						np.setTrangThai(1);
						np.setMaNguoiDuyet(manguoiduyet);
						result_staff.setSoPhepConLai(result_staff.getSoPhepConLai() - 1); // cập nhật lại số ngày nghỉ phép còn lại
						ApiResponse<NghiPhep> resp = new ApiResponse<NghiPhep>(0, "Success", repoNP.save(np));
						// goi lai service user de cap nhat lai so phep con lai
						final String uri3 = "https://userteam07.herokuapp.com/api/update_sophepconlai/" + result_staff.getID();
						System.out.println("api update: " + uri3);
						RestTemplate restTemplate3 = new RestTemplate();					
						User _nv = new User(result_staff.getID(), result_staff.getHoTen(), result_staff.getUserName(), result_staff.getPassWord(), result_staff.getGioiTinh(), result_staff.getChucVu(),
								result_staff.getDiaChi(), result_staff.getEmail(), result_staff.getSDT(), result_staff.getAvatar());
						User result3 = restTemplate3.postForObject(uri3, _nv, User.class);
						return new ResponseEntity<>(resp, HttpStatus.CREATED);

					}
				} else if (Caculatebetweentwoday(np.getNgayBatDau(), np.getNgayKetThuc()) <= 3) {
					// kiểm tra xem mã người duyệt đơn này có phải là manager cấp 1 của nhân viên
					// này hay không?

					final String uri = "https://duanteam07.herokuapp.com/api/get_manager1_of_staff/"
							+ np.getMaNhanVien();
					System.out.println("api: " + uri);
					RestTemplate restTemplate = new RestTemplate();
					ThamGiaDuAn result = restTemplate.getForObject(uri, ThamGiaDuAn.class);
					System.out.println("mapm api: " + result.getMaTL());
					System.out.println("mapm input: " + manguoiduyet);
					if (result != null && result.getMaTL().equals(manguoiduyet)) { // kiểm tra xem data trả về từ api
																					// tìm
																					// manager cấp 1 theo id_staff có
																					// tồn
																					// tại k và mã người duyệt đúng là
																				// manager cấp 1 của staff đó hay k?
						np.setTrangThai(1);
						np.setMaNguoiDuyet(manguoiduyet);
						//result_staff.setSoPhepConLai(result_staff.getSoPhepConLai() - 1); // cập nhật lại số ngày nghỉ phép còn lại
						ApiResponse<NghiPhep> resp = new ApiResponse<NghiPhep>(0, "Success", repoNP.save(np));
						System.out.println("so phep sau khi tru: " + result_staff.getSoPhepConLai());
						// goi lai service user de cap nhat lai so phep con lai
						final String uri3 = "https://userteam07.herokuapp.com/api/update_sophepconlai/" + result_staff.getID();
						System.out.println("api update: " + uri3);
						RestTemplate restTemplate3 = new RestTemplate();					
						User _nv = new User(result_staff.getID(), result_staff.getHoTen(), result_staff.getUserName(), result_staff.getPassWord(), result_staff.getGioiTinh(), result_staff.getChucVu(),
								result_staff.getDiaChi(), result_staff.getEmail(), result_staff.getSDT(), result_staff.getAvatar());
						User result3 = restTemplate3.postForObject(uri3, _nv, User.class);
						//ApiResponse<User> resp1 = new ApiResponse<User>(0, "Success", repoUser.save(result_staff));
						//new ResponseEntity<>(resp1, HttpStatus.CREATED);
						// ApiResponse<OT> resp = new ApiResponse<OT>(0,"Success",repoOT.save(ot));
						return new ResponseEntity<>(resp, HttpStatus.CREATED);

					}

				}

			}

			System.out.println("Trang thai nghi phep sau khi cap nhat: " + np.getTrangThai());

			ApiResponse<NghiPhep> resp = new ApiResponse<NghiPhep>(1, "Order id wrong or id manager wrong!", null);

			return new ResponseEntity<>(resp, HttpStatus.CREATED);
		} catch (

		Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// tu choi don nghi phep
	@PutMapping("/reject_request_nghiphep")
	public ResponseEntity<ApiResponse<NghiPhep>> Reject_request_nghiphep(
			@RequestParam(value = "id", required = false) String id,
			@RequestParam(value = "manguoiduyet", required = false) String manguoiduyet,
			@RequestParam(value = "lydotuchoi", required = false) String lydotuchoi) {

		try {
			// duyệt table nghỉ phép theo id
			Query q = new Query();
			q.addCriteria(Criteria.where("ID").is(id));
			NghiPhep np = mongoTemplate.findOne(q, NghiPhep.class);

			
			//goi service user de lay infor nhanvien 
			final String uri1 = "https://userteam07.herokuapp.com/api/staff_nghiphep/"+ np.getMaNhanVien();
			RestTemplate restTemplate1 = new RestTemplate();
			User result_staff = restTemplate1.getForObject(uri1, User.class);

			
			System.out.println("trang thai: " + np.getTrangThai());
			System.out.println("So Phep con lai: " + result_staff.getSoPhepConLai());
			System.out.println(np.getTrangThai() == 0);
			if (np.getTrangThai() == 0) { // kiểm tra xem đơn nghỉ phép có trạng thái là pending
				System.out.println("so ngay nghi phep: " + Caculatebetweentwoday(np.getNgayBatDau(), np.getNgayKetThuc()));

				if (Caculatebetweentwoday(np.getNgayBatDau(), np.getNgayKetThuc()) > 3) {
					// kiểm tra xem mã người duyệt đơn này có phải là manager cấp 2 của nhân viên
					// này hay không?

					final String uri = "https://duanteam07.herokuapp.com/api/get_manager2_of_staff/" + np.getMaNhanVien();
					System.out.println("api: " + uri);
					RestTemplate restTemplate = new RestTemplate();
					DuAn result = restTemplate.getForObject(uri, DuAn.class);
					System.out.println("result: " + result);
					System.out.println("mapm api sau khi sua : " + result.getMaPM());
					System.out.println("mapm input: " + manguoiduyet);
					if (result != null && result.getMaPM().equals(manguoiduyet)) { // kiểm tra xem data trả về từ api
																					// tìm manager cấp 2 theo id_staff
																					// có tồn tại k và mã người duyệt
																					// đúng là manager cấp 2 của staff
																					// đó hay k?
						// result.getMaTL().equals(manguoiduyet)
						np.setTrangThai(2);
						np.setMaNguoiDuyet(manguoiduyet);
						np.setLyDoTuChoi(lydotuchoi);
						ApiResponse<NghiPhep> resp = new ApiResponse<NghiPhep>(0, "Success", repoNP.save(np));
						
						
						return new ResponseEntity<>(resp, HttpStatus.CREATED);

					}
				} else if (Caculatebetweentwoday(np.getNgayBatDau(), np.getNgayKetThuc()) <= 3) {
					// kiểm tra xem mã người duyệt đơn này có phải là manager cấp 1 của nhân viên
					// này hay không?

					final String uri = "https://duanteam07.herokuapp.com/api/get_manager1_of_staff/"+ np.getMaNhanVien();
					System.out.println("api: " + uri);
					RestTemplate restTemplate = new RestTemplate();
					ThamGiaDuAn result = restTemplate.getForObject(uri, ThamGiaDuAn.class);
					System.out.println("mapm api: " + result.getMaTL());
					System.out.println("mapm input: " + manguoiduyet);
					if (result != null && result.getMaTL().equals(manguoiduyet)) { // kiểm tra xem data trả về từ api
																					// tìm
																					// manager cấp 1 theo id_staff có
																					// tồn
																					// tại k và mã người duyệt đúng là
																					// manager cấp 1 của staff đó hay k?
						np.setTrangThai(2);
						np.setMaNguoiDuyet(manguoiduyet);
						np.setLyDoTuChoi(lydotuchoi);
						ApiResponse<NghiPhep> resp = new ApiResponse<NghiPhep>(0, "Success", repoNP.save(np));
						
						return new ResponseEntity<>(resp, HttpStatus.CREATED);

					}

				}

			}

			System.out.println("Trang thai nghi phep sau khi cap nhat: " + np.getTrangThai());

			ApiResponse<NghiPhep> resp = new ApiResponse<NghiPhep>(1, "Order id wrong or id manager wrong!", null);

			return new ResponseEntity<>(resp, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// nhan vien xem lai lich su nghi phep va so phep con lai trong nam

	@GetMapping("/get_np_history/{MaNV_input}")
	public ResponseEntity<ApiResponse<NghiPhepResponse>> Get_np_history(
			@PathVariable(value = "MaNV_input") String MaNV_input) {
		try {
			System.out.println(MaNV_input);

			List<NghiPhep> wfhlst = new ArrayList<NghiPhep>();
			Query q = new Query();
			q.addCriteria(Criteria.where("MaNhanVien").is(MaNV_input));
			wfhlst = mongoTemplate.find(q, NghiPhep.class);
			
			//goi service user de lay infor nhanvien 
			final String uri1 = "https://userteam07.herokuapp.com/api/staff_nghiphep/"+ MaNV_input;
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
