package isi.shoppingCart.usecases.ports;

import isi.shoppingCart.entities.Customer;

public interface CustomerRepository {
    Customer getCustomer();
    void save(Customer customer);
}
