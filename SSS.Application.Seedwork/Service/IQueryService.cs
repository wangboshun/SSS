using SSS.Domain.Seedwork.Model;

using System;
using System.Collections.Generic;
using System.Linq.Expressions;

namespace SSS.Application.Seedwork.Service
{
    public interface IQueryService<TEntity, TInput, TOutput>
        where TEntity : Entity
        where TInput : InputDtoBase
        where TOutput : OutputDtoBase
    {
        TOutput Get(string id);

        TOutput Get(Expression<Func<TEntity, bool>> predicate);

        Pages<List<TOutput>> GetPageBySql(string sql, int pageindex = 0, int pagesize = 10);

        Pages<List<TOutput>> GetPageBySql(string sql, Expression<Func<TEntity, bool>> predicate, int pageindex = 0,int pagesize = 10);

        Pages<List<TOutput>> GetPage(TInput input);

        Pages<List<TOutput>> GetPage(TInput input, Expression<Func<TEntity, bool>> predicate);

        bool Delete(string id);

        /// <summary>
        ///     获取父级
        /// </summary>
        /// <param name="parentid"></param>
        /// <returns></returns>
        bool GetParent(string parentid);

        bool AddParentId(string parentid, string input_parentid, out string out_parentid);
    }
}