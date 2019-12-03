namespace SqlSugar
{
    public class DefaultServices
    {
        public static ICacheService ReflectionInoCache = new ReflectionInoCacheService();
        public static ICacheService DataInoCache = null;
        public static ISerializeService Serialize = new SerializeService();
    }
}