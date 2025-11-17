package co.woloszyn.tournament.usecase;

import co.woloszyn.tournament.dto.TournamentMatchDTO;
import co.woloszyn.tournament.dto.TournamentSubscriptionDTO;
import co.woloszyn.tournament.entity.TournamentMatchEntity;
import co.woloszyn.tournament.entity.TournamentSubscription;
import co.woloszyn.tournament.repository.TournamentMatchRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class CreateTournamentMatch {

    @Inject
    GetTournamentSubscription getTournamentSubscription;

    @Inject
    TournamentMatchRepository tournamentMatchRepository;

    @Transactional
    public List<TournamentMatchDTO> createMatches(Long tournamentId) {
        if (!tournamentMatchRepository.findByTournamentId(tournamentId).isEmpty()) {
            throw new BadRequestException("Matches have already been created for this tournament.");
        }

        List<TournamentSubscriptionDTO> subscriptions = getTournamentSubscription.getSubscriptionByTournamentId(tournamentId);
        List<TournamentMatchDTO> matches = new ArrayList<>();

        if (subscriptions == null || subscriptions.size() < 2) {
            return matches;
        }

        for (int i = 0; i < subscriptions.size(); i++) {
            TournamentSubscriptionDTO home = subscriptions.get(i);
            for (int j = i + 1; j < subscriptions.size(); j++) {
                TournamentSubscriptionDTO away = subscriptions.get(j);

                TournamentMatchDTO match = new TournamentMatchDTO();
                match.setHomePlayerId(home.getId());
                match.setAwayPlayerId(away.getId());
                matches.add(match);
            }
        }

        matches.forEach(m -> {
            TournamentMatchEntity tournamentMatchEntity = new TournamentMatchEntity();
            tournamentMatchEntity.setCreatedAt(LocalDateTime.now());
            tournamentMatchEntity.setUpdatedAt(LocalDateTime.now());
            tournamentMatchEntity.setHomeTeam(tournamentMatchRepository.getEntityManager().getReference(TournamentSubscription.class, m.getHomePlayerId()));
            tournamentMatchEntity.setVersusTeam(tournamentMatchRepository.getEntityManager().getReference(TournamentSubscription.class, m.getAwayPlayerId()));

            tournamentMatchRepository.persist(tournamentMatchEntity);
        });

        return matches;
    }

    @Transactional
    public void setWinner(Long matchId, Long winnerSubscriptionId, String scoreBoard) {
        TournamentMatchEntity match = tournamentMatchRepository.findById(matchId);
        if (match == null) {
            throw new BadRequestException("Match not found.");
        }
        tournamentMatchRepository.updateWinner(winnerSubscriptionId, matchId, scoreBoard);
    }

}
