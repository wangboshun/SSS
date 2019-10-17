using System;
using System.Collections.Generic;
using System.Linq.Expressions;
using SSS.Domain.Seedwork.Model;

namespace SSS.Application.Seedwork.Service
{
    public interface IQueryService<TEntity, TInput, TOutput>
        where TEntity : Entity
        where TInput : InputDtoBase
        where TOutput : OutputDtoBase
    {
        TOutput Get(string id);

        TOutput Get(Expression<Func<TEntity, bool>> predicate);

        Pages<List<TOutput>> GetList(TInput input, int pageindex = 0, int pagesize = 10);

        Pages<List<TOutput>> GetList(Expression<Func<TEntity, bool>> predicate, int pageindex = 0, int pagesize = 10);
    }
}