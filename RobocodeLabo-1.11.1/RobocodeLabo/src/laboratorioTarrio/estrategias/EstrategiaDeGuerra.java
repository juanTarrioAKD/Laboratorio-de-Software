package laboratorioTarrio.estrategias;

import robocode.JuniorRobot;

public sealed interface EstrategiaDeGuerra permits EstrategiaCampera, EstrategiaParedErratica, EstrategaJohnPrice.EstrategiaEvasiva {
    public abstract void run(JuniorRobot robot);
    public void onScannedRobot(JuniorRobot robot);
    public void onHitByBullet(JuniorRobot robot);
    public void onHitWall(JuniorRobot robot);
}