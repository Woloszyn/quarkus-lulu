package co.woloszyn.tournament.repository;

import co.woloszyn.tournament.entity.TournamentAdditionalInfo;
import java.util.List;

public interface TournamentAdditionalInfoRepository {
    TournamentAdditionalInfo findById(Long id);
    List<TournamentAdditionalInfo> listByTournamentId(Long tournamentId);
    TournamentAdditionalInfo save(TournamentAdditionalInfo info);
    boolean deleteById(Long id);
}

