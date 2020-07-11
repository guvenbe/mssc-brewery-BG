package guru.springframework.msscbrewery.web.controler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import guru.springframework.msscbrewery.services.CustomerSevice;
import guru.springframework.msscbrewery.web.model.BeerDto;
import guru.springframework.msscbrewery.web.model.CustomerDto;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @MockBean
    CustomerSevice customerSevice;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    CustomerDto validCustomer;

    @BeforeEach
    void setUp() {
        validCustomer = CustomerDto.builder()
                .id(UUID.randomUUID())
                .name("Rochester Brewery")
                .build();
    }

    @AfterEach
    void tearDown() {
        reset(customerSevice);
    }

    @Test
    public void testGetCustomerById() throws Exception {
        given(customerSevice.getCustomerById(any())).willReturn(validCustomer);

        MvcResult result = mockMvc.perform(get("/api/v1/customer/" + validCustomer.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(validCustomer.getId().toString())))
                .andExpect(jsonPath("$.name", is(validCustomer.getName())))
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());

    }

    @Test
    public void handlePost() throws Exception {

        CustomerDto customerDto = validCustomer;
        customerDto.setId(null);
        CustomerDto savedCustomerDto = CustomerDto.builder()
                .id(UUID.randomUUID())
                .name("New Customer")
                .build();

        String customerDtoJson = objectMapper.writeValueAsString(savedCustomerDto);
        given(customerSevice.saveNewCustomer(any())).willReturn(savedCustomerDto);
        MvcResult result = mockMvc.perform(post("/api/v1/customer/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(customerDtoJson))
                .andExpect(status().isCreated())
                .andReturn();
        ArgumentCaptor<CustomerDto> customerDtoCaptor = ArgumentCaptor.forClass(CustomerDto.class);
        then(customerSevice).should(times(1))
                .saveNewCustomer(customerDtoCaptor.capture());

        CustomerDto dtoArgument = customerDtoCaptor.getValue();
        assertThat(dtoArgument.getName()).isEqualTo(savedCustomerDto.getName());

    }

    @Test
    public void handleUpdate() throws Exception {
        //given
        CustomerDto customerDto = validCustomer;
        String customeDtoJson = objectMapper.writeValueAsString(customerDto);

        mockMvc.perform(put("/api/v1/customer/" + validCustomer.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(customeDtoJson))
                .andExpect(status().isNoContent());

        ArgumentCaptor<CustomerDto> customerDtoCaptor = ArgumentCaptor.forClass(CustomerDto.class);
        then(customerSevice).should(times(1)).updateCustomer(any(), customerDtoCaptor.capture());
        CustomerDto customerDtoCaptorValue = customerDtoCaptor.getValue();
        assertThat(customerDtoCaptorValue.getName()).isEqualTo(customerDto.getName());
    }

    @Test
    public  void deleteCustomer() throws Exception {
        //given
        CustomerDto customerDto = validCustomer;
        mockMvc.perform(delete("/api/v1/customer/" + validCustomer.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        then(customerSevice).should(times(1)).deleteCustomerById(validCustomer.getId());
    }
}