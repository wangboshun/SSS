using Microsoft.AspNetCore.Http;
using System.Text;

namespace SSS.Infrastructure.Seedwork.Cache.Session
{
    public class SessionCache
    {
        private readonly IHttpContextAccessor _httpContextAccessor;
        private ISession _session => _httpContextAccessor.HttpContext.Session;

        public SessionCache(IHttpContextAccessor httpContextAccessor)
        {
            _httpContextAccessor = httpContextAccessor;
        }

        public void StringSet(string key, string value)
        {
            _session.Set(key, Encoding.UTF8.GetBytes(value));
        }

        public string StringGet(string key)
        {
            _session.TryGetValue(key, out var data); 
            return data == null ? null : Encoding.UTF8.GetString(data);
        }

        public void Remove(string key)
        {
            _session.Remove(key);
        }
    }
}
