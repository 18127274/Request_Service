package Request.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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

import Request.model.ApiResponse;
import Request.model.Check_in_out;
import Request.model.DuAn;
import Request.model.OT;
import Request.repository.DuAnRepository;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.time.Period;

import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/api")

public class DuAnController {
	@Autowired
	DuAnRepository repoda;
	@Autowired
	MongoTemplate mongoTemplate;
	@Autowired
	MongoOperations mongoOperation;

//	@GetMapping("/view_all_list_request_wfh")
//	public ResponseEntity<ApiResponse<List<WFH>>> View_all_list_request_wfh() {
//		try {
//			List<WFH> wfhlst = new ArrayList<WFH>();
//
//			repoWFH.findAll().forEach(wfhlst::add);
//
//			if (wfhlst.isEmpty()) {
//				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//			}
//
//			ApiResponse<List<WFH>> resp = new ApiResponse<List<WFH>>(0, "Success", wfhlst);
//			return new ResponseEntity<>(resp, HttpStatus.CREATED);
//		} catch (Exception e) {
//			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//	}

	@GetMapping("/view_duan")
	public ResponseEntity<ApiResponse<List<DuAn>>> View_duan() {
		try {
			List<DuAn> wfhlst = new ArrayList<DuAn>();

			repoda.findAll().forEach(wfhlst::add);
			if (wfhlst.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			// System.out.println(wfhlst);
			ApiResponse<List<DuAn>> resp = new ApiResponse<List<DuAn>>(0, "Success", wfhlst);
			return new ResponseEntity<>(resp, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/post_duan")
	public ResponseEntity<ApiResponse<DuAn>> post_duan(@RequestBody DuAn duan) {
		try {
			// DateTimeFormatter dateTimeFormatter =
			// DateTimeFormatter.ofPattern("dd/MM/yyyy");
			// String ngayot = ot.getNgayOT();
			// LocalDate localDateObj = LocalDate.parse(ot.getNgayOT(), dateTimeFormatter);
			// //String to LocalDate
			// System.out.println(localDateObj.format(dateTimeFormatter)); // 14/07/2018
			// System.out.println(localDateObj.getClass().getName());
			duan.setID(UUID.randomUUID().toString());
			DuAn _duan = repoda.save(new DuAn(duan.getID(), duan.getMaPM(), duan.getMoTa(), duan.getNgayBatDau(),
					duan.getNgayKetThuc(), duan.getTenDuAn()));
			ApiResponse<DuAn> resp = new ApiResponse<DuAn>(0, "Success", _duan);
			return new ResponseEntity<>(resp, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

//	@GetMapping("/view_duan")
//	public ResponseEntity<List<DuAn>> View_duan() {
//		System.out.println("dasdpoasdpoakdps--------123");
//		try {
//			List<DuAn> wfhlst = new ArrayList<DuAn>();
//			
//			repoda.findAll().forEach(wfhlst::add);
//			
//			System.out.println(wfhlst.isEmpty());
//			if (wfhlst.isEmpty()) {
//				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//			}
//			//System.out.println(wfhlst);
//			System.out.println("dasdpoasdpoakdps--------dasd");
//			
//			return new ResponseEntity<>(wfhlst, HttpStatus.CREATED);
//		} catch (Exception e) {
//			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//	}
//	
//	Query q = new Query();
//	q.addCriteria(Criteria.where("mapm").is("123456"));
	// wfhlst = mongoTemplate.find(q, DuAn.class);

}