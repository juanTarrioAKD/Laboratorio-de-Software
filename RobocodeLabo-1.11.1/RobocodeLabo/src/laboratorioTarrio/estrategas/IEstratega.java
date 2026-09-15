package laboratorioTarrio.estrategas;

import laboratorioTarrio.estrategias.EstrategiaDeGuerra;
import robocode.JuniorRobot;

public interface IEstratega {
    EstrategiaDeGuerra analizarEntorno(JuniorRobot robot);
}
