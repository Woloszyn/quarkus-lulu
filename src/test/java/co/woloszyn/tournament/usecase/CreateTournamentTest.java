package co.woloszyn.tournament.usecase;

import co.woloszyn.customer.entity.Customer;
import co.woloszyn.tournament.dto.CreateTournamentRequestDTO;
import co.woloszyn.tournament.dto.PlaceDTO;
import co.woloszyn.tournament.dto.TournamentDTO;
import co.woloszyn.tournament.entity.Place;
import co.woloszyn.tournament.entity.Tournament;
import co.woloszyn.tournament.enumeration.TournamentStatus;
import co.woloszyn.tournament.enumeration.TournamentType;
import co.woloszyn.tournament.repository.TournamentRepository;
import io.quarkus.test.InjectMock;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.BadRequestException;
import jakarta.inject.Inject;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@QuarkusTest
class CreateTournamentTest {

    @InjectMock
    TournamentRepository tournamentRepository;

    @InjectMock
    GetTournament getTournament;

    @InjectMock
    CreateTournamentMatch createTournamentMatch;

    @InjectMock
    GetPlace getPlace;

    // EntityManager criado manualmente no setup como mock
    EntityManager entityManager;

    @Inject
    CreateTournament createTournament;

    @BeforeEach
    void setUp() {
        entityManager = mock(EntityManager.class);
        when(tournamentRepository.getEntityManager()).thenReturn(entityManager);
    }

    @Test
    void shouldCreatePhysicalTournament() {
        CreateTournamentRequestDTO req = new CreateTournamentRequestDTO();
        req.setName("My Tournament");
        req.setDescription("Desc");
        req.setDate(LocalDateTime.now().plusDays(5));
        req.setType(TournamentType.BASQUETE);
        req.setPrice(BigDecimal.valueOf(15.5));
        req.setPlaceId(1L);
        req.setOnline(false);
        req.setMaxQuantity(32);

        Long customerId = 10L;
        // place DTO returned by GetPlace must indicate ownership
        PlaceDTO placeDto = new PlaceDTO();
        placeDto.setId(1L);
        placeDto.setCustomerId(customerId);
        when(getPlace.getById(1L)).thenReturn(placeDto);

        // entity manager references
        Place placeRef = new Place();
        placeRef.setId(1L);
        when(entityManager.getReference(Place.class, 1L)).thenReturn(placeRef);

        Customer customerRef = new Customer();
        customerRef.setId(customerId);
        when(entityManager.getReference(Customer.class, customerId)).thenReturn(customerRef);

        when(tournamentRepository.save(any(Tournament.class))).thenAnswer(invocation -> {
            Tournament t = invocation.getArgument(0);
            t.setId(100L);
            return t;
        });

        TournamentDTO dto = new TournamentDTO();
        dto.setId(100L);
        dto.setName(req.getName());
        when(getTournament.getById(100L, customerId)).thenReturn(dto);

        TournamentDTO result = createTournament.createTournament(req, customerId);

        assertNotNull(result);
        assertEquals(100L, result.getId());
        assertEquals(req.getName(), result.getName());

        ArgumentCaptor<Tournament> captor = ArgumentCaptor.forClass(Tournament.class);
        verify(tournamentRepository).save(captor.capture());
        Tournament saved = captor.getValue();
        assertNotNull(saved.getCustomer());
        assertEquals(customerRef.getId(), saved.getCustomer().getId());
        assertEquals(req.getName(), saved.getName());
        assertFalse(saved.getOnline());
        assertEquals(req.getDescription(), saved.getDescription());
        assertEquals(req.getType(), saved.getType());
        assertEquals(req.getPrice(), saved.getPrice());
        assertEquals(req.getMaxQuantity(), saved.getMaxQuantity());
        assertNotNull(saved.getPlace());
        assertEquals(placeRef.getId(), saved.getPlace().getId());

        verify(getTournament).getById(100L, customerId);
    }

    @Test
    void shouldCreateOnlineTournament() {
        CreateTournamentRequestDTO req = new CreateTournamentRequestDTO();
        req.setName("Online Tournament");
        req.setDescription("Online Desc");
        req.setDate(LocalDateTime.now().plusDays(10));
        req.setType(TournamentType.BRAWL_STARS);
        req.setPrice(BigDecimal.ZERO);
        req.setPlaceId(2L);
        req.setOnline(true);
        req.setMaxQuantity(64);

        Long customerId = 20L;
        PlaceDTO placeDto = new PlaceDTO();
        placeDto.setId(2L);
        placeDto.setCustomerId(customerId);
        when(getPlace.getById(2L)).thenReturn(placeDto);

        Place placeRef = new Place();
        placeRef.setId(2L);
        when(entityManager.getReference(Place.class, 2L)).thenReturn(placeRef);

        Customer customerRef = new Customer();
        customerRef.setId(customerId);
        when(entityManager.getReference(Customer.class, customerId)).thenReturn(customerRef);

        when(tournamentRepository.save(any(Tournament.class))).thenAnswer(invocation -> {
            Tournament t = invocation.getArgument(0);
            t.setId(200L);
            return t;
        });

        TournamentDTO dto = new TournamentDTO();
        dto.setId(200L);
        dto.setName(req.getName());
        when(getTournament.getById(200L, customerId)).thenReturn(dto);

        TournamentDTO result = createTournament.createTournament(req, customerId);

        assertNotNull(result);
        assertEquals(200L, result.getId());
        assertEquals(req.getName(), result.getName());

        ArgumentCaptor<Tournament> captor = ArgumentCaptor.forClass(Tournament.class);
        verify(tournamentRepository).save(captor.capture());
        Tournament saved = captor.getValue();
        assertNotNull(saved.getCustomer());
        assertEquals(customerRef.getId(), saved.getCustomer().getId());
        assertEquals(req.getName(), saved.getName());
        assertTrue(saved.getOnline());
        assertEquals(req.getDescription(), saved.getDescription());
        assertEquals(req.getType(), saved.getType());
        assertEquals(req.getPrice(), saved.getPrice());
        assertEquals(req.getMaxQuantity(), saved.getMaxQuantity());
        assertNotNull(saved.getPlace());
        assertEquals(placeRef.getId(), saved.getPlace().getId());

        verify(getTournament).getById(200L, customerId);
    }

    @Test
    void nullRequestShouldThrow() {
        assertThrows(BadRequestException.class, () -> createTournament.createTournament(null, 1L));
    }

    @Test
    void blankNameShouldThrow() {
        CreateTournamentRequestDTO req = new CreateTournamentRequestDTO();
        req.setName("   ");
        req.setDescription("desc");
        req.setDate(LocalDateTime.now().plusDays(1));
        req.setType(TournamentType.BRAWL_STARS);
        req.setPlaceId(1L);
        assertThrows(BadRequestException.class, () -> createTournament.createTournament(req, 1L));
    }

    @Test
    void missingPlaceIdShouldThrow() {
        CreateTournamentRequestDTO req = new CreateTournamentRequestDTO();
        req.setName("T");
        req.setDescription("desc");
        req.setDate(LocalDateTime.now().plusDays(1));
        req.setType(TournamentType.BRAWL_STARS);
        req.setPlaceId(null);
        assertThrows(BadRequestException.class, () -> createTournament.createTournament(req, 1L));
    }
}

