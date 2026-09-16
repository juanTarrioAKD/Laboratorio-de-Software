package laboratorioTarrio.estrategias;

import robocode.JuniorRobot;

public final class EstrategaGhost implements IEstratega {

    static final class EstrategiaPerseguidora implements IEstrategiaDeGuerra {

        private static final double DISTANCIA_ATAQUE = 150;

        // Última posición relativa conocida del enemigo (actualizada en onScannedRobot).
        private int ultimaBearing = 0;
        private boolean tieneObjetivo = false;

        @Override
        public void run(JuniorRobot robot) {
            if (tieneObjetivo) {
                // Nos orientamos hacia donde vimos al enemigo por última vez y avanzamos.
                robot.turnRight(ultimaBearing);
                robot.ahead(40);
                tieneObjetivo = false; // se vuelve a marcar 'true' en el próximo escaneo
            } else {
                // Sin objetivo reciente: barremos el cañón para reencontrarlo.
                robot.turnGunRight(30);
            }
        }

        @Override
        public void onScannedRobot(JuniorRobot robot) {
            ultimaBearing = robot.scannedBearing;
            tieneObjetivo = true;

            robot.bearGunTo(robot.scannedBearing);
            robot.fire(calcularPotencia(robot.scannedDistance));
        }

        @Override
        public void onHitByBullet(JuniorRobot robot) {
            robot.bearGunTo(robot.hitByBulletBearing);
            robot.fire(calcularPotencia(robot.scannedDistance));
        }

        @Override
        public void onHitWall(JuniorRobot robot) {
            robot.back(20);
        }

        private double calcularPotencia(double distancia) {
            return (distancia < DISTANCIA_ATAQUE) ? 3 : 2;
        }
    }

    private static final IEstrategiaDeGuerra PERSEGUIDORA = new EstrategiaPerseguidora();

    public static final EstrategaGhost INSTANCE = new EstrategaGhost();
    private EstrategaGhost() {}

    @Override
    public IEstrategiaDeGuerra analizarEntorno(JuniorRobot robot) {
        // Queda un solo rival y hay energía suficiente: vamos al ataque.
        if (robot.others <= 1 && robot.energy > 40) {
            return PERSEGUIDORA;
        }
        // Varios enemigos vivos, o poca energía: nos ponemos evasivos.
        return EstrategiaParedErratica.INSTANCE;
    }
}
