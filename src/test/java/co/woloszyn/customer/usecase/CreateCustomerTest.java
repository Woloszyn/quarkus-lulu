package co.woloszyn.customer.usecase;

import co.woloszyn.customer.dto.CustomerCreateDTO;
import co.woloszyn.customer.dto.CustomerResponseDTO;
import co.woloszyn.customer.entity.Customer;
import co.woloszyn.customer.repository.CustomerRepository;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateCustomerTest {

    @Mock
    CustomerRepository repository;

    @InjectMocks
    CreateCustomer createCustomer;

    @Test
    public void testCreateValidCustomer() {
        CustomerCreateDTO dto = new CustomerCreateDTO();
        dto.setName("John Doe");
        dto.setEmail("john@example.com");
        dto.setPassword("secret");

        // repository.findByEmail should return null (no existing user)
        when(repository.findByEmail(dto.getEmail())).thenReturn(null);

        // When persist is called, simulate that the DB assigned an ID
        doAnswer(invocation -> {
            Customer c = invocation.getArgument(0);
            c.setId(42L);
            return null;
        }).when(repository).persist(any(Customer.class));

        CustomerResponseDTO resp = createCustomer.create(dto);

        assertNotNull(resp);
        assertEquals(42L, resp.getId());
        assertEquals(dto.getName(), resp.getName());
        assertEquals(dto.getEmail(), resp.getEmail());

        // verify that password was hashed before persisting
        ArgumentCaptor<Customer> captor = ArgumentCaptor.forClass(Customer.class);
        verify(repository).persist(captor.capture());
        Customer persisted = captor.getValue();
        assertNotNull(persisted.getPasswordHash());
        assertTrue(persisted.getPasswordHash().startsWith("pbkdf2_sha256$"));
    }

    @Test
    public void testCreateDuplicateEmailThrowsConflict() {
        CustomerCreateDTO dto = new CustomerCreateDTO();
        dto.setName("Jane");
        dto.setEmail("jane@example.com");
        dto.setPassword("pw");

        // simulate existing customer
        when(repository.findByEmail(dto.getEmail())).thenReturn(new Customer());

        WebApplicationException ex = assertThrows(WebApplicationException.class, () -> createCustomer.create(dto));
        Response r = ex.getResponse();
        assertEquals(Response.Status.CONFLICT.getStatusCode(), r.getStatus());
        assertEquals("email already registered", r.getEntity());
    }

    @Test
    public void testCreateInvalidDtoThrowsBadRequest() {
        // missing email
        CustomerCreateDTO dto = new CustomerCreateDTO();
        dto.setName("No Email");
        dto.setPassword("pw");
        dto.setEmail(null);

        WebApplicationException ex = assertThrows(WebApplicationException.class, () -> createCustomer.create(dto));
        Response r = ex.getResponse();
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), r.getStatus());
        assertEquals("name, email and password are required", r.getEntity());
    }
}

