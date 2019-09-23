package org.vmbus.main;

import org.pmw.tinylog.Configurator;
import org.pmw.tinylog.Level;
import org.wmbus.simulations.network.multiple.WMBUSSimulationMultipleRun;
import org.wmbus.simulations.network.single.WMBUSSimulationRun;

public class SimulationsMultipleMain {
    public static void main(String[] args) {
        Configurator.currentConfig()
                .formatPattern("{level}: {class}.{method}()\t{message}")
                .level(Level.OFF)
                .activate();

        WMBUSSimulationMultipleRun simulation = new WMBUSSimulationMultipleRun(
                Integer.parseInt(args[0]),
                Integer.parseInt(args[0]),
                System.getProperty("user.dir") + "/mresults/"
        );
        simulation.run();
    }
}
