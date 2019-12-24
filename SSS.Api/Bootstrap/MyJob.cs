using Quartz;

using System;
using System.Threading.Tasks;

namespace SSS.Api.Bootstrap
{
    public class MyJob : IJob //创建IJob的实现类，并实现Excute方法。
    {
        public Task Execute(IJobExecutionContext context)
        {
            return Task.Run(() =>
            {
                Console.WriteLine($"---{DateTime.Now.ToString()}---");
            });
        }
    }
}