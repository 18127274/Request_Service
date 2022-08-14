package Request.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import Request.model.NhanVien;

@Repository
public interface NhanVienRepository extends MongoRepository<NhanVien, String> {

}
