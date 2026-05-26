package isi.shoppingCart.adapters.ui;

import isi.shoppingCart.entities.CartItem;
import isi.shoppingCart.entities.Customer;
import isi.shoppingCart.entities.Product;
import isi.shoppingCart.entities.Purchase;
import isi.shoppingCart.usecases.dto.OperationResult;
import isi.shoppingCart.usecases.services.ShoppingCartApp;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.List;

public class MainView {
    private ShoppingCartApp shoppingCartApp;

    private VBox catalogBox;
    private VBox cartBox;
    private VBox purchaseBox;
    private Label customerLabel;
    private Label totalLabel;

    public MainView() {
        shoppingCartApp = new ShoppingCartApp();

        catalogBox = new VBox(10);
        cartBox = new VBox(10);
        purchaseBox = new VBox(10);
        customerLabel = new Label();
        totalLabel = new Label("Total: $ 0.0");
    }

    public Scene createScene() {
        VBox catalogPanel = createCatalogPanel();
        VBox cartPanel = createCartPanel();

        HBox content = new HBox(20);
        content.setPadding(new Insets(15));
        content.getChildren().addAll(catalogPanel, cartPanel);

        HBox.setHgrow(catalogPanel, Priority.ALWAYS);
        HBox.setHgrow(cartPanel, Priority.ALWAYS);

        refreshCatalog();
        refreshCart();
        refreshCustomer();
        refreshPurchases();

        BorderPane root = new BorderPane();
        root.setCenter(content);

        return new Scene(root, 850, 520);
    }

    private VBox createCatalogPanel() {
        Label title = new Label("Catálogo");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        VBox panel = new VBox(10);
        panel.getChildren().addAll(title, catalogBox);
        panel.setPrefWidth(400);
        panel.setStyle("-fx-border-color: lightgray; -fx-padding: 10;");
        return panel;
    }

    private VBox createCartPanel() {
        Label title = new Label("Carrito");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Button confirmButton = new Button("Confirmar compra");
        confirmButton.setOnAction(event -> {
            OperationResult result = shoppingCartApp.confirmPurchase();
            showMessage(result.getMessage());
            refreshCatalog();
            refreshCart();
            refreshPurchases();
        });

        Button emptyCartButton = new Button("Vaciar carrito");
        emptyCartButton.setOnAction(event -> showMessage("Por implementar"));

        HBox cartButtons = new HBox(10);
        cartButtons.getChildren().addAll(confirmButton, emptyCartButton);

        Label purchaseTitle = new Label("Compras");
        purchaseTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        VBox panel = new VBox(10);
        panel.getChildren().addAll(title, customerLabel, cartBox, totalLabel, cartButtons, purchaseTitle, purchaseBox);
        panel.setPrefWidth(400);
        panel.setStyle("-fx-border-color: lightgray; -fx-padding: 10;");
        return panel;
    }

    private void refreshCustomer() {
        Customer customer = shoppingCartApp.getCustomer();

        if (customer != null) {
            customerLabel.setText("Cliente: " + customer.getName());
        }
    }

    private void refreshCatalog() {
        catalogBox.getChildren().clear();

        List<Product> products = shoppingCartApp.getCatalogProducts();
        int i;

        for (i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            HBox row = new HBox(10);

            Label nameLabel = new Label(product.getName());
            Label priceLabel = new Label("$ " + product.getPrice());
            Label availableLabel = new Label("Disponible: " + product.getAvailableQuantity());
            Button increaseButton = new Button("+");
            Button addButton = new Button("Agregar");

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            increaseButton.setOnAction(event -> {
                    OperationResult result = shoppingCartApp.increaseAvailableQuantity(product.getId());

            if (!result.isSuccess()) {
                showMessage(result.getMessage());
            }
            refreshCatalog();
            });

            addButton.setOnAction(event -> {
                OperationResult result = shoppingCartApp.addProductToCart(product.getId());

                if (!result.isSuccess()) {
                    showMessage(result.getMessage());
                }

                refreshCart();
            });

            row.getChildren().addAll(nameLabel, priceLabel, availableLabel, spacer, increaseButton, addButton);
            row.setStyle("-fx-padding: 5; -fx-border-color: #DDDDDD;");

            catalogBox.getChildren().add(row);
        }
    }

    private void refreshCart() {
        cartBox.getChildren().clear();

        List<CartItem> items = shoppingCartApp.getCartItems();
        int i;

        for (i = 0; i < items.size(); i++) {
            CartItem item = items.get(i);
            HBox row = new HBox(10);

            Label nameLabel = new Label(item.getProduct().getName());
            Label quantityLabel = new Label("Cantidad: " + item.getQuantity());
            Label subtotalLabel = new Label("Subtotal: $ " + item.getSubtotal());

            Button subtractButton = new Button("-");
            Button deleteButton = new Button("\uD83D\uDDD1");

            subtractButton.setOnAction(event -> showMessage("Por implementar"));
            deleteButton.setOnAction(event -> showMessage("Por implementar"));

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            row.getChildren().addAll(nameLabel, quantityLabel, subtotalLabel, spacer, subtractButton, deleteButton);
            row.setStyle("-fx-padding: 5; -fx-border-color: #DDDDDD;");

            cartBox.getChildren().add(row);
        }

        totalLabel.setText("Total: $ " + shoppingCartApp.getCartTotal());
    }

    private void refreshPurchases() {
        purchaseBox.getChildren().clear();

        List<Purchase> purchases = shoppingCartApp.getPurchases();
        int i;

        for (i = 0; i < purchases.size(); i++) {
            Purchase purchase = purchases.get(i);
            Label purchaseLabel = new Label("Compra " + purchase.getId()
                    + " - " + purchase.getCustomer().getName()
                    + " - Total: $ " + purchase.getTotal());

            purchaseLabel.setStyle("-fx-padding: 5; -fx-border-color: #DDDDDD;");
            purchaseBox.getChildren().add(purchaseLabel);
        }
    }

    private void showMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Mensaje");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
