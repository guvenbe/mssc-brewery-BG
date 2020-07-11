package guru.springframework.msscbrewery.web.controler;

import guru.springframework.msscbrewery.services.CustomerSevice;
import guru.springframework.msscbrewery.web.model.CustomerDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping()
    ResponseEntity handleCreateCustomer(@RequestBody CustomerDto customerDto){
        CustomerDto savedCustomerDto = customerSevice.saveNewCustomer(customerDto);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location" ,"/api/v1/customer" +savedCustomerDto.getId());
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

}
