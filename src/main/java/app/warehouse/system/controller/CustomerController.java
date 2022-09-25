package app.warehouse.system.controller;

import app.warehouse.system.dto.CustomerDto;
import app.warehouse.system.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
