package org.wmbus.simulations.stats;

import java.util.ArrayList;

public class WMBUSSimulationPathTrack {
    public ArrayList<Integer> path;
    public double distance;
    public boolean success;
    public boolean requestType;

    public WMBUSSimulationPathTrack(boolean requestType) {
        this.path = new ArrayList<Integer>();
        this.distance = 0;
        this.requestType = requestType;
    }


    public void addHop(Integer source, Integer destination, double distance){
        this.distance += distance;
        if (!path.isEmpty()){
            path.add(destination);
        } else {
            path.add(source);
            path.add(destination);
        }
        this.distance+=distance;
    }

    public String toString(){
        String rtype = this.requestType?"RQ":"RS";
        String s = this.success?"SUCC":"FAIL";
        String d = String.valueOf(this.distance);
        String ps = "";
        for (int i = 0; i < path.size(); i++) {
            if (i==0){
                ps += String.valueOf(path.get(i));
            }else{
                ps = ps + ',' + String.valueOf(path.get(i));
            }
        }
        return rtype+'\t'+s+'\t'+d+'\t'+ps;
    }

}
