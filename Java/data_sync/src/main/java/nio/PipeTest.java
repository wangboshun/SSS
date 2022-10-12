package nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;

public class PipeTest {
    public static void main(String[] args) throws IOException {
        Pipe pipe = Pipe.open();

        Pipe.SinkChannel sink = pipe.sink();
        Pipe.SourceChannel source = pipe.source();

        ByteBuffer write_buffer = ByteBuffer.allocate(1024);
        write_buffer.put(("hello world!").getBytes());
        write_buffer.flip();
        while (write_buffer.hasRemaining()) {
            sink.write(write_buffer);
        }

        ByteBuffer read_buffer = ByteBuffer.allocate(1024);
        int len = source.read(read_buffer);

        System.out.println(new String(read_buffer.array(), 0, len));
        sink.close();
        source.close();
    }

}
