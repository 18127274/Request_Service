package Request.controller;

import java.util.ArrayList;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.time.LocalDate;
import java.util.Date;

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

import Request.model.ApiResponse;
import Request.model.NghiPhep;
import Request.model.NhanVien;
import Request.model.OT;
import Request.model.WFH;
import Request.repository.NghiPhepRepository;
import Request.repository.NhanVienRepository;
import Request.repository.OTRepository;
import Request.repository.WFHRepository;

import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@RestController
@CrossOrigin
@RequestMapping(value = "/api")

public class NhanvienController {
	@Autowired
	NhanVienRepository repoNV;
	@Autowired
	MongoTemplate mongoTemplate;
	@Autowired
	MongoOperations mongoOperation;

	@GetMapping("/view_staff_list")
	public ResponseEntity<List<NhanVien>> View_staff_list() {
		try {
			List<NhanVien> nhanvienlst = new ArrayList<NhanVien>();

			repoNV.findAll().forEach(nhanvienlst::add);

			if (nhanvienlst.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(nhanvienlst, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/view_staff_by_id/{id_staff}")
	public ResponseEntity<ApiResponse<List<NhanVien>>> View_staff_by_id(
			@PathVariable(value = "id_staff") String id_staff) {
		try {
			List<NhanVien> nhanvienlst = new ArrayList<NhanVien>();
			Query q = new Query();
			q.addCriteria(Criteria.where("ID").is(id_staff));
			nhanvienlst = mongoTemplate.find(q, NhanVien.class);
			System.out.println("Hello :" + id_staff);
			System.out.println("status :" + nhanvienlst.isEmpty());
			if (nhanvienlst.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			ApiResponse<List<NhanVien>> resp = new ApiResponse<List<NhanVien>>(0, "Success", nhanvienlst);
			return new ResponseEntity<>(resp, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

//	@PostMapping("/create_staff")
//	public ResponseEntity<ApiResponse<NhanVien>> Create_staff(@RequestBody NhanVien nv) {
//		try {
//			// List<NhanVien> wfhlst = new ArrayList<NhanVien>();
//			// Query q = new Query();
//			// q.addCriteria(Criteria.where("MaNhanVien").is(nv.getID()));
//			// q.addCriteria(Criteria.where("MaNhanVien").is(np.getMaNhanVien())).addCriteria(Criteria.where("TrangThai").is("0"));
//			// wfhlst = mongoTemplate.find(q, NhanVien.class);
//
//			nv.setID(UUID.randomUUID().toString());
//			NhanVien _nv = repoNV.save(new NhanVien(nv.getID(), nv.getHoTen(), nv.getGioiTinh(), nv.getChucVu(),
//					nv.getDiaChi(), nv.getEmail(), nv.getSDT(), 12, "" ,"0"));
//			ApiResponse<NhanVien> resp = new ApiResponse<NhanVien>(0, "Success", _nv);
//			return new ResponseEntity<>(resp, HttpStatus.CREATED);
//
////			ApiResponse<NhanVien> resp = new ApiResponse<NhanVien>(0, "Can't request", null);
////			return new ResponseEntity<>(resp, HttpStatus.CREATED);
//		} catch (Exception e) {
//			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//	}

//	@PutMapping("/update_profile_staff")
//	public ResponseEntity<ApiResponse<NhanVien>> Update_profile_staff(@RequestParam(value = "id_staff") String id_staff, @RequestParam(value = "img_link") String img_link,
//			@RequestParam(value = "diachi") String diachi, @RequestParam(value = "email") String email, @RequestParam(value = "sdt") String sdt) {
//		//System.out.println("Hello " + id_wfh);
//		try {						
//			Query q = new Query();
//			q.addCriteria(Criteria.where("ID").is(id_staff));
//			NhanVien wfh = mongoTemplate.findOne(q, NhanVien.class);
//			System.out.println(wfh.getID());
//			if(wfh.getID() != "") {
//				wfh.setIMG_Link(img_link);
//				wfh.setDiaChi(diachi);	
//				wfh.setEmail(email);
//				wfh.setSDT(sdt);
//				ApiResponse<NhanVien> resp = new ApiResponse<NhanVien>(0, "Update Successfully", repoNV.save(wfh));
//				return new ResponseEntity<>(resp, HttpStatus.CREATED);
//			}
//			ApiResponse<NhanVien> resp = new ApiResponse<NhanVien>(0, "Staff ID is wrong!", null);
//			return new ResponseEntity<>(resp, HttpStatus.CREATED);
//			
//		} catch (Exception e) {
//			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//	}

//	@PutMapping("/reject_request_wfh/{id_wfh}")
//	public ResponseEntity<WFH> Reject_request_wfh(@PathVariable(value = "id_wfh") long id_wfh,
//			@RequestParam ("lydotuchoi") String lydotuchoi) {
//		try {						
//			Query q = new Query();
//			q.addCriteria(Criteria.where("ID").is(id_wfh));
//			WFH wfh = mongoTemplate.findOne(q, WFH.class);
//			wfh.setTrangThai("2");	
//			wfh.setLyDoTuChoi(lydotuchoi);
//			return new ResponseEntity<>(repoWFH.save(wfh), HttpStatus.OK);
//		} catch (Exception e) {
//			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//	}

}

//---------parse string to localdate
//import java.text.ParseException;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.time.format.FormatStyle;
//
//public class Main {
//    public static void main(String[] args) throws ParseException {
//        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//        String dateString = "14/07/2018";
//
//        LocalDate localDateObj = LocalDate.parse(dateString, dateTimeFormatter);  //String to LocalDate
//
//        System.out.println(localDateObj.format(dateTimeFormatter));    // 14/07/2018
//        System.out.println(localDateObj.getClass().getName());
//        
//    }
//}

//-------caculate distance between date 
//import java.text.SimpleDateFormat;
//import java.time.LocalDate;
//import java.util.Date;
//import java.time.Period;
//
//public class HelloWorld {
//
//    public static void main(String []args) {
//        LocalDate dateBefore = LocalDate.of(2022, 12, 31);
//        LocalDate dateAfter = LocalDate.of(2023, 1, 2);
//
//        Period period = Period.between(dateBefore, dateAfter);
//        long daysDiff = Math.abs(period.getDays());
//
//        System.out.println(" The number of days between dates: " + daysDiff);
//    }
//}