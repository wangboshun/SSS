using Microsoft.Extensions.Caching.Memory;
using System;

namespace SSS.Infrastructure.Seedwork.Cache.MemoryCache
{
    public class MemoryCacheEx
    {
        private readonly IMemoryCache _cache;

        public MemoryCacheEx(IMemoryCache cache)
        {
            _cache = cache;
        }

        /// <summary>
        /// 字符串存储
        /// </summary>
        /// <param name="key"></param>
        /// <param name="value"></param>
        public void StringSet(string key, string value)
        {
            _cache.Set(key, value);
        }

        /// <summary>
        /// 泛型存储
        /// </summary>
        /// <typeparam name="T"></typeparam>
        /// <param name="key"></param>
        /// <param name="value"></param>
        public void Set<T>(string key, T value)
        {
            _cache.Set(key, value);
        }

        /// <summary>
        /// 设置缓存时间，分钟为单位 (滑动过期)
        /// </summary>
        /// <param name="key">key</param>
        /// <param name="value">value</param>
        /// <param name="minute">minute</param>
        public void StringSet(string key, string value, double minute)
        {
            MemoryCacheEntryOptions options = new MemoryCacheEntryOptions { SlidingExpiration = TimeSpan.FromMinutes(minute) };
            _cache.Set(key, value, options);
        }

        /// <summary>
        /// 泛型存储 设置缓存时间，分钟为单位 (滑动过期)
        /// </summary>
        /// <param name="key">key</param>
        /// <param name="value">value</param>
        /// <param name="minute">minute</param>
        public void Set<T>(string key, T value, double minute)
        {
            MemoryCacheEntryOptions options = new MemoryCacheEntryOptions { SlidingExpiration = TimeSpan.FromMinutes(minute) };
            _cache.Set(key, value, options);
        }

        /// <summary>
        /// 设置缓存时间,配置过期时间(绝对过期)
        /// </summary>
        /// <param name="key">key</param>
        /// <param name="value">value</param>
        /// <param name="minute">minute</param>
        public void StringSet(string key, string value, DateTime minute)
        {
            MemoryCacheEntryOptions options = new MemoryCacheEntryOptions { AbsoluteExpiration = minute };
            _cache.Set(key, value, options);
        }

        /// <summary>
        /// 泛型存储 设置缓存时间,配置过期时间(绝对过期)
        /// </summary>
        /// <param name="key">key</param>
        /// <param name="value">value</param>
        /// <param name="minute">minute</param>
        public void Set<T>(string key, T value, DateTime minute)
        {
            MemoryCacheEntryOptions options = new MemoryCacheEntryOptions { AbsoluteExpiration = minute };
            _cache.Set(key, value, options);
        }

        /// <summary>
        /// 字符串取出
        /// </summary>
        /// <param name="key">key</param>
        /// <returns>字符串</returns>
        public string StringGet(string key)
        {
            return _cache.Get<string>(key);
        }

        /// <summary>
        ///  泛型取出
        /// </summary>
        /// <typeparam name="T">泛型</typeparam>
        /// <param name="key">key</param>
        /// <returns>泛型</returns>
        public T Get<T>(string key)
        {
            return _cache.Get<T>(key);
        }

        /// <summary>
        /// 删除key
        /// </summary>
        /// <param name="key"></param>
        public void Remove(string key)
        {
            _cache.Remove(key);
        }
    }
}
