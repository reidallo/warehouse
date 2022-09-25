package app.warehouse.system.service;

import app.warehouse.system.dto.TruckDto;
import org.springframework.data.domain.Page;

public interface TruckService {

    Page<TruckDto> getAllActiveTrucks(Integer pageNo, Integer pageSize, String sortBy);

    TruckDto getTruckById(Long truckId);
}
