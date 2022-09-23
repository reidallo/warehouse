package app.warehouse.system.mapper;

import java.util.Set;

public interface IMapper<E, D> {

    E toEntity(D dto);
    D toDto(E entity);
    Set<E> toEntitySet(Set<D> dtoList);
}
