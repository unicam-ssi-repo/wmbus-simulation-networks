package org.wmbus.simulations.network.single;

import org.wmbus.protocol.config.WMBusDeviceConfig;

import java.io.File;

public class WMBUSSimulationRun {
    private int networkSizeFrom;
    private int networkSizeTo;

    private String folderPath;
    private WMBusDeviceConfig deviceConfig;
    private int minNodeDistance;
    private int maxNodeDistance;
    private int networkSizePX;
    private boolean savePathOutput;
    private boolean convergencePathOutput;

    public WMBUSSimulationRun(int networkSizeFrom, int networkSizeTo, int networkSizePX, String folderPath, WMBusDeviceConfig deviceConfig, int minNodeDistance, int maxNodeDistance, boolean savePathOutput, boolean convergencePathOutput) {
        this.networkSizeFrom = networkSizeFrom;
        this.networkSizeTo = networkSizeTo;
        this.networkSizePX = networkSizePX;
        this.folderPath = folderPath;
        this.deviceConfig = deviceConfig;
        this.minNodeDistance = minNodeDistance;
        this.maxNodeDistance = maxNodeDistance;
        this.convergencePathOutput = convergencePathOutput;
        this.savePathOutput = savePathOutput;
    }

    public void run() {
        WMBUSSimulations simulations = new WMBUSSimulations();

        for (int nodes = this.networkSizeFrom; nodes < this.networkSizeTo + 1; nodes++) {

            String baseFolder = this.folderPath + String.valueOf(nodes) +"("+String.valueOf(this.minNodeDistance)+"_"+String.valueOf(this.maxNodeDistance)+")"+ "/";
            File dir = new File(baseFolder);
            boolean succ = dir.mkdir();

            if (!succ) {
                System.out.println("Failed to create " + baseFolder+ "Please clean the directory.");
                continue;
            }
            simulations.performSimulationsForANetwork(nodes,0, networkSizePX,this.minNodeDistance,this.maxNodeDistance, baseFolder,deviceConfig, savePathOutput, convergencePathOutput );
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
