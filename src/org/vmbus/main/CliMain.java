package org.vmbus.main;

import org.apache.commons.cli.*;
import org.pmw.tinylog.Configurator;
import org.pmw.tinylog.Level;
import org.wmbus.protocol.config.WMBusDeviceConfig;
import org.wmbus.simulations.network.multiple.WMBUSSimulationMultipleRun;
import org.wmbus.simulations.network.single.WMBUSSimulationRun;

public class CliMain {
    public static void main(String[] args) {


        Options options = new Options();

        Option convergenceTime = new Option("tpd", "trasmitterpowerdbm", true, "Trasmitter power dbm");
        convergenceTime.setType(Double.class);
        options.addOption(convergenceTime);

        Option notConvergenceTime = new Option("nor", "numberofretrasmission", true, "Device number of retrasmission");
        notConvergenceTime.setType(Integer.class);
        options.addOption(notConvergenceTime);

        Option convergencePercentage = new Option("cp", "antennagain", true, "Antenna gain");
        convergencePercentage.setType(Double.class);
        options.addOption(convergencePercentage);

        Option notConvergencePercentage = new Option("nv", "noisevariance", true, "Noise variance");
        notConvergencePercentage.setType(Double.class);
        options.addOption(notConvergencePercentage);

        Option skippingStep = new Option("non", "numberofnodes", true, "Number of nodes");
        skippingStep.setType(Integer.class);
        options.addOption(skippingStep);

        Option multiple = new Option("m", "multi", false, "Multiple network convergence");
        multiple.setType(Boolean.class);
        options.addOption(multiple);

        Option mindistance = new Option("mind", "mindistance", true, "Min distance");
        mindistance.setType(Integer.class);
        options.addOption(mindistance);

        Option maxdistance = new Option("maxd", "maxdistance", true, "Max distance");
        maxdistance.setType(Integer.class);
        options.addOption(maxdistance);

        Option ns = new Option("ns", "networksize", true, "Network Size");
        maxdistance.setType(Integer.class);
        options.addOption(ns);

        Option vo = new Option("v", "verbose", true, "Verbose (OFF,INFO,TRACE)");
        vo.setType(String.class);
        options.addOption(vo);


        // Parsing stuff.
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        }
        catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);

            System.exit(1);
        }

        double CONF_TRASMITTER_POWER_LEVEL_DBM  = Double.parseDouble(cmd.getOptionValue("trasmitterpowerdbm","10"));
        int CONF_NUMBER_OF_RETRASMISSION    = Integer.parseInt(cmd.getOptionValue("numberofretrasmission" ,"2"));
        double CONF_ANTENNA_GAIN_DB  = Double.parseDouble(cmd.getOptionValue("antennagain", "3"));
        double WMBUS_FREQUENCY_NOISE_DBM =Double.parseDouble(cmd.getOptionValue("noisevariance", "-70"));
        int NUMBEROFNODES  = Integer.parseInt(cmd.getOptionValue("numberofnodes", "10"));
        int networkSize  = Integer.parseInt(cmd.getOptionValue("networksize", "5000"));
        int minDistance  = Integer.parseInt(cmd.getOptionValue("mindistance", "5"));
        int maxDistance = Integer.parseInt(cmd.getOptionValue("maxdistance", "250"));
        boolean multi = Boolean.parseBoolean(cmd.getOptionValue("multi", "false"));
        String verbose = cmd.getOptionValue("verbose", "OFF");

        Configurator.currentConfig()
                .formatPattern("{level}: {class}.{method}()\t{message}")
                .level(Level.valueOf(verbose))
                .activate();

        WMBusDeviceConfig wmbusDeviceConfig = new WMBusDeviceConfig(CONF_TRASMITTER_POWER_LEVEL_DBM,
                CONF_NUMBER_OF_RETRASMISSION,
                CONF_ANTENNA_GAIN_DB,
                WMBUS_FREQUENCY_NOISE_DBM);

        if (multi){
            WMBUSSimulationMultipleRun simulation = new WMBUSSimulationMultipleRun( wmbusDeviceConfig,
                    NUMBEROFNODES,
                    NUMBEROFNODES,
                    System.getProperty("user.dir") + "/mresults/",
                    minDistance,
                    maxDistance,
                    networkSize
            );
            simulation.run();
        } else {
            WMBUSSimulationRun simulation = new WMBUSSimulationRun(
                    NUMBEROFNODES,
                    NUMBEROFNODES,
                    networkSize,
                    System.getProperty("user.dir") + "/results/",
                    wmbusDeviceConfig,
                    minDistance,
                    maxDistance
            );
            simulation.run();
        }
    }
}
