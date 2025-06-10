package app.warehouse.system.controller;

import app.warehouse.system.dto.CustomerDto;
import app.warehouse.system.exception.MessageHandler;
import app.warehouse.system.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer")
@AllArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping(value = "/")
    public ResponseEntity<Page<CustomerDto>> getAllOrders(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "5") Integer pageSize,
            @RequestParam(defaultValue = "firstName") String sortBy) {
        return ResponseEntity.ok(customerService.getAllCustomers(pageNo, pageSize, sortBy));
    }

    @GetMapping(value = "/id")
    public ResponseEntity<CustomerDto> getCustomerById(@RequestParam(name = "customerId") Long customerId) {
        return ResponseEntity.ok(customerService.getCustomerById(customerId));
    }

    @PutMapping(value = "/")
    public MessageHandler updateCustomer(@RequestBody CustomerDto customerDto) {
        return customerService.updateCustomer(customerDto);
    }

    @PutMapping(value = "/disable")
    public MessageHandler disable(@RequestParam(name = "customerId") Long customerId) {
        return customerService.disableCustomer(customerId);
    }
}
