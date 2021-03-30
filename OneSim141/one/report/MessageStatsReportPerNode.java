/* 
 * Copyright 2010 Aalto University, ComNet
 * Released under GPLv3. See LICENSE.txt for details. 
 */
package report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import core.DTNHost;
import core.Message;
import core.MessageListener;

/**
 * Report for generating different kind of total statistics about message
 * relaying performance. Messages that were created during the warm up period
 * are ignored.
 * <P>
 * <strong>Note:</strong> if some statistics could not be created (e.g. overhead
 * ratio if no messages were delivered) "NaN" is reported for double values and
 * zero for integer median(s).
 */
public class MessageStatsReportPerNode extends Report implements MessageListener {

    private HashMap<DTNHost, Integer> nrofDroppedNode;

    /**
     * Constructor.
     */
    public MessageStatsReportPerNode() {
        init();
    }

    @Override
    protected void init() {
        super.init();
        this.nrofDroppedNode = new HashMap<DTNHost, Integer>();
    }

    public void messageDeleted(Message m, DTNHost where, boolean dropped) {
        if (dropped) {
            if (nrofDroppedNode.containsKey(where)) {
                this.nrofDroppedNode.put(where, nrofDroppedNode.get(where) + 1);
            } else {
                this.nrofDroppedNode.put(where, 1);
            }
        }

    }

    public void messageTransferAborted(Message m, DTNHost from, DTNHost to) {}

    public void messageTransferred(Message m, DTNHost from, DTNHost to,
            boolean finalTarget) {}

    public void newMessage(Message m) {}

    public void messageTransferStarted(Message m, DTNHost from, DTNHost to) {}

    @Override
    public void done() {
        String print = "Host\tTotal Drop\n";
        for (Map.Entry<DTNHost, Integer> entry : nrofDroppedNode.entrySet()) {
            DTNHost key = entry.getKey();
            Integer value = entry.getValue();
            print += key + "\t" + value + "\n";
        }
        write(print);
        super.done();
    }
}
