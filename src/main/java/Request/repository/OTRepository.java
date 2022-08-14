package Request.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import Request.model.OT;

@Repository
public interface OTRepository extends MongoRepository<OT, String> {

}
