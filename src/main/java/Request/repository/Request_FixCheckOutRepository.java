package Request.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import Request.model.Request_FixCheckOut;

@Repository
public interface Request_FixCheckOutRepository extends MongoRepository<Request_FixCheckOut, String> {

}
