package nio;

import java.nio.ByteBuffer;

public class BufferTest {

    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        int position = buffer.position();
        int limit = buffer.limit();
        int capacity = buffer.capacity();

        System.out.println("position: " + position);
        System.out.println("limit: " + limit);
        System.out.println("capacity: " + capacity);

        System.out.println("------put data------");

        String str = "hello";
        buffer.put(str.getBytes());

        position = buffer.position();
        limit = buffer.limit();
        capacity = buffer.capacity();

        System.out.println("position: " + position);
        System.out.println("limit: " + limit);
        System.out.println("capacity: " + capacity);

        System.out.println("------flip data------");
        buffer.flip();

        position = buffer.position();
        limit = buffer.limit();
        capacity = buffer.capacity();

        System.out.println("position: " + position);
        System.out.println("limit: " + limit);
        System.out.println("capacity: " + capacity);

        System.out.println("------get data------");

        byte[] bytes = new byte[buffer.limit()];
        buffer.get(bytes);
        System.out.println("读取到的数据：" + new String(bytes, 0, bytes.length));

        position = buffer.position();
        limit = buffer.limit();
        capacity = buffer.capacity();

        System.out.println("position: " + position);
        System.out.println("limit: " + limit);
        System.out.println("capacity: " + capacity);

        System.out.println("------rewind data------");
        buffer.rewind();

        position = buffer.position();
        limit = buffer.limit();
        capacity = buffer.capacity();

        System.out.println("position: " + position);
        System.out.println("limit: " + limit);
        System.out.println("capacity: " + capacity);


        System.out.println("------mark data------");
        bytes = new byte[3];
        buffer.get(bytes);
        System.out.println("读取到的数据：" + new String(bytes, 0, 3));

        position = buffer.position();
        limit = buffer.limit();
        capacity = buffer.capacity();

        System.out.println("position: " + position);
        System.out.println("limit: " + limit);
        System.out.println("capacity: " + capacity);
        buffer.mark();

        buffer.reset();
        bytes = new byte[2];
        buffer.get(bytes);
        System.out.println("读取到的数据：" + new String(bytes, 0, 2));

        position = buffer.position();
        limit = buffer.limit();
        capacity = buffer.capacity();

        System.out.println("position: " + position);
        System.out.println("limit: " + limit);
        System.out.println("capacity: " + capacity);

        if (buffer.hasRemaining()) {
            System.out.println("还有剩余的数据！");
        } else {
            System.out.println("没有剩余的数据！");
        }


        System.out.println("------clean data------");
        buffer.clear();

        position = buffer.position();
        limit = buffer.limit();
        capacity = buffer.capacity();

        System.out.println("position: " + position);
        System.out.println("limit: " + limit);
        System.out.println("capacity: " + capacity);
    }
}
