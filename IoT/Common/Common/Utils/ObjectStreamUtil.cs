using Newtonsoft.Json;

namespace Common.Utils
{
    public class ObjectStreamUtil
    {
        // 每批的大小
        private readonly static int batchSize = 200;

        /// <summary>
        /// 批次序列化
        /// </summary>
        /// <param name="obj"></param>
        /// <param name="filePath"></param>
        public static void SerializeObject(List<List<string>> grid, string directoryPath)
        {
            int count = (int)Math.Ceiling((double)grid.Count / batchSize);

            //串行计算
            /* for (int i = 0; i < count; i++)
             {
                 int startIndex = i * batchSize;
                 int endIndex = Math.Min(startIndex + batchSize, grid.Count);
                 File.WriteAllText(directoryPath + @"\" + i + ".json", TextUtil.CompressText(JsonConvert.SerializeObject(grid.GetRange(startIndex, endIndex - startIndex))));
             }*/

            //使用Parallel.For进行并行计算
            Parallel.For(0, count, i =>
            {
                int startIndex = i * batchSize;
                int endIndex = Math.Min(startIndex + batchSize, grid.Count);
                File.WriteAllText(directoryPath + @"\" + i + ".json", TextUtil.CompressText(JsonConvert.SerializeObject(grid.GetRange(startIndex, endIndex - startIndex))));
            });
        }


        /// <summary>
        /// 批次反序列化
        /// </summary>
        /// <param name="directoryPath"></param>
        /// <returns></returns>
        public static List<List<string>> DeserializeObject(string directoryPath)
        {
            List<List<string>> grid = new List<List<string>>();
            if (Directory.Exists(directoryPath))
            {
                string[] filePaths = Directory.GetFiles(directoryPath);

                //串行计算
                /*foreach (string filePath in filePaths)
                {
                    grid.AddRange(JsonConvert.DeserializeObject<List<List<decimal[]>>>(TextUtil.DepressText(File.ReadAllText(filePath))));
                }*/

                //使用Parallel.For进行并行计算
                Parallel.ForEach(filePaths, filePath =>
                {
                    grid.AddRange(JsonConvert.DeserializeObject<List<List<string>>>(TextUtil.DepressText(File.ReadAllText(filePath))));
                });
            }
            return grid;
        }




        /******************************************测试*********************************************/
        /// <summary>
        /// 批次序列化
        /// </summary>
        /// <param name="obj"></param>
        /// <param name="filePath"></param>
        public static void SerializeObject2(List<List<string>> grid, string directoryPath)
        {
            int count = (int)Math.Ceiling((double)grid.Count / batchSize);

            //串行计算
            /* for (int i = 0; i < count; i++)
             {
                 int startIndex = i * batchSize;
                 int endIndex = Math.Min(startIndex + batchSize, grid.Count);
                 File.WriteAllText(directoryPath + @"\" + i + ".json", TextUtil.CompressText(JsonConvert.SerializeObject(grid.GetRange(startIndex, endIndex - startIndex))));
             }*/

            //使用Parallel.For进行并行计算
            Parallel.For(0, count, i =>
            {
                int startIndex = i * batchSize;
                int endIndex = Math.Min(startIndex + batchSize, grid.Count);
                File.WriteAllText(directoryPath + @"\" + i + ".json", TextUtil.CompressText(JsonConvert.SerializeObject(grid.GetRange(startIndex, endIndex - startIndex))));
            });
        }


        /// <summary>
        /// 批次反序列化
        /// </summary>
        /// <param name="directoryPath"></param>
        /// <returns></returns>
        public static List<List<string>> DeserializeObject2(string directoryPath)
        {
            List<List<string>> grid = new List<List<string>>();
            if (Directory.Exists(directoryPath))
            {
                string[] filePaths = Directory.GetFiles(directoryPath);

                //串行计算
                /*foreach (string filePath in filePaths)
                {
                    grid.AddRange(JsonConvert.DeserializeObject<List<List<decimal[]>>>(TextUtil.DepressText(File.ReadAllText(filePath))));
                }*/

                //使用Parallel.For进行并行计算
                Parallel.ForEach(filePaths, filePath =>
                {
                    grid.AddRange(JsonConvert.DeserializeObject<List<List<string>>>(TextUtil.DepressText(File.ReadAllText(filePath))));
                });
            }
            return grid;
        }

    }
}
