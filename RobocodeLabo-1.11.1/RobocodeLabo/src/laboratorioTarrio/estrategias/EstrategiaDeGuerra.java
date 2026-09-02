package laboratorioTarrio.estrategias;

import robocode.JuniorRobot;

public sealed abstract class EstrategiaDeGuerra permits EstrategiaCampera, EstrategiaParedErratica {

    public abstract void run(JuniorRobot robot);

    public void onScannedRobot(JuniorRobot robot) {
        robot.bearGunTo(robot.scannedBearing);
        robot.fire(calcularPotencia(robot.scannedDistance));
    }

    public void onHitByBullet(JuniorRobot robot) {
        robot.bearGunTo(robot.hitByBulletBearing);
        robot.fire(calcularPotencia(robot.scannedDistance));
    }

    public void onHitWall(JuniorRobot robot) {
        robot.back(10);
    }

    protected double calcularPotencia(double distancia) { return (distancia < 100) ? 3 : 2; }
}