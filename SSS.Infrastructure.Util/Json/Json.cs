using Newtonsoft.Json;
using Newtonsoft.Json.Linq;

using System.Collections;

namespace SSS.Infrastructure.Util.Json
{
    public static class Json
    {
        public static dynamic GetJsonValue(this string json, string key)
        {
            var obj = JObject.Parse(json);
            return obj[key];
        }

        /// <summary>
        /// 多层嵌套 string jsonData =
        /// "{\"name\":\"lily\",\"age\":23,\"addr\":{\"city\":\"guangzhou\",\"province\":\"guangdong\"}}";
        /// JObject jsonObj = JObject.Parse(jsonData);
        /// Response.Write(GetJsonValue(jsonObj.Children(), "province"));
        /// </summary>
        /// <param name="jToken"></param>
        /// <param name="key"></param>
        /// <returns></returns>
        public static dynamic GetJsonValue(this JEnumerable<JToken> jToken, string key)
        {
            IEnumerator enumerator = jToken.GetEnumerator();
            while (enumerator.MoveNext())
            {
                var jc = (JToken)enumerator.Current;
                if (jc is JObject || ((JProperty)jc).Value is JObject) return GetJsonValue(jc.Children(), key);

                if (((JProperty)jc).Name == key) return ((JProperty)jc).Value;
            }

            return null;
        }

        public static T ToEntity<T>(this string json) where T : new()
        {
            return JsonConvert.DeserializeObject<T>(json);
        }

        public static JObject ToJObject(this string str)
        {
            return JObject.Parse(str);
        }

        public static string ToJson(this object obj)
        {
            return JsonConvert.SerializeObject(obj);
        }
    }
}