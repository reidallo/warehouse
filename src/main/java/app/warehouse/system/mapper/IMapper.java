package app.warehouse.system.mapper;

import java.text.ParseException;
import java.util.List;

public interface IMapper<E, D> {

    E toEntity(D dto) throws ParseException;
    D toDto(E entity);
    List<E> toEntityList(List<D> dtoList);
}
