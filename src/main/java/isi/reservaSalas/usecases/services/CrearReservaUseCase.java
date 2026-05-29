package isi.reservaSalas.usecases.services;

import isi.reservaSalas.entities.Reserva;
import isi.reservaSalas.entities.Sala;
import isi.reservaSalas.entities.TipoSala;
import isi.reservaSalas.usecases.dto.OperationResult;
import isi.reservaSalas.usecases.ports.ReservaRepository;
import isi.reservaSalas.usecases.ports.SalaRepository;
import isi.reservaSalas.usecases.ports.UsuarioRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public class CrearReservaUseCase {
    private final SalaRepository salaRepository;
    private final ReservaRepository reservaRepository;
    private final UsuarioRepository usuarioRepository;

    public CrearReservaUseCase(SalaRepository salaRepository,
                               ReservaRepository reservaRepository,
                               UsuarioRepository usuarioRepository) {
        this.salaRepository = salaRepository;
        this.reservaRepository = reservaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public OperationResult execute(String usuarioId,
                                   TipoSala tipoSala,
                                   int capacidadMinima,
                                   LocalDate fecha,
                                   LocalTime horaInicio,
                                   LocalTime horaFin,
                                   String motivo) {
        try {
            validarDatos(usuarioId, tipoSala, capacidadMinima, fecha, horaInicio, horaFin, motivo);

            if (!usuarioRepository.buscarPorId(usuarioId).isPresent()) {
                throw new BusinessException("El usuario no existe");
            }

            Sala salaDisponible = seleccionarSalaDisponible(tipoSala, capacidadMinima, fecha, horaInicio, horaFin);
            Reserva reserva = new Reserva(
                    UUID.randomUUID().toString(),
                    salaDisponible.getId(),
                    usuarioId,
                    fecha,
                    horaInicio,
                    horaFin,
                    motivo,
                    LocalDateTime.now()
            );

            reservaRepository.guardar(reserva);
            return OperationResult.success("Reserva creada correctamente", reserva.getId(), salaDisponible.getNombre());
        } catch (BusinessException | IllegalArgumentException ex) {
            return OperationResult.failure(ex.getMessage());
        }
    }

    private void validarDatos(String usuarioId,
                              TipoSala tipoSala,
                              int capacidadMinima,
                              LocalDate fecha,
                              LocalTime horaInicio,
                              LocalTime horaFin,
                              String motivo) {
        if (usuarioId == null || usuarioId.trim().isEmpty()) {
            throw new BusinessException("El usuario es obligatorio");
        }
        if (tipoSala == null) {
            throw new BusinessException("Debe seleccionar un tipo de sala");
        }
        if (capacidadMinima <= 0) {
            throw new BusinessException("La capacidad minima debe ser mayor a cero");
        }
        if (fecha == null) {
            throw new BusinessException("La fecha es obligatoria");
        }
        if (horaInicio == null || horaFin == null) {
            throw new BusinessException("La hora de inicio y fin son obligatorias");
        }
        if (!horaInicio.isBefore(horaFin)) {
            throw new BusinessException("La hora de inicio debe ser anterior a la hora de fin");
        }
        if (motivo == null || motivo.trim().isEmpty()) {
            throw new BusinessException("El motivo es obligatorio");
        }

        LocalDate hoy = LocalDate.now();
        LocalTime ahora = LocalTime.now();
        if (fecha.isBefore(hoy) || (fecha.equals(hoy) && !horaInicio.isAfter(ahora))) {
            throw new BusinessException("La reserva debe programarse para una fecha y hora futura");
        }
    }

    private Sala seleccionarSalaDisponible(TipoSala tipoSala,
                                           int capacidadMinima,
                                           LocalDate fecha,
                                           LocalTime horaInicio,
                                           LocalTime horaFin) {
        List<Sala> salas = salaRepository.buscarActivasPorTipo(tipoSala);

        for (Sala sala : salas) {
            if (sala.getCapacidad() >= capacidadMinima
                    && !reservaRepository.existeCruce(sala.getId(), fecha, horaInicio, horaFin)) {
                return sala;
            }
        }

        throw new BusinessException("No hay salas disponibles para el tipo, capacidad y horario solicitados");
    }
}
