/* 
 * 
 * 
 */
package report;

/**
 * Records the average buffer occupancy and its variance with format:
 * <p>
 * <Simulation time> <average buffer occupancy % [0..100]> <variance>
 * </p>
 *
 *
 */
import java.util.*;
//import java.util.List;
//import java.util.Map;

import core.DTNHost;
import core.Message;
import core.MessageListener;
import core.Settings;
import core.SimClock;
import core.UpdateListener;

public class RatarataBufferTiapNode extends Report implements UpdateListener {

    /**
     * Record occupancy every nth second -setting id ({@value}). Defines the
     * interval how often (seconds) a new snapshot of buffer occupancy is taken
     * previous:5
     */
    public static final String BUFFER_REPORT_INTERVAL = "occupancyInterval";
    /**
     * Default value for the snapshot interval
     */
    public static final int DEFAULT_BUFFER_REPORT_INTERVAL = 3600;

    private double lastRecord = Double.MIN_VALUE;
    private int interval;

    private Map<DTNHost, LinkedList<Double>> bufferCounts = new HashMap<DTNHost, LinkedList<Double>>();
    private Map<DTNHost, Double> mapofBuffer = new HashMap<DTNHost, Double>();
    private int updateCounter = 0;  //new added

    public RatarataBufferTiapNode() {
        super();

        Settings settings = getSettings();
        if (settings.contains(BUFFER_REPORT_INTERVAL)) {
            interval = settings.getInt(BUFFER_REPORT_INTERVAL);
        } else {
            interval = -1;
            /* not found; use default */
        }

        if (interval < 0) {
            /* not found or invalid value -> use default */
            interval = DEFAULT_BUFFER_REPORT_INTERVAL;
        }
    }

    public void updated(List<DTNHost> hosts) {
        if (isWarmup()) {
            return;
        }

        if (SimClock.getTime() - lastRecord >= interval) {
            lastRecord = SimClock.getTime();
            printLine(hosts);
            updateCounter++; // new added
        }
        /**
         * for (DTNHost ho : hosts ) { double temp = ho.getBufferOccupancy();
         * temp = (temp<=100.0)?(temp):(100.0); if
         * (bufferCounts.containsKey(ho.getAddress()))
         * bufferCounts.put(ho.getAddress(),
         * (bufferCounts.get(ho.getAddress()+temp))/2); else
         * bufferCounts.put(ho.getAddress(), temp); } }
         */
//        for (DTNHost host : hosts) {
//            double temp = host.getBufferOccupancy();
//            temp = (temp<=100.0)?(temp):(100.0);
//            if(bufferCounts.containsKey(host.getAddress())){
//                bufferCounts.put(host, (bufferCounts.get(host.getAddress()+temp)/2));
//            } else {
//                bufferCounts.put(host, temp);
//            }
//        }
    }

    /**
     * Prints a snapshot of the average buffer occupancy
     *
     * @param hosts The list of hosts in the simulation
     */
    private void printLine(List<DTNHost> hosts) {
        for (DTNHost h : hosts) {
            double tmp = h.getBufferOccupancy();
            tmp = (tmp <= 100.0) ? (tmp) : (100.0);

            //example 1
            LinkedList<Double> sampleBuffer;
            if (!bufferCounts.containsKey(h)) {
                sampleBuffer = new LinkedList<>();
            } else {
                sampleBuffer = bufferCounts.get(h);
            }
            sampleBuffer.add(tmp);
            bufferCounts.put(h, sampleBuffer);

            //example 2 - perhitungan rata2 di method done menggunakan perbandingan dari updateCounter
            //mapofBuffer.put(h, !mapofBuffer.containsKey(h) ? tmp : mapofBuffer.get(h)+tmp);
        }

//		for (DTNHost h : hosts ) {
//			double temp = h.getBufferOccupancy();
//			temp = (temp<=100.0)?(temp):(100.0);
//			if (bufferCounts.containsKey(h)){
//				//bufferCounts.put(h, (bufferCounts.get(h)+temp)/2); seems WRONG
//				
//				bufferCounts.put(h, bufferCounts.get(h)+temp);
//				//write (""+ bufferCounts.get(h));
//			}
//			else {
//			bufferCounts.put(h, temp);
//			//write (""+ bufferCounts.get(h));
//			}
//		}
    }

    @Override
    public void done() {
//        write("Node\tAverageBufferOccupancy\n");
        //write - example1
//        for (Map.Entry<DTNHost, LinkedList<Double>> entry : bufferCounts.entrySet()) {
//            DTNHost a = entry.getKey();
//            String b = getAverage(entry.getValue());
//            write(a + " " + b + ' ');
//        }

        // - example2
//        for (Map.Entry<DTNHost, Double> entry : mapofBuffer.entrySet()){
//            DTNHost key = entry.getKey();
//            Double value = entry.getValue()/updateCounter;
//            write(key + "\t" + value);
//        }
        //write - per interval
        for (Map.Entry<DTNHost, LinkedList<Double>> entry : bufferCounts.entrySet()) {
            DTNHost a = entry.getKey();
            LinkedList<Double> b = entry.getValue();
            String hasil = " " + a + ", ";
            for (int i = 0; i < b.size(); i++) {
                hasil += b.get(i).toString() + "\t";
            }
            write(hasil);
        }
        super.done();
    }
}
