package guru.springframework.msscbrewery.services;

import guru.springframework.msscbrewery.web.model.CustomerDto;

import java.util.UUID;

public interface CustomerSevice {
    CustomerDto getCustomerById(UUID customerId);
}
