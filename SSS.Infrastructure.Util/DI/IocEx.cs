using Autofac;

using System;

namespace SSS.Infrastructure.Util.DI
{
    public class IocEx
    {
        public static IServiceProvider Instance { get; set; }

        public static ILifetimeScope Container { set; get; }
    }
}