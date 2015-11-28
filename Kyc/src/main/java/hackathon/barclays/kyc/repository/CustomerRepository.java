package hackathon.barclays.kyc.repository;


import hackathon.barclays.kyc.model.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository<Customer, String> {

    public Customer findByName(String name);


}
