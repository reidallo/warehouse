package app.warehouse.system.mapper;

import java.text.ParseException;
import java.util.Set;

public interface IMapper<E, D> {

    E toEntity(D dto) throws ParseException;
    D toDto(E entity);
    Set<E> toEntitySet(Set<D> dtoList);
}
