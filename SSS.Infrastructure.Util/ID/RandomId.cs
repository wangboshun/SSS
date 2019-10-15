using System;
using System.Text;

namespace SSS.Infrastructure.Util.ID
{
    public class RandomId
    {
        private static readonly object syncRoot = new object(); //加锁对象
        private static RandomId randomid;

        public static RandomId Instance()
        {
            if (randomid == null)
                randomid = new RandomId();
            return randomid;
        }

        public string GetId(int len = 8)
        {
            lock (syncRoot)
            {
                char[] constant =
                {
                    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                    'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
                    'U', 'V', 'W', 'X', 'Y', 'Z'
                };

                StringBuilder str = new StringBuilder();
                Random rd = new Random();
                for (int i = 0; i < len; i++)
                    str.Append(constant[rd.Next(constant.Length)]);

                return str.ToString();
            }
        }
    }
}