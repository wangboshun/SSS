using Microsoft.Extensions.Logging;
using Microsoft.Extensions.Options;

using Newtonsoft.Json;

using StackExchange.Redis;

using System;
using System.Collections.Generic;

namespace SSS.Infrastructure.Seedwork.Cache.Redis
{
    public class RedisCache
    {
        private readonly ConnectionMultiplexer _connect;

        private readonly IDatabase _db;

        private readonly ILogger _logger;

        public RedisCache(IOptions<RedisOptions> options, ILogger<RedisCache> logger)
        {
            _logger = logger;

            try
            {
                if (!string.IsNullOrWhiteSpace(options.Value.host))
                    _connect = ConnectionMultiplexer.Connect(new ConfigurationOptions
                    { EndPoints = { { options.Value.host, options.Value.port } } });

                else
                    _connect = ConnectionMultiplexer.Connect(new ConfigurationOptions
                    { EndPoints = { { "localhost", 6379 } } });

                if (_connect.IsConnected)
                    _db = _connect.GetDatabase();
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
            _db.KeyDelete(key);
        }

        /// <summary>
        ///     统计Key数量
        /// </summary>
        /// <returns></returns>
        public int Count()
        {
            return Convert.ToInt32(_db.Execute("DBSIZE").ToString());
        }

        #region String操作

        public void StringSet(string key, string value)
        {
            _db.StringSet(key, value);
        }

        /// <summary>
        ///     设置缓存时间，分钟为单位
        /// </summary>
        /// <param name="key">key</param>
        /// <param name="value">value</param>
        /// <param name="minute">minute</param>
        public void StringSet(string key, string value, double minute)
        {
            var time = TimeSpan.FromMinutes(minute);
            _db.StringSet(key, value, time);
        }

        public string StringGet(string key)
        {
            return _db.StringGet(key);
        }

        #endregion

        #region List操作

        public void ListSet<T>(string key, List<T> value)
        {
            foreach (var item in value)
                _db.ListRightPush(key, JsonConvert.SerializeObject(item));
        }

        public List<T> ListGet<T>(string key)
        {
            var data = _db.ListRange(key);
            if (data == null || data.Length < 1)
                return null;

            var result = new List<T>();
            foreach (var item in data)
                result.Add(JsonConvert.DeserializeObject<T>(item));

            return result;
        }

        public T ListGetByIndex<T>(string key, int index)
        {
            var data = _db.ListGetByIndex(key, index);
            return JsonConvert.DeserializeObject<T>(data);
        }

        #endregion
    }
}