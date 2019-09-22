package org.wmbus.simulations;

import java.io.File;

public class WMBUSSimulationRun {
    private int networkSizeFrom;
    private int networkSizeTo;
    private String folderPath;

    public WMBUSSimulationRun(int networkSizeFrom, int networkSizeTo, String folderPath) {
        this.networkSizeFrom = networkSizeFrom;
        this.networkSizeTo = networkSizeTo;
        this.folderPath = folderPath;
    }

    public void run() {
        WMBUSSimulations simulations = new WMBUSSimulations();

        for (int nodes = this.networkSizeFrom; nodes < this.networkSizeTo + 1; nodes++) {

            String baseFolder = this.folderPath + String.valueOf(nodes) + "/";
            File dir = new File(baseFolder);
            boolean succ = dir.mkdir();

            if (!succ) {
                System.out.println("Failed to create " + baseFolder);
                continue;
            }
            simulations.performSimulationsForANetwork(nodes,0,5,25, baseFolder);
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
