package co.woloszyn.tournament.usecase;

import co.woloszyn.tournament.dto.GameTableRowDTO;
import co.woloszyn.tournament.dto.TournamentMatchDTO;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@ApplicationScoped
public class GetTournamentTable {

    private final GetTournamentMatch getTournamentMatch;

    public GetTournamentTable(GetTournamentMatch getTournamentMatch) {
        this.getTournamentMatch = getTournamentMatch;
    }

    public List<GameTableRowDTO> getTable(Long tournamentId) {
        List<TournamentMatchDTO> matches = getTournamentMatch.getAllByTournamentId(tournamentId);
        List<TournamentMatchDTO> matchesCompleted = matches.stream().filter(m -> m.getHomeScore() != null && m.getAwayScore() != null).toList();
        List<Long> playerIds = matches.stream()
                .flatMap(m -> Stream.of(m.getHomePlayerId(), m.getAwayPlayerId()))
                .distinct()
                .toList();
        return generateTable(playerIds, matchesCompleted);
    }

    private List<GameTableRowDTO> generateTable(List<Long> playerIds, List<TournamentMatchDTO> matchesCompleted) {
        List<GameTableRowDTO> table = new ArrayList<>();
        for (Long playerId: playerIds) {
            String playerName = "";
            int score = 0;
            int gamesPlayed = 0;
            int pointScored = 0;
            int pointConceded = 0;
            List<TournamentMatchDTO> playerMatches = matchesCompleted.stream()
                    .filter(m -> m.getHomePlayerId().equals(playerId) || m.getAwayPlayerId().equals(playerId))
                    .toList();
            for (TournamentMatchDTO match: playerMatches) {
                boolean isHome = match.getHomePlayerId().equals(playerId);
                playerName = isHome ? match.getHomePlayer() : match.getAwayPlayer();
                gamesPlayed += 1;
                pointScored += isHome ? match.getHomeScore() : match.getAwayScore();
                pointConceded += isHome ? match.getAwayScore() : match.getHomeScore();
                if(match.getWinnerId().equals(playerId)) {
                    score += 1;
                }
            }
            GameTableRowDTO row = new GameTableRowDTO(playerName, score, gamesPlayed, pointScored - pointConceded);
            table.add(row);
        }
        return table.stream().sorted(
                (r1, r2) -> {
                    if (r2.getScore() != r1.getScore()) {
                        return Integer.compare(r2.getScore(), r1.getScore());
                    } else if (r2.getPointDiference() != r1.getPointDiference()) {
                        return Integer.compare(r2.getPointDiference(), r1.getPointDiference());
                    } else {
                        return 0;
                    }
                }
        ).toList();
    }

}
