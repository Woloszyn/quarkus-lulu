package co.woloszyn.auth.usecase;

import co.woloszyn.auth.dto.LoginDTO;
import co.woloszyn.auth.dto.LoginRequestDTO;
import co.woloszyn.customer.entity.Customer;
import co.woloszyn.customer.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.ws.rs.WebApplicationException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LoginTest {

    @Mock
    CustomerRepository customerRepository;

    @Mock
    JwtUtil jwtUtil;

    @Test
    public void testLoginSuccess() {
        Login login = new Login();
        login.customerRepository = customerRepository;
        login.jwtUtil = jwtUtil;

        String username = "user@example.com";
        String password = "secret";
        Customer customer = new Customer();
        customer.setId(123L);
        customer.setName("John Doe");
        customer.setEmail(username);
        customer.setPasswordHash(PasswordUtil.hashPassword(password));

        when(customerRepository.findByEmail(username)).thenReturn(customer);
        when(jwtUtil.generateToken(username)).thenReturn("tok");
        when(jwtUtil.generateRefreshToken(username)).thenReturn("ref");
        when(jwtUtil.getExpirationSeconds()).thenReturn(3600L);

        LoginRequestDTO req = new LoginRequestDTO();
        req.setUsername(username);
        req.setPassword(password);

        LoginDTO dto = login.login(req);
        assertNotNull(dto);
        assertEquals("tok", dto.getToken());
        assertEquals("ref", dto.getRefreshToken());
        assertEquals("John Doe", dto.getName());
        assertEquals(Long.valueOf(123L), dto.getCustomerId());
        assertEquals(Long.valueOf(3600L), dto.getExpiresIn());
    }

    @Test
    public void testLoginNullRequest() {
        Login login = new Login();
        WebApplicationException ex = assertThrows(WebApplicationException.class, () -> login.login(null));
        assertEquals(400, ex.getResponse().getStatus());
    }

    @Test
    public void testLoginUnknownUser() {
        Login login = new Login();
        login.customerRepository = customerRepository;

        when(customerRepository.findByEmail("u")).thenReturn(null);

        LoginRequestDTO req = new LoginRequestDTO();
        req.setUsername("u");
        req.setPassword("p");

        WebApplicationException ex = assertThrows(WebApplicationException.class, () -> login.login(req));
        assertEquals(401, ex.getResponse().getStatus());
    }

    @Test
    public void testLoginWrongPassword() {
        Login login = new Login();
        login.customerRepository = customerRepository;

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("A");
        customer.setEmail("e");
        customer.setPasswordHash(PasswordUtil.hashPassword("correct"));

        when(customerRepository.findByEmail("e")).thenReturn(customer);

        LoginRequestDTO req = new LoginRequestDTO();
        req.setUsername("e");
        req.setPassword("wrong");

        WebApplicationException ex = assertThrows(WebApplicationException.class, () -> login.login(req));
        assertEquals(401, ex.getResponse().getStatus());
    }
}

