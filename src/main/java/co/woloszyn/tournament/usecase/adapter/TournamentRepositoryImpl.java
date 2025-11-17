package co.woloszyn.tournament.usecase.adapter;

import co.woloszyn.tournament.entity.Tournament;
import co.woloszyn.tournament.enumeration.TournamentStatus;
import co.woloszyn.tournament.repository.TournamentRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class TournamentRepositoryImpl implements TournamentRepository {

    @Override
    public Tournament findById(Long id) {
        return Tournament.findById(id);
    }

    @Override
    public List<Tournament> listAll() {
        return Tournament.listAll();
    }

    @Override
    public Tournament findByDescription(String description) {
        return Tournament.find("description", description).firstResult();
    }

    @Override
    public Tournament save(Tournament tournament) {
        Tournament.persist(tournament);
        return tournament;
    }

    @Override
    public void updateStatus(Long id, TournamentStatus status) {
        Tournament.update("status = ?1 where id = ?2", status, id);
    }

    @Override
    public boolean deleteById(Long id) {
        return Tournament.deleteById(id);
    }

    @Override
    public List<Tournament> listByPlaceId(Long placeId) {
        return Tournament.find("place.id", placeId).list();
    }

    @Override
    public List<Tournament> listByCustomerId(Long customerId) {
        return Tournament.find("customer.id", customerId).list();
    }
}
