package co.woloszyn.tournament.usecase;

import co.woloszyn.tournament.dto.TournamentDTO;
import co.woloszyn.tournament.dto.TournamentMatchDTO;
import co.woloszyn.tournament.dto.TournamentSubscriptionDTO;
import co.woloszyn.tournament.entity.TournamentMatchEntity;
import co.woloszyn.tournament.entity.TournamentSubscription;
import co.woloszyn.tournament.repository.TournamentMatchRepository;
import io.quarkus.test.InjectMock;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.BadRequestException;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
class CreateTournamentMatchTest {

    @InjectMock
    TournamentMatchRepository tournamentMatchRepository;

    @InjectMock
    GetTournamentSubscription getTournamentSubscription;

    // EntityManager created manually in setup as mock
    EntityManager entityManager;

    @Inject
    CreateTournamentMatch createTournamentMatch;

    @BeforeEach
    void setUp() {
        entityManager = mock(EntityManager.class);
        when(tournamentMatchRepository.getEntityManager()).thenReturn(entityManager);
    }

    @Test
    void shouldCreateMatches() {
        Long tournamentId = 5L;
        when(tournamentMatchRepository.findByTournamentId(tournamentId)).thenReturn(Collections.emptyList());

        TournamentDTO tDto = new TournamentDTO();
        tDto.setId(100L);
        tDto.setName("T1");

        TournamentSubscriptionDTO s1 = new TournamentSubscriptionDTO(1L, tDto, 11L);
        TournamentSubscriptionDTO s2 = new TournamentSubscriptionDTO(2L, tDto, 12L);
        TournamentSubscriptionDTO s3 = new TournamentSubscriptionDTO(3L, tDto, 13L);

        List<TournamentSubscriptionDTO> subscriptions = Arrays.asList(s1, s2, s3);
        when(getTournamentSubscription.getSubscriptionByTournamentId(tournamentId)).thenReturn(subscriptions);

        // prepare mocked TournamentSubscription references returned by EntityManager
        TournamentSubscription subRef1 = mock(TournamentSubscription.class);
        when(subRef1.getId()).thenReturn(1L);
        TournamentSubscription subRef2 = mock(TournamentSubscription.class);
        when(subRef2.getId()).thenReturn(2L);
        TournamentSubscription subRef3 = mock(TournamentSubscription.class);
        when(subRef3.getId()).thenReturn(3L);

        when(entityManager.getReference(TournamentSubscription.class, 1L)).thenReturn(subRef1);
        when(entityManager.getReference(TournamentSubscription.class, 2L)).thenReturn(subRef2);
        when(entityManager.getReference(TournamentSubscription.class, 3L)).thenReturn(subRef3);

        // call
        List<TournamentMatchDTO> result = createTournamentMatch.createMatches(tournamentId);

        // expect 3 matches for 3 players (n choose 2)
        assertNotNull(result);
        assertEquals(3, result.size());

        // verify persisted matches
        ArgumentCaptor<TournamentMatchEntity> captor = ArgumentCaptor.forClass(TournamentMatchEntity.class);
        verify(tournamentMatchRepository, times(3)).persist(captor.capture());
        List<TournamentMatchEntity> persisted = captor.getAllValues();

        // Check that persisted entities reference the mocked subscription references
        // Order of persisted corresponds to generation order: (1,2), (1,3), (2,3)
        assertEquals(1L, persisted.get(0).getHomeTeam().getId());
        assertEquals(2L, persisted.get(0).getVersusTeam().getId());

        assertEquals(1L, persisted.get(1).getHomeTeam().getId());
        assertEquals(3L, persisted.get(1).getVersusTeam().getId());

        assertEquals(2L, persisted.get(2).getHomeTeam().getId());
        assertEquals(3L, persisted.get(2).getVersusTeam().getId());
    }

    @Test
    void shouldReturnEmptyWhenLessThanTwoSubscriptions() {
        Long tournamentId = 6L;
        when(tournamentMatchRepository.findByTournamentId(tournamentId)).thenReturn(Collections.emptyList());

        TournamentDTO tDto = new TournamentDTO();
        tDto.setId(200L);
        tDto.setName("T2");

        TournamentSubscriptionDTO only = new TournamentSubscriptionDTO(10L, tDto, 21L);
        when(getTournamentSubscription.getSubscriptionByTournamentId(tournamentId)).thenReturn(Collections.singletonList(only));

        List<TournamentMatchDTO> result = createTournamentMatch.createMatches(tournamentId);
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(tournamentMatchRepository, never()).persist(any(TournamentMatchEntity.class));
    }

    @Test
    void shouldNotCreateIfMatchesAlreadyExist() {
        Long tournamentId = 7L;
        when(tournamentMatchRepository.findByTournamentId(tournamentId)).thenReturn(Collections.singletonList(new TournamentMatchEntity()));

        BadRequestException ex = assertThrows(BadRequestException.class, () -> createTournamentMatch.createMatches(tournamentId));
        assertEquals("Matches have already been created for this tournament.", ex.getMessage());
    }

    @Test
    void setWinnerShouldThrowWhenMatchNotFound() {
        when(tournamentMatchRepository.findById(100L)).thenReturn(null);
        BadRequestException ex = assertThrows(BadRequestException.class, () -> createTournamentMatch.setWinner(100L, 1L, "1-0"));
        assertEquals("Match not found.", ex.getMessage());
    }

    @Test
    void setWinnerShouldCallUpdateWhenMatchExists() {
        TournamentMatchEntity match = new TournamentMatchEntity();
        match.setId(200L);
        when(tournamentMatchRepository.findById(200L)).thenReturn(match);

        createTournamentMatch.setWinner(200L, 11L, "2-1");

        verify(tournamentMatchRepository).updateWinner(11L, 200L, "2-1");
    }
}