import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import isi.shoppingCart.entities.Product;
import isi.shoppingCart.entities.PurchaseItem;

public class PurchaseItemTest
{
    @Test
    public void getProductTest()
    {
        Product product = new Product(1, "Laptop", 30.99, 10);
        PurchaseItem purchaseItem = new PurchaseItem(product, 2, 50.00);

        Product result = purchaseItem.getProduct();

        assertEquals(product, result);

    }

    @Test
    public void getQuantityTest()
    {
        Product product = new Product(1, "Laptop", 30.99, 10);
        PurchaseItem purchaseItem = new PurchaseItem(product, 2, 50.00);

        int result = purchaseItem.getQuantity();

        assertEquals(2, result);
    }

    @Test
    public void getUnitPriceTest()
    {
        Product product = new Product(1, "Laptop", 30.99, 10);
        PurchaseItem purchaseItem = new PurchaseItem(product, 2, 50.00);

        double result = purchaseItem.getUnitPrice();

        assertEquals(50.00, result);
    }

    @Test
    public void getSubtotalTest()
    {
        Product product = new Product(1, "Laptop", 30.99, 10);
        PurchaseItem purchaseItem = new PurchaseItem(product, 2, 50.00);

        double result = purchaseItem.getSubtotal();

        assertEquals(100.00, result);
    }
}
