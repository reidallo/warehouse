package app.warehouse.system.service.implementation;

import app.warehouse.system.dto.TruckDto;
import app.warehouse.system.exception.ExceptionHandler;
import app.warehouse.system.mapper.TruckMapper;
import app.warehouse.system.model.Truck;
import app.warehouse.system.repository.TruckRepository;
import app.warehouse.system.service.TruckService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TruckServiceImpl implements TruckService {

    private final TruckRepository truckRepository;
    private final TruckMapper truckMapper;

    @Override
    public Page<TruckDto> getAllActiveTrucks(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.ASC, sortBy));
        Page<Truck> pagedResult = truckRepository.findAllByActive(pageable);
        return pagedResult.map(truckMapper::toDto);
    }

    @Override
    public TruckDto getTruckById(Long truckId) {
        Truck truck = truckRepository.findTruckByTruckIdAndActiveIsTrue(truckId).orElseThrow(() ->
                new ExceptionHandler(String.format(ExceptionHandler.NOT_FOUND, "Truck")));
        return truckMapper.toDto(truck);
    }
}
