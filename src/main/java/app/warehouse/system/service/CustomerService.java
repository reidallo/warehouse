package app.warehouse.system.service;

import app.warehouse.system.dto.CustomerDto;
import org.springframework.data.domain.Page;

public interface CustomerService {

    Page<CustomerDto> getAllCustomers(Integer pageNo, Integer pageSize, String sortBy);
}
