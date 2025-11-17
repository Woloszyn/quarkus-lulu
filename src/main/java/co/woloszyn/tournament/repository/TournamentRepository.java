package co.woloszyn.tournament.repository;

import co.woloszyn.tournament.entity.Tournament;
import co.woloszyn.tournament.enumeration.TournamentStatus;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import java.util.List;

public interface TournamentRepository extends PanacheRepositoryBase<Tournament, Long> {
    Tournament findById(Long id);
    List<Tournament> listAll();
    Tournament findByDescription(String description);
    Tournament save(Tournament tournament);
    void updateStatus(Long id, TournamentStatus status);
    boolean deleteById(Long id);
    List<Tournament> listByPlaceId(Long placeId);
    List<Tournament> listByCustomerId(Long customerId);
}
