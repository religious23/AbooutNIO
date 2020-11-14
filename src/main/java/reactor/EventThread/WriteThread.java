package reactor.EventThread;

import reactor.EventThreadGroup.ReadThreadGroup;
import reactor.EventThreadGroup.WriteThreadGroup;
import reactor.Log.Logger;
import reactor.inter.EventThread;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * @author 王文
 * @date 2020/11/05
 * @motto 恢弘志士之气，不宜妄自菲薄
 */
public class WriteThread extends Thread implements EventThread {

    private final Logger logger = new Logger();

    private WriteThreadGroup writeThreadGroup;

    private LinkedList<SelectionKey> taskList = new LinkedList<SelectionKey>();

    // 每个线程有一个属于自己的 selector
    private Selector selector;

    public WriteThread() throws Exception {
        selector = Selector.open();
    }

    public LinkedList<SelectionKey> getTaskList() {
        return taskList;
    }

    public void setTaskList(LinkedList<SelectionKey> taskList) {
        this.taskList = taskList;
    }

    public Selector getSelector() {
        return selector;
    }

    public void setSelector(Selector selector) {
        this.selector = selector;
    }

    public WriteThreadGroup getWriteThreadGroup() {
        return writeThreadGroup;
    }

    public void setWriteThreadGroup(WriteThreadGroup writeThreadGroup) {
        this.writeThreadGroup = writeThreadGroup;
    }

    @Override
    public void run() {
        while (true) {
            try {
                logger.log(this.getName() + " ready to select......");

                // 这个select 是阻塞的
                int readyKeys = selector.select();

                if (readyKeys > 0) {
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();

                        iterator.remove();

                        if (key.isWritable()) {
                            writeHandler(key);
                            // 取消订购写事件
                            key.interestOps(key.interestOps() & ~SelectionKey.OP_WRITE);
                        }
                    }
                }


                // run all task
                if (!taskList.isEmpty()) {
                    SelectionKey key = taskList.poll();
                    SocketChannel clientChannel = (SocketChannel) key.channel();
                    clientChannel.register(selector, SelectionKey.OP_WRITE, key.attachment());
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 回写
     *
     * @param key
     */
    private void writeHandler(SelectionKey key) throws Exception {
        System.out.println(this.getName() + " writeHandler.......");
        // 服务器写回消息:得到事件发生的Socket通道
        SocketChannel channel = (SocketChannel) key.channel();
        String msg = (String) key.attachment();

        // 给客户端写回消息
        String baskStr = new String(msg + "\n");
        ByteBuffer outBuffer = ByteBuffer.wrap(baskStr.getBytes("utf-8"));
        channel.write(outBuffer);
    }

}
