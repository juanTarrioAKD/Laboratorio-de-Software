package laboratorio.Estrategias;

import robocode.JuniorRobot;

public interface EstrategiaDeGuerra {

    void run(JuniorRobot);
    void onScannedRobot(JuniorRobot);
    void onHitByBullet(JuniorRobot);
    void onHitWall(JuniorRobot);

}