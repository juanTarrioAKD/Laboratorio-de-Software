package laboratorio.estrategias;

import robocode.JuniorRobot;

public sealed interface EstrategiaDeGuerra permits EstrategiaCampera {

    void run(JuniorRobot robot);
    void onScannedRobot(JuniorRobot robot);
    void onHitByBullet(JuniorRobot robot);
    void onHitWall(JuniorRobot robot);

}