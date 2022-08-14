package Request.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import Request.model.TaiKhoan;

@Repository
public interface TaiKhoanRepository extends MongoRepository<TaiKhoan, String> {

}
