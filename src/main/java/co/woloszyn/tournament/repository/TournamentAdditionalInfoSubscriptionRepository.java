package co.woloszyn.tournament.repository;

import co.woloszyn.tournament.entity.TournamentAdditionalInfoSubscription;
import java.util.List;

public interface TournamentAdditionalInfoSubscriptionRepository {
    TournamentAdditionalInfoSubscription findById(Long id);
    List<TournamentAdditionalInfoSubscription> listByAdditionalInfoId(Long additionalInfoId);
    TournamentAdditionalInfoSubscription save(TournamentAdditionalInfoSubscription subscription);
    boolean deleteById(Long id);
}

