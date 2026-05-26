package isi.shoppingCart.usecases.services;

import isi.shoppingCart.entities.Cart;
import isi.shoppingCart.entities.CartItem;
import isi.shoppingCart.entities.Customer;
import isi.shoppingCart.entities.Product;
import isi.shoppingCart.entities.Purchase;
import isi.shoppingCart.infrastructure.repositories.InMemoryCartRepository;
import isi.shoppingCart.infrastructure.repositories.InMemoryCustomerRepository;
import isi.shoppingCart.infrastructure.repositories.InMemoryProductRepository;
import isi.shoppingCart.infrastructure.repositories.InMemoryPurchaseRepository;
import isi.shoppingCart.usecases.dto.OperationResult;
import isi.shoppingCart.usecases.ports.CartRepository;
import isi.shoppingCart.usecases.ports.CustomerRepository;
import isi.shoppingCart.usecases.ports.ProductRepository;
import isi.shoppingCart.usecases.ports.PurchaseRepository;
import java.util.List;

public class ShoppingCartApp {
    private ProductRepository productRepository;
    private CartRepository cartRepository;
    private CustomerRepository customerRepository;
    private PurchaseRepository purchaseRepository;
    private AgregarProductoAlCarritoUseCase agregarProductoAlCarritoUseCase;
    private ConfirmarCompraUseCase confirmarCompraUseCase;
    private IncreaseAvailableQuantityUseCase increaseAvailableQuantityUseCase;

    public ShoppingCartApp() {
        productRepository = new InMemoryProductRepository();
        cartRepository = new InMemoryCartRepository();
        customerRepository = new InMemoryCustomerRepository();
        purchaseRepository = new InMemoryPurchaseRepository();
        agregarProductoAlCarritoUseCase = new AgregarProductoAlCarritoUseCase(productRepository, cartRepository);
        confirmarCompraUseCase = new ConfirmarCompraUseCase(cartRepository, customerRepository, purchaseRepository, productRepository);
        increaseAvailableQuantityUseCase = new IncreaseAvailableQuantityUseCase(productRepository);

        cargarDatosIniciales();
    }

    public ShoppingCartApp(ProductRepository productRepository,
                           CartRepository cartRepository,
                           CustomerRepository customerRepository,
                           PurchaseRepository purchaseRepository,
                           AgregarProductoAlCarritoUseCase agregarProductoAlCarritoUseCase,
                           ConfirmarCompraUseCase confirmarCompraUseCase,
                           IncreaseAvailableQuantityUseCase increaseAvailabeQuantityUseCase) {
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.customerRepository = customerRepository;
        this.purchaseRepository = purchaseRepository;
        this.agregarProductoAlCarritoUseCase = agregarProductoAlCarritoUseCase;
        this.confirmarCompraUseCase = confirmarCompraUseCase;
        this.increaseAvailableQuantityUseCase = increaseAvailabeQuantityUseCase;
    }

    private void cargarDatosIniciales() {
        cargarClienteInicial();
        cargarCatalogoInicial();
        cargarCarritoInicial();
    }

    private void cargarClienteInicial() {
        customerRepository.save(new Customer(1, "Cliente de prueba"));
    }

    private void cargarCatalogoInicial() {
        productRepository.save(new Product(1, "Laptop", 2500.0, 3));
        productRepository.save(new Product(2, "Mouse", 80.0, 2));
        productRepository.save(new Product(3, "Teclado", 150.0, 5));
        productRepository.save(new Product(4, "Monitor", 900.0, 1));
        productRepository.save(new Product(5, "Audifonos", 200.0, 4));
        productRepository.save(new Product(6, "Webcam", 180.0, 2));
    }

    private void cargarCarritoInicial() {
        Cart cart = new Cart();

        Product product1 = productRepository.findById(1);
        Product product2 = productRepository.findById(2);
        Product product3 = productRepository.findById(3);

        if (product1 != null) {
            cart.addProduct(product1);
        }

        if (product2 != null) {
            cart.addProduct(product2);
            cart.addProduct(product2);
        }

        if (product3 != null) {
            cart.addProduct(product3);
        }

        cartRepository.save(cart);
    }

    public Customer getCustomer() {
        return customerRepository.getCustomer();
    }

    public List<Product> getCatalogProducts() {
        return productRepository.findAll();
    }

    public List<CartItem> getCartItems() {
        Cart cart = cartRepository.getCart();
        return cart.getItems();
    }

    public double getCartTotal() {
        Cart cart = cartRepository.getCart();
        return cart.getTotal();
    }

    public List<Purchase> getPurchases() {
        return purchaseRepository.findAll();
    }

    public OperationResult addProductToCart(int productId) {
        return agregarProductoAlCarritoUseCase.execute(productId);
    }

    public OperationResult confirmPurchase() {
        return confirmarCompraUseCase.execute();
    }

    public OperationResult increaseAvailableQuantity(int productId) {
        return increaseAvailableQuantityUseCase.execute(productId);
    }
}
