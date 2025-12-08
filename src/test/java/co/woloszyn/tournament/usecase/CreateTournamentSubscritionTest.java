package co.woloszyn.tournament.usecase;

import co.woloszyn.customer.entity.Customer;
import co.woloszyn.tournament.dto.TournamentDTO;
import co.woloszyn.tournament.dto.TournamentSubscriptionDTO;
import co.woloszyn.tournament.entity.Tournament;
import co.woloszyn.tournament.entity.TournamentSubscription;
import co.woloszyn.tournament.repository.TournamentSubscriptionRepository;
import io.quarkus.test.InjectMock;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.BadRequestException;
import jakarta.inject.Inject;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@QuarkusTest
class CreateTournamentSubscritionTest {

    @InjectMock
    TournamentSubscriptionRepository tournamentSubscriptionRepository;

    @InjectMock
    GetTournamentSubscription getTournamentSubscription;

    @InjectMock
    GetTournament getTournament;

    // EntityManager criado manualmente no setup como mock
    EntityManager entityManager;

    @Inject
    CreateTournamentSubscrition createTournamentSubscrition;

    @BeforeEach
    void setUp() {
        entityManager = mock(EntityManager.class);
        when(tournamentSubscriptionRepository.getEntityManager()).thenReturn(entityManager);
    }

    @Test
    void shouldCreateSubscription() {
        Long tournamentId = 1L;
        Long customerId = 10L;

        when(getTournamentSubscription.isCustomerSubscribedToTournament(tournamentId, customerId)).thenReturn(false);

        Tournament tournamentRef = new Tournament();
        tournamentRef.setId(tournamentId);
        when(entityManager.getReference(Tournament.class, tournamentId)).thenReturn(tournamentRef);

        Customer customerRef = new Customer();
        customerRef.setId(customerId);
        when(entityManager.getReference(Customer.class, customerId)).thenReturn(customerRef);

        when(tournamentSubscriptionRepository.save(any(TournamentSubscription.class))).thenAnswer(invocation -> {
            TournamentSubscription s = invocation.getArgument(0);
            // simulate DB id assignment
            try {
                java.lang.reflect.Field idField = s.getClass().getDeclaredField("id");
                idField.setAccessible(true);
                idField.set(s, 5L);
            } catch (Exception ignored) {}
            return s;
        });

        TournamentDTO tDto = new TournamentDTO();
        tDto.setId(tournamentId);
        tDto.setName("T-1");
        when(getTournament.getById(tournamentId, customerId)).thenReturn(tDto);

        TournamentSubscriptionDTO result = createTournamentSubscrition.createSubscription(tournamentId, customerId);

        assertNotNull(result);
        assertEquals(5L, result.getId());
        assertEquals(customerId, result.getCustomerId());
        assertNotNull(result.getTournament());
        assertEquals(tournamentId, result.getTournament().getId());

        ArgumentCaptor<TournamentSubscription> captor = ArgumentCaptor.forClass(TournamentSubscription.class);
        verify(tournamentSubscriptionRepository).save(captor.capture());
        TournamentSubscription saved = captor.getValue();
        assertNotNull(saved.getTournament());
        assertNotNull(saved.getCustomer());
        assertEquals(tournamentRef.getId(), saved.getTournament().getId());
        assertEquals(customerRef.getId(), saved.getCustomer().getId());

        verify(getTournament).getById(tournamentId, customerId);
    }

    @Test
    void nullTournamentShouldThrow() {
        BadRequestException ex = assertThrows(BadRequestException.class, () -> createTournamentSubscrition.createSubscription(null, 1L));
        assertEquals("Tournament ID must not be null", ex.getMessage());
    }

    @Test
    void nullCustomerShouldThrow() {
        BadRequestException ex = assertThrows(BadRequestException.class, () -> createTournamentSubscrition.createSubscription(1L, null));
        assertEquals("Customer ID must not be null", ex.getMessage());
    }

    @Test
    void alreadySubscribedShouldThrow() {
        Long tournamentId = 2L;
        Long customerId = 20L;
        when(getTournamentSubscription.isCustomerSubscribedToTournament(tournamentId, customerId)).thenReturn(true);

        BadRequestException ex = assertThrows(BadRequestException.class, () -> createTournamentSubscrition.createSubscription(tournamentId, customerId));
        assertEquals("Customer is already subscribed to this tournament", ex.getMessage());

        verify(tournamentSubscriptionRepository, never()).save(any(TournamentSubscription.class));
    }
}

