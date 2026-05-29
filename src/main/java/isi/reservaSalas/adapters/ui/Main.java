package isi.reservaSalas.adapters.ui;

import isi.reservaSalas.infrastructure.DataSeeder;
import isi.reservaSalas.infrastructure.repositories.InMemoryReservaRepository;
import isi.reservaSalas.infrastructure.repositories.InMemorySalaRepository;
import isi.reservaSalas.infrastructure.repositories.InMemoryUsuarioRepository;
import isi.reservaSalas.usecases.ports.ReservaRepository;
import isi.reservaSalas.usecases.ports.SalaRepository;
import isi.reservaSalas.usecases.ports.UsuarioRepository;
import isi.reservaSalas.usecases.services.CrearReservaUseCase;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        SalaRepository salaRepository = new InMemorySalaRepository();
        ReservaRepository reservaRepository = new InMemoryReservaRepository();
        UsuarioRepository usuarioRepository = new InMemoryUsuarioRepository();

        DataSeeder.cargarDatosIniciales(salaRepository, usuarioRepository);

        CrearReservaUseCase crearReservaUseCase = new CrearReservaUseCase(
                salaRepository,
                reservaRepository,
                usuarioRepository
        );

        MainView mainView = new MainView(crearReservaUseCase);
        Scene scene = new Scene(mainView.getRoot(), 520, 460);

        stage.setTitle("Reserva de Salas");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
