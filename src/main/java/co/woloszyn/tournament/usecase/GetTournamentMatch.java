package co.woloszyn.tournament.usecase;

import co.woloszyn.customer.dto.CustomerResponseDTO;
import co.woloszyn.tournament.dto.TournamentDTO;
import co.woloszyn.tournament.dto.TournamentMatchDTO;
import co.woloszyn.tournament.dto.TournamentSubscriptionDTO;
import co.woloszyn.tournament.entity.TournamentMatchEntity;
import co.woloszyn.tournament.repository.TournamentMatchRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class GetTournamentMatch {

    @Inject
    TournamentMatchRepository tournamentMatchRepository;

    @Inject
    GetTournament getTournament;

    @Inject
    GetTournamentSubscription getTournamentSubscription;

    @Inject
    GetCustomer getCustomer;

    public List<TournamentMatchDTO> getAllByTournamentId(Long tournamentId) {
        TournamentDTO tournamentDTO = getTournament.getById(tournamentId, null);
        List<TournamentMatchEntity> matches = tournamentMatchRepository.findByTournamentId(tournamentId);
        List<TournamentMatchDTO> dtoMatches = new ArrayList<>();
        matches.forEach((m) -> {
            TournamentSubscriptionDTO homeSubscription = getTournamentSubscription.getById(m.getHomeTeam().getId());
            TournamentSubscriptionDTO awaySubscription = getTournamentSubscription.getById(m.getVersusTeam().getId());

            CustomerResponseDTO homeCustomerDTO = getCustomer.getCustomerById(homeSubscription.getCustomerId());
            CustomerResponseDTO awayCustomerDTO = getCustomer.getCustomerById(awaySubscription.getCustomerId());

            TournamentMatchDTO matchDTO = new TournamentMatchDTO();

            if (m.getWinner() != null) {
                TournamentSubscriptionDTO winnerSubscription = getTournamentSubscription.getById(m.getWinner().getId());
                CustomerResponseDTO winnerDTO = getCustomer.getCustomerById(winnerSubscription.getCustomerId());
                matchDTO.setWinner(winnerDTO.getName());
                matchDTO.setWinnerId(winnerSubscription.getId());
            }

            if (m.getScoreboard() != null) {
                StringBuilder stringBuilderAwayScore = new StringBuilder();
                String stringBuilderHomeScore = String.valueOf(m.getScoreboard().charAt(0)) + m.getScoreboard().charAt(1);
                stringBuilderAwayScore.append(m.getScoreboard().charAt(3)).append(m.getScoreboard().charAt(4));
                matchDTO.setHomeScore(Integer.valueOf(stringBuilderHomeScore));
                matchDTO.setAwayScore(Integer.valueOf(stringBuilderAwayScore.toString()));
            }
            matchDTO.setMatchId(m.getId());
            matchDTO.setHomePlayer(homeCustomerDTO.getName());
            matchDTO.setAwayPlayer(awayCustomerDTO.getName());
            matchDTO.setHomePlayerId(m.getHomeTeam().getId());
            matchDTO.setAwayPlayerId(m.getVersusTeam().getId());

            dtoMatches.add(matchDTO);
        });

        return dtoMatches;
    }

}