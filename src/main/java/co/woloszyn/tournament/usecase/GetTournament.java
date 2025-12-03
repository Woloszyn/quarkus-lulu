package co.woloszyn.tournament.usecase;

import co.woloszyn.tournament.dto.PlaceDTO;
import co.woloszyn.tournament.dto.TournamentDTO;
import co.woloszyn.tournament.entity.Tournament;
import co.woloszyn.tournament.enumeration.TournamentStatus;
import co.woloszyn.tournament.repository.TournamentRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class GetTournament {
    @Inject
    TournamentRepository tournamentRepository;

    @Inject
    GetPlace getPlace;

    @Inject
    GetTournamentSubscription getTournamentSubscription;

    public List<TournamentDTO> getTournamentsByPlaceId(Long placeId, Long customerIdRequest) {
        List<Tournament> tournaments = tournamentRepository.listByPlaceId(placeId);
        return tournaments.stream()
                .map(t-> this.mapToDTO(t, customerIdRequest))
                .toList();
    }

    public List<TournamentDTO> getTournamentsByCustomerId(Long customerId) {
        List<Tournament> tournaments = tournamentRepository.listByCustomerId(customerId);
        return tournaments.stream()
                .map(t-> this.mapToDTO(t, customerId))
                .toList();
    }

    public List<TournamentDTO> getAllCreated(Long customerIdRequest) {
        List<Tournament> tournaments = tournamentRepository.listAll();
        return tournaments.stream()
                .map(t-> this.mapToDTO(t, customerIdRequest))
                .filter(t -> t.getStatus() == TournamentStatus.CREATED || t.getStatus() == TournamentStatus.ONGOING)
                .toList();
    }

    public TournamentDTO getById(Long tournamentId, Long customerIdRequest) {
        Tournament t = tournamentRepository.findById(tournamentId);
        return mapToDTO(t, customerIdRequest);
    }

    @Transactional
    public TournamentDTO mapToDTO(Tournament t, Long customerIdRequest) {
        if (t == null) return null;

        if (t.getDate().isBefore(LocalDateTime.now())) {
            tournamentRepository.updateStatus(t.getId(), TournamentStatus.COMPLETED);
            t.setStatus(TournamentStatus.COMPLETED);
        }

        Long totalSubscribed = getTournamentSubscription.countTotalTournament(t.getId());
        Boolean isSubscribed = false;
        if (customerIdRequest != null) {
            isSubscribed = getTournamentSubscription.isCustomerSubscribedToTournament(t.getId(), customerIdRequest);
        }

        TournamentDTO dto = new TournamentDTO();
        dto.setId(t.getId());
        dto.setName(t.getName());
        dto.setDescription(t.getDescription());
        dto.setDate(t.getDate());
        dto.setType(t.getType());
        dto.setImageUrl(t.getType().getImageUrl());
        dto.setPrice(t.getPrice());
        dto.setOnline(t.getOnline());
        dto.setCustomerId(t.getCustomer().getId());
        if (t.getPlace() != null) {
            PlaceDTO place = getPlace.getById(t.getPlace().getId());
            dto.setPlace(place);
        }
        dto.setCreatedAt(t.getCreatedAt());
        dto.setUpdatedAt(t.getUpdatedAt());
        dto.setStatus(t.getStatus());
        dto.setMaxQuantity(t.getMaxQuantity());
        dto.setPlayersSubscribed(totalSubscribed.intValue());
        dto.setPlayerIsSubscribed(isSubscribed);
        return dto;
    }
}

