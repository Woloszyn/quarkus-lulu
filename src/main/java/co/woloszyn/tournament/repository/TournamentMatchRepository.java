package co.woloszyn.tournament.repository;

import co.woloszyn.tournament.entity.TournamentMatchEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import java.util.List;
import java.util.Optional;

public interface TournamentMatchRepository extends PanacheRepositoryBase<TournamentMatchEntity, Long> {

    Optional<TournamentMatchEntity> findByTournamentIdAndMatchId(Long tournamentId, Long matchId);
    List<TournamentMatchEntity> findByTournamentId(Long tournamentId);

    void updateWinner(Long winnerId, Long matchId, String scoreBoard);
}
