package org.vmbus.main;

import org.pmw.tinylog.Configurator;
import org.pmw.tinylog.Level;
import org.wmbus.simulations.WMBUSSimulationRun;

public class SimulationsMain {
    public static void main(String[] args) {
        Configurator.currentConfig()
                .formatPattern("{level}: {class}.{method}()\t{message}")
                .level(Level.OFF)
                .activate();

        WMBUSSimulationRun simulation = new WMBUSSimulationRun(
                Integer.parseInt(args[0]),
                Integer.parseInt(args[0]),
                System.getProperty("user.dir") + "/results/"
        );
        simulation.run();
    }
}
