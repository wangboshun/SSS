using Autofac;

using System;

namespace SSS.Infrastructure.Util.DI
{
    public class IocEx
    {
        public static ILifetimeScope Container { set; get; }
        public static IServiceProvider Instance { get; set; }
    }
}