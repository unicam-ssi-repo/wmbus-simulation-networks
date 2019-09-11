package org.wmbus.simulations;

import org.wmbus.simulation.ResultTable;
import org.wmbus.simulation.convergence.model.ConvergenceModel;
import org.wmbus.simulation.convergence.model.InitialSkipWithTimesAverageConvergenceModel;
import org.wmbus.simulation.convergence.model.config.InitialSkipTimesAverageConvergenceConfigModel;
import org.wmbus.simulation.convergence.state.ConvergenceState;
import org.wmbus.simulation.events.WMbusSimulationEventInterface;
import org.wmbus.simulation.stats.WMBusStats;
import yang.generators.twod.interconnected.NetworkGeneratorInterconnected2DInstance;
import yang.helpers.NetworkGeneratorHelper;
import yang.nodes.YangNetwork;
import yang.simulation.network.SimulationNetworkWithDistance;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class WMBUSSimulations implements WMbusSimulationEventInterface {

    private ConvergenceModel convergenceModel;
    private String convergenceResult;

    public void WMBUSSimulations() {
        this.convergenceModel = new InitialSkipWithTimesAverageConvergenceModel(
                new InitialSkipTimesAverageConvergenceConfigModel(
                        1000,
                        1,
                        5,
                        5,
                        5
                )
        );

    }

    public void provideMeasure(double measuredValue, int step, double convergenceValue) {
        String stepStr = "";
        if (step== ConvergenceState.CONVERGENCE_STATE_CONTINUE){
            stepStr = "CONTINUE";
        } else if (step== ConvergenceState.CONVERGENCE_STATE_STOP_CONVERGENCE){
            stepStr = "CONVERGE";
        } else if (step== ConvergenceState.CONVERGENCE_STATE_STOP_NOTCONVERGENCE){
            stepStr = "NONVERGE";
        }
        convergenceResult += measuredValue+" "+stepStr+" "+convergenceValue+'\n';
    }

    public boolean performSimulationsForANetwork(int nodes, String networkPath) {
        // Generate network.
        int networkLoad = 0;

        NetworkGeneratorInterconnected2DInstance neighbornSpace;
        YangNetwork attempt = NetworkGeneratorHelper.generateInterconnectedRadiusNetwork(
                nodes,
                5000, // Only the radius. 2*5000 -> 10000 space.
                100,
                1500,
                5000,
                5000,
                -1);
        // No generated network.
        if (attempt == null) {
            return false;
        }

        String np = networkPath + String.valueOf(networkLoad);
        File networkDir = new File(np);
        boolean networkSucc = networkDir.mkdir();

        if (networkSucc == false) {
            return false;
        }

        // Network generated.
        // EXPORT NETWORK.
        attempt.save(networkPath + String.valueOf(networkLoad) + "/with_space.csv", true);
        attempt.save(networkPath + String.valueOf(networkLoad) + "/without_space.csv", false);
        attempt.saveNetworkImage(networkPath + String.valueOf(networkLoad) + "/with_space.png");

        neighbornSpace = (NetworkGeneratorInterconnected2DInstance) attempt.withNeighbor;

        boolean withHamming = false;
        boolean withWakeUp = false;
        boolean withDetailNoise = false;
        String folder;
        int c = 0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 2; k++) {
                    withHamming = (i == 0);
                    withWakeUp = (j == 0);
                    withDetailNoise = (k == 0);
                    System.out.println("Launch simulation #"+String.valueOf(c));
                    System.out.flush();
                    c++;

                    convergenceResult = "";
                    WMBusStats result = WMBUSSimulationGenerator.performSimulation(
                            new SimulationNetworkWithDistance(neighbornSpace), withHamming, withWakeUp, withDetailNoise, this
                    );
                    ResultTable res = result.printResults();

                    folder = networkPath + String.valueOf(networkLoad) + "/" +
                            (withHamming ? "wH" : "nH") +
                            (withWakeUp ? "wW" : "nW") +
                            (withDetailNoise ? "wD" : "nD");
                    File dir = new File(folder);
                    boolean succ = dir.mkdir();

                    String prettyPrint = res.prettyPrint();

                    try (PrintWriter out = new PrintWriter(folder+"/readableFormat.txt")) {
                        out.println(prettyPrint);
                        out.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    String csv = res.printHeader(true) + '\n' + res.printValues(true);

                    try (PrintWriter out = new PrintWriter(folder+"/data.csv")) {
                        out.println(csv);
                        out.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    try (PrintWriter out = new PrintWriter(folder+"/convergence.csv")) {
                        out.println(convergenceResult);
                        out.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }


                }
            }
        }
        return true;
    }
}
