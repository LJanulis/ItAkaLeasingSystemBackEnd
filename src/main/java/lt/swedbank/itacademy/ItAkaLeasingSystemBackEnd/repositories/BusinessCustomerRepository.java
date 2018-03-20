package lt.swedbank.itacademy.ItAkaLeasingSystemBackEnd.repositories;

import lt.swedbank.itacademy.ItAkaLeasingSystemBackEnd.beans.documents.BusinessCustomer;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Lukas on 2018-03-20.
 */
public interface BusinessCustomerRepository extends CrudRepository<BusinessCustomer, String> {

}