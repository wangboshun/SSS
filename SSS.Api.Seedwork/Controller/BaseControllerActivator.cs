using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Controllers;
using Microsoft.AspNetCore.Mvc.Internal;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using SSS.Infrastructure.Util.Attribute;

namespace SSS.Api.Seedwork.Controller
{
    public class BaseControllerActivator : IControllerActivator
    {
        private readonly ITypeActivatorCache _typeActivatorCache;
        private static IDictionary<string, IEnumerable<PropertyInfo>> _publicPropertyCache = new Dictionary<string, IEnumerable<PropertyInfo>>();

        public BaseControllerActivator(ITypeActivatorCache typeActivatorCache)
        {
            _typeActivatorCache = typeActivatorCache;
        }

        public object Create(ControllerContext controllerContext)
        {
            if (controllerContext == null)
            {
                throw new ArgumentNullException(nameof(controllerContext));
            }

            if (controllerContext.ActionDescriptor == null)
            {
                throw new ArgumentException(nameof(ControllerContext.ActionDescriptor));
            }

            var controllerTypeInfo = controllerContext.ActionDescriptor.ControllerTypeInfo;

            if (controllerTypeInfo == null)
            {
                throw new ArgumentException(nameof(controllerContext.ActionDescriptor.ControllerTypeInfo));
            }

            var serviceProvider = controllerContext.HttpContext.RequestServices;
            var instance = _typeActivatorCache.CreateInstance<object>(serviceProvider, controllerTypeInfo.AsType());
            if (instance != null)
            {
                if (!_publicPropertyCache.ContainsKey(controllerTypeInfo.FullName))
                {
                    var ps = controllerTypeInfo.GetProperties(BindingFlags.Public | BindingFlags.Instance).AsEnumerable();
                    ps = ps.Where(c => c.GetCustomAttribute<AutowiredAttribute>() != null);
                    _publicPropertyCache[controllerTypeInfo.FullName] = ps;
                }

                var requireServices = _publicPropertyCache[controllerTypeInfo.FullName];
                foreach (var item in requireServices)
                {
                    var service = serviceProvider.GetService(item.PropertyType);
                    if (service == null)
                    {
                        throw new InvalidOperationException($"Unable to resolve service for type '{item.PropertyType.FullName}' while attempting to activate '{controllerTypeInfo.FullName}'");
                    }
                    item.SetValue(instance, service);
                }
            }
            return instance;
        }

        public void Release(ControllerContext context, object controller)
        {
            if (context == null)
            {
                throw new ArgumentNullException(nameof(context));
            }

            if (controller == null)
            {
                throw new ArgumentNullException(nameof(controller));
            }

            var disposable = controller as IDisposable;
            if (disposable != null)
            {
                disposable.Dispose();
            }
        }
    }
}
