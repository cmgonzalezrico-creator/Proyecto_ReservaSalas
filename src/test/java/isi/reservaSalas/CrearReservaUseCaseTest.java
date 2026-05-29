package isi.reservaSalas;

import isi.reservaSalas.entities.Reserva;
import isi.reservaSalas.entities.Sala;
import isi.reservaSalas.entities.TipoSala;
import isi.reservaSalas.entities.Usuario;
import isi.reservaSalas.usecases.dto.OperationResult;
import isi.reservaSalas.usecases.ports.ReservaRepository;
import isi.reservaSalas.usecases.ports.SalaRepository;
import isi.reservaSalas.usecases.ports.UsuarioRepository;
import isi.reservaSalas.usecases.services.CrearReservaUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CrearReservaUseCaseTest {

    // ─── Stubs simples ────────────────────────────────────────────────────────

    static class StubUsuarioRepo implements UsuarioRepository {
        private final List<Usuario> usuarios = new ArrayList<>();
        void agregar(Usuario u) { usuarios.add(u); }

        @Override public Optional<Usuario> buscarPorId(String id) {
            return usuarios.stream().filter(u -> u.getId().equals(id)).findFirst();
        }
        @Override public void guardar(Usuario u) { usuarios.add(u); }
    }

    static class StubSalaRepo implements SalaRepository {
        private final List<Sala> salas = new ArrayList<>();
        void agregar(Sala s) { salas.add(s); }

        @Override public Optional<Sala> buscarPorId(String id) {
            return salas.stream().filter(s -> s.getId().equals(id)).findFirst();
        }
        @Override public List<Sala> buscarActivasPorTipo(TipoSala tipo) {
            List<Sala> result = new ArrayList<>();
            for (Sala s : salas) { if (s.getTipo() == tipo && s.isActiva()) result.add(s); }
            return result;
        }
        @Override public void guardar(Sala s) { salas.add(s); }
    }

    static class StubReservaRepo implements ReservaRepository {
        private final List<Reserva> reservas = new ArrayList<>();
        void agregar(Reserva r) { reservas.add(r); }

        @Override public List<Reserva> buscarPorSalaYFecha(String salaId, LocalDate fecha) {
            List<Reserva> result = new ArrayList<>();
            for (Reserva r : reservas) {
                if (r.getSalaId().equals(salaId) && r.getFecha().equals(fecha)) result.add(r);
            }
            return result;
        }
        @Override public boolean existeCruce(String salaId, LocalDate fecha, LocalTime ini, LocalTime fin) {
            for (Reserva r : buscarPorSalaYFecha(salaId, fecha)) {
                if (r.seCruzaCon(fecha, ini, fin)) return true;
            }
            return false;
        }
        @Override public void guardar(Reserva r) { reservas.add(r); }
    }

    // ─── Setup ───────────────────────────────────────────────────────────────

    private StubUsuarioRepo usuarioRepo;
    private StubSalaRepo salaRepo;
    private StubReservaRepo reservaRepo;
    private CrearReservaUseCase useCase;

    // Fecha futura fija para que los tests no fallen con el paso del tiempo
    private static final LocalDate FECHA_FUTURA = LocalDate.of(2030, 8, 20);
    private static final LocalTime INICIO = LocalTime.of(9, 0);
    private static final LocalTime FIN    = LocalTime.of(11, 0);

    @BeforeEach
    void setUp() {
        usuarioRepo = new StubUsuarioRepo();
        salaRepo    = new StubSalaRepo();
        reservaRepo = new StubReservaRepo();
        useCase     = new CrearReservaUseCase(salaRepo, reservaRepo, usuarioRepo);

        usuarioRepo.agregar(new Usuario("u1", "Ana Torres", "ana@isi.edu"));
        salaRepo.agregar(new Sala("s1", "Aula 101", TipoSala.AULA, 30, true));
    }

    // ─── Regla: reserva exitosa ───────────────────────────────────────────────

    @Test
    void reservaValida_retornaSuccess() {
        OperationResult resultado = useCase.execute(
                "u1", TipoSala.AULA, 10, FECHA_FUTURA, INICIO, FIN, "Clase de Java");

        assertTrue(resultado.isSuccess());
        assertEquals("Aula 101", resultado.getSalaAsignada());
        assertNotNull(resultado.getReservaId());
    }

    // ─── Regla: usuario debe existir ─────────────────────────────────────────

    @Test
    void usuarioNoExiste_retornaFailure() {
        OperationResult resultado = useCase.execute(
                "noExiste", TipoSala.AULA, 10, FECHA_FUTURA, INICIO, FIN, "Clase");

        assertFalse(resultado.isSuccess());
        assertTrue(resultado.getMessage().contains("usuario"));
    }

    // ─── Regla: campos obligatorios ──────────────────────────────────────────

    @Test
    void motivoVacio_retornaFailure() {
        OperationResult resultado = useCase.execute(
                "u1", TipoSala.AULA, 10, FECHA_FUTURA, INICIO, FIN, "");

        assertFalse(resultado.isSuccess());
        assertTrue(resultado.getMessage().contains("motivo"));
    }

    @Test
    void tipoSalaNulo_retornaFailure() {
        OperationResult resultado = useCase.execute(
                "u1", null, 10, FECHA_FUTURA, INICIO, FIN, "Clase");

        assertFalse(resultado.isSuccess());
    }

    // ─── Regla: capacidad mínima > 0 ─────────────────────────────────────────

    @Test
    void capacidadMinimaCero_retornaFailure() {
        OperationResult resultado = useCase.execute(
                "u1", TipoSala.AULA, 0, FECHA_FUTURA, INICIO, FIN, "Clase");

        assertFalse(resultado.isSuccess());
        assertTrue(resultado.getMessage().contains("capacidad"));
    }

    // ─── Regla: fecha y hora deben ser futuras ────────────────────────────────

    @Test
    void fechaPasada_retornaFailure() {
        OperationResult resultado = useCase.execute(
                "u1", TipoSala.AULA, 10, LocalDate.of(2020, 1, 1), INICIO, FIN, "Clase");

        assertFalse(resultado.isSuccess());
        assertTrue(resultado.getMessage().toLowerCase().contains("futura") ||
                   resultado.getMessage().toLowerCase().contains("fecha"));
    }

    // ─── Regla: horaInicio < horaFin ─────────────────────────────────────────

    @Test
    void horaInicioMayorQueFin_retornaFailure() {
        OperationResult resultado = useCase.execute(
                "u1", TipoSala.AULA, 10, FECHA_FUTURA,
                LocalTime.of(14, 0), LocalTime.of(9, 0), "Clase");

        assertFalse(resultado.isSuccess());
        assertTrue(resultado.getMessage().toLowerCase().contains("hora"));
    }

    // ─── Regla: no hay sala disponible ───────────────────────────────────────

    @Test
    void noHaySalasDelTipoSolicitado_retornaFailure() {
        // Pedimos LABORATORIO pero solo hay AULA
        OperationResult resultado = useCase.execute(
                "u1", TipoSala.LABORATORIO, 10, FECHA_FUTURA, INICIO, FIN, "Práctica");

        assertFalse(resultado.isSuccess());
        assertTrue(resultado.getMessage().toLowerCase().contains("disponible"));
    }

    @Test
    void capacidadInsuficiente_retornaFailure() {
        // El aula tiene capacidad 30; pedimos mínimo 50
        OperationResult resultado = useCase.execute(
                "u1", TipoSala.AULA, 50, FECHA_FUTURA, INICIO, FIN, "Clase");

        assertFalse(resultado.isSuccess());
        assertTrue(resultado.getMessage().toLowerCase().contains("disponible"));
    }

    // ─── Regla: no se permiten cruces de horario ─────────────────────────────

    @Test
    void salaYaOcupada_retornaFailure() {
        // Primera reserva ocupa el aula de 09:00 a 11:00
        useCase.execute("u1", TipoSala.AULA, 10, FECHA_FUTURA, INICIO, FIN, "Primera clase");

        // Segunda reserva en el mismo horario → no debe encontrar sala
        OperationResult resultado = useCase.execute(
                "u1", TipoSala.AULA, 10, FECHA_FUTURA, INICIO, FIN, "Segunda clase");

        assertFalse(resultado.isSuccess());
        assertTrue(resultado.getMessage().toLowerCase().contains("disponible"));
    }

    @Test
    void dosReservasSinCruce_ambasExitosas() {
        // 09:00–11:00
        OperationResult r1 = useCase.execute(
                "u1", TipoSala.AULA, 10, FECHA_FUTURA, INICIO, FIN, "Primera clase");
        // 11:00–13:00  → contigua, no se cruza
        OperationResult r2 = useCase.execute(
                "u1", TipoSala.AULA, 10, FECHA_FUTURA,
                LocalTime.of(11, 0), LocalTime.of(13, 0), "Segunda clase");

        assertTrue(r1.isSuccess());
        assertTrue(r2.isSuccess());
    }
}
