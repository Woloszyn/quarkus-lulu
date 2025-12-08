package co.woloszyn.tournament.usecase;

import co.woloszyn.customer.entity.Customer;
import co.woloszyn.tournament.dto.CreatePlaceRequestDTO;
import co.woloszyn.tournament.dto.PlaceDTO;
import co.woloszyn.tournament.entity.Place;
import co.woloszyn.tournament.repository.PlaceRepository;
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
class CreatePlaceTest {

    @InjectMock
    PlaceRepository placeRepository;

    @InjectMock
    GetPlace getPlace;

    // EntityManager criado manualmente no setup como mock
    EntityManager entityManager;

    @Inject
    CreatePlace createPlace;

    @BeforeEach
    void setUp() {
        entityManager = mock(EntityManager.class);
        when(placeRepository.getEntityManager()).thenReturn(entityManager);
    }

    @Test
    void shouldCreatePhysicalPlace() {
        CreatePlaceRequestDTO req = new CreatePlaceRequestDTO();
        req.setName("My Place");
        req.setOnline(false);
        req.setStreet("Main St");
        req.setCity("City");
        req.setCountry("Country");
        req.setNeighborhood("Hood");
        req.setHouseNumber("123");

        Long customerId = 10L;
        Customer customerRef = new Customer();
        customerRef.setId(customerId);
        when(entityManager.getReference(Customer.class, customerId)).thenReturn(customerRef);

        when(placeRepository.save(any(Place.class))).thenAnswer(invocation -> {
            Place p = invocation.getArgument(0);
            p.setId(1L);
            return p;
        });

        PlaceDTO dto = new PlaceDTO();
        dto.setId(1L);
        dto.setName(req.getName());
        when(getPlace.getById(1L)).thenReturn(dto);

        PlaceDTO result = createPlace.createPlace(req, customerId);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(req.getName(), result.getName());

        ArgumentCaptor<Place> captor = ArgumentCaptor.forClass(Place.class);
        verify(placeRepository).save(captor.capture());
        Place saved = captor.getValue();
        assertNotNull(saved.getCustomer());
        assertEquals(customerRef.getId(), saved.getCustomer().getId());
        assertEquals(req.getName(), saved.getLocalName());
        assertFalse(saved.getOnline());
        assertEquals(req.getStreet(), saved.getStreet());
        assertEquals(req.getCity(), saved.getCity());
        assertEquals(req.getCountry(), saved.getCountry());
        assertEquals(req.getNeighborhood(), saved.getNeighborhood());
        assertEquals(req.getHouseNumber(), saved.getHouseNumber());

        verify(getPlace).getById(1L);
    }

    @Test
    void shouldCreateOnlinePlace() {
        CreatePlaceRequestDTO req = new CreatePlaceRequestDTO();
        req.setName("Online Place");
        req.setOnline(true);
        req.setUrl("https://example.com");

        Long customerId = 20L;
        Customer customerRef = new Customer();
        customerRef.setId(customerId);
        when(entityManager.getReference(Customer.class, customerId)).thenReturn(customerRef);

        when(placeRepository.save(any(Place.class))).thenAnswer(invocation -> {
            Place p = invocation.getArgument(0);
            p.setId(2L);
            return p;
        });

        PlaceDTO dto = new PlaceDTO();
        dto.setId(2L);
        dto.setName(req.getName());
        when(getPlace.getById(2L)).thenReturn(dto);

        PlaceDTO result = createPlace.createPlace(req, customerId);

        assertNotNull(result);
        assertEquals(2L, result.getId());
        assertEquals(req.getName(), result.getName());

        ArgumentCaptor<Place> captor = ArgumentCaptor.forClass(Place.class);
        verify(placeRepository).save(captor.capture());
        Place saved = captor.getValue();
        assertNotNull(saved.getCustomer());
        assertEquals(customerRef.getId(), saved.getCustomer().getId());
        assertEquals(req.getName(), saved.getLocalName());
        assertTrue(saved.getOnline());
        assertEquals(req.getUrl(), saved.getUrl());

        verify(getPlace).getById(2L);
    }

    @Test
    void nullRequestShouldThrow() {
        assertThrows(BadRequestException.class, () -> createPlace.createPlace(null, 1L));
    }

    @Test
    void blankNameShouldThrow() {
        CreatePlaceRequestDTO req = new CreatePlaceRequestDTO();
        req.setName("  ");
        req.setOnline(true);
        req.setUrl("https://example.com");
        assertThrows(BadRequestException.class, () -> createPlace.createPlace(req, 1L));
    }

    @Test
    void onlineWithoutUrlShouldThrow() {
        CreatePlaceRequestDTO req = new CreatePlaceRequestDTO();
        req.setName("Online");
        req.setOnline(true);
        req.setUrl(null);
        assertThrows(BadRequestException.class, () -> createPlace.createPlace(req, 1L));
    }

    @Test
    void physicalMissingFieldsShouldThrow() {
        CreatePlaceRequestDTO req = new CreatePlaceRequestDTO();
        req.setName("Physical");
        req.setOnline(false);
        // missing street, city, country, neighborhood, houseNumber
        assertThrows(BadRequestException.class, () -> createPlace.createPlace(req, 1L));
    }
}
