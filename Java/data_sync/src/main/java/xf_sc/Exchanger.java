package xf_sc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;

/**
 * @author WBS
 * Date:2022/6/11
 */

public class Exchanger implements RecordSender, RecordReceiver {

    private final Channel channel;
    private final List<Object> buffer;
    private int bufferIndex = 0;

    private int bufferSize;

    private Condition notEmpty;

    public Exchanger(final Channel channel) {
        this.channel = channel;
        this.bufferSize = 32;
        this.buffer = new ArrayList<Object>(bufferSize);
    }


    @Override
    public Object getFromReader() {
        boolean isEmpty = (this.bufferIndex >= this.buffer.size());
        if (isEmpty) {
            receive();
        }
        Object o = this.buffer.get(this.bufferIndex++);
        return o;
    }

    private void receive() {
        this.channel.doPullAll(this.buffer);
        this.bufferIndex = 0;
        this.bufferSize = this.buffer.size();
    }

    @Override
    public Object createRecord() {
        return null;
    }

    @Override
    public void sendToWriter(Object o) {
        this.buffer.add(o);
        this.bufferIndex++;
        if (this.bufferIndex >=this.bufferSize) {
            flush();
        }
    }

    public void flush() {
        this.channel.doPushAll(this.buffer);
        this.buffer.clear();
        this.bufferIndex = 0;
    }
}
