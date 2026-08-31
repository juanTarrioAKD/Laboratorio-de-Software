package laboratorio.estrategias;

import robocode.JuniorRobot;

public final class EstrategiaCampera implements EstrategiaDeGuerra{


    @Override
    public void run(JuniorRobot robot) {
        int margen = 50;

        Punto[] esquinas = {
                new Punto(margen, margen),
                new Punto(margen, robot.fieldHeight - margen),
                new Punto(robot.fieldWidth - margen, margen),
                new Punto(robot.fieldWidth - margen, robot.fieldHeight - margen)
                // Porque las guardo de esta manera? actualmente el juego arranca por default en 0,0
                // si no pongo un margen de pixeles el robot se chocha con la pared cuando queire ir a la esquina
                // por lo tanto hago que no se choche ya que va a esar 50 pixels alejado de la pared.
                // sin el margen las coordenadas quedan:
                // (0,0) esquina inferior izquierda
                // (fieldWidth,0) esquina inferior derecha
                // (0,fieldHeight) esquina superior izquierda
                // (fieldWidth,fieldHeight) esquina superior derecha
                // obs: es igual que las coordenadas de los robots de pascal en TALLER.
        };

        Punto esquinaElegida = esquinas[0];
        double distanciaMinima = Double.MAX_VALUE;

        for (Punto esquina : esquinas) {
            double dx = esquina.x() - robot.robotX;
            double dy = esquina.y() - robot.robotY;
            double distancia = Math.hypot(dx, dy);
            // dx calcula la distancia que me tengo que mover en el eje x
            // dy calcula la distancia que me tengo que mover en el eje y
            // lo que hace hypot es basicamente calcular la hipotenusa, dado el cateto x
            // y el cateto hace el teorema de pitagoras el cuadrado de la hipotenuesa
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
        // aca no me andaba el calculo para ver hacia donde tiene que ir el robot, pero es porque
        // en el juego el robot se maneja con grados y la funcion atan2 devuelve en radianes, por
        // lo tanto tengo que pasarlo a grados con toDegrees.
        // obs: trunco a int porque la funcion de turnTo recibe un int y Math retorna un double.
        if (angulo < 0) {
            angulo += 360;
            // aca paso lo mismo, el robot se maneja de 0 a 360 y el resultado actual
            // se maneja de -180 a 180 (por atan2), por lo tanto con esta suma de 360
            // arreglo para que el turnTo lo entienda (esto no lo podisa resolver y me lo resolvio Claude XD)
        }
        robot.turnTo(angulo);
        int distancia = (int) Math.hypot(dx, dy);
        robot.ahead(distancia);

        while (true) {
            robot.turnGunRight(360);
        }
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