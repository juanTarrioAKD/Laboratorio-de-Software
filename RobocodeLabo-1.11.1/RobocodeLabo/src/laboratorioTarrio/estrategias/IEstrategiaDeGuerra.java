package laboratorioTarrio.estrategias;

import robocode.JuniorRobot;

public sealed interface IEstrategiaDeGuerra permits EstrategiaCampera, EstrategiaParedErratica, EstrategaJohnPrice.EstrategiaEvasiva, EstrategaGhost.EstrategiaPerseguidora {
    public void run(JuniorRobot robot);
    public void onScannedRobot(JuniorRobot robot);
    public void onHitByBullet(JuniorRobot robot);
    public void onHitWall(JuniorRobot robot);
}