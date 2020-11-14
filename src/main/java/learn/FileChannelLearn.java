package learn;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;

/**
 * @author 王文
 * @date 2020/11/05
 * @motto 恢弘志士之气，不宜妄自菲薄
 */
public class FileChannelLearn {
    public static void main(String[] args) throws Exception {
        RandomAccessFile file = new RandomAccessFile("nio-data.txt", "rw");
        FileChannel channel = file.getChannel();

        ByteBuffer buffer = ByteBuffer.allocateDirect(50);
        int read = channel.read(buffer);

        // 切换读模式 读出往数组里写
        buffer.flip();
        byte[] bytes = new byte[buffer.limit()];
        buffer.get(bytes);
        System.out.println(Arrays.toString(bytes));

        channel.close();
        file.close();
    }
}
