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
            System.out.println("Create network with " + String.valueOf(nodes)+ " nodes ");
            System.out.flush();
            simulations.performSimulationsForANetwork(nodes, baseFolder);

        }
    }
}
