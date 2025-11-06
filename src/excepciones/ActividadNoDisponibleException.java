package excepciones;

public class ActividadNoDisponibleException extends Exception {

    public ActividadNoDisponibleException() {
        super("La actividad no está disponible para inscripción.");
    }

    public ActividadNoDisponibleException(String mensaje) {
        super(mensaje);
    }
}

