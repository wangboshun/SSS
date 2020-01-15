using AutoMapper;
using AutoMapper.QueryableExtensions;

using SSS.Infrastructure.Util.DI;

using System.Collections;
using System.Linq;

namespace SSS.Infrastructure.Util.Mapper
{
    public static class MapperEx
    {
        private static readonly IMapper mapper = (IMapper)IocEx.Instance.GetService(typeof(IMapper));

        public static IQueryable<TOutput> MapperToOutPut<TOutput>(this IEnumerable data) where TOutput : class
        {
            return data?.AsQueryable().ProjectTo<TOutput>(mapper.ConfigurationProvider);
        }
    }
}