package org.wmbus.simulations;

import org.wmbus.simulation.WMBusSimulation;
import org.wmbus.simulation.WMbusSimulationConfig;
import org.wmbus.simulation.convergence.model.ConvergenceModel;
import org.wmbus.simulation.convergence.model.InitialSkipWithTimesAverageConvergenceModel;
import org.wmbus.simulation.convergence.model.config.InitialSkipTimesAverageConvergenceConfigModel;
import org.wmbus.simulation.events.WMbusSimulationEventInterface;
import org.wmbus.simulation.stats.WMBusStats;
import yang.simulation.network.SimulationNetworkWithDistance;

public class WMBUSSimulationGenerator {
    public static WMBusStats performSimulation(SimulationNetworkWithDistance simulationWrapper, boolean withHamming, boolean withWakeup, boolean withDetailNoise, WMbusSimulationEventInterface events) {
        ConvergenceModel simulationConvergence = new InitialSkipWithTimesAverageConvergenceModel(new InitialSkipTimesAverageConvergenceConfigModel(
                1000,
                1,
                5,
                10,
                10
        ));
        WMbusSimulationConfig config = new WMbusSimulationConfig(
                withHamming, withWakeup, withDetailNoise
        );

        WMBusSimulation simulation = new WMBusSimulation(simulationWrapper.network, config, simulationConvergence, events);
        simulation.run();
        return simulation.getResults();
    }
}
