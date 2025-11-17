package co.woloszyn.tournament.usecase.adapter;

import co.woloszyn.tournament.entity.TournamentSubscription;
import co.woloszyn.tournament.repository.TournamentSubscriptionRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class TournamentSubscriptionRepositoryImpl implements TournamentSubscriptionRepository {

    @Override
    public TournamentSubscription findById(Long id) {
        return TournamentSubscription.findById(id);
    }

    @Override
    public List<TournamentSubscription> listByTournamentId(Long tournamentId) {
        return TournamentSubscription.list("tournament.id", tournamentId);
    }

    @Override
    public List<TournamentSubscription> listByCustomerId(Long customerId) {
        return TournamentSubscription.list("customer.id", customerId);
    }

    @Override
    public TournamentSubscription save(TournamentSubscription subscription) {
        TournamentSubscription.persist(subscription);
        return subscription;
    }

    @Override
    public boolean deleteById(Long id) {
        return TournamentSubscription.deleteById(id);
    }

    @Override
    public boolean existsByTournamentIdAndCustomerId(Long tournamentId, Long customerId) {
        return count("tournament.id = ?1 and customer.id = ?2", tournamentId, customerId) > 0;
    }

    @Override
    public Long countByTournamentId(Long tournamentId) {
        return count("tournament.id = ?1", tournamentId);
    }
}

