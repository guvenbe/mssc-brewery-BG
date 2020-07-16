package guru.springframework.msscbrewery.web.controler;

import guru.springframework.msscbrewery.services.CustomerSevice;
import guru.springframework.msscbrewery.web.model.CustomerDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
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
    ResponseEntity handleCreateCustomer(@Valid @RequestBody CustomerDto customerDto){
        CustomerDto savedCustomerDto = customerSevice.saveNewCustomer(customerDto);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location" ,"/api/v1/customer" +savedCustomerDto.getId().toString());
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @PutMapping("/{customerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void  handleUpdate(@PathVariable("customerId") UUID customerId,
                                                    @Valid @RequestBody CustomerDto customerDto ) {
        customerSevice.updateCustomer(customerId, customerDto);

    }

    @DeleteMapping("/{customerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBeer(@PathVariable("customerId") UUID customerId){
        customerSevice.deleteCustomerById(customerId);
    }

    ResponseEntity<List> validationHandler(ConstraintViolationException e){

        List<String> erros = new ArrayList<>(e.getConstraintViolations().size());
        e.getConstraintViolations().forEach(constraintViolation -> {
            erros.add(constraintViolation.getPropertyPath() + " " + constraintViolation.getMessage());
        });

        return new ResponseEntity<>(erros,HttpStatus.BAD_REQUEST);
    }
}
