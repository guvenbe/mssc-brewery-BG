package guru.springframework.msscbrewery.web.controler.v2;

import guru.springframework.msscbrewery.services.BeerService;
import guru.springframework.msscbrewery.services.v2.BeerServiceV2;
import guru.springframework.msscbrewery.web.model.BeerDto;
import guru.springframework.msscbrewery.web.model.v2.BeerDtoV2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RequestMapping("/api/v2/beer")
@RestController
public class BeerControllerV2 {

    private final BeerServiceV2 beerService;

    public BeerControllerV2(BeerServiceV2 beerServiceV2) {
        this.beerService = beerServiceV2;
    }

    @GetMapping("/{beerId}")
    public ResponseEntity<BeerDtoV2> getBeerById(@PathVariable UUID beerId){
        return new ResponseEntity<>(beerService.getBeerById(beerId), HttpStatus.OK);
    }

    @PostMapping //POST - create new beer
    public ResponseEntity handlePost(@RequestBody BeerDtoV2 beerDto){
//    public ResponseEntity handlePost( BeerDtoV2 beerDtoV2){
        BeerDtoV2 savedDto =beerService.saveNewBeer(beerDto);
        HttpHeaders headers = new HttpHeaders();
        //todo : add hostname to URL
        headers.add("Location", "/api/v2/beer" + savedDto.getId().toString());
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @PutMapping("/{beerId}")
    public ResponseEntity<HttpStatus> handleUpdate(@PathVariable("beerId") UUID beerId,
                                                   @RequestBody BeerDtoV2 beerDtoV2){
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
