package lt.swedbank.itacademy.ItAkaLeasingSystemBackEnd.repositories;

import lt.swedbank.itacademy.ItAkaLeasingSystemBackEnd.beans.documents.Customer;
import lt.swedbank.itacademy.ItAkaLeasingSystemBackEnd.beans.enums.CustomerType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, String>{

    List<Customer> findAll();

    List<Customer> findCustomersByCustomerType(CustomerType customerType);

    boolean existsCustomerByUserID(String userID);

    Customer findCustomerByUserID(String userID);
    boolean existsCustomerByUserIDAndEmail(String userID, String email);
}
