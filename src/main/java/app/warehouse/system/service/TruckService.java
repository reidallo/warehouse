package app.warehouse.system.service;

import app.warehouse.system.dto.TruckDto;
import app.warehouse.system.exception.MessageHandler;
import org.springframework.data.domain.Page;

public interface TruckService {

    Page<TruckDto> getAllActiveTrucks(Integer pageNo, Integer pageSize, String sortBy);

    TruckDto getTruckById(Long truckId);

    MessageHandler addNewTruck(TruckDto truckDto);

    MessageHandler disableTruck(Long truckId);
}
