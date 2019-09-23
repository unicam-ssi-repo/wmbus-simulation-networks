package org.wmbus.simulations.network.multiple;

import org.wmbus.simulations.network.single.WMBUSSimulations;

import java.io.File;

public class WMBUSSimulationMultipleRun {
    private int networkSizeFrom;
    private int networkSizeTo;
    private String folderPath;

    public WMBUSSimulationMultipleRun(int networkSizeFrom, int networkSizeTo, String folderPath) {
        this.networkSizeFrom = networkSizeFrom;
        this.networkSizeTo = networkSizeTo;
        this.folderPath = folderPath;
    }

    public void run() {
        WMBUSMultipleSimulations simulations = new WMBUSMultipleSimulations();

        for (int nodes = this.networkSizeFrom; nodes < this.networkSizeTo + 1; nodes++) {

            String baseFolder = this.folderPath + String.valueOf(nodes) + "/";
            File dir = new File(baseFolder);
            boolean succ = dir.mkdir();

            if (!succ) {
                System.out.println("Failed to create " + baseFolder);
                continue;
            }
            simulations.performSimulationsForANetwork(nodes,5,25, baseFolder);
            /*for (int networkRange = 0; networkRange < 3; networkRange++){
                if (networkRange == 0){
                    simulations.performSimulationsForANetwork(nodes,networkRange,10,50, baseFolder);
                }else if (networkRange == 1 ){
                    simulations.performSimulationsForANetwork(nodes,networkRange,50,100, baseFolder);
                }else {
                    simulations.performSimulationsForANetwork(nodes,networkRange,100,150, baseFolder);
                }

            }*/


        }
    }
}
