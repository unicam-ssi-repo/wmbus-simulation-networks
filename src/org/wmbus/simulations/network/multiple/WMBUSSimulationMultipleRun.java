package org.wmbus.simulations.network.multiple;

import org.wmbus.protocol.config.WMBusDeviceConfig;
import org.wmbus.simulations.network.single.WMBUSSimulations;

import java.io.File;

public class WMBUSSimulationMultipleRun {
    private WMBusDeviceConfig wmbusDeviceConfig;
    private int networkSizeFrom;
    private int networkSizeTo;
    private String folderPath;
    private int minNodeDistance;
    private int maxNodeDistance;
    private int networkSizePX;

    public WMBUSSimulationMultipleRun(WMBusDeviceConfig wmbusDeviceConfig, int networkSizeFrom, int networkSizeTo, String folderPath, int minNodeDistance, int maxNodeDistance, int networkSizePX) {
        this.wmbusDeviceConfig = wmbusDeviceConfig;
        this.networkSizeFrom = networkSizeFrom;
        this.networkSizeTo = networkSizeTo;
        this.folderPath = folderPath;
        this.minNodeDistance = minNodeDistance;
        this.maxNodeDistance = maxNodeDistance;
        this.networkSizePX = networkSizePX;
    }

    public void run() {
        WMBUSMultipleSimulations simulations = new WMBUSMultipleSimulations();

        for (int nodes = this.networkSizeFrom; nodes < this.networkSizeTo + 1; nodes++) {

            String baseFolder = this.folderPath + String.valueOf(nodes) + "/";
            File dir = new File(baseFolder);
            boolean succ = dir.mkdir();

            if (!succ) {
                System.out.println("Failed to create " + baseFolder+ " . Please clean the directory");
                continue;
            }
            simulations.performSimulationsForANetwork(nodes, this.networkSizePX, this.minNodeDistance,this.maxNodeDistance, baseFolder, wmbusDeviceConfig);
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
