package co.woloszyn.tournament.usecase;

import co.woloszyn.customer.dto.CustomerResponseDTO;
import co.woloszyn.customer.entity.Customer;
import co.woloszyn.customer.repository.CustomerRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import static co.woloszyn.common.AssertionUtils.assertNotNull;

@ApplicationScoped
public class GetCustomer {

    @Inject
    CustomerRepository customerRepository;

    public CustomerResponseDTO getCustomerById(Long customerId) {
        assertNotNull(customerId, "Customer ID cannot be null");
        return toDto(customerRepository.findById(customerId));
    }

    private CustomerResponseDTO toDto(Customer customer) {
        CustomerResponseDTO dto = new CustomerResponseDTO();
        if (customer != null) {
            dto.setId(customer.getId());
            dto.setName(customer.getName());
            dto.setEmail(customer.getEmail());
            return dto;
        }
        return null;
    }

}
