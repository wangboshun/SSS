using Enyim.Caching.Memcached;

using Microsoft.Extensions.Logging;
using Microsoft.Extensions.Options;

using SSS.Infrastructure.Seedwork.Cache.Redis;

using System;

namespace SSS.Infrastructure.Seedwork.Cache.Memcached
{
    public class MemCached
    {
        private readonly ILogger _logger;

        private readonly IMemcachedClient _memcache;


        public MemCached(IOptions<MemCachedOptions> options, ILogger<MemCached> logger)
        {
            _logger = logger;

            try
            {
                MemcachedCluster cluster;

                if (!string.IsNullOrWhiteSpace(options.Value.host))
                    cluster = new MemcachedCluster($"{options.Value.host}:{options.Value.port}");
                else
                    cluster = new MemcachedCluster("localhost:11212");

                cluster.Start();

                _memcache = cluster.GetClient();
            }
            catch (Exception ex)
            {
                _logger.LogError(new EventId(ex.HResult), ex, ex.Message);
            }
        }

        /// <summary>
        ///     根据Key删除缓存
        /// </summary>
        /// <param name="key"></param>
        public void Remove(string key)
        {
            _memcache.DeleteAsync(key);
        }

        public void StringSet(string key, string value)
        {
            _memcache.SetAsync(key, value);
        }

        /// <summary>
        ///     设置缓存时间，分钟为单位
        /// </summary>
        /// <param name="key">key</param>
        /// <param name="value">value</param>
        /// <param name="minute">minute</param>
        public void StringSet(string key, string value, double minute)
        {
            TimeSpan time = TimeSpan.FromMinutes(minute);
            _memcache.SetAsync(key, value, time);
        }

        public string StringGet(string key)
        {
            return _memcache.GetAsync<string>(key).Result;
        }
    }
}