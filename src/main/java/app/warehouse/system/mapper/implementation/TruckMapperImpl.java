package app.warehouse.system.mapper.implementation;


import app.warehouse.system.dto.TruckDto;
import app.warehouse.system.mapper.TruckMapper;
import app.warehouse.system.model.Truck;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class TruckMapperImpl implements TruckMapper {

    @Override
    public Truck toEntity(TruckDto dto) {
        if (dto == null)
            return null;
        Truck entity = new Truck();
        if (dto.getTruckId() != null)
            entity.setTruckId(dto.getTruckId());
        if (dto.getChassisNumber() != null)
            entity.setChassisNumber(dto.getChassisNumber());
        if (dto.getLicensePlate() != null)
            entity.setLicensePlate(dto.getLicensePlate());
        entity.setActive(dto.isActive());
        return entity;
    }

    @Override
    public TruckDto toDto(Truck entity) {
        if (entity == null)
            return null;
        TruckDto dto = new TruckDto();
        if (entity.getTruckId() != null)
            dto.setTruckId(entity.getTruckId());
        if (entity.getChassisNumber() != null)
            dto.setChassisNumber(entity.getChassisNumber());
        if (entity.getLicensePlate() != null)
            dto.setLicensePlate(entity.getLicensePlate());
        dto.setActive(entity.isActive());
        return dto;
    }

    @Override
    public Set<Truck> toEntitySet(Set<TruckDto> dtoSet) {
        Set<Truck> entitySet = new HashSet<>();
        dtoSet.forEach(dto -> entitySet.add(toEntity(dto)));
        return entitySet;
    }

    @Override
    public Set<TruckDto> toDtoSet(Set<Truck> entitySet) {
        Set<TruckDto> dtoSet = new HashSet<>();
        entitySet.forEach(entity -> dtoSet.add(toDto(entity)));
        return dtoSet;
    }
}
