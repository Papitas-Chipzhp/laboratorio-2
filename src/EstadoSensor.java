public enum EstadoSensor {
    OPERATIVO("El sensor funciona dentro de parámetros normales"),
    INACTIVO("El sensor está desactivado eléctricamente"),
    SIN_LECTURA("El sensor está activo pero aún no ha registrado datos"),
    FUERA_DE_RANGO("La lectura actual sobrepasa los límites físicos permitidos"),
    ERROR_LECTURA("Ocurrió un error al intentar comunicarse con el hardware");

    private final String descripcion;

    EstadoSensor(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}