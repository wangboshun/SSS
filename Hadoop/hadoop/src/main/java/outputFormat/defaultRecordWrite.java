package outputFormat;

import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

import java.io.IOException;

public class defaultRecordWrite extends RecordWriter<Text, NullWritable> {
    private FSDataOutputStream baiduFs;
    private FSDataOutputStream outherFs;

    public defaultRecordWrite(TaskAttemptContext job) {
        try {
            FileSystem fileSystem = FileSystem.get(job.getConfiguration());
            baiduFs = fileSystem.create(new Path("/baidu_input"));
            outherFs = fileSystem.create(new Path("/other_input"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void write(Text text, NullWritable nullWritable) throws IOException, InterruptedException {
        String s = text.toString();
        if (s.contains("baidu")) {
            baiduFs.writeBytes(s + "\n");
        } else {
            outherFs.writeBytes(s + "\n");
        }
    }

    @Override
    public void close(TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
        IOUtils.closeStream(baiduFs);
        IOUtils.closeStream(outherFs);
    }
}
