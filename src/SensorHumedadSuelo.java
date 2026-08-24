public class SensorHumedadSuelo extends Sensor {

    public SensorHumedadSuelo(String id, String ubicacion, boolean activo) {
        super(id, ubicacion, activo);
    }

    @Override
    protected double realizarLectura() {
        return Math.random() * 100.0;
    }

    @Override
    protected boolean esUmbralCritico() {
        return getUltimaLectura() < 20.0; 
    }
}