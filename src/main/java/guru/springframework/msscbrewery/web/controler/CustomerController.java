package guru.springframework.msscbrewery.web.controler;

import guru.springframework.msscbrewery.services.CustomerSevice;
import guru.springframework.msscbrewery.web.model.CustomerDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequestMapping("/api/v1/customer")
@RestController
public class CustomerController {

    private final CustomerSevice customerSevice;

    public CustomerController(CustomerSevice customerSevice) {
        this.customerSevice = customerSevice;
    }

    @GetMapping("/{customerId}")
    ResponseEntity<CustomerDto> getCustomerById(@PathVariable UUID customerId){
        return new ResponseEntity<>(customerSevice.getCustomerById(customerId), HttpStatus.OK);
    }

}
