package co.woloszyn.tournament.usecase;

import co.woloszyn.customer.entity.Customer;
import co.woloszyn.tournament.dto.CreateTournamentRequestDTO;
import co.woloszyn.tournament.dto.PlaceDTO;
import co.woloszyn.tournament.dto.TournamentDTO;
import co.woloszyn.tournament.entity.Place;
import co.woloszyn.tournament.entity.Tournament;
import co.woloszyn.tournament.enumeration.TournamentStatus;
import co.woloszyn.tournament.repository.TournamentRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;

import static co.woloszyn.common.AssertionUtils.*;

@ApplicationScoped
public class CreateTournament {

    @Inject
    TournamentRepository tournamentRepository;

    @Inject
    GetTournament getTournament;

    @Inject
    CreateTournamentMatch createTournamentMatch;

    @Inject
    GetPlace getPlace;

    @Transactional
    public TournamentDTO createTournament(CreateTournamentRequestDTO requestDTO, Long customerId) {
        validateRequest(requestDTO);

        PlaceDTO place = getPlace.getById(requestDTO.getPlaceId());
        assertNotNull(place, "Place must exist");
        assertBoolean(place.getCustomerId().equals(customerId), true, "Place must belong to the customer");

        Tournament t = new Tournament();
        t.setName(requestDTO.getName());
        t.setDescription(requestDTO.getDescription());
        t.setDate(requestDTO.getDate());

        t.setType(requestDTO.getType());
        t.setPrice(requestDTO.getPrice());
        t.setPlace(tournamentRepository.getEntityManager().getReference(Place.class, requestDTO.getPlaceId()));
        t.setCustomer(tournamentRepository.getEntityManager().getReference(Customer.class, customerId));
        t.setCreatedAt(LocalDateTime.now());
        t.setUpdatedAt(LocalDateTime.now());
        t.setOnline(requestDTO.getOnline());
        t.setStatus(TournamentStatus.CREATED);
        t.setMaxQuantity(requestDTO.getMaxQuantity());

        t = tournamentRepository.save(t);
        return getTournament.getById(t.getId(), customerId);
    }

    @Transactional
    public void cancelTournament(Long tournamentId, Long customerId) {
        Tournament t = tournamentRepository.findById(tournamentId);
        assertNotNull(t, "Tournament must exist");
        assertBoolean(t.getCustomer().getId().equals(customerId), true, "Tournament must belong to the customer");
        assertBoolean(t.getStatus() == TournamentStatus.CREATED, true, "Only CREATED tournaments can be cancelled");

        tournamentRepository.updateStatus(tournamentId, TournamentStatus.CANCELED);
    }

    private void validateRequest(CreateTournamentRequestDTO requestDTO) {
        assertNotNull(requestDTO, "CreateTournamentRequestDTO must not be null");
        assertStringNotEmpty(requestDTO.getName(), "Tournament name must not be blank");
        assertStringNotEmpty(requestDTO.getDescription(), "Tournament description must not be blank");
        assertNotNull(requestDTO.getDate(), "Tournament date must not be null");
        assertNotNull(requestDTO.getType(), "Tournament type must not be null");
        assertNotNull(requestDTO.getPlaceId(), "Place id must not be null");
    }

    @Transactional
    public void startTournament(Long tournamentId, Long customerId) {
        Tournament t = tournamentRepository.findById(tournamentId);
        assertNotNull(t, "Tournament must exist");
        assertBoolean(t.getCustomer().getId().equals(customerId), true, "Tournament must belong to the customer");
        assertBoolean(t.getStatus() == TournamentStatus.CREATED, true, "Only CREATED tournaments can be started");
        createTournamentMatch.createMatches(tournamentId);
        tournamentRepository.updateStatus(tournamentId, TournamentStatus.ONGOING);
    }
}
