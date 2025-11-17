package co.woloszyn.tournament.usecase;

import co.woloszyn.tournament.dto.PlaceDTO;
import co.woloszyn.tournament.entity.Place;
import co.woloszyn.tournament.repository.PlaceRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class GetPlace {
    @Inject
    PlaceRepository placeRepository;

    public List<PlaceDTO> getPlacesByCustomerId(Long customerId) {
        List<Place> places = placeRepository.listByCustomerId(customerId);
        return places.stream()
                .map(this::mapToDTO)
                .toList();
    }

    public List<PlaceDTO> getPlacesByCustomerIdAndOnline(Long customerId, boolean online) {
        System.out.println("Getting places for customerId: " + customerId + " with online: " + online);
        List<Place> places = placeRepository.listByCustomerIdAndOnline(customerId, online);
        return places.stream()
                .map(this::mapToDTO)
                .toList();
    }

    public PlaceDTO getById(Long placeId) {
        Place place = placeRepository.findById(placeId);
        return mapToDTO(place);
    }

    private PlaceDTO mapToDTO(Place place) {
        if (place == null) {
            return null;
        }

        PlaceDTO dto = new PlaceDTO();
        dto.setId(place.getId());
        if (place.getCustomer() != null) {
            dto.setCustomerId(place.getCustomer().getId());
        }
        dto.setName(place.getLocalName());
        dto.setOnline(place.getOnline());
        dto.setUrl(place.getUrl());
        dto.setLatitude(place.getLatitude());
        dto.setLongitude(place.getLongitude());
        dto.setCreatedAt(place.getCreatedAt());
        dto.setUpdatedAt(place.getUpdatedAt());
        dto.setCity(place.getCity());
        dto.setState(place.getState());
        dto.setCountry(place.getCountry());
        dto.setStreet(place.getStreet());
        dto.setHouseNumber(place.getHouseNumber());
        dto.setNeighborhood(place.getNeighborhood());

        return dto;
    }

}
