package springframework.springreactivemongo.mapper;

import org.mapstruct.Mapper;
import springframework.springreactivemongo.domain.Beer;
import springframework.springreactivemongo.model.BeerDTO;

@Mapper
public interface BeerMapper {

    BeerDTO beerToBeerDto(Beer beer);

    Beer beerDtoToBeer(BeerDTO beerDTO);
}
