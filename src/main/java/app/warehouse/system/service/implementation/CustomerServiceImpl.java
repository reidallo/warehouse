package app.warehouse.system.service.implementation;

import app.warehouse.system.dto.CustomerDto;
import app.warehouse.system.exception.ExceptionHandler;
import app.warehouse.system.exception.MessageHandler;
import app.warehouse.system.exception.Messages;
import app.warehouse.system.mapper.CustomerMapper;
import app.warehouse.system.model.Customer;
import app.warehouse.system.model.User;
import app.warehouse.system.repository.CustomerRepository;
import app.warehouse.system.repository.UserRepository;
import app.warehouse.system.service.CustomerService;
import app.warehouse.system.statics.MessageStatus;
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
    private final UserRepository userRepository;

    @Override
    public Page<CustomerDto> getAllCustomers(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.ASC, sortBy));
        Page<Customer> pagedResult = customerRepository.getCustomersByActive(pageable);
        return pagedResult.map(customerMapper::toDto);
    }

    @Override
    public CustomerDto getCustomerById(Long customerId) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() ->
                new ExceptionHandler(String.format(ExceptionHandler.NOT_FOUND, "User")));
        return customerMapper.toDto(customer);
    }

    @Override
    public MessageHandler updateCustomer(CustomerDto customerDto) {

        Customer customer = customerMapper.toEntity(customerDto);
        if (customer.getFirstName() == null) {
            MessageHandler.message(MessageStatus.ERROR, "First name can not be null!");
            return new MessageHandler(MessageHandler.hashMap);
        }
        if (customer.getLastName() == null) {
            MessageHandler.message(MessageStatus.ERROR, "Last name can not be null!");
            return new MessageHandler(MessageHandler.hashMap);
        }
        if (customer.getAddress() == null) {
            MessageHandler.message(MessageStatus.ERROR, "Address can not be null!");
            return new MessageHandler(MessageHandler.hashMap);
        }
        customerRepository.save(customer);

        MessageHandler.message(MessageStatus.SUCCESS, String.format(Messages.SUCCESS, "Customer", "updated"));
        return new MessageHandler(MessageHandler.hashMap);
    }

    @Override
    public MessageHandler disableCustomer(Long customerId) {

        Customer customer = customerRepository.findById(customerId).orElseThrow(() ->
                new ExceptionHandler(String.format(ExceptionHandler.NOT_FOUND, "User")));
        User user = customer.getUser();
        user.setActive(false);
        userRepository.save(user);

        MessageHandler.message(MessageStatus.SUCCESS, String.format(Messages.SUCCESS, "User", "disabled"));
        return new MessageHandler(MessageHandler.hashMap);
    }
}
