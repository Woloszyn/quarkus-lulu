package co.woloszyn.tournament.usecase;

import co.woloszyn.tournament.dto.TournamentDTO;
import co.woloszyn.tournament.dto.TournamentSubscriptionDTO;
import co.woloszyn.tournament.entity.TournamentSubscription;
import co.woloszyn.tournament.repository.TournamentSubscriptionRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class GetTournamentSubscription {

    @Inject
    TournamentSubscriptionRepository tournamentSubscriptionRepository;

    public boolean isCustomerSubscribedToTournament(Long tournamentId, Long customerId) {
        return tournamentSubscriptionRepository.existsByTournamentIdAndCustomerId(tournamentId, customerId);
    }

    public Long countTotalTournament(Long tournamentId) {
        return tournamentSubscriptionRepository.countByTournamentId(tournamentId);
    }

    public List<TournamentSubscriptionDTO> getSubscriptionsByCustomerId(Long customerId) {
        List<TournamentSubscription> tournamentSubscriptions = tournamentSubscriptionRepository.listByCustomerId(customerId);
        return tournamentSubscriptions.stream().map(t->
        {
            TournamentDTO tournamentDTO = new TournamentDTO();
            tournamentDTO.setId(t.getTournament().getId());
            tournamentDTO.setName(t.getTournament().getName());
            return new TournamentSubscriptionDTO(t.getId(),tournamentDTO, customerId);
        }).toList();
    }

    public TournamentSubscriptionDTO getById(Long subscriptionId) {
        TournamentSubscription t = tournamentSubscriptionRepository.findById(subscriptionId);
        if (t == null) {
            return null;
        }
        TournamentDTO tournamentDTO = new TournamentDTO();
        tournamentDTO.setId(t.getTournament().getId());
        tournamentDTO.setName(t.getTournament().getName());
        return new TournamentSubscriptionDTO(t.getId(),tournamentDTO, t.getCustomer().getId());
    }

    public List<TournamentSubscriptionDTO> getSubscriptionByTournamentId(Long tournamentId) {
        List<TournamentSubscription> tournamentSubscriptions = tournamentSubscriptionRepository.listByTournamentId(tournamentId);
        return tournamentSubscriptions.stream().map(t->
        {
            TournamentDTO tournamentDTO = new TournamentDTO();
            tournamentDTO.setId(t.getTournament().getId());
            tournamentDTO.setName(t.getTournament().getName());
            return new TournamentSubscriptionDTO(t.getId(),tournamentDTO, t.getCustomer().getId());
        }).toList();
    }
}
