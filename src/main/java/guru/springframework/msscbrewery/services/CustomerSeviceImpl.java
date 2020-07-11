package guru.springframework.msscbrewery.services;

import guru.springframework.msscbrewery.web.model.CustomerDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class CustomerSeviceImpl implements CustomerSevice {
    @Override
    public CustomerDto getCustomerById(UUID customerId) {
        return CustomerDto.builder().id(UUID.randomUUID()).name("Rochester Brewery").build();
    }

    @Override
    public CustomerDto saveNewCustomer(CustomerDto customerDto) {
        return CustomerDto.builder().id(UUID.randomUUID()).name("Lotus Brewery").build();
    }

    @Override
    public void updateCustomer(UUID id, CustomerDto customerDto) {
        log.debug("Updating customer...");
    }

    @Override
    public void deleteCustomerById(UUID id) {
        log.debug("Deleting custome...");
    }
}
