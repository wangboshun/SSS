using Microsoft.Extensions.DependencyInjection;

using SSS.Infrastructure.Util.Attribute;

using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;

namespace SSS.Api.Seedwork.ServiceCollection
{
    public static class ServiceExtension
    {
        #region 自动注册服务

        /// <summary>
        ///     自动注册服务
        /// </summary>
        /// <param name="services">注册服务的集合（向其中注册）</param>
        /// <param name="ImplementationType">要注册的类型</param>
        public static void AutoRegisterService(this IServiceCollection services, Type ImplementationType)
        {
            //获取类型的 UseDIAttribute 属性 对应的对象
            DIServiceAttribute attr =
                ImplementationType.GetCustomAttribute(typeof(DIServiceAttribute)) as DIServiceAttribute;
            ////获取类实现的所有接口
            //Type[] types = ImplementationType.GetInterfaces();
            List<Type> types = attr.GetTargetTypes();
            var lifetime = attr.Lifetime;
            //遍历类实现的每一个接口
            foreach (var t in types)
            {
                //将类注册为接口的实现-----但是存在一个问题，就是担心 如果一个类实现了IDisposible接口 担心这个类变成了这个接口的实现
                ServiceDescriptor serviceDescriptor = new ServiceDescriptor(t, ImplementationType, lifetime);
                services.Add(serviceDescriptor);
            }
        }

        #endregion

        #region 将程序集中的所有符合条件的类型全部注册到 IServiceCollection中 

        /// <summary>
        ///     将程序集中的所有符合条件的类型全部注册到 IServiceCollection 中
        /// </summary>
        /// <param name="services">IServiceCollection</param>
        /// <param name="AassemblyName">程序集名字</param>
        public static void AutoRegisterServicesFromAssembly(this IServiceCollection services,
            string AassemblyName)
        {
            //根据程序集的名字 获取程序集中所有的类型
            Type[] types = Assembly.Load(AassemblyName).GetTypes();

            //过滤上述程序集 首先按照传进来的条件进行过滤 接着要求Type必须是类，而且不能是抽象类
            IEnumerable<Type> list = types.Where(t => t.IsClass && !t.IsAbstract);
            foreach (var item in list)
            {
                IEnumerable<Attribute> attrs = item.GetCustomAttributes();
                //遍历类的所有特性
                foreach (var attr in attrs)
                    //如果在其特性中发现特性是 UseDIAttribute 特性 就将这个类注册到DI容器中去
                    //并跳出当前的循环 开始对下一个类进行循环
                    if (attr is DIServiceAttribute)
                    {
                        services.AutoRegisterService(item);
                        break;
                    }
            }
        }

        #endregion

        #region Autowired

        /// <summary>
        ///     注入字段和属性
        /// </summary>
        /// <param name="serviceProvider"></param>
        /// <param name="instance"> 待注入的对象实例或类型实例 </param>
        /// <returns></returns>
        public static IServiceProvider Autowired(this IServiceProvider serviceProvider, object instance)
        {
            if (serviceProvider == null || instance == null) return serviceProvider;
            var flags = BindingFlags.Public | BindingFlags.NonPublic;
            var type = instance as Type ?? instance.GetType();
            if (instance is Type)
            {
                instance = null;
                flags |= BindingFlags.Static;
            }
            else
            {
                flags |= BindingFlags.Instance;
            }


            foreach (var field in type.GetFields(flags))
            {
                var attr = field.GetCustomAttributes().OfType<IServiceProviderFactory<IServiceProvider>>()
                    .LastOrDefault();
                var value = attr?.CreateServiceProvider(serviceProvider).GetServiceOrCreateInstance(field.FieldType);
                if (value != null) field.SetValue(instance, value);
            }

            foreach (var property in type.GetProperties(flags))
            {
                var attr = property.GetCustomAttributes().OfType<IServiceProviderFactory<IServiceProvider>>()
                    .FirstOrDefault();
                var value = attr?.CreateServiceProvider(serviceProvider)
                    .GetServiceOrCreateInstance(property.PropertyType);
                if (value != null) property.SetValue(instance, value);
            }

            return serviceProvider;
        }

        /// <summary>
        ///     从服务中获取对象或动态创建对象, 并注入字段和属性
        /// </summary>
        /// <param name="serviceProvider">服务提供程序</param>
        /// <param name="type">待获取或创建的对象类型</param>
        /// <returns></returns>
        public static object GetServiceOrCreateInstance(this IServiceProvider serviceProvider, Type type)
        {
            if (serviceProvider == null) return Activator.CreateInstance(type);

            var obj = ActivatorUtilities.GetServiceOrCreateInstance(serviceProvider, type);
            if (obj != null) serviceProvider.Autowired(obj);
            return obj;
        }

        /// <summary>
        ///     获取用于创建指定对象并注入字段和属性的委托方法
        /// </summary>
        /// <param name="serviceProvider">服务提供程序</param>
        /// <param name="instanceType">待创建的对象类型</param>
        /// <param name="argumentTypes">额外构造参数</param>
        /// <returns></returns>
        public static ObjectFactory CreateFactory(this IServiceProvider serviceProvider, Type instanceType,
            Type[] argumentTypes)
        {
            var factory = ActivatorUtilities.CreateFactory(instanceType, argumentTypes);
            if (factory == null) return factory;
            return (provider, args) =>
            {
                var obj = factory(provider, args);
                if (obj != null) provider?.Autowired(obj);
                return obj;
            };
        }

        /// <summary>
        ///     动态创建对象, 并注入字段和属性
        /// </summary>
        /// <param name="provider">服务提供程序</param>
        /// <param name="instanceType">待创建的对象类型</param>
        /// <param name="parameters">额外构造参数</param>
        /// <returns></returns>
        public static object CreateInstance(this IServiceProvider provider, Type instanceType,
            params object[] parameters)
        {
            var obj = ActivatorUtilities.CreateInstance(provider, instanceType, parameters);
            if (obj != null) provider.Autowired(obj);
            return obj;
        }

        /// <summary>
        ///     动态创建对象, 并注入字段和属性
        /// </summary>
        /// <typeparam name="T">待创建的对象类型</typeparam>
        /// <param name="provider">服务提供程序</param>
        /// <param name="parameters">额外构造参数</param>
        /// <returns></returns>
        public static T CreateInstance<T>(this IServiceProvider provider, params object[] parameters)
        {
            return (T)CreateInstance(provider, typeof(T), parameters);
        }

        /// <summary>
        ///     从服务中获取对象或动态创建对象, 并注入字段和属性
        /// </summary>
        /// <typeparam name="T">待获取或创建的对象类型</typeparam>
        /// <param name="provider">服务提供程序</param>
        /// <returns></returns>
        public static T GetServiceOrCreateInstance<T>(this IServiceProvider provider)
        {
            return (T)GetServiceOrCreateInstance(provider, typeof(T));
        }

        /// <summary>
        ///     通过服务中的 <see cref="IServiceProviderFactory{IServiceProvider}" /> 重新编译提供程序
        /// </summary>
        /// <param name="provider"></param>
        /// <returns></returns>
        public static IServiceProvider RebuildFromFactory(this IServiceProvider provider)
        {
            var factories = provider.GetServices<IServiceProviderFactory<IServiceProvider>>();
            foreach (var factory in factories) provider = factory.CreateServiceProvider(provider);
            return provider;
        }

        #endregion
    }
}