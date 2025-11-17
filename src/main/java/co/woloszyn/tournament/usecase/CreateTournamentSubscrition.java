package co.woloszyn.tournament.usecase;

import co.woloszyn.customer.entity.Customer;
import co.woloszyn.tournament.dto.TournamentDTO;
import co.woloszyn.tournament.dto.TournamentSubscriptionDTO;
import co.woloszyn.tournament.entity.Tournament;
import co.woloszyn.tournament.entity.TournamentSubscription;
import co.woloszyn.tournament.repository.TournamentSubscriptionRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;

import static co.woloszyn.common.AssertionUtils.assertBoolean;
import static co.woloszyn.common.AssertionUtils.assertNotNull;

@ApplicationScoped
public class CreateTournamentSubscrition {

    @Inject
    TournamentSubscriptionRepository tournamentSubscriptionRepository;

    @Inject
    GetTournamentSubscription getTournamentSubscription;

    @Inject
    GetTournament getTournament;

    @Transactional
    public TournamentSubscriptionDTO createSubscription(Long tournamentId, Long customerId) {
        assertNotNull(tournamentId, "Tournament ID must not be null");
        assertNotNull(customerId, "Customer ID must not be null");

        assertBoolean(
            getTournamentSubscription.isCustomerSubscribedToTournament(tournamentId, customerId),
    false,
    "Customer is already subscribed to this tournament");

        TournamentSubscription tournamentSubscription = new TournamentSubscription();
        tournamentSubscription.setTournament(tournamentSubscriptionRepository.getEntityManager().getReference(Tournament.class, tournamentId));
        tournamentSubscription.setCustomer(tournamentSubscriptionRepository.getEntityManager().getReference(Customer.class, customerId));
        tournamentSubscription.setCreatedAt(LocalDateTime.now());
        tournamentSubscription.setUpdatedAt(LocalDateTime.now());

        tournamentSubscription = tournamentSubscriptionRepository.save(tournamentSubscription);
        TournamentDTO tournamentDTO = getTournament.getById(tournamentId, customerId);
        return new TournamentSubscriptionDTO(tournamentSubscription.getId(), tournamentDTO, customerId);
    }

}
