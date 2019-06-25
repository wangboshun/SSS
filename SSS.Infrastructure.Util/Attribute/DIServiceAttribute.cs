using Microsoft.Extensions.DependencyInjection;
using System;
using System.Collections.Generic;

namespace SSS.Infrastructure.Util.Attribute
{
    [AttributeUsage(AttributeTargets.All, AllowMultiple = false, Inherited = false)]
    public class DIServiceAttribute : System.Attribute
    {
        public List<Type> TargetTypes = new List<Type>();
        public ServiceLifetime lifetime;
        public DIServiceAttribute(ServiceLifetime argLifetime, params Type[] argTargets)
        {
            lifetime = argLifetime;
            foreach (var argTarget in argTargets)
            {
                TargetTypes.Add(argTarget);
            }
        }

        public List<Type> GetTargetTypes()
        {
            return TargetTypes;
        }

        public ServiceLifetime Lifetime
        {
            get => lifetime;
        }
    }
}
