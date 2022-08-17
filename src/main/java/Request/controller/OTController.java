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
import Request.model.OT;
import Request.model.ThamGiaDuAn;
import Request.model.List_request;
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
	public ResponseEntity<ApiResponse<List<OT>>> XemDSOT_ID(@PathVariable(value = "MaOT_input") String MaOT_input) {

		try {
			List<OT> otlst = new ArrayList<OT>();
			Query q = new Query();
			q.addCriteria(Criteria.where("ID").is(MaOT_input));
			otlst = mongoTemplate.find(q, OT.class);
			if (otlst.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			ApiResponse<List<OT>> resp = new ApiResponse<List<OT>>(0, "Success", otlst);
			return new ResponseEntity<>(resp, HttpStatus.OK);
		} catch (Exception e) {
			ApiResponse<List<OT>> resp = new ApiResponse<>(1, "Internal error", null);
			return new ResponseEntity<>(resp, HttpStatus.CREATED);
		}
	}
	
	@GetMapping("/get_all_list_request_ot_of_staff/{MaNV_input}")
	public ResponseEntity<ApiResponse<List<OT>>> Get_all_list_request_ot_of_staff(@PathVariable(value = "MaNV_input") String MaNV_input) {
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
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/unaccepted_ot")
	public ResponseEntity<ApiResponse<List<OT>>> XemDSOTChuaDuyet() {
		try {
			List<OT> otlst = new ArrayList<OT>();
			Query q = new Query();
			q.addCriteria(Criteria.where("TrangThai").is(0));
			otlst = mongoTemplate.find(q, OT.class);

			if (otlst.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			System.out.println(otlst.size());
			ApiResponse<List<OT>> resp = new ApiResponse<List<OT>>(0, "Success", otlst);
			return new ResponseEntity<>(resp, HttpStatus.OK);
		} catch (Exception e) {
			ApiResponse<List<OT>> resp = new ApiResponse<>(1, "Internal error", null);
			return new ResponseEntity<>(resp, HttpStatus.CREATED);
		}
	}

	@GetMapping("/get_ot_nv/{MaNV_input}")
	public ResponseEntity<ApiResponse<List<OT>>> XemDSOT_MaNV(@PathVariable(value = "MaNV_input") String MaNV_input) {

		try {
			List<OT> otlst = new ArrayList<OT>();
			Query q = new Query();
			q.addCriteria(Criteria.where("MaNhanVien").is(MaNV_input));
			otlst = mongoTemplate.find(q, OT.class);
			if (otlst.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			ApiResponse<List<OT>> resp = new ApiResponse<List<OT>>(0, "Success", otlst);
			return new ResponseEntity<>(resp, HttpStatus.OK);
		} catch (Exception e) {
			ApiResponse<List<OT>> resp = new ApiResponse<>(1, "Internal error", null);
			return new ResponseEntity<>(resp, HttpStatus.CREATED);
		}
	}

	@PutMapping("/accept_ot")
	public ResponseEntity<ApiResponse<OT>> TiepNhanOT(@RequestBody OT accept
//			@RequestParam("MaOT_input") String MaOT_input,
//			@RequestParam("TinhTrang_input") String TinhTrang_input,
//			@RequestParam("LyDoTuChoi_input") String LyDoTuChoi_input,
//			@RequestParam("MaNguoiDuyet_input") String MaNguoiDuyet_input
			){
		try {
			Query q = new Query();
			q.addCriteria(Criteria.where("ID").is(accept.getID()));
			OT ot = mongoTemplate.findOne(q, OT.class);
			
			String uri = "https://gatewayteam07.herokuapp.com/api/get_manager1_of_staff/" + ot.getMaNhanVien();
			RestTemplate restTemplate = new RestTemplate();
			ThamGiaDuAn manager = restTemplate.getForObject(uri, ThamGiaDuAn.class);
			
			if(manager.getID() != null && manager.getMaTL().equals(accept.getMaNguoiDuyet())) {
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
	public ResponseEntity<ApiResponse<OT>> Request_ot(@RequestBody OT ot) {
		try {
			// DateTimeFormatter dateTimeFormatter =
			// DateTimeFormatter.ofPattern("dd/MM/yyyy");
			// String ngayot = ot.getNgayOT();
			// LocalDate localDateObj = LocalDate.parse(ot.getNgayOT(), dateTimeFormatter);
			// //String to LocalDate
			// System.out.println(localDateObj.format(dateTimeFormatter)); // 14/07/2018
			// System.out.println(localDateObj.getClass().getName());
			ot.setID(UUID.randomUUID().toString());
			OT _ot = repoOT.save(new OT(ot.getID(), ot.getMaNhanVien(), ot.getNgayOT(), ot.getSoGio(), ot.getLyDoOT()));
			ApiResponse<OT> resp = new ApiResponse<OT>(0, "Success", _ot);
			return new ResponseEntity<>(resp, HttpStatus.CREATED);
		} catch (Exception e) {
			ApiResponse<OT> resp = new ApiResponse<OT>(1, "Internal error", null);
			return new ResponseEntity<>(resp, HttpStatus.CREATED);
		}
	}
	
	@GetMapping("/list_ot_manager")
	public ResponseEntity<ApiResponse<List<OT>>> list_ot_manager(@RequestParam("id_lead") String id_lead_input,
			@RequestParam("status") int status_input) {
		try {
			String uri = "https://gatewayteam07.herokuapp.com/api/list_staff_manager1/" + id_lead_input;
			RestTemplate restTemplate = new RestTemplate();
			List_ThamGiaDuAn call = restTemplate.getForObject(uri, List_ThamGiaDuAn.class);
			List<ThamGiaDuAn> staff = call.getListstaff();
			
			if (status_input<0 || status_input>2) {
				ApiResponse<List<OT>> resp = new ApiResponse<List<OT>>(1, "invalid status", null);
				return new ResponseEntity<>(resp, HttpStatus.OK);
			}
			
			List<OT> otlst = new ArrayList<OT>();
			Query q = new Query();
			q.addCriteria(Criteria.where("TrangThai").is(status_input));
			otlst = mongoTemplate.find(q, OT.class);
			if (otlst.isEmpty()) {
				ApiResponse<List<OT>> resp = new ApiResponse<List<OT>>(1, "Empty data", otlst);
				return new ResponseEntity<>(resp, HttpStatus.OK);
			}
			List<OT> result = new ArrayList<OT>();
			for (OT i : otlst) {
				for (ThamGiaDuAn y : staff) {
					if(i.getMaNhanVien().equals(y.getMaNV())) {
						result.add(i);
					}
				}
			}
			ApiResponse<List<OT>> resp = new ApiResponse<List<OT>>(0, "Success", result);
			return new ResponseEntity<>(resp, HttpStatus.OK);
		} catch (Exception e) {
			ApiResponse<List<OT>> resp = new ApiResponse<List<OT>>(1, "ID lead not exist", null);
			return new ResponseEntity<>(resp, HttpStatus.OK);
		}
	}

}
