package lt.swedbank.itacademy.ItAkaLeasingSystemBackEnd.repositories;

import lt.swedbank.itacademy.ItAkaLeasingSystemBackEnd.beans.documents.VehicleLeasing;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Lukas on 2018-03-20.
 */
public interface VehicleLeasingRepository extends CrudRepository<VehicleLeasing, String> {

}