package co.woloszyn.tournament.repository;

import co.woloszyn.tournament.entity.TournamentSubscription;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import java.util.List;

public interface TournamentSubscriptionRepository extends PanacheRepositoryBase<TournamentSubscription, Long> {
    TournamentSubscription findById(Long id);
    List<TournamentSubscription> listByTournamentId(Long tournamentId);
    List<TournamentSubscription> listByCustomerId(Long customerId);
    TournamentSubscription save(TournamentSubscription subscription);
    boolean deleteById(Long id);

    boolean existsByTournamentIdAndCustomerId(Long tournamentId, Long customerId);

    Long countByTournamentId(Long tournamentId);
}

