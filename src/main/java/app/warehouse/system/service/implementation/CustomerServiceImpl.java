package app.warehouse.system.service.implementation;

import app.warehouse.system.dto.CustomerDto;
import app.warehouse.system.mapper.CustomerMapper;
import app.warehouse.system.model.Customer;
import app.warehouse.system.repository.CustomerRepository;
import app.warehouse.system.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public Page<CustomerDto> getAllCustomers(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.ASC, sortBy));
        Page<Customer> pagedResult = customerRepository.getCustomersByActive(pageable);
        return pagedResult.map(customerMapper::toDto);
    }
}
