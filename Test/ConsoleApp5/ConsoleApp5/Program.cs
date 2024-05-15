using System.Text;

using Newtonsoft.Json;

namespace ConsoleApp5
{
    internal class Program
    {
        static void Main(string[] args)
        {
            Console.WriteLine("Hello, World!");

            var files = Directory.GetFiles(@"C:\\Users\\WBS\\Desktop\\log");
            files = files.Order().ToArray();
            //"74AD9CA0543DA3C0"
            List<string> list = new List<string>();

            StringBuilder sb = new StringBuilder();
            Dictionary<DateTime, string> dict = new Dictionary<DateTime, string>();
            foreach (var f in files)
            {
                var lines = File.ReadAllLines(f);
                foreach (var item in lines)
                {
                    if (item.Contains("74AD9CA0543DA3C0"))
                    {
                        //list.Add(item);
                        var r = JsonConvert.DeserializeObject<Root>(item);
                        var t =Convert.ToDateTime(r.tag.tag_info.time);
                        if (!dict.ContainsKey(t))
                        {
                            dict.Add(t, item);
                        } 
                    }
                }
            }

            var dic1Asc = dict.OrderBy(o => o.Key).ToDictionary(o => o.Key, p => p.Value);
            foreach (var item in dic1Asc)
            {
                list.Add(item.Value);
                sb.Append(item.Value);
                sb.Append("\r\n");
            }

            var s = sb.ToString();

        }
    }


    public class Anc_info
    {
        /// <summary>
        /// 
        /// </summary>
        public string ancID { get; set; }

        /// <summary>
        /// 
        /// </summary>
        public string dimen { get; set; }

        /// <summary>
        /// 
        /// </summary>
        public string type { get; set; }

        /// <summary>
        /// 
        /// </summary>
        public string x { get; set; }

        /// <summary>
        /// 
        /// </summary>
        public string y { get; set; }

        /// <summary>
        /// 
        /// </summary>
        public string z { get; set; }

    }



    public class Cmd
    {
        /// <summary>
        /// 
        /// </summary>
        public string cmd { get; set; }

        /// <summary>
        /// 
        /// </summary>
        public string cmd_type { get; set; }

    }



    public class Acc
    {
        /// <summary>
        /// 
        /// </summary>
        public string tag_acc_x { get; set; }

        /// <summary>
        /// 
        /// </summary>
        public string tag_acc_y { get; set; }

        /// <summary>
        /// 
        /// </summary>
        public string tag_acc_z { get; set; }

    }



    public class Coord
    {
        /// <summary>
        /// 
        /// </summary>
        public string x { get; set; }

        /// <summary>
        /// 
        /// </summary>
        public string y { get; set; }

        /// <summary>
        /// 
        /// </summary>
        public string z { get; set; }

    }



    public class Range
    {
        /// <summary>
        /// 
        /// </summary>
        public string alarm_dist { get; set; }

        /// <summary>
        /// 
        /// </summary>
        public string dist { get; set; }

        /// <summary>
        /// 
        /// </summary>
        public string warning_dist { get; set; }

    }



    public class Tag_info
    {
        /// <summary>
        /// 
        /// </summary>
        public Acc acc { get; set; }

        /// <summary>
        /// 
        /// </summary>
        public string battery_val { get; set; }

        /// <summary>
        /// 
        /// </summary>
        public string charge_state { get; set; }

        /// <summary>
        /// 
        /// </summary>
        public Coord coord { get; set; }

        /// <summary>
        /// 
        /// </summary>
        public string heartrate { get; set; }

        /// <summary>
        /// 
        /// </summary>
        public string is_alarm { get; set; }

        /// <summary>
        /// 
        /// </summary>
        public string is_lowbattery { get; set; }

        /// <summary>
        /// 
        /// </summary>
        public Range range { get; set; }

        /// <summary>
        /// 
        /// </summary>
        public string seq { get; set; }

        /// <summary>
        /// 
        /// </summary>
        public string tagID { get; set; }

        /// <summary>
        /// 
        /// </summary>
        public string time { get; set; }

        /// <summary>
        /// 
        /// </summary>
        public string transport_data { get; set; }

    }



    public class Tag
    {
        /// <summary>
        /// 
        /// </summary>
        public Anc_info anc_info { get; set; }

        /// <summary>
        /// 
        /// </summary>
        public Cmd cmd { get; set; }

        /// <summary>
        /// 
        /// </summary>
        public Tag_info tag_info { get; set; }

    }



    public class Root
    {
        /// <summary>
        /// 
        /// </summary>
        public Tag tag { get; set; }

    }


}
