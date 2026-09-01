package laboratorio.estrategias;

import robocode.JuniorRobot;

public sealed abstract class EstrategiaDeGuerra permits EstrategiaCampera, EstrategiaParedErratica {

    public abstract void run(JuniorRobot robot);
    public abstract void onScannedRobot(JuniorRobot robot);
    public abstract void onHitByBullet(JuniorRobot robot);
    public abstract void onHitWall(JuniorRobot robot);
    protected double calcularPotencia(double distancia) { return (distancia < 100) ? 3 : 2; }
}