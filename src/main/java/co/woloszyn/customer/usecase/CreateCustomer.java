package co.woloszyn.customer.usecase;

import co.woloszyn.auth.usecase.PasswordUtil;
import co.woloszyn.customer.dto.CustomerCreateDTO;
import co.woloszyn.customer.dto.CustomerResponseDTO;
import co.woloszyn.customer.entity.Customer;
import co.woloszyn.customer.repository.CustomerRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class CreateCustomer {

    @Inject
    CustomerRepository repository;

    @Transactional
    public CustomerResponseDTO create(CustomerCreateDTO dto) {
        if (dto == null || dto.getEmail() == null || dto.getPassword() == null) {
            throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).entity("name, email and password are required").build());
        }

        // check if email already exists
        if (repository.findByEmail(dto.getEmail()) != null) {
            throw new WebApplicationException(Response.status(Response.Status.CONFLICT).entity("email already registered").build());
        }

        Customer c = new Customer();
        c.setName(dto.getName());
        c.setEmail(dto.getEmail());
        // hash password with PBKDF2 util
        c.setPasswordHash(PasswordUtil.hashPassword(dto.getPassword()));

        repository.persist(c);

        CustomerResponseDTO resp = new CustomerResponseDTO();
        resp.setId(c.getId());
        resp.setName(c.getName());
        resp.setEmail(c.getEmail());

        return resp;
    }

}
