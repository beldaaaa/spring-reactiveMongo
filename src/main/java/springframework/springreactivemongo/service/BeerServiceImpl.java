package springframework.springreactivemongo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import springframework.springreactivemongo.mapper.BeerMapper;
import springframework.springreactivemongo.model.BeerDTO;
import springframework.springreactivemongo.repository.BeerRepository;

@Service
@RequiredArgsConstructor
public class BeerServiceImpl implements BeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    @Override
    public Mono<BeerDTO> saveBeer(Mono<BeerDTO> beerDTO) {
        return beerDTO.map(beerMapper::beerDtoToBeer)
                .flatMap(beerRepository::save)//repository is going to return a new publisher
                .map(beerMapper::beerToBeerDTO);
    }

    @Override
    public Mono<BeerDTO> findBeerById(String beerId) {
        return null;
    }
}
