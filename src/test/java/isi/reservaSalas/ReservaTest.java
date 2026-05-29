package isi.reservaSalas;

import isi.reservaSalas.entities.Reserva;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class ReservaTest {

    // Datos base reutilizables
    private static final LocalDate FECHA = LocalDate.of(2030, 6, 15);
    private static final LocalTime INICIO = LocalTime.of(9, 0);
    private static final LocalTime FIN    = LocalTime.of(11, 0);

    private Reserva reservaValida() {
        return new Reserva("r1", "sala1", "usuario1", FECHA, INICIO, FIN, "Clase de prueba", LocalDateTime.now());
    }

    // ─── Regla 1: campos obligatorios ────────────────────────────────────────

    @Test
    void reservaConTodosLosDatos_seCreaSinProblema() {
        Reserva r = reservaValida();
        assertEquals("r1", r.getId());
        assertEquals("sala1", r.getSalaId());
        assertEquals("usuario1", r.getUsuarioId());
    }

    @Test
    void reservaSinId_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () ->
                new Reserva("", "sala1", "usuario1", FECHA, INICIO, FIN, "Motivo", null));
    }

    @Test
    void reservaSinSala_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () ->
                new Reserva("r1", null, "usuario1", FECHA, INICIO, FIN, "Motivo", null));
    }

    @Test
    void reservaSinUsuario_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () ->
                new Reserva("r1", "sala1", "  ", FECHA, INICIO, FIN, "Motivo", null));
    }

    @Test
    void reservaSinFecha_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () ->
                new Reserva("r1", "sala1", "usuario1", null, INICIO, FIN, "Motivo", null));
    }

    @Test
    void reservaSinMotivo_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () ->
                new Reserva("r1", "sala1", "usuario1", FECHA, INICIO, FIN, "", null));
    }

    // ─── Regla 2: horaInicio debe ser anterior a horaFin ─────────────────────

    @Test
    void reservaConHoraInicioIgualAFin_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () ->
                new Reserva("r1", "sala1", "usuario1", FECHA,
                        LocalTime.of(10, 0), LocalTime.of(10, 0), "Motivo", null));
    }

    @Test
    void reservaConHoraInicioMayorQueFin_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () ->
                new Reserva("r1", "sala1", "usuario1", FECHA,
                        LocalTime.of(12, 0), LocalTime.of(9, 0), "Motivo", null));
    }

    // ─── Regla 3: detección de cruce de horarios ─────────────────────────────

    @Test
    void seCruzaCon_horarioSolapado_retornaTrue() {
        // reserva: 09:00–11:00  |  nueva: 10:00–12:00  →  se cruzan
        Reserva r = reservaValida();
        assertTrue(r.seCruzaCon(FECHA, LocalTime.of(10, 0), LocalTime.of(12, 0)));
    }

    @Test
    void seCruzaCon_horarioExactoIgual_retornaTrue() {
        Reserva r = reservaValida();
        assertTrue(r.seCruzaCon(FECHA, INICIO, FIN));
    }

    @Test
    void seCruzaCon_horarioContiguo_noSeCruza() {
        // reserva: 09:00–11:00  |  nueva: 11:00–13:00  →  NO se cruzan
        Reserva r = reservaValida();
        assertFalse(r.seCruzaCon(FECHA, LocalTime.of(11, 0), LocalTime.of(13, 0)));
    }

    @Test
    void seCruzaCon_diferenteFecha_noSeCruza() {
        Reserva r = reservaValida();
        assertFalse(r.seCruzaCon(FECHA.plusDays(1), INICIO, FIN));
    }

    @Test
    void seCruzaCon_horarioAnteriorSinTocar_noSeCruza() {
        // reserva: 09:00–11:00  |  nueva: 07:00–09:00  →  NO se cruzan
        Reserva r = reservaValida();
        assertFalse(r.seCruzaCon(FECHA, LocalTime.of(7, 0), LocalTime.of(9, 0)));
    }
}
