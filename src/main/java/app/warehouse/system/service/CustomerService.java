package app.warehouse.system.service;

import app.warehouse.system.dto.CustomerDto;
import app.warehouse.system.exception.MessageHandler;
import org.springframework.data.domain.Page;

public interface CustomerService {

    Page<CustomerDto> getAllCustomers(Integer pageNo, Integer pageSize, String sortBy);

    CustomerDto getCustomerById(Long customerId);

    MessageHandler updateCustomer(CustomerDto customerDto);

    MessageHandler disableCustomer(Long customerId);
}
