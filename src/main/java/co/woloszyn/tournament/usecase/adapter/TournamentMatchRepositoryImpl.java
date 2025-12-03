package co.woloszyn.tournament.usecase.adapter;

import co.woloszyn.tournament.entity.TournamentMatchEntity;
import co.woloszyn.tournament.repository.TournamentMatchRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class TournamentMatchRepositoryImpl implements TournamentMatchRepository {

    @Override
    public Optional<TournamentMatchEntity> findByTournamentIdAndMatchId(Long tournamentId, Long matchId) {
        return find("homeTeam.tournament.id = ?1 and matchId = ?2", tournamentId, matchId).firstResultOptional();
    }

    @Override
    public List<TournamentMatchEntity> findByTournamentId(Long tournamentId) {
        return find("homeTeam.tournament.id = ?1", tournamentId).list();
    }

    @Override
    public void updateWinner(Long winnerId, Long matchId, String scoreBoard) {
        update("winner.id = ?1, scoreboard = ?2, updatedAt = ?3 where id = ?4",
                winnerId, scoreBoard, LocalDateTime.now(), matchId);
    }
}
