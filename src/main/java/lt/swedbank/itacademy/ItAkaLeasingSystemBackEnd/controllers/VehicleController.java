package lt.swedbank.itacademy.ItAkaLeasingSystemBackEnd.controllers;

import lt.swedbank.itacademy.ItAkaLeasingSystemBackEnd.beans.documents.Vehicle;
import lt.swedbank.itacademy.ItAkaLeasingSystemBackEnd.beans.response.VehicleResponse;
import lt.swedbank.itacademy.ItAkaLeasingSystemBackEnd.services.VehicleService;
import lt.swedbank.itacademy.ItAkaLeasingSystemBackEnd.utils.EndPoints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @RequestMapping(value = EndPoints.VEHICLES, method = RequestMethod.GET)
    public List<VehicleResponse> getAllVehicles(){
        return vehicleService.getAllVehicles();
    }

    @RequestMapping(value = EndPoints.VEHICLES_ADD, method = RequestMethod.POST)
    public VehicleResponse addVehicle(@Valid @RequestBody Vehicle vehicle){
        return new VehicleResponse(vehicleService.addNewVehicle(vehicle));
    }
}
