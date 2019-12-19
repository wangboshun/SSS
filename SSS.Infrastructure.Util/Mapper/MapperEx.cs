using AutoMapper;
using AutoMapper.QueryableExtensions;

using SSS.Infrastructure.Util.Http;

using System.Collections;
using System.Linq;

namespace SSS.Infrastructure.Util.Mapper
{
    public static class MapperEx
    {
        private static IMapper mapper = (IMapper)HttpContextService.Current.RequestServices.GetService(typeof(IMapper));

        public static IQueryable<TOutput> MapperToOutPut<TOutput>(this IEnumerable data) where TOutput : class
        {
            return data?.AsQueryable().ProjectTo<TOutput>(mapper.ConfigurationProvider);
        }
    }
}
