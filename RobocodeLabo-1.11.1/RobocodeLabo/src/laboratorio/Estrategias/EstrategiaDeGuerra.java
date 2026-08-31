package laboratorio.Estrategias;

import robocode.JuniorRobot;

public sealed interface EstrategiaDeGuerra permits EstrategiaCampera{

    void run(JuniorRobot);
    void onScannedRobot(JuniorRobot);
    void onHitByBullet(JuniorRobot);
    void onHitWall(JuniorRobot);

}