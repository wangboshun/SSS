package xf_sc;

import java.util.Collection;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author WBS
 * Date:2022/6/11
 */

public class Channel {
    private final ReentrantLock lock;
    private final Condition notInsufficient;
    private final Condition notEmpty;
    private int bufferSize = 0;
    private ArrayBlockingQueue<Object> queue = null;

    public Channel() {
        this.queue = new ArrayBlockingQueue<Object>(100);
        this.bufferSize = 32;
        lock = new ReentrantLock();
        notInsufficient = lock.newCondition();
        notEmpty = lock.newCondition();
    }

    public void close() {

    }

    public void clear() {
        this.queue.clear();
    }

    protected void doPushAll(Collection<Object> rs) {
        try {
            lock.lockInterruptibly();
            while (rs.size() > this.queue.remainingCapacity()) {
                notInsufficient.await(200L, TimeUnit.MILLISECONDS);
            }
            this.queue.addAll(rs);
            notEmpty.signalAll();
        } catch (InterruptedException e) {
        } finally {
            lock.unlock();
        }
    }

    protected void doPullAll(Collection<Object> rs) {
        assert rs != null;
        rs.clear();
        try {
            lock.lockInterruptibly();
            while (this.queue.drainTo(rs, bufferSize) <= 0) {
                notEmpty.await(200L, TimeUnit.MILLISECONDS);
            }
            notInsufficient.signalAll();
        } catch (InterruptedException e) {

        } finally {
            lock.unlock();
        }
    }

    public int size() {
        return this.queue.size();
    }


    public boolean isEmpty() {
        return this.queue.isEmpty();
    }
}
