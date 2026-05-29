package isi.reservaSalas.usecases.dto;

public class OperationResult {
    private final boolean success;
    private final String message;
    private final String reservaId;
    private final String salaAsignada;

    private OperationResult(boolean success, String message, String reservaId, String salaAsignada) {
        this.success = success;
        this.message = message;
        this.reservaId = reservaId;
        this.salaAsignada = salaAsignada;
    }

    public static OperationResult success(String message, String reservaId, String salaAsignada) {
        return new OperationResult(true, message, reservaId, salaAsignada);
    }

    public static OperationResult failure(String message) {
        return new OperationResult(false, message, null, null);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getReservaId() {
        return reservaId;
    }

    public String getSalaAsignada() {
        return salaAsignada;
    }
}
