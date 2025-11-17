package co.woloszyn.tournament.usecase.adapter;

import co.woloszyn.tournament.entity.TournamentAdditionalInfo;
import co.woloszyn.tournament.repository.TournamentAdditionalInfoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class TournamentAdditionalInfoRepositoryImpl implements TournamentAdditionalInfoRepository {

    @Override
    public TournamentAdditionalInfo findById(Long id) {
        return TournamentAdditionalInfo.findById(id);
    }

    @Override
    public List<TournamentAdditionalInfo> listByTournamentId(Long tournamentId) {
        return TournamentAdditionalInfo.list("tournament.id", tournamentId);
    }

    @Override
    public TournamentAdditionalInfo save(TournamentAdditionalInfo info) {
        TournamentAdditionalInfo.persist(info);
        return info;
    }

    @Override
    public boolean deleteById(Long id) {
        return TournamentAdditionalInfo.deleteById(id);
    }
}

