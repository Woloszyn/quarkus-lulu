package co.woloszyn.tournament.usecase.adapter;

import co.woloszyn.tournament.entity.Place;
import co.woloszyn.tournament.repository.PlaceRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class PlaceRepositoryImpl implements PlaceRepository {

    @Override
    public Place findById(Long id) {
        return Place.findById(id);
    }

    @Override
    public List<Place> listByCustomerId(Long customerId) {
        return Place.list("customer.id", customerId);
    }

    @Override
    public List<Place> listByCustomerIdAndOnline(Long customerId, boolean online) {
        return Place.find("customer.id = :customer_id and online = :online", Parameters.with("customer_id", customerId).and("online", online)).list();
    }

    @Override
    public Place save(Place place) {
        Place.persist(place);
        return place;
    }

    @Override
    public boolean deleteById(Long id) {
        return Place.deleteById(id);
    }
}
