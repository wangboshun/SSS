
using System.IO.Compression;

namespace Common.Utils
{

    /// <summary>
    /// 文本解压缩帮助类
    /// </summary>
    public static class TextUtil
    {

        /// <summary>
        /// 压缩文本
        /// </summary>
        /// <param name="text">需要压缩的字符串</param>
        /// <returns></returns>
        public static string CompressText(string text)
        {
            if (string.IsNullOrEmpty(text))
            {
                return text;
            }

            using MemoryStream memoryStream = new MemoryStream();
            using GZipStream gZipStream = new GZipStream(memoryStream, CompressionMode.Compress, leaveOpen: true);
            using StreamWriter streamWriter = new StreamWriter(gZipStream);
            streamWriter.Write(text);
            streamWriter.Flush();
            gZipStream.Flush();
            return Convert.ToBase64String(memoryStream.ToArray());
        }


        /// <summary>
        /// 解压文本
        /// </summary>
        /// <param name="comText">需要解压的字符串</param>
        /// <returns></returns>
        public static string DepressText(string comText)
        {
            if (string.IsNullOrEmpty(comText))
            {
                return comText;
            }

            using MemoryStream stream = new MemoryStream(Convert.FromBase64String(comText));
            using GZipStream stream2 = new GZipStream(stream, CompressionMode.Decompress, leaveOpen: true);
            using StreamReader streamReader = new StreamReader(stream2);
            return streamReader.ReadToEnd();
        }
    }
}
