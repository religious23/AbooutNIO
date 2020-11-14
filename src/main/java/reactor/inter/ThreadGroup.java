package reactor.inter;

/**
 * @author 王文
 * @date 2020/11/05
 * @motto 恢弘志士之气，不宜妄自菲薄
 */
public interface ThreadGroup {

    /**
     * 初始化线程组
     *
     * @param threadNumbers 线程数量
     * @throws Exception
     */
    void initThreads(int threadNumbers) throws Exception;

    /**
     * 启动所有线程
     */
    void startThreads();

    /**
     * 获取均分的下一个事件线程对象
     *
     * @return
     */
    EventThread allocateEventThread();

}
