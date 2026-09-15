package laboratorioTarrio.estrategias;
import robocode.JuniorRobot;
import java.util.Random;

public final class EstrategiaParedErratica implements EstrategiaDeGuerra {

    private static final int MARGEN = 30;
    private static final int PASO_MIN = 60;
    private static final int PASO_MAX = 200;
    private static final int ARCO_MEDIO = 75; // grados a cada lado del centro del barrido
    private static final int INCREMENTO_BARRIDO = 20; // grados que avanza el cañón en cada paso

    private final Random random = new Random();

    // Dirección perpendicular a la pared, apuntando hacia adentro del campo.
    private int anguloBase;
    // Posición actual del barrido, relativa a anguloBase (-ARCO_MEDIO .. +ARCO_MEDIO).
    private int offsetBarrido = 0;
    // Sentido del barrido: +1 o -1.
    private int sentidoBarrido = 1;

    @Override
    public void run(JuniorRobot robot) {

        double distIzquierda = robot.robotX;
        double distDerecha   = robot.fieldWidth - robot.robotX;
        double distAbajo     = robot.robotY;
        double distArriba    = robot.fieldHeight - robot.robotY;

        double minima = Math.min(Math.min(distIzquierda, distDerecha), Math.min(distAbajo, distArriba));
        boolean paredHorizontal = (minima == distAbajo || minima == distArriba);

        Punto destinoInicial;
        if (minima == distIzquierda) {
            destinoInicial = new Punto(MARGEN, robot.robotY);
            anguloBase = 90; // pared izquierda -> el campo está hacia el este
        } else if (minima == distDerecha) {
            destinoInicial = new Punto(robot.fieldWidth - MARGEN, robot.robotY);
            anguloBase = 270; // pared derecha -> el campo está hacia el oeste
        } else if (minima == distAbajo) {
            destinoInicial = new Punto(robot.robotX, MARGEN);
            anguloBase = 0; // pared inferior -> el campo está hacia el norte
        } else {
            destinoInicial = new Punto(robot.robotX, robot.fieldHeight - MARGEN);
            anguloBase = 180; // pared superior -> el campo está hacia el sur
        }

        irA(robot, destinoInicial);

        Punto extremo1;
        Punto extremo2;
        if (paredHorizontal) {
            double y = destinoInicial.y();
            extremo1 = new Punto(MARGEN, (int) y);
            extremo2 = new Punto(robot.fieldWidth - MARGEN, (int) y);
        } else {
            double x = destinoInicial.x();
            extremo1 = new Punto((int) x, MARGEN);
            extremo2 = new Punto((int) x, robot.fieldHeight - MARGEN);
        }

        patrullarErratico(robot, extremo1, extremo2, paredHorizontal);
    }

    private void patrullarErratico(JuniorRobot robot, Punto extremo1, Punto extremo2, boolean horizontal) {
        // Ya no orientamos el cuerpo en base al cañón: el cuerpo mira hacia
        // extremo2 para poder usar ahead()/back(), pero el cañón se maneja
        // aparte con barrerCanion(), de forma independiente.
        orientarHacia(robot, extremo2);

        while (true) {
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

            barrerCanion(robot);
        }
    }

    /**
     * Hace oscilar el cañón dentro de un arco de +/- ARCO_MEDIO grados,
     * centrado en anguloBase (la dirección que apunta hacia el campo,
     * lejos de la pared). Cuando llega a un extremo del arco, invierte
     * el sentido, generando un barrido tipo "radar de vigilancia" que
     * nunca pierde tiempo mirando hacia la pared.
     */
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


        /** Gira el robot para quedar orientado hacia el destino, sin moverse. */
        private void orientarHacia(JuniorRobot robot, Punto destino) {
            double dx = destino.x() - robot.robotX;
            double dy = destino.y() - robot.robotY;
            int angulo = (int) Math.round(Math.toDegrees(Math.atan2(dx, dy)));
            if (angulo < 0) {
                angulo += 360;
            }
            robot.turnTo(angulo);
        }

        /** Gira hacia el destino y avanza en línea recta hasta él. */
        private void irA(JuniorRobot robot, Punto destino) {
            orientarHacia(robot, destino);
            double dx = destino.x() - robot.robotX;
            double dy = destino.y() - robot.robotY;
            int distancia = (int) Math.round(Math.hypot(dx, dy));
            robot.ahead(distancia);
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
