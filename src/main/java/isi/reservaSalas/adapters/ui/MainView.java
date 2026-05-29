package isi.reservaSalas.adapters.ui;

import isi.reservaSalas.entities.TipoSala;
import isi.reservaSalas.usecases.dto.OperationResult;
import isi.reservaSalas.usecases.services.CrearReservaUseCase;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.LocalTime;

public class MainView {
    private final CrearReservaUseCase crearReservaUseCase;
    private final VBox root;
    private final TextField usuarioIdField;
    private final ComboBox<TipoSala> tipoSalaComboBox;
    private final Spinner<Integer> capacidadSpinner;
    private final DatePicker fechaPicker;
    private final TextField horaInicioField;
    private final TextField horaFinField;
    private final TextField motivoField;

    public MainView(CrearReservaUseCase crearReservaUseCase) {
        this.crearReservaUseCase = crearReservaUseCase;
        this.usuarioIdField = new TextField("USR-1");
        this.tipoSalaComboBox = new ComboBox<TipoSala>(FXCollections.observableArrayList(TipoSala.values()));
        this.capacidadSpinner = new Spinner<Integer>(1, 200, 20);
        this.fechaPicker = new DatePicker(LocalDate.now().plusDays(1));
        this.horaInicioField = new TextField("08:00");
        this.horaFinField = new TextField("10:00");
        this.motivoField = new TextField("Clase programada");
        this.root = construirVista();
    }

    public Parent getRoot() {
        return root;
    }

    private VBox construirVista() {
        tipoSalaComboBox.getSelectionModel().select(TipoSala.AULA);

        GridPane form = new GridPane();
        form.setHgap(12);
        form.setVgap(12);

        agregarCampo(form, 0, "Usuario", usuarioIdField);
        agregarCampo(form, 1, "Tipo de sala", tipoSalaComboBox);
        agregarCampo(form, 2, "Capacidad", capacidadSpinner);
        agregarCampo(form, 3, "Fecha", fechaPicker);
        agregarCampo(form, 4, "Hora inicio", horaInicioField);
        agregarCampo(form, 5, "Hora fin", horaFinField);
        agregarCampo(form, 6, "Motivo", motivoField);

        Button crearButton = new Button("Crear reserva");
        crearButton.setMaxWidth(Double.MAX_VALUE);
        crearButton.setOnAction(event -> crearReserva());

        VBox contenedor = new VBox(18, new Label("Crear Reserva"), form, crearButton);
        contenedor.setPadding(new Insets(24));
        return contenedor;
    }

    private void agregarCampo(GridPane form, int fila, String etiqueta, javafx.scene.Node campo) {
        form.add(new Label(etiqueta), 0, fila);
        form.add(campo, 1, fila);
    }

    private void crearReserva() {
        OperationResult result = crearReservaUseCase.execute(
                usuarioIdField.getText(),
                tipoSalaComboBox.getValue(),
                capacidadSpinner.getValue(),
                fechaPicker.getValue(),
                LocalTime.parse(horaInicioField.getText().trim()),
                LocalTime.parse(horaFinField.getText().trim()),
                motivoField.getText()
        );

        if (result.isSuccess()) {
            mostrarMensaje(
                    Alert.AlertType.INFORMATION,
                    "Reserva creada",
                    result.getMessage() + "\nSala asignada: " + result.getSalaAsignada()
            );
        } else {
            mostrarMensaje(Alert.AlertType.ERROR, "No se pudo crear la reserva", result.getMessage());
        }
    }

    private void mostrarMensaje(Alert.AlertType tipo, String titulo, String contenido) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(titulo);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}
