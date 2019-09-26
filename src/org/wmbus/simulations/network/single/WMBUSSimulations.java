package org.wmbus.simulations.network.single;

import org.wmbus.protocol.config.WMBusDeviceConfig;
import org.wmbus.simulation.ResultTable;
import org.wmbus.simulation.convergence.model.ConvergenceModel;
import org.wmbus.simulation.convergence.model.InitialSkipWithTimesAverageConvergenceModel;
import org.wmbus.simulation.convergence.model.config.InitialSkipTimesAverageConvergenceConfigModel;
import org.wmbus.simulation.convergence.state.ConvergenceState;
import org.wmbus.simulation.events.WMbusSimulationEventInterface;
import org.wmbus.simulation.stats.WMBusStats;
import org.wmbus.simulations.WMBUSSimulationGenerator;
import org.wmbus.simulations.stats.WMBUSSimulationPathTrack;
import yang.generators.twod.interconnected.NetworkGeneratorInterconnected2DInstance;
import yang.helpers.NetworkGeneratorHelper;
import yang.nodes.YangNetwork;
import yang.simulation.network.SimulationNetworkWithDistance;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

public class WMBUSSimulations implements WMbusSimulationEventInterface {

    private ConvergenceModel convergenceModel;
    private PrintWriter convergenceResult;
    private WMBUSSimulationPathTrack track;
    private PrintWriter pathResult;
    private PrintWriter readableFormat;
    private PrintWriter dataCsvFormat;

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
        convergenceResult.println( measuredValue+'\t'+stepStr+'\t'+convergenceValue);
    }

    @Override
    public void pathStart(boolean requestType) {
        this.track = new WMBUSSimulationPathTrack(requestType);
    }

    @Override
    public void pathEnd(boolean success) {
        // Can be triggered many times.
        if (this.track != null){
            this.track.success = success;
            // save data.
            pathResult.println(this.track.toString());
            this.track = null;
        }
    }

    @Override
    public void pathRequest(int source, int destination, double distance) {
        this.track.addHop(source,destination,distance);
    }

    @Override
    public void pathPredict(Integer integer, ArrayList<Integer> path) {
        String ps = "";
        for (int i = 0; i < path.size(); i++) {
            if (i==0){
                ps += String.valueOf(path.get(i));
            }else{
                ps = ps + ',' + String.valueOf(path.get(i));
            }
        }
        pathResult.println("PR"+'\t'+"NULL"+'\t'+integer+'\t'+ps);
    }

    @Override
    public void globalPathEnd(boolean b) {
        pathResult.println("DT"+'\t'+b+"\tNULL\tNULL");
    }

    public boolean performSimulationsForANetwork(int nodes, int networkLoad, int networkSize, int nodeMinRadius, int nodeMaxRadius, String networkPath, WMBusDeviceConfig deviceConfig) {
        // Generate network.

        NetworkGeneratorInterconnected2DInstance neighbornSpace;
        System.out.println("Create network with " + String.valueOf(nodes)+ " nodes  networkLoad:"+String.valueOf(networkLoad));
        Instant nstart = Instant.now();
        // Radius and master position
        YangNetwork attempt = NetworkGeneratorHelper.generateInterconnectedRadiusNetwork(nodes,networkSize/2,nodeMinRadius,nodeMaxRadius,networkSize/2,networkSize/2,-1);
        Instant nend = Instant.now();
        System.out.println("Network created in  " + Duration.between(nstart, nend).toSeconds() + " seconds");
        System.out.flush();
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
        attempt.saveNetworkImage(networkSize,networkSize, nodeMaxRadius, networkPath + String.valueOf(networkLoad) + "/with_space.png");

        neighbornSpace = (NetworkGeneratorInterconnected2DInstance) attempt.withNeighbor;

        boolean withHamming = false;
        boolean withWakeUp = false;
        boolean withDetailNoise = false;
        String folder;
        int c = 0;
        for (int i = 0; i < 2; i++) {
            // for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 2; k++) {
                    withHamming = (i == 0);
                    withWakeUp = false;
                    withDetailNoise = (k == 0);
                    Instant start = Instant.now();
                    c++;
                    // convergenceResult = "";
                    // pathResult = "";
                    System.out.println("Simulation #"+String.valueOf(c)+" starts");
                    System.out.flush();
                    folder = networkPath + String.valueOf(networkLoad) + "/" +
                            (withHamming ? "wH" : "nH") +
                            (withWakeUp ? "wW" : "nW") +
                            (withDetailNoise ? "wD" : "nD");
                    File dir = new File(folder);
                    boolean succ = dir.mkdir();

                    try {
                        readableFormat = new PrintWriter(folder+"/readableFormat.txt");
                        dataCsvFormat= new PrintWriter(folder+"/data.csv");
                        convergenceResult = new PrintWriter(folder+"/convergence.csv");
                        pathResult = new PrintWriter(folder+"/paths.csv");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    WMBusStats result = WMBUSSimulationGenerator.performSimulation(
                            new SimulationNetworkWithDistance(neighbornSpace), withHamming, withWakeUp, withDetailNoise, this,
                            deviceConfig);

                    Instant end = Instant.now();
                    System.out.println("Simulation #"+String.valueOf(c)+ " Duration: "+ Duration.between(start, end).toSeconds() + " seconds");
                    System.out.flush();
                    ResultTable res = result.printResults();




                    String prettyPrint = res.prettyPrint();

                    readableFormat.println(prettyPrint);
                    readableFormat.close();

                    String csv = res.printHeader(true) + '\n' + res.printValues(true);

                    dataCsvFormat.println(prettyPrint);
                    dataCsvFormat.close();

                    convergenceResult.close();
                    pathResult.close();

                }
            //}
        }
        return true;
    }
}
