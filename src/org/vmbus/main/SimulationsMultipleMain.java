package org.vmbus.main;

import org.pmw.tinylog.Configurator;
import org.pmw.tinylog.Level;
import org.wmbus.protocol.config.WMBusDeviceConfig;
import org.wmbus.simulations.network.multiple.WMBUSSimulationMultipleRun;

public class SimulationsMultipleMain {
    public static void main(String[] args) {
        Configurator.currentConfig()
                .formatPattern("{level}: {class}.{method}()\t{message}")
                .level(Level.TRACE)
                .activate();
        double CONF_TRASMITTER_POWER_LEVEL_DBM = 10;
        int CONF_NUMBER_OF_RETRASMISSION = 2;
        double CONF_ANTENNA_GAIN_DB = 3;
        double WMBUS_FREQUENCY_NOISE_DBM = -70;

        WMBusDeviceConfig wmbusDeviceConfig = new WMBusDeviceConfig(CONF_TRASMITTER_POWER_LEVEL_DBM,
                CONF_NUMBER_OF_RETRASMISSION,
                CONF_ANTENNA_GAIN_DB,
                WMBUS_FREQUENCY_NOISE_DBM);

        WMBUSSimulationMultipleRun simulation = new WMBUSSimulationMultipleRun(
                wmbusDeviceConfig,
                Integer.parseInt(args[0]),
                Integer.parseInt(args[0]),
                System.getProperty("user.dir") + "/mresults/",
                5,
                250,
                5000,
                false, true);
        simulation.run();
    }
}
