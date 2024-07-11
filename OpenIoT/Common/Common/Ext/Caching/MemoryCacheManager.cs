using Microsoft.Extensions.Caching.Memory;


namespace Common.Ext.Caching
{
    public class MemoryCacheManager
    {
        private static IMemoryCache _memoryCache = null;

        /// <summary>
        /// 获取缓存实例(单例模式)
        /// </summary>
        static MemoryCacheManager()
        {
            if (_memoryCache == null)
            {
                _memoryCache = new MemoryCache(new MemoryCacheOptions());
            }
        }

        /// <summary>
        /// 设置指定的键关联的值
        /// </summary>
        /// <param name="key">关键字</param>
        /// <param name="value">值</param>
        /// <param name="cacheTime">缓存时间（单位为分钟）</param>
        public void Set(string key, object value, int cacheTime)
        {
            _memoryCache.Set(key, value, TimeSpan.FromMinutes(cacheTime));
        }

        /// <summary>
        /// 获取指定的键关联的值
        /// </summary>
        /// <param name="key">关键字</param>
        /// <returns></returns>
        public object Get(string key)
        {
            return _memoryCache.Get(key);
        }


        /// <summary>
        /// 删除指定的键关联的值
        /// </summary>
        /// <param name="key">关键字</param>
        /// <returns></returns>
        public void Remove(string key)
        {
            _memoryCache.Remove(key);
        }

    }
}
