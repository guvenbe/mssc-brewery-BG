package guru.springframework.msscbrewery.web.controler.v2;

import guru.springframework.msscbrewery.services.BeerService;
import guru.springframework.msscbrewery.services.v2.BeerServiceV2;
import guru.springframework.msscbrewery.web.model.BeerDto;
import guru.springframework.msscbrewery.web.model.v2.BeerDtoV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.awt.*;

import java.util.ArrayList;
import java.util.UUID;
import java.util.List;

@Slf4j
@Validated
@RequestMapping("/api/v2/beer")
@RequiredArgsConstructor
@RestController
public class BeerControllerV2 {

    private final BeerServiceV2 beerService;

    @GetMapping("/{beerId}")
    public ResponseEntity<BeerDtoV2> getBeerById(@NotNull @PathVariable UUID beerId){
        return new ResponseEntity<>(beerService.getBeerById(beerId), HttpStatus.OK);
    }

    @PostMapping //POST - create new beer
    public ResponseEntity handlePost(@Valid @NotNull @RequestBody BeerDtoV2 beerDto){
//    public ResponseEntity handlePost( BeerDtoV2 beerDtoV2){
        log.debug("in handle post.,.");
        val savedDto =beerService.saveNewBeer(beerDto);
        val headers = new HttpHeaders();
        //todo : add hostname to URL
        headers.add("Location", "/api/v2/beer" + savedDto.getId().toString());
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @PutMapping("/{beerId}")
    public ResponseEntity<HttpStatus> handleUpdate(@PathVariable("beerId") UUID beerId,
                                                   @Valid @RequestBody BeerDtoV2 beerDtoV2){
//    public ResponseEntity handleUpdate(@PathVariable("beerId") UUID beerId,  BeerDtoV2 beerDtoV2){
            beerService.updateBeer(beerId, beerDtoV2);
            return new ResponseEntity(HttpStatus.NO_CONTENT);

    }

    @DeleteMapping("/{beerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
     public void deleteBeer(@PathVariable("beerId") UUID beerId ){
        beerService.deleBeerById(beerId);
    }

}
