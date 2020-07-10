package guru.springframework.msscbrewery.web.controler;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.msscbrewery.services.BeerService;
import guru.springframework.msscbrewery.web.model.BeerDto;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BeerController.class)
class BeerControllerTest {
    @MockBean
    BeerService beerService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Captor
    ArgumentCaptor<String> stringArgumentCaptor;

    BeerDto validBeer;

    @BeforeEach
    void setUp() {

        validBeer= BeerDto.builder()
                .id(UUID.randomUUID())
                .beerName("Galaxy Cat")
                .beerStyle("Pale Ale")
                .upc(123456789012L)
                .build();
    }
    @AfterEach
    void tearDown() {
        reset(beerService);
    }

    @Test
    public void testGetBeerById() throws Exception {
        given(beerService.getBeerById(any())).willReturn(validBeer);

        MvcResult result = mockMvc.perform(get("/api/v1/beer/" + validBeer.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", Matchers.is(validBeer.getId().toString())))
                .andExpect(jsonPath("$.beerName", Matchers.is(validBeer.getBeerName())))
                .andExpect(jsonPath("$.beerStyle", Matchers.is(validBeer.getBeerStyle())))
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());

    }

    @Test
    public void  handlePost() throws Exception {

        BeerDto beerDto =validBeer;
        beerDto.setId(null);

        BeerDto savedDto = BeerDto.builder()
                .id(UUID.randomUUID())
                .beerName("New Beer")
                .build();
        String beerDtoJson = objectMapper.writeValueAsString(beerDto);
        given(beerService.saveNewBeer(any())).willReturn(savedDto);



        mockMvc.perform(post("/api/v1/beer/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(beerDtoJson))
                .andExpect(status().isCreated());

        ArgumentCaptor<BeerDto> beerDtoCaptor = ArgumentCaptor.forClass(BeerDto.class);
        verify(beerService,times(1))
                .saveNewBeer(beerDtoCaptor.capture());
        BeerDto dtoArgument = beerDtoCaptor.getValue();
       assertThat(dtoArgument.getBeerName()).isEqualTo("Galaxy Cat");
       assertThat(dtoArgument.getBeerStyle()).isEqualTo("Pale Ale");
       assertThat(dtoArgument.getUpc()).isEqualTo(123456789012L);

    }
}