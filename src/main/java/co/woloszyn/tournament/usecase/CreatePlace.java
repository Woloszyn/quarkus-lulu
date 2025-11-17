package co.woloszyn.tournament.usecase;

import co.woloszyn.customer.entity.Customer;
import co.woloszyn.tournament.dto.CreatePlaceRequestDTO;
import co.woloszyn.tournament.dto.PlaceDTO;
import co.woloszyn.tournament.entity.Place;
import co.woloszyn.tournament.repository.PlaceRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;

import static co.woloszyn.common.AssertionUtils.*;

@ApplicationScoped
public class CreatePlace {

    @Inject
    PlaceRepository placeRepository;

    @Inject
    GetPlace getPlace;

    @Transactional
    public PlaceDTO createPlace(CreatePlaceRequestDTO requestDTO, Long customerId) {
        validateRequest(requestDTO);

        Place place = new Place();
        place.setCustomer(placeRepository.getEntityManager().getReference(Customer.class, customerId));
        place.setLocalName(requestDTO.getName());
        place.setCity(requestDTO.getCity());
        place.setCountry(requestDTO.getCountry());
        place.setState(requestDTO.getState());
        place.setStreet(requestDTO.getStreet());
        place.setUrl(requestDTO.getUrl());
        place.setHouseNumber(requestDTO.getHouseNumber());
        place.setNeighborhood(requestDTO.getNeighborhood());
        place.setOnline(requestDTO.getOnline());
        place.setCreatedAt(LocalDateTime.now());
        place.setUpdatedAt(LocalDateTime.now());

        place = placeRepository.save(place);
        return getPlace.getById(place.getId());
    }

    private void validateRequest(CreatePlaceRequestDTO requestDTO) {
        assertNotNull(requestDTO, "CreatePlaceRequestDTO must not be null");
        assertStringNotEmpty(requestDTO.getName(), "Place name must not be blank");
        if (requestDTO.getOnline()) {
            assertStringNotEmpty(requestDTO.getUrl(), "Place URL must not be blank for online places");
        } else {
            assertStringNotEmpty(requestDTO.getStreet(), "Place street must not be blank for physical places");
            assertStringNotEmpty(requestDTO.getCity(), "Place city must not be blank for physical places");
            assertStringNotEmpty(requestDTO.getCountry(), "Place country must not be blank for physical places");
            assertStringNotEmpty(requestDTO.getNeighborhood(), "Place neighborhood must not be blank for physical places");
            assertStringNotEmpty(requestDTO.getHouseNumber(), "Place house number must not be blank for physical places");
        }
    }

}
