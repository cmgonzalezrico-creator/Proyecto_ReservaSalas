package isi.shoppingCart.infrastructure.repositories;

import isi.shoppingCart.entities.Customer;
import isi.shoppingCart.usecases.ports.CustomerRepository;

public class InMemoryCustomerRepository implements CustomerRepository {
    private Customer customer;

    public Customer getCustomer() {
        return customer;
    }

    public void save(Customer customer) {
        this.customer = customer;
    }
}
