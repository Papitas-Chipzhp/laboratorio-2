public class SensorTemperatura extends Sensor {

    public SensorTemperatura(String id, String ubicacion, boolean activo) {
        super(id, ubicacion, activo);
    }

    @Override
    protected double realizarLectura() {
        double min = 15.0;
        double max = 45.0;
        return min + (Math.random() * (max - min));
    }

    @Override
    protected boolean esUmbralCritico() {
        return getUltimaLectura() > 38.0; // Umbral de temperatura
    }
}