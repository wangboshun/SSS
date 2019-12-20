using System;
using System.Collections.Generic;
using System.IO;
using System.Text;

namespace SSS.Infrastructure.Util.IO
{
    public static class IO
    {
        private static readonly List<object> Obj = new List<object>(10);

        private static List<object> TenObj
        {
            get
            {
                if (Obj.Count == 0)
                    for (int i = 0; i < 10; i++)
                        Obj.Add(new object());
                return Obj;
            }
        }

        private static object GetLockObj(int length)
        {
            int i = length % 9;
            return TenObj[i];
        }

        /// <summary>
        ///     先自动识别UTF8，否则归到Default编码读取
        /// </summary>
        public static string ReadAllText(string fileName)
        {
            return ReadAllText(fileName, Encoding.Default);
        }

        /// <summary>
        ///     读取所有
        /// </summary>
        /// <param name="fileName">文件名和路径</param>
        /// <param name="encoding">编码格式</param>
        /// <returns></returns>
        public static string ReadAllText(string fileName, Encoding encoding)
        {
            try
            {
                if (!File.Exists(fileName)) return string.Empty;
                byte[] buff = null;
                lock (GetLockObj(fileName.Length))
                {
                    if (!File.Exists(fileName)) //多线程情况处理
                        return string.Empty;
                    buff = File.ReadAllBytes(fileName);
                }

                if (buff == null)
                    return string.Empty;

                if (buff[0] == 239 && buff[1] == 187 && buff[2] == 191)
                    return Encoding.UTF8.GetString(buff, 3, buff.Length - 3);

                if (buff[0] == 255 && buff[1] == 254)
                {
                    return Encoding.Unicode.GetString(buff, 2, buff.Length - 2);
                }

                if (buff[0] == 254 && buff[1] == 255)
                {
                    if (buff.Length > 3 && buff[2] == 0 && buff[3] == 0)
                        return Encoding.UTF32.GetString(buff, 4, buff.Length - 4);
                    return Encoding.BigEndianUnicode.GetString(buff, 2, buff.Length - 2);
                }

                return encoding.GetString(buff);
            }
            catch (Exception)
            {
                throw new ApplicationException();
            } 
        }

        /// <summary>
        ///     文件写操作
        /// </summary>
        /// <param name="fileName">文件名称和路径</param>
        /// <param name="text">内容</param>
        public static bool Write(string fileName, string text)
        {
            return Save(fileName, text, false, Encoding.Default);
        }

        /// <summary>
        ///     文件写操作
        /// </summary>
        /// <param name="fileName">文件名称和路径</param>
        /// <param name="text">内容</param>
        /// <param name="encode">编码格式</param>
        public static bool Write(string fileName, string text, Encoding encode)
        {
            return Save(fileName, text, false, encode);
        }

        /// <summary>
        ///     文件写操作---追加
        /// </summary>
        /// <param name="fileName">文件名称和路径</param>
        /// <param name="text">内容</param>
        public static bool Append(string fileName, string text)
        {
            return Save(fileName, text, true);
        }

        /// <summary>
        ///     文件保存操作
        /// </summary>
        /// <param name="fileName">文件名称和路径</param>
        /// <param name="text">内容</param>
        /// <param name="isAppend">是否追加</param>
        /// <returns></returns>
        public static bool Save(string fileName, string text, bool isAppend = false)
        {
            return Save(fileName, text, isAppend, Encoding.Default);
        }

        /// <summary>
        ///     文件保存操作
        /// </summary>
        /// <param name="fileName">文件名称和路径</param>
        /// <param name="text">内容</param>
        /// <param name="isAppend">是否追加</param>
        /// <param name="encode">编码格式</param>
        /// <returns></returns>
        public static bool Save(string fileName, string text, bool isAppend, Encoding encode)
        {
            try
            {
                string folder = Path.GetDirectoryName(fileName);
                if (!Directory.Exists(folder))
                    Directory.CreateDirectory(folder);

                lock (GetLockObj(fileName.Length))
                {
                    using StreamWriter writer = new StreamWriter(fileName, isAppend, encode);
                    writer.Write(text);
                    writer.Close();
                }

                return true;
            }
            catch (Exception)
            {
                throw new ApplicationException();
            } 
        }

        /// <summary>
        ///     删除文件
        /// </summary>
        /// <param name="fileName">文件名和路径</param>
        public static bool Delete(string fileName)
        {
            try
            {
                if (File.Exists(fileName))
                    lock (GetLockObj(fileName.Length))
                    {
                        if (File.Exists(fileName))
                        {
                            File.Delete(fileName);
                            return true;
                        }
                    }
            }
            catch (Exception)
            {
                throw new ApplicationException();
            }

            return false;
        }
    }
}