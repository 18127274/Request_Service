package Request;

//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//
//import org.bson.Document;
//
//import com.mongodb.MongoClient;
//import com.mongodb.MongoClientURI;
//import com.mongodb.client.MongoCollection;
//import com.mongodb.client.MongoDatabase;
//import org.bson.types.ObjectId;
//
//
//@SpringBootApplication
//public class DemoWebApiApplication {
//
//	public static void main(String[] args) {
//		SpringApplication.run(DemoWebApiApplication.class, args);
//	}
//	MongoClientURI uri = new MongoClientURI(
//		    "mongodb+srv://<user name>:<password>@<cluster hostname>/test?retryWrites=true&w=majority&connectTimeoutMS=30000&socketTimeoutMS=30000");
//		
//	try(MongoClient mongoClient = new MongoClient(uri))
//		{
//			MongoDatabase database = mongoClient.getDatabase("test");			
//			MongoCollection<Document> collection = database.getCollection("test");
//			Document query = new Document("_id", new ObjectId("5e234fe121fcf183e83ddce2"));
//	        Document result = collection.find(query).iterator().next();
//
//	        System.out.println("Test3: "+result.getString("test3"));
//		}		
//}

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Request {

	public static void main(String[] args) {
		SpringApplication.run(Request.class, args);
	}
}
//rdsdskd