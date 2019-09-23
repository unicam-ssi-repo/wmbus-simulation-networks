package org.wmbus.simulations.network.multiple;

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

public class WMBUSMultipleSimulations implements WMbusSimulationEventInterface {
    private PrintWriter convergenceResult;
    private WMBUSSimulationPathTrack track;
    private PrintWriter pathResult;
    private PrintWriter readableFormat;
    private PrintWriter dataCsvFormat;

    public void WMBUSMultipleSimulations() {
        // generate array of multiple convergences.
    }

    public void provideMeasure(double measuredValue, int step, double convergenceValue) {
        String stepStr = "";
        if (step == ConvergenceState.CONVERGENCE_STATE_CONTINUE) {
            stepStr = "CONTINUE";
        } else if (step == ConvergenceState.CONVERGENCE_STATE_STOP_CONVERGENCE) {
            stepStr = "CONVERGE";
        } else if (step == ConvergenceState.CONVERGENCE_STATE_STOP_NOTCONVERGENCE) {
            stepStr = "NONVERGE";
        }
        convergenceResult.println(measuredValue + '\t' + stepStr + '\t' + convergenceValue);
    }

    @Override
    public void pathStart(boolean requestType) {
        this.track = new WMBUSSimulationPathTrack(requestType);
    }

    @Override
    public void pathEnd(boolean success) {
        // Can be triggered many times.
        if (this.track != null) {
            this.track.success = success;
            // save data.
            pathResult.println(this.track.toString());
            this.track = null;
        }
    }

    @Override
    public void pathRequest(int source, int destination, double distance) {
        this.track.addHop(source, destination, distance);
    }

    @Override
    public void pathPredict(Integer integer, ArrayList<Integer> path) {
        String ps = "";
        for (int i = 0; i < path.size(); i++) {
            if (i == 0) {
                ps += String.valueOf(path.get(i));
            } else {
                ps = ps + ',' + String.valueOf(path.get(i));
            }
        }
        pathResult.println("PR" + '\t' + "NULL" + '\t' + integer + '\t' + ps);
    }

    @Override
    public void globalPathEnd(boolean b) {
        pathResult.println("DT" + '\t' + b + "\tNULL\tNULL");
    }

    public boolean performSimulationsForANetwork(int nodes, int nodeMinRadius, int nodeMaxRadius, String networkPath) {
        // Generate network.

        NetworkGeneratorInterconnected2DInstance neighbornSpace;
        boolean converges = false;
        int networkLoad = 0;
        ArrayList<Integer> convergenceLastMessage = new ArrayList<Integer>();
        ArrayList<ConvergenceModel> convergenceModels;

        convergenceModels = new ArrayList<ConvergenceModel>();
        for (int i = 0; i < 4;i++){
            convergenceModels.add(new InitialSkipWithTimesAverageConvergenceModel(
                    new InitialSkipTimesAverageConvergenceConfigModel(
                            100,
                            1,
                            5,
                            5,
                            5
                    )
            ));
        }

        for (int i = 0; i < 4;i++) {
            convergenceLastMessage.add(ConvergenceState.CONVERGENCE_STATE_CONTINUE);
        }
        PrintWriter globalLog = null;
        try {
            globalLog = new PrintWriter(networkPath + "/multipleSimulation.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while (true){
            System.out.println("Create network with " + String.valueOf(nodes) + " nodes  networkLoad:" + String.valueOf(networkLoad));
            Instant nstart = Instant.now();
            YangNetwork attempt = NetworkGeneratorHelper.generateInterconnectedRadiusNetwork(nodes, 2500, nodeMinRadius, nodeMaxRadius, 2500, 2500, -1);
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
            attempt.saveNetworkImage(5000, 5000, networkPath + String.valueOf(networkLoad) + "/with_space.png");

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
                    c++;
                    Instant start = Instant.now();
                    // convergenceResult = "";
                    // pathResult = "";
                    System.out.println("Simulation #" + String.valueOf(c) + " starts");
                    System.out.flush();
                    folder = networkPath + String.valueOf(networkLoad) + "/" +
                            (withHamming ? "wH" : "nH") +
                            (withWakeUp ? "wW" : "nW") +
                            (withDetailNoise ? "wD" : "nD");
                    File dir = new File(folder);
                    boolean succ = dir.mkdir();

                    try {
                        readableFormat = new PrintWriter(folder + "/readableFormat.txt");
                        dataCsvFormat = new PrintWriter(folder + "/data.csv");
                        convergenceResult = new PrintWriter(folder + "/convergence.csv");
                        pathResult = new PrintWriter(folder + "/paths.csv");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    WMBusStats result = WMBUSSimulationGenerator.performSimulation(
                            new SimulationNetworkWithDistance(neighbornSpace), withHamming, withWakeUp, withDetailNoise, this
                    );
                    Instant end = Instant.now();
                    System.out.println("Simulation #" + String.valueOf(c) + " Duration: " + Duration.between(start, end).toSeconds() + " seconds");
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
                    double fault = (result.masterTrasmissionFaultWithNoUpdate + result.masterTrasmissionFaultWithUpdate) / result.masterSentMessage;
                    int convMeasure = convergenceModels.get(c - 1).addMeasure(fault);
                    System.out.println("Network Simulation Convergence: " + convergenceModels.get(c - 1).percentageConvergence+ " %");

                    if (convMeasure == ConvergenceState.CONVERGENCE_STATE_STOP_CONVERGENCE) {
                        System.out.println("Network Convergence Opinion: CONVERGE");
                        globalLog.println( "" + String.valueOf(networkLoad)+ '\t' + String.valueOf(c-1)+ '\t'+ "OK");
                    } else if (convMeasure == ConvergenceState.CONVERGENCE_STATE_STOP_NOTCONVERGENCE) {
                        System.out.println("Network Convergence Opinion: NOTCONVERGE");
                        globalLog.println( "" + String.valueOf(networkLoad)+ '\t' + String.valueOf(c-1)+ '\t'+ "NC");
                    } else {
                        System.out.println("Network Convergence Opinion: CONTINUE");
                        globalLog.println( "" + String.valueOf(networkLoad)+ '\t' + String.valueOf(c-1)+ '\t'+ "CO");
                    }
                    convergenceLastMessage.set(c-1,convMeasure);

                    globalLog.flush();
                }
            }

            System.out.print("Convergence State:");
            for (int i = 0; i < 4; i++) {
                if (convergenceLastMessage.get(i) == ConvergenceState.CONVERGENCE_STATE_STOP_CONVERGENCE) {
                    System.out.print("OK-");
                } else if (convergenceLastMessage.get(i) == ConvergenceState.CONVERGENCE_STATE_STOP_NOTCONVERGENCE) {
                    System.out.print("NO-");
                } else {
                    System.out.print("CO-");
                }
            }
            System.out.println("");

            int cc = 0; // count convergence
            for (int i = 0; i < 4; i++) {
                if (convergenceLastMessage.get(i) == ConvergenceState.CONVERGENCE_STATE_STOP_NOTCONVERGENCE){
                    System.out.println("Network No convergence in the simulation: "+i);
                    globalLog.close();
                    return false;
                } else if (convergenceLastMessage.get(i) == ConvergenceState.CONVERGENCE_STATE_STOP_CONVERGENCE){
                    cc++;
                }
            }
            if (cc==4){
                System.out.println("All the simulation convergences");
                globalLog.close();
                return true;
            } else if (cc==3){
                System.out.println("Almost!!! Only one miss, we can do it!");
            }else if (cc==2){
                System.out.println("Fifty Fifty throw the coin! ( 2 simulation converge )");
            }else if (cc==1){
                System.out.println("Something happen. One simulation converge! huppy!");
            }
            networkLoad++;
        }
    }
}