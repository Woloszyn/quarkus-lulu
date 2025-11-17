package co.woloszyn.customer.repository;

import co.woloszyn.customer.entity.Customer;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CustomerRepository implements PanacheRepository<Customer> {

    public Customer findByEmail(String email) {
        return find("email", email).firstResult();
    }
}

