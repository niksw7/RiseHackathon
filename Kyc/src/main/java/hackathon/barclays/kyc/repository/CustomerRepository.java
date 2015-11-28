package hackathon.barclays.kyc.repository;


import hackathon.barclays.kyc.model.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository<Customer, Integer> {

    public Customer findByName(String name);


}
