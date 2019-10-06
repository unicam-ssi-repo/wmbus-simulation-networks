package org.vmbus.main;

import org.pmw.tinylog.Configurator;
import org.pmw.tinylog.Level;
import org.wmbus.protocol.config.WMBusDeviceConfig;
import org.wmbus.simulations.network.single.WMBUSSimulationRun;

public class SimulationsMain {
    public static void main(String[] args) {
        Configurator.currentConfig()
                .formatPattern("{level}: {class}.{method}()\t{message}")
                .level(Level.OFF)
                .activate();

        double CONF_TRASMITTER_POWER_LEVEL_DBM = 10;
        int CONF_NUMBER_OF_RETRASMISSION = 2;
        double CONF_ANTENNA_GAIN_DB = 3;
        double WMBUS_FREQUENCY_NOISE_DBM = -70;

        WMBusDeviceConfig wmbusDeviceConfig = new WMBusDeviceConfig(CONF_TRASMITTER_POWER_LEVEL_DBM,
                CONF_NUMBER_OF_RETRASMISSION,
                CONF_ANTENNA_GAIN_DB,
                WMBUS_FREQUENCY_NOISE_DBM);

        WMBUSSimulationRun simulation = new WMBUSSimulationRun(
                Integer.parseInt(args[0]),
                Integer.parseInt(args[0]),
                5000,
                System.getProperty("user.dir") + "/results/",
                wmbusDeviceConfig,
                5,
                250,
                false, true);
        simulation.run();
    }
}
