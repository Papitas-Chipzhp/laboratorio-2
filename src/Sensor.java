public abstract class Sensor {

    private final String id;
    private final String ubicacion;
    private volatile boolean activo;

    private volatile boolean lecturaTomada = false;
    private volatile boolean errorLectura = false;
    private volatile double ultimaLectura = 0.0;

    public Sensor(String id, String ubicacion, boolean activo) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El ID no puede ser nulo ni estar vacío.");
        }
        if (ubicacion == null || ubicacion.isBlank()) {
            throw new IllegalArgumentException("La ubicación no puede ser nula ni estar vacía.");
        }

        this.id = id.trim();
        this.ubicacion = ubicacion.trim();
        this.activo = activo;
    }

    public String getId() {
        return id;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public boolean isLecturaTomada() {
        return lecturaTomada;
    }

    public boolean isErrorLectura() {
        return errorLectura;
    }

    public double getUltimaLectura() {
        return ultimaLectura;
    }

    public final double tomarLectura() {
        if (!activo) {
            throw new IllegalStateException("No se puede tomar lectura: el sensor " + id + " está INACTIVO.");
        }

        try {
            double lectura = realizarLectura();
            
            this.ultimaLectura = lectura;
            this.lecturaTomada = true;
            this.errorLectura = false;
            
            return lectura;
        } catch (Exception e) {
            this.errorLectura = true;
            throw new RuntimeException("Fallo en la comunicación con el sensor " + id + ": " + e.getMessage(), e);
        }
    }

    protected abstract double realizarLectura();

    public EstadoSensor evaluarEstado() {
        if (!activo) {
            return EstadoSensor.INACTIVO;
        }
        if (errorLectura) {
            return EstadoSensor.ERROR_LECTURA;
        }
        if (!lecturaTomada) {
            return EstadoSensor.SIN_LECTURA;
        }
        return EstadoSensor.OPERATIVO;
    }

    public boolean esCritico() {
        if (errorLectura || evaluarEstado() == EstadoSensor.FUERA_DE_RANGO) {
            return true;
        }
        return lecturaTomada && esUmbralCritico();
    }

    protected abstract boolean esUmbralCritico();
}