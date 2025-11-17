package co.woloszyn.customer.framework;

import co.woloszyn.customer.dto.CustomerCreateDTO;
import co.woloszyn.customer.dto.CustomerResponseDTO;
import co.woloszyn.customer.entity.Customer;
import co.woloszyn.customer.repository.CustomerRepository;
import co.woloszyn.auth.usecase.PasswordUtil;
import co.woloszyn.customer.usecase.CreateCustomer;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

@Path("customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerResource {

    @Inject
    CreateCustomer createCustomer;

    @POST
    public CustomerResponseDTO create(CustomerCreateDTO dto) {
        return createCustomer.create(dto);
    }
}
