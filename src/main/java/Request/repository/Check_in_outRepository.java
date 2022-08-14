package Request.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import Request.model.Check_in_out;

@Repository
public interface Check_in_outRepository extends MongoRepository<Check_in_out, String> {

}
