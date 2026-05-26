import isi.shoppingCart.entities.Customer;
import isi.shoppingCart.entities.Product;
import isi.shoppingCart.entities.Purchase;
import isi.shoppingCart.entities.PurchaseItem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PurchaseTest {

    @Test
    void agregarItemNuevoDebeCrearItemEnCompra() {
        Customer customer = new Customer(1, "Juan");
        Product product = new Product(1, "Mouse", 50000, 10);
        Purchase purchase = new Purchase(1, customer);
        PurchaseItem item = new PurchaseItem(product, 1, 50000);

        purchase.addItem(item);

        assertEquals(1, purchase.getItems().size());
        assertEquals("Mouse", purchase.getItems().get(0).getProduct().getName());
        assertEquals(1, purchase.getItems().get(0).getQuantity());
        assertEquals(50000, purchase.getTotal());
    }
}
