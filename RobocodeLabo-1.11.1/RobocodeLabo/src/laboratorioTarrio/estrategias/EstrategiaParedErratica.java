package laboratorioTarrio.estrategias;

import robocode.JuniorRobot;
import java.util.Random;

public final class EstrategiaParedErratica implements IEstrategiaDeGuerra {

    public static final EstrategiaParedErratica INSTANCE = new EstrategiaParedErratica();
    private EstrategiaParedErratica() {}

    private static final int MARGEN = 30;
    private static final int PASO_MIN = 60;
    private static final int PASO_MAX = 200;
    private static final int ARCO_MEDIO = 75;
    private static final int INCREMENTO_BARRIDO = 20;

    private final Random random = new Random();

    // Estado persistente entre llamadas a run()
    private boolean yaPosicionado = false;
    private int anguloBase;
    private Punto extremo1;
    private Punto extremo2;
    private boolean horizontal;
    private int offsetBarrido = 0;
    private int sentidoBarrido = 1;

    @Override
    public void run(JuniorRobot robot) {
        if (!yaPosicionado) {
            posicionarseEnPared(robot);
            yaPosicionado = true;
            return; // este turno solo nos ubicamos
        }
        darUnPasoErratico(robot);
        barrerCanion(robot);
    }

    private void posicionarseEnPared(JuniorRobot robot) {
        double distIzquierda = robot.robotX;
        double distDerecha   = robot.fieldWidth - robot.robotX;
        double distAbajo     = robot.robotY;
        double distArriba    = robot.fieldHeight - robot.robotY;

        double minima = Math.min(Math.min(distIzquierda, distDerecha), Math.min(distAbajo, distArriba));
        horizontal = (minima == distAbajo || minima == distArriba);

        Punto destinoInicial;
        if (minima == distIzquierda) {
            destinoInicial = new Punto(MARGEN, robot.robotY);
            anguloBase = 90;
        } else if (minima == distDerecha) {
            destinoInicial = new Punto(robot.fieldWidth - MARGEN, robot.robotY);
            anguloBase = 270;
        } else if (minima == distAbajo) {
            destinoInicial = new Punto(robot.robotX, MARGEN);
            anguloBase = 0;
        } else {
            destinoInicial = new Punto(robot.robotX, robot.fieldHeight - MARGEN);
            anguloBase = 180;
        }

        irA(robot, destinoInicial);

        if (horizontal) {
            double y = destinoInicial.y();
            extremo1 = new Punto(MARGEN, (int) y);
            extremo2 = new Punto(robot.fieldWidth - MARGEN, (int) y);
        } else {
            double x = destinoInicial.x();
            extremo1 = new Punto((int) x, MARGEN);
            extremo2 = new Punto((int) x, robot.fieldHeight - MARGEN);
        }

        orientarHacia(robot, extremo2);
    }

    private void darUnPasoErratico(JuniorRobot robot) {
        int pasos = PASO_MIN + random.nextInt(PASO_MAX - PASO_MIN + 1);
        boolean avanzar = random.nextBoolean();

        double posActual = horizontal ? robot.robotX : robot.robotY;
        double limiteMin = horizontal ? extremo1.x() : extremo1.y();
        double limiteMax = horizontal ? extremo2.x() : extremo2.y();

        if (avanzar && posActual + pasos > limiteMax) {
            avanzar = false;
        } else if (!avanzar && posActual - pasos < limiteMin) {
            avanzar = true;
        }

        if (avanzar) {
            robot.ahead(pasos);
        } else {
            robot.back(pasos);
        }
    }

    private void barrerCanion(JuniorRobot robot) {
        offsetBarrido += INCREMENTO_BARRIDO * sentidoBarrido;
        if (offsetBarrido > ARCO_MEDIO) {
            offsetBarrido = ARCO_MEDIO;
            sentidoBarrido = -1;
        } else if (offsetBarrido < -ARCO_MEDIO) {
            offsetBarrido = -ARCO_MEDIO;
            sentidoBarrido = 1;
        }
        int anguloObjetivo = (anguloBase + offsetBarrido + 360) % 360;
        robot.turnGunTo(anguloObjetivo);
    }

    private void orientarHacia(JuniorRobot robot, Punto destino) {
        double dx = destino.x() - robot.robotX;
        double dy = destino.y() - robot.robotY;
        int angulo = (int) Math.round(Math.toDegrees(Math.atan2(dx, dy)));
        if (angulo < 0) angulo += 360;
        robot.turnTo(angulo);
    }

    private void irA(JuniorRobot robot, Punto destino) {
        orientarHacia(robot, destino);
        double dx = destino.x() - robot.robotX;
        double dy = destino.y() - robot.robotY;
        robot.ahead((int) Math.round(Math.hypot(dx, dy)));
    }

    @Override
    public void onScannedRobot(JuniorRobot robot) {
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
        robot.back(10);
    }

    private double calcularPotencia(double distancia) { return (distancia < 100) ? 3 : 2; }
}