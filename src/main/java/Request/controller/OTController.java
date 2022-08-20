package Request.controller;

import java.time.LocalDate;
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
import Request.model.List_Staff;
import Request.model.OT;
import Request.model.OT_Response;
import Request.model.ThamGiaDuAn;
import Request.model.User;
import Request.model.WFH;
import Request.model.approve_ot;
import Request.model.List_ThamGiaDuAn;
import Request.repository.NghiPhepRepository;
import Request.repository.NhanVienRepository;
import Request.repository.OTRepository;
import Request.repository.WFHRepository;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/api")

public class OTController {
	@Autowired
	NhanVienRepository repoNV;
	@Autowired
	WFHRepository repoWFH;
	@Autowired
	NghiPhepRepository repoNP;
	@Autowired
	OTRepository repoOT;
	@Autowired
	MongoTemplate mongoTemplate;
	@Autowired
	MongoOperations mongoOperation;

	@GetMapping("/get_ot_id/{MaOT_input}")
	public ResponseEntity<ApiResponse<List<OT_Response>>> XemDSOT_ID(
			@PathVariable(value = "MaOT_input") String MaOT_input) {

		try {
			List<OT> otlst = new ArrayList<OT>();
			Query q = new Query();
			q.addCriteria(Criteria.where("ID").is(MaOT_input));
			otlst = mongoTemplate.find(q, OT.class);
			if (otlst.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			List<OT_Response> resp = new ArrayList<OT_Response>();
			for (OT i : otlst) {
				String uri = "https://gatewayteam07.herokuapp.com/api/staff_nghiphep/" + i.getMaNhanVien();
				RestTemplate restTemplate = new RestTemplate();
				User staff = restTemplate.getForObject(uri, User.class);
				OT_Response temp = new OT_Response(i, staff);
				resp.add(temp);
			}
			ApiResponse<List<OT_Response>> resp1 = new ApiResponse<>(0, "Success", resp);
			return new ResponseEntity<>(resp1, HttpStatus.OK);
		} catch (Exception e) {
			ApiResponse<List<OT_Response>> resp = new ApiResponse<>(1, "Internal error", null);
			return new ResponseEntity<>(resp, HttpStatus.CREATED);
		}
	}

	@GetMapping("/get_all_list_request_ot_of_staff/{MaNV_input}")
	public ResponseEntity<ApiResponse<List<OT>>> Get_all_list_request_ot_of_staff(
			@PathVariable(value = "MaNV_input") String MaNV_input) {
		try {
			List<OT> wfhlst = new ArrayList<OT>();
			Query q = new Query();
			q.addCriteria(Criteria.where("MaNhanVien").is(MaNV_input));

			wfhlst = mongoTemplate.find(q, OT.class);
			if (wfhlst.isEmpty()) {
				ApiResponse<List<OT>> resp = new ApiResponse<List<OT>>(1, "Request is empty!", null);
				return new ResponseEntity<>(resp, HttpStatus.OK);
				// return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			ApiResponse<List<OT>> resp = new ApiResponse<List<OT>>(0, "Success", wfhlst);
			return new ResponseEntity<>(resp, HttpStatus.OK);
		} catch (Exception e) {
			ApiResponse<List<OT>> resp1 = new ApiResponse<>(1, "Failure!", null);
			return new ResponseEntity<>(resp1, HttpStatus.OK);
		}
	}

	@GetMapping("/unaccepted_ot")
	public ResponseEntity<ApiResponse<List<OT_Response>>> XemDSOTChuaDuyet() {
		try {
			List<OT> otlst = new ArrayList<OT>();
			Query q = new Query();
			q.addCriteria(Criteria.where("TrangThai").is(0));
			otlst = mongoTemplate.find(q, OT.class);

			if (otlst.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			List<OT_Response> resp = new ArrayList<OT_Response>();
			for (OT i : otlst) {
				String uri = "https://gatewayteam07.herokuapp.com/api/staff_nghiphep/" + i.getMaNhanVien();
				RestTemplate restTemplate = new RestTemplate();
				User staff = restTemplate.getForObject(uri, User.class);
				OT_Response temp = new OT_Response(i, staff);
				resp.add(temp);
			}

			ApiResponse<List<OT_Response>> resp1 = new ApiResponse<>(0, "Success", resp);
			return new ResponseEntity<>(resp1, HttpStatus.OK);
		} catch (Exception e) {
			ApiResponse<List<OT_Response>> resp = new ApiResponse<>(1, "Internal error", null);
			return new ResponseEntity<>(resp, HttpStatus.CREATED);
		}
	}

	@GetMapping("/get_ot_nv/{MaNV_input}")
	public ResponseEntity<ApiResponse<List<OT_Response>>> XemDSOT_MaNV(
			@PathVariable(value = "MaNV_input") String MaNV_input) {

		try {
			List<OT> otlst = new ArrayList<OT>();
			Query q = new Query();
			q.addCriteria(Criteria.where("MaNhanVien").is(MaNV_input));
			otlst = mongoTemplate.find(q, OT.class);
			if (otlst.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			List<OT_Response> resp = new ArrayList<OT_Response>();
			for (OT i : otlst) {
				String uri = "https://gatewayteam07.herokuapp.com/api/staff_nghiphep/" + i.getMaNhanVien();
				RestTemplate restTemplate = new RestTemplate();
				User staff = restTemplate.getForObject(uri, User.class);
				OT_Response temp = new OT_Response(i, staff);
				resp.add(temp);
			}

			ApiResponse<List<OT_Response>> resp1 = new ApiResponse<>(0, "Success", resp);
			return new ResponseEntity<>(resp1, HttpStatus.OK);
		} catch (Exception e) {
			ApiResponse<List<OT_Response>> resp = new ApiResponse<>(1, "Internal error", null);
			return new ResponseEntity<>(resp, HttpStatus.CREATED);
		}
	}

	@PutMapping("/approve_ot")
	public ResponseEntity<ApiResponse<List<OT_Response>>> approve_ot(@RequestBody approve_ot approve
//			@RequestParam("MaOT_input") String MaOT_input,
//			@RequestParam("TinhTrang_input") String TinhTrang_input,
//			@RequestParam("LyDoTuChoi_input") String LyDoTuChoi_input,
//			@RequestParam("MaNguoiDuyet_input") String MaNguoiDuyet_input
	) {
		try {
			List<OT> result = new ArrayList<OT>();
			if (!approve.getAction().toUpperCase().equals("ACCEPT")
					&& !(approve.getAction().toUpperCase().equals("REJECT"))) {
				ApiResponse<List<OT_Response>> resp = new ApiResponse<>(1, "Action only accept or reject", null);
				return new ResponseEntity<>(resp, HttpStatus.CREATED);
			}
			if (approve.getAction().toUpperCase().equals("REJECT")
					&& (approve.getReason() == null || approve.getReason().equals(""))) {
				ApiResponse<List<OT_Response>> resp = new ApiResponse<>(1, "Invalid reason", null);
				return new ResponseEntity<>(resp, HttpStatus.CREATED);
			}
//			 || (approve.getAction().equals("REJECT") && (approve.getReason().equals("") || approve.getReason() == null
			for (String request_id : approve.getList_id_request()) {
				// Find request
				Query q = new Query();
				q.addCriteria(Criteria.where("ID").is(request_id));
				OT ot = mongoTemplate.findOne(q, OT.class);
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
					repoOT.save(ot);
					result.add(ot);
				} else {
					ApiResponse<List<OT_Response>> resp = new ApiResponse<>(1,
							"invalid input or Leader don't have permission", null);
					return new ResponseEntity<>(resp, HttpStatus.OK);
				}
			}
			List<OT_Response> resp = new ArrayList<OT_Response>();
			for (OT i : result) {
				String uri = "https://gatewayteam07.herokuapp.com/api/staff_nghiphep/" + i.getMaNhanVien();
				RestTemplate restTemplate = new RestTemplate();
				User staff = restTemplate.getForObject(uri, User.class);
				OT_Response temp = new OT_Response(i, staff);
				resp.add(temp);
			}

			ApiResponse<List<OT_Response>> resp1 = new ApiResponse<>(0, "Success", resp);
			return new ResponseEntity<>(resp1, HttpStatus.OK);
//			ApiResponse<List<OT>> resp = new ApiResponse<>(0, "Success", result);
//			return new ResponseEntity<>(resp, HttpStatus.OK);
		} catch (Exception e) {
			ApiResponse<List<OT_Response>> resp = new ApiResponse<>(1, "Invalid OT id", null);
			return new ResponseEntity<>(resp, HttpStatus.CREATED);
		}
	}

	@PutMapping("/accept_ot")
	public ResponseEntity<ApiResponse<OT>> accept_ot(@RequestBody OT accept
//			@RequestParam("MaOT_input") String MaOT_input,
//			@RequestParam("TinhTrang_input") String TinhTrang_input,
//			@RequestParam("LyDoTuChoi_input") String LyDoTuChoi_input,
//			@RequestParam("MaNguoiDuyet_input") String MaNguoiDuyet_input
	) {
		try {
			Query q = new Query();
			q.addCriteria(Criteria.where("ID").is(accept.getID()));
			OT ot = mongoTemplate.findOne(q, OT.class);

			String uri = "https://gatewayteam07.herokuapp.com/api/get_manager1_of_staff/" + ot.getMaNhanVien();
			RestTemplate restTemplate = new RestTemplate();
			ThamGiaDuAn manager = restTemplate.getForObject(uri, ThamGiaDuAn.class);

			if (manager.getID() != null && manager.getMaTL().equals(accept.getMaNguoiDuyet())) {
				ot.setLyDoTuChoi(accept.getLyDoTuChoi());
				ot.setTrangThai(accept.getTrangThai());
				ot.setMaNguoiDuyet(accept.getMaNguoiDuyet());
				ApiResponse<OT> resp = new ApiResponse<OT>(0, "Success", repoOT.save(ot));
				return new ResponseEntity<>(resp, HttpStatus.OK);
			}
			ApiResponse<OT> resp = new ApiResponse<OT>(1, "invalid input or NguoiDuyet don't have permission", null);
			return new ResponseEntity<>(resp, HttpStatus.OK);
		} catch (Exception e) {
			ApiResponse<OT> resp = new ApiResponse<OT>(1, "Internal error", null);
			return new ResponseEntity<>(resp, HttpStatus.CREATED);
		}
	}

	@PostMapping("/request_ot")
	public ResponseEntity<ApiResponse<OT_Response>> Request_ot(@RequestBody OT ot) {
		try {
			List<OT> wfhlst = new ArrayList<OT>();
			Query q = new Query();
			ot.setNgayOT(ot.getNgayOT().plusDays(1));
			// q.addCriteria(Criteria.where("MaNhanVien").is(wfh.getMaNhanVien()));
			q.addCriteria(Criteria.where("MaNhanVien").is(ot.getMaNhanVien()))
					.addCriteria(Criteria.where("TrangThai").is(0));
			wfhlst = mongoTemplate.find(q, OT.class);
			
			if(wfhlst.isEmpty()) {
				if((ot.getNgayOT().isBefore(LocalDate.now()) && ot.getNgayOT().getMonth() != LocalDate.now().getMonth()) 
						|| (ot.getNgayOT().isAfter(LocalDate.now()) && ot.getNgayOT().getYear() != LocalDate.now().getYear())) {
					ApiResponse<OT_Response> resp = new ApiResponse<>(1, "Invalid date", null);
					return new ResponseEntity<>(resp, HttpStatus.CREATED);
				}
				ot.setID(UUID.randomUUID().toString());
				OT _ot = repoOT.save(new OT(ot.getID(), ot.getMaNhanVien(), ot.getNgayOT(), ot.getSoGio(), ot.getLyDoOT()));

				String uri = "https://gatewayteam07.herokuapp.com/api/staff_nghiphep/" + _ot.getMaNhanVien();
				RestTemplate restTemplate = new RestTemplate();
				User staff = restTemplate.getForObject(uri, User.class);
				OT_Response resp = new OT_Response(_ot, staff);

				ApiResponse<OT_Response> resp1 = new ApiResponse<>(0, "Success", resp);
				return new ResponseEntity<>(resp1, HttpStatus.CREATED);
			}
			
			ApiResponse<OT_Response> resp = new ApiResponse<>(1, "Can't request because you have petition", null);
			return new ResponseEntity<>(resp, HttpStatus.CREATED);
			
		} catch (Exception e) {
			ApiResponse<OT_Response> resp = new ApiResponse<>(1, "Miss input", null);
			return new ResponseEntity<>(resp, HttpStatus.CREATED);
		}
	}

	@GetMapping("/list_ot_manager")
	public ResponseEntity<ApiResponse<List<OT_Response>>> list_ot_manager(@RequestParam("id_lead") String id_lead_input,
			@RequestParam("status") int status_input) {
		try {
			// Tra ve list staff cua manager
			String uri = "https://gatewayteam07.herokuapp.com/api/list_staff_manager1/" + id_lead_input;
			RestTemplate restTemplate = new RestTemplate();
			List_Staff call = restTemplate.getForObject(uri, List_Staff.class);
			List<String> staff = call.getListstaff();

			// Check input status
			if (status_input < 0 || status_input > 2) {
				ApiResponse<List<OT_Response>> resp = new ApiResponse<>(1, "invalid status", null);
				return new ResponseEntity<>(resp, HttpStatus.OK);
			}

			// Lay ra tat ca request OT co status nhu input
			List<OT> otlst = new ArrayList<OT>();
			Query q = new Query();
			q.addCriteria(Criteria.where("TrangThai").is(status_input));
			otlst = mongoTemplate.find(q, OT.class);
			if (otlst.isEmpty()) {
				ApiResponse<List<OT_Response>> resp = new ApiResponse<>(0, "Empty data", null);
				return new ResponseEntity<>(resp, HttpStatus.OK);
			}
			List<OT> result = new ArrayList<OT>();

			// Lay ra nhung request cua nhung staff duoi quyen manager input
			for (OT i : otlst) {
				for (String y : staff) {
					if (i.getMaNhanVien().equals(y)) {
						result.add(i);
					}
				}
			}

			List<OT_Response> resp = new ArrayList<OT_Response>();
			for (OT i : result) {
				String uri1 = "https://gatewayteam07.herokuapp.com/api/staff_nghiphep/" + i.getMaNhanVien();
				RestTemplate restTemplate1 = new RestTemplate();
				User staff1 = restTemplate1.getForObject(uri1, User.class);
				OT_Response temp = new OT_Response(i, staff1);
				resp.add(temp);
			}

			ApiResponse<List<OT_Response>> resp1 = new ApiResponse<>(0, "Success", resp);
			return new ResponseEntity<>(resp1, HttpStatus.OK);
//			ApiResponse<List<OT>> resp = new ApiResponse<List<OT>>(0, "Success", result);
//			return new ResponseEntity<>(resp, HttpStatus.OK);
		} catch (Exception e) {
			ApiResponse<List<OT_Response>> resp = new ApiResponse<>(1, "ID lead not exist", null);
			return new ResponseEntity<>(resp, HttpStatus.OK);
		}
	}

}
