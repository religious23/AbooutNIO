package reactor.EventThreadGroup;

import reactor.EventThread.ReadThread;
import reactor.inter.EventThread;
import reactor.inter.ThreadGroup;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 王文
 * @date 2020/11/05
 * @motto 恢弘志士之气，不宜妄自菲薄
 */
public class ReadThreadGroup implements ThreadGroup {
    private String threadName = "Reader-Thread-";

    public ArrayList<ReadThread> threads = new ArrayList<ReadThread>();

    private WriteThreadGroup writeThreadGroup;

    // 当前线程组的分配自增序列
    AtomicInteger atomicInteger = new AtomicInteger();

    public WriteThreadGroup getWriteThreadGroup() {
        return writeThreadGroup;
    }

    public void setWriteThreadGroup(WriteThreadGroup writeThreadGroup) {
        this.writeThreadGroup = writeThreadGroup;
    }

    /**
     * 初始化线程组
     * 创建线程 并再创建的过程中 初始化每个对应的 selector
     *
     * @param threadNumbers 线程数量
     */
    @Override
    public void initThreads(int threadNumbers) throws Exception {
        for (int i = 0; i < threadNumbers; i++) {
            ReadThread readerThread = new ReadThread();
            readerThread.setName(threadName + i);
            readerThread.setReadThreadGroup(this);

            threads.add(readerThread);
        }
    }

    /**
     * 启动所有创建好的线程
     */
    @Override
    public void startThreads() {
        for (ReadThread thread : threads) {
            thread.start();
        }
    }

    /**
     * 获取均分的下一个事件线程对象
     *
     * @return
     */
    @Override
    public EventThread allocateEventThread() {
        int xId = atomicInteger.getAndIncrement();

        int i = xId % threads.size();

        return threads.get(i);
    }

}
