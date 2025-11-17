package co.woloszyn.tournament.usecase.adapter;

import co.woloszyn.tournament.entity.TournamentAdditionalInfoSubscription;
import co.woloszyn.tournament.repository.TournamentAdditionalInfoSubscriptionRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class TournamentAdditionalInfoSubscriptionRepositoryImpl implements TournamentAdditionalInfoSubscriptionRepository {

    @Override
    public TournamentAdditionalInfoSubscription findById(Long id) {
        return TournamentAdditionalInfoSubscription.findById(id);
    }

    @Override
    public List<TournamentAdditionalInfoSubscription> listByAdditionalInfoId(Long additionalInfoId) {
        return TournamentAdditionalInfoSubscription.list("additionalInfo.id", additionalInfoId);
    }

    @Override
    public TournamentAdditionalInfoSubscription save(TournamentAdditionalInfoSubscription subscription) {
        TournamentAdditionalInfoSubscription.persist(subscription);
        return subscription;
    }

    @Override
    public boolean deleteById(Long id) {
        return TournamentAdditionalInfoSubscription.deleteById(id);
    }
}

