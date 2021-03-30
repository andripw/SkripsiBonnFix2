package report;

import core.*;

import java.util.*;

public class SuccessRatioTiapSourceNode extends Report implements MessageListener {

    private Map<DTNHost, Integer> jmlPerSourceNode;
    private Map<DTNHost, Integer> creation;

    public SuccessRatioTiapSourceNode() {
        init();
    }

    @Override
    protected void init() {
        super.init();
        this.creation = new HashMap<DTNHost, Integer>();
        this.jmlPerSourceNode = new HashMap<DTNHost, Integer>();
    }

    @Override
    public void newMessage(Message m) {
        if (isWarmup()) {
            addWarmupID(m.getId());
            return;
        }

        if (creation.containsKey(m.getFrom())) {
            creation.put(m.getFrom(), creation.get(m.getFrom()) + 1);
        } else {
            creation.put(m.getFrom(), 1);
        }
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
        if (isWarmup()) {
            addWarmupID(m.getId());
            return;
        }

        if (firstDelivery) {
            if (jmlPerSourceNode.containsKey(m.getFrom())) {
                jmlPerSourceNode.put(m.getFrom(), jmlPerSourceNode.get(m.getFrom()) + 1);
            } else {
                jmlPerSourceNode.put(m.getFrom(), 1);
            }
        }
    }

    public void done() {
        for (Map.Entry<DTNHost, Integer> entry : creation.entrySet()) {
            DTNHost h = entry.getKey();
            int c = entry.getValue();
            int s;

            if (jmlPerSourceNode.containsKey(h)) {
                s = jmlPerSourceNode.get(h);
            } else {
                s = 0;
            }

            write(h.toString() + "\t Pesan terkirim " + s + '/' + c);

        }
        super.done();
    }
}
