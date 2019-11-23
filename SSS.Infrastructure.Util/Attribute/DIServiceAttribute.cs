using Microsoft.Extensions.DependencyInjection;

using System;
using System.Collections.Generic;

namespace SSS.Infrastructure.Util.Attribute
{
    [AttributeUsage(AttributeTargets.All, Inherited = false)]
    public class DIServiceAttribute : System.Attribute
    {
        public readonly ServiceLifetime _lifetime;
        public List<Type> TargetTypes = new List<Type>();

        public DIServiceAttribute(ServiceLifetime lifetime, params Type[] argTargets)
        {
            _lifetime = lifetime;
            foreach (var argTarget in argTargets) TargetTypes.Add(argTarget);
        }

        public ServiceLifetime Lifetime => _lifetime;

        public List<Type> GetTargetTypes()
        {
            return TargetTypes;
        }
    }
}