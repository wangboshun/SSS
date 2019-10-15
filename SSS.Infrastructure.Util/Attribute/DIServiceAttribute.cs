using System;
using System.Collections.Generic;
using Microsoft.Extensions.DependencyInjection;

namespace SSS.Infrastructure.Util.Attribute
{
    [AttributeUsage(AttributeTargets.All, Inherited = false)]
    public class DIServiceAttribute : System.Attribute
    {
        public ServiceLifetime lifetime;
        public List<Type> TargetTypes = new List<Type>();

        public DIServiceAttribute(ServiceLifetime argLifetime, params Type[] argTargets)
        {
            lifetime = argLifetime;
            foreach (var argTarget in argTargets) TargetTypes.Add(argTarget);
        }

        public ServiceLifetime Lifetime => lifetime;

        public List<Type> GetTargetTypes()
        {
            return TargetTypes;
        }
    }
}