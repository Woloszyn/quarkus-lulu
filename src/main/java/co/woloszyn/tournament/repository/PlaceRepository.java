package co.woloszyn.tournament.repository;

import co.woloszyn.tournament.entity.Place;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import java.util.List;

public interface PlaceRepository extends PanacheRepository<Place> {
    Place findById(Long id);
    List<Place> listByCustomerId(Long customerId);
    List<Place> listByCustomerIdAndOnline(Long customerId, boolean online);
    Place save(Place place);
    boolean deleteById(Long id);
}

