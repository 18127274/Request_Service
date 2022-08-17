package Request.controller;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.format.annotation.DateTimeFormat;
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

import Request.model.ApiResponse;
import Request.model.CheckIn_Response;
import Request.model.Check_in_out;
import Request.model.NhanVien;
import Request.model.OT;
import Request.repository.Check_in_outRepository;
import Request.repository.NghiPhepRepository;
import Request.repository.NhanVienRepository;
import Request.repository.OTRepository;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/api")

public class CheckInOutController {
	@Autowired
	NhanVienRepository repoNV;
	@Autowired
	Check_in_outRepository repoCheckIn;
	@Autowired
	NghiPhepRepository repoNP;
	@Autowired
	OTRepository repoOT;
	@Autowired
	MongoTemplate mongoTemplate;
	@Autowired
	MongoOperations mongoOperation;

	@GetMapping("/get_checkin_all")
	public ResponseEntity<ApiResponse<List<Check_in_out>>> XemDSCheckin() {

		try {
			List<Check_in_out> checklist = new ArrayList<Check_in_out>();

			repoCheckIn.findAll(Sort.by(Sort.Direction.DESC, "GioBatDau")).forEach(checklist::add);

			if (checklist.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			ApiResponse<List<Check_in_out>> resp = new ApiResponse<List<Check_in_out>>(0, "Success", checklist);
			return new ResponseEntity<>(resp, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/get_time")
	public ResponseEntity<ApiResponse<CheckIn_Response>> CheckinTimeMonthYear(@RequestParam("MaNV") String MaNV_input,
			@RequestParam("Year") int Year_input, @RequestParam("Month") int Month_input) {

		try {
			List<Check_in_out> checklst = new ArrayList<Check_in_out>();
			repoCheckIn.findAll(Sort.by(Sort.Direction.DESC, "GioBatDau")).forEach(checklst::add);
			if (checklst.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			long monthhour = 0;
			List<Check_in_out> monthlist = new ArrayList<>();
			long yearhour = 0;
			for (Check_in_out i : checklst) {
				if (i.getMaNV().equals(MaNV_input) && i.getGioBatDau().getMonthValue() == Month_input
						&& i.getGioBatDau().getYear() == Year_input) {
					monthlist.add(i);
					if (i.getGioKetThuc() != null) {
						monthhour += i.getGioBatDau().until(i.getGioKetThuc(), ChronoUnit.HOURS);
					}
				}
				if (i.getMaNV().equals(MaNV_input) && i.getGioBatDau().getYear() == Year_input) {
					if (i.getGioKetThuc() != null) {
						yearhour += i.getGioBatDau().until(i.getGioKetThuc(), ChronoUnit.HOURS);
					}
				}
			}
//			Collections.sort(monthlist, Collections.reverseOrder());
			CheckIn_Response result = new CheckIn_Response(monthlist, monthhour, yearhour);
			if (result.getMonthTime() >= 200) {
				result.setStatus(1);
			}
			ApiResponse<CheckIn_Response> resp = new ApiResponse<CheckIn_Response>(0, "Success", result);
			return new ResponseEntity<>(resp, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/get_checkin_nv/{MaNV}")
	public ResponseEntity<ApiResponse<List<Check_in_out>>> XemDSCheckIn_MaNV(
			@PathVariable(value = "MaNV") String MaNV_input) {

		try {
			List<Check_in_out> checklst = new ArrayList<Check_in_out>();
			Query q = new Query();
			q.addCriteria(Criteria.where("MaNV").is(MaNV_input));

			checklst = mongoTemplate.find(q, Check_in_out.class);
			Collections.sort(checklst, Comparator.comparing(Check_in_out::getGioBatDau).reversed());
			if (checklst.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
//			Collections.sort(checklst, Collections.reverseOrder());
			ApiResponse<List<Check_in_out>> resp = new ApiResponse<List<Check_in_out>>(0, "Success", checklst);
			return new ResponseEntity<>(resp, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/check_out/{MaNV}")
	public <Int> ResponseEntity<ApiResponse<Check_in_out>> check_out(@PathVariable("MaNV") String MaNV_input) {
		try {
			// get list
			LocalDateTime currentdate = LocalDateTime.now();
			List<Check_in_out> checklst = new ArrayList<Check_in_out>();
			Query q = new Query();
			q.addCriteria(Criteria.where("MaNV").is(MaNV_input));
			checklst = mongoTemplate.find(q, Check_in_out.class);

			// check in loop
			for (Check_in_out i : checklst) {
//				if(i.getMaNV().equals(MaNV_input) && i.getGioKetThuc() != null && i.getGioKetThuc().getDayOfMonth() == currentdate.getDayOfMonth() && i.getGioKetThuc().getMonthValue() == currentdate.getMonthValue() && i.getGioKetThuc().getYear() == currentdate.getYear()) {
//					ApiResponse<Check_in_out> resp = new ApiResponse<>(0,"Already checkout",i);
//					return new ResponseEntity<>(resp, HttpStatus.CREATED);
//				}
				if (i.getMaNV().equals(MaNV_input) && !(i.getGioKetThuc() != null)
						&& i.getGioBatDau().getDayOfMonth() == currentdate.getDayOfMonth()
						&& i.getGioBatDau().getMonthValue() == currentdate.getMonthValue()
						&& i.getGioBatDau().getYear() == currentdate.getYear()) {
					i.setGioKetThuc(LocalDateTime.now().plusHours(7));
					repoCheckIn.save(i);
					i.setGioKetThuc(i.getGioKetThuc().minusHours(7));
					ApiResponse<Check_in_out> resp = new ApiResponse<Check_in_out>(0, "Success", i);
					return new ResponseEntity<>(resp, HttpStatus.OK);
				}
			}
			ApiResponse<Check_in_out> resp = new ApiResponse<>(1, "Not checkin yet", null);
			return new ResponseEntity<>(resp, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

//	@PostMapping("/check_in/{MaNV}")
//	public ResponseEntity<ApiResponse<Check_in_out>> check_in(@PathVariable("MaNV") String MaNV_input) {
//		try {
//			// get list
//			LocalDateTime currentdate = LocalDateTime.now();
//			List<Check_in_out> checklst = new ArrayList<Check_in_out>();
//			Query q = new Query();
//			q.addCriteria(Criteria.where("MaNV").is(MaNV_input));
//			checklst = mongoTemplate.find(q, Check_in_out.class);
//
//			// check in loop
//			for (Check_in_out i : checklst) {
//				if (i.getMaNV().equals(MaNV_input) && !(i.getGioKetThuc() != null)
//						&& i.getGioBatDau().getDayOfMonth() == currentdate.getDayOfMonth()
//						&& i.getGioBatDau().getMonthValue() == currentdate.getMonthValue()
//						&& i.getGioBatDau().getYear() == currentdate.getYear()) {
//					ApiResponse<Check_in_out> resp = new ApiResponse<>(1, "Not Checkout yet", null);
//					return new ResponseEntity<>(resp, HttpStatus.CREATED);
//				}
//			}
//			//test
//			String ID = UUID.randomUUID().toString();
//			Check_in_out _check_in = repoCheckIn.save(new Check_in_out(ID, MaNV_input));
//			_check_in.setGioBatDau(_check_in.getGioBatDau().minusHours(7));
//			ApiResponse<Check_in_out> resp = new ApiResponse<Check_in_out>(0, "Success", _check_in);
//			return new ResponseEntity<>(resp, HttpStatus.CREATED);
//		} catch (Exception e) {
//			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//	}

}
