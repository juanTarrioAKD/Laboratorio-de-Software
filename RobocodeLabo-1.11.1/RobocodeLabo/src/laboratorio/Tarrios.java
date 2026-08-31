package laboratorio;
import robocode.*;
import laboratorio.estrategias.EstrategiaDeGuerra;
import laboratorio.estrategias.EstrategiaCampera;

public class Tarrios extends JuniorRobot
{
	private final EstrategiaDeGuerra estrategia;
	public Tarrios() {
		this.estrategia = new EstrategiaCampera();
	}

	@Override	
	public void run() {

		setColors(orange, blue, white, yellow, black);
		estrategia.run(this);
	}

	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	@Override
	public void onScannedRobot() {
		estrategia.onScannedRobot(this);
	}

	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */
	@Override
	public void onHitByBullet() {
		estrategia.onHitByBullet(this);
	}
	
	/**
	 * onHitWall: What to do when you hit a wall
	 */
	@Override
	public void onHitWall() {
		estrategia.onHitWall(this);
	}	
}