package report;

import core.*;

import java.util.*;

public class CopyPesan extends Report implements MessageListener, UpdateListener {

    public static final String COPY_REPORT_INTERVAL = "copyInterval";
    public static final int DEFAULT_COPY_REPORT_INTERVAL = 800;
    private Map<Integer, Integer> jmlCopyPesan = new HashMap<Integer, Integer>();
    private double lastRecord = Double.MIN_VALUE;
    private int interval;
    private int nrofRelayed;
    private int updateCounter = 0;  //new added

    public CopyPesan() {
        super();
        
        this.nrofRelayed = 0;
        this.interval = 0;
        Settings settings = getSettings();
        if (settings.contains(COPY_REPORT_INTERVAL)) {
            interval = settings.getInt(COPY_REPORT_INTERVAL);
        } else {
            interval = -1;
            /* not found; use default */
        }

        if (interval < 0) {
            /* not found or invalid value -> use default */
            interval = DEFAULT_COPY_REPORT_INTERVAL;
        }
    }

    @Override
    public void newMessage(Message m) {
       
    }

    @Override
    public void messageTransferStarted(Message m, DTNHost from, DTNHost to) {
      
    }

    @Override
    public void messageDeleted(Message m, DTNHost where, boolean dropped) {
       
    }

    @Override
    public void messageTransferAborted(Message m, DTNHost from, DTNHost to) {
        
    }

    @Override
    public void messageTransferred(Message m, DTNHost from, DTNHost to, boolean firstDelivery) {
        this.nrofRelayed++;
    }

    @Override
    public void updated(List<DTNHost> hosts) {
        if (isWarmup()) {
            return;
        }

        if (SimClock.getTime() - lastRecord >= interval) {
            lastRecord = SimClock.getTime();
            printLine();
            updateCounter++; // new added
        }
    }
    
    private void printLine(){
        this.jmlCopyPesan.put(updateCounter++, nrofRelayed);
    }
    
    @Override
    public void done(){
        for (Map.Entry<Integer, Integer> entry : jmlCopyPesan.entrySet()){
            Integer a = entry.getKey();
            Integer b = entry.getValue();
            write(a + " " + b + " ");
        }
        
        super.done();
    }
}
