package hello.f2boy.mydubbo.io.nio1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Random;

public class Nio1Client {

    public static void main(String[] args) throws IOException, InterruptedException {
        boolean blockMode = new Random().nextBoolean();

        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 1234));
        socketChannel.configureBlocking(blockMode);
        ByteBuffer buffer = ByteBuffer.allocate(256);

        buffer.put("I love you\n".getBytes());
        buffer.put("Do you love me?\n".getBytes());
        buffer.flip();
        while (buffer.hasRemaining()) {
            System.out.println(buffer);
            socketChannel.write(buffer);
        }

        buffer.clear();
        buffer.put("bye\n".getBytes());
        buffer.flip();
        while (buffer.hasRemaining()) {
            System.out.println(buffer);
            socketChannel.write(buffer);
        }

        Thread.sleep(3000);

        socketChannel.close();
    }

}
