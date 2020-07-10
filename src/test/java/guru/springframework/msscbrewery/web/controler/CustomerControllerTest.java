package guru.springframework.msscbrewery.web.controler;

import guru.springframework.msscbrewery.services.CustomerSevice;
import guru.springframework.msscbrewery.web.model.CustomerDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @MockBean
    CustomerSevice customerSevice;

    @Autowired
    MockMvc mockMvc;

    CustomerDto validCustomer;

    @BeforeEach
    void setUp() {
        validCustomer= CustomerDto.builder()
                .id(UUID.randomUUID())
                .name("Rochester Brewery")
                .build();
    }

    @AfterEach
    void tearDown() {
        reset(customerSevice);
    }

    @Test
    void testGetCustomerById() throws Exception {
        given(customerSevice.getCustomerById(any())).willReturn(validCustomer);

        MvcResult result = mockMvc.perform(get("/api/v1/customer/" + validCustomer.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(validCustomer.getId().toString())))
                .andExpect(jsonPath("$.name", is(validCustomer.getName())))
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());

    }
}