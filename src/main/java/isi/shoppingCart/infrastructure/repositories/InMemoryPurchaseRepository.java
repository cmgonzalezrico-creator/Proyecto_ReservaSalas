package isi.shoppingCart.infrastructure.repositories;

import isi.shoppingCart.entities.Purchase;
import isi.shoppingCart.usecases.ports.PurchaseRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InMemoryPurchaseRepository implements PurchaseRepository {
    private List<Purchase> purchases;
    private int nextId;

    public InMemoryPurchaseRepository() {
        purchases = new ArrayList<Purchase>();
        nextId = 1;
    }

    public int getNextId() {
        int id = nextId;
        nextId = nextId + 1;
        return id;
    }

    public List<Purchase> findAll() {
        return Collections.unmodifiableList(purchases);
    }

    public void save(Purchase purchase) {
        purchases.add(purchase);
    }
}
