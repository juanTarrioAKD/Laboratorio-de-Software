package laboratorioTarrio.estrategias; // mismo paquete que EstrategiaDeGuerra, por el sealed

import laboratorioTarrio.estrategas.IEstratega;
import robocode.JuniorRobot;

public final class EstrategaJohnPrice implements IEstratega {

    private static final class EstrategiaEvasiva implements EstrategiaDeGuerra {
        private static final double DISTANCIA_MAXIMA_DISPARO = 250; // más lejos que esto, no vale la pena tirar

        private int sentido = 1; // alterna el lado del zigzag turno a turno

        @Override
        public void run(JuniorRobot robot) {
            // Un paso de zigzag por turno: giro corto + avance, alternando el sentido
            robot.turnRight(30 * sentido);
            robot.ahead(40);
            sentido *= -1;
        }

        @Override
        public void onScannedRobot(JuniorRobot robot) {
            if (robot.scannedDistance > DISTANCIA_MAXIMA_DISPARO) {
                return; // está muy lejos, no malgasto energía disparando
            }
            robot.bearGunTo(robot.scannedBearing);
            robot.fire(calcularPotencia(robot.scannedDistance));
        }

        @Override
        public void onHitByBullet(JuniorRobot robot) {
            sentido *= -1; // cambia el zigzag al toque de que te pegan, para ser menos predecible
        }

        @Override
        public void onHitWall(JuniorRobot robot) {
            robot.back(20);
            sentido *= -1;
        }

        private double calcularPotencia(double distancia) { return (distancia < 100) ? 3 : 2; }
    }

    private static final EstrategiaDeGuerra EVASIVA = new EstrategiaEvasiva();

    public static final EstrategaJohnPrice INSTANCE = new EstrategaJohnPrice();
    private EstrategaJohnPrice() {}

    @Override
    public EstrategiaDeGuerra analizarEntorno(JuniorRobot robot) {
        if (robot.energy < 50) {
            return EVASIVA;
        }
        return EstrategiaCampera.INSTANCE;
    }
}