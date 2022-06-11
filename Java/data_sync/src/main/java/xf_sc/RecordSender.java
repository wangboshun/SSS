package xf_sc;

/**
 * @author WBS
 * Date:2022/6/11
 */

public interface RecordSender {

    public Object createRecord();

    public void sendToWriter(Object record);

}
