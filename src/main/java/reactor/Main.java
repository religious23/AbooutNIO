package reactor;

/**
 * @author 王文
 * @date 2020/11/05
 * @motto 恢弘志士之气，不宜妄自菲薄
 */
public class Main {
    public static void main(String[] args) throws Exception {
        BootStrap bootStrap = new BootStrap();
        bootStrap.bindPort(815).bindThreadPoolSize(3).start().sync();
    }
}
