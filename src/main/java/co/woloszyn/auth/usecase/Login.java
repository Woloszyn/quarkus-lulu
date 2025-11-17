package co.woloszyn.auth.usecase;

import co.woloszyn.auth.dto.LoginDTO;
import co.woloszyn.auth.dto.LoginRequestDTO;
import co.woloszyn.customer.entity.Customer;
import co.woloszyn.customer.repository.CustomerRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class Login {

    @Inject
    JwtUtil jwtUtil;

    @Inject
    CustomerRepository customerRepository;

    public LoginDTO login(LoginRequestDTO request) {
        if (request == null || request.getUsername() == null || request.getPassword() == null) {
            throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).entity("username and password required").build());
        }

        Customer customer = customerRepository.findByEmail(request.getUsername());
        if (customer == null) {
            throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED).entity("invalid credentials").build());
        }

        if (customer.getPasswordHash() == null || !PasswordUtil.verifyPassword(request.getPassword(), customer.getPasswordHash())) {
            throw new WebApplicationException(Response.status(Response.Status.UNAUTHORIZED).entity("invalid credentials").build());
        }

        String token = jwtUtil.generateToken(request.getUsername());
        String refresh = jwtUtil.generateRefreshToken(request.getUsername());

        LoginDTO dto = new LoginDTO();
        dto.setToken(token);
        dto.setName(customer.getName());
        dto.setCustomerId(customer.getId());
        dto.setRefreshToken(refresh);
        dto.setExpiresIn(jwtUtil.getExpirationSeconds());

        return dto;
    }

}
