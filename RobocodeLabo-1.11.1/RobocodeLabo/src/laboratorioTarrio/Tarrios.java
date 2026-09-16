package laboratorioTarrio;
import robocode.*;
import laboratorioTarrio.estrategias.IEstrategiaDeGuerra;
import laboratorioTarrio.estrategias.IEstratega;
import laboratorioTarrio.estrategias.EstrategaJohnPrice;

public class Tarrios extends JuniorRobot
{

	private final IEstratega estratega;
	private IEstrategiaDeGuerra estrategia;

	public Tarrios() {
		this.estratega = EstrategaJohnPrice.INSTANCE;
	}

	@Override	
	public void run() {
		setColors(orange, blue, white, yellow, black);
		while (true) {
			estrategia = estratega.analizarEntorno(this);
			estrategia.run(this);
		}
	}

	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	@Override
	public void onScannedRobot() {
		if (estrategia != null) {
			estrategia.onScannedRobot(this);
		}
	}

	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */
	@Override
	public void onHitByBullet() {
		if (estrategia != null) {
			estrategia.onHitByBullet(this);
		}
	}
	
	/**
	 * onHitWall: What to do when you hit a wall
	 */
	@Override
	public void onHitWall() {
		if (estrategia != null) {
			estrategia.onHitWall(this);
		}
	}	
}