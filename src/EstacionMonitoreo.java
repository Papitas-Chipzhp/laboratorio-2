import java.util.ArrayList;
import java.util.List;

public class EstacionMonitoreo {

    private final List<Sensor> sensores = new ArrayList<>();

    public void agregarSensor(Sensor sensor) {
        if (sensor != null) {
            sensores.add(sensor);
        }
    }

    public void procesarLecturas() {
        for (Sensor sensor : sensores) {
            try {
                double lectura = sensor.tomarLectura();
                System.out.println("Sensor [" + sensor.getId() + "] -> Lectura: " 
                        + String.format("%.2f", lectura));
            } catch (Exception e) {
                System.err.println("Sensor [" + sensor.getId() + "] ERROR: " + e.getMessage());
            }
        }
    }

    // Ahora cumple OCP: No usa instanceof ni conoce reglas de sensores
    public List<Sensor> obtenerSensoresCriticos() {
        List<Sensor> criticos = new ArrayList<>();
        for (Sensor sensor : sensores) {
            if (sensor.esCritico()) {
                criticos.add(sensor);
            }
        }
        return criticos;
    }

    public List<Sensor> getSensores() {
        return new ArrayList<>(sensores);
    }
}