package laboratorioTarrio.estrategias;

import robocode.JuniorRobot;

public final class EstrategiaCampera implements IEstrategiaDeGuerra{

    public static final EstrategiaCampera INSTANCE = new EstrategiaCampera();
    private EstrategiaCampera() {}

    private static final int MARGEN = 50;

    private boolean yaLlegue = false; // estado que antes vivía implícito antes del while(true)


    @Override
    public void run(JuniorRobot robot) {
        if (!yaLlegue) {
            irALaEsquinaMasCercana(robot);
            yaLlegue = true;
            return; // este turno solo nos movimos
        }
        // Ya estamos en la esquina: un paso de giro de cañón por turno
        robot.turnGunRight(20);
    }

    private void irALaEsquinaMasCercana(JuniorRobot robot) {
        Punto[] esquinas = {
                new Punto(MARGEN, MARGEN),
                new Punto(MARGEN, robot.fieldHeight - MARGEN),
                new Punto(robot.fieldWidth - MARGEN, MARGEN),
                new Punto(robot.fieldWidth - MARGEN, robot.fieldHeight - MARGEN)
                // sin el margen las coordenadas quedan:
                // (0,0) esquina inferior izquierda
                // (fieldWidth,0) esquina inferior derecha
                // (0,fieldHeight) esquina superior izquierda
                // (fieldWidth,fieldHeight) esquina superior derecha
        };

        Punto esquinaElegida = esquinas[0];
        double distanciaMinima = Double.MAX_VALUE;

        for (Punto esquina : esquinas) {
            double dx = esquina.x() - robot.robotX;
            double dy = esquina.y() - robot.robotY;
            double distancia = Math.hypot(dx, dy);
            // dx calcula la distancia que me tengo que mover en el eje x
            // dy calcula la distancia que me tengo que mover en el eje y
            // hypot calcula la hipotenusa dado el cateto x
            // y el cateto hace el teorema de pitagoras: el cuadrado de la hipotenuesa
            // es igual a la suma del cuadrado de los catetos.
            // ejemplo: si mi robot esta en (200,150) x=200 y=150
            //          y tengo que ir a la esquina (0,0) que con el margen
            //          agregado queda (50,50) dx=50-200 y dy=50-150 = raiz((-150^2)+(-100^2))= hipotenusa
            if (distancia < distanciaMinima) {
                distanciaMinima = distancia;
                esquinaElegida = esquina;
            }
        }

        double dx = esquinaElegida.x() - robot.robotX;
        double dy = esquinaElegida.y() - robot.robotY;
        int angulo = (int) Math.toDegrees(Math.atan2(dx, dy));
        // en el juego el robot se maneja con grados y la funcion atan2 devuelve en radianes, por
        // lo tanto tengo que pasarlo a grados con toDegrees.
        // obs: trunco a int porque la funcion de turnTo recibe un int y Math retorna un double.
        if (angulo < 0) {
            angulo += 360;
            // aca paso lo mismo, el robot se maneja de 0 a 360 y el resultado actual
            // se maneja de -180 a 180 (por atan2), por lo tanto con esta suma de 360
        }
        robot.turnTo(angulo);
        int distancia = (int) Math.hypot(dx, dy);
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