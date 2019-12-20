using AutoMapper;

using FluentValidation;

using Microsoft.Extensions.DependencyInjection;

using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Domain.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;
using SSS.Infrastructure.Util.Mapper;

using System;
using System.Collections.Generic;
using System.Linq;
using System.Linq.Expressions;

namespace SSS.Application.Seedwork.Service
{
    [DIService(ServiceLifetime.Scoped, typeof(IQueryService<,,>))]
    public class QueryService<TEntity, TInput, TOutput> : IQueryService<TEntity, TInput, TOutput>
        where TEntity : Entity
        where TInput : InputDtoBase
        where TOutput : OutputDtoBase
    {
        public readonly IErrorHandler Error;
        public readonly IMapper Mapper;

        public readonly IRepository<TEntity> Repository;

        public readonly IValidator<TInput> Validator;

        public QueryService(IMapper mapper, IRepository<TEntity> repository,
            IErrorHandler error, IValidator<TInput> validator)
        {
            Mapper = mapper;
            Repository = repository;
            Error = error;
            Validator = validator;
        }

        public TOutput Get(string id)
        {
            return Mapper.Map<TOutput>(Repository.Get(id));
        }

        public TOutput Get(Expression<Func<TEntity, bool>> predicate)
        {
            return Mapper.Map<TOutput>(Repository.Get(predicate));
        }

        public Pages<List<TOutput>> GetPage(TInput input)
        {
            List<TOutput> list;
            int count = 0;

            if (input.pageindex == 0 && input.pagesize == 0)
            {
                list = Repository.GetAll().MapperToOutPut<TOutput>().ToList();
                count = list.Count;
            }
            else
                list = Repository.GetPage(input.pageindex, input.pagesize > 0 ? input.pagesize : 10, ref count).MapperToOutPut<TOutput>().ToList();

            return new Pages<List<TOutput>>(list, count);
        }

        public Pages<List<TOutput>> GetPage(TInput input, Expression<Func<TEntity, bool>> predicate)
        {
            List<TOutput> list;

            int count = 0;

            if (input.pageindex == 0 && input.pagesize == 0)
            {
                list = Repository.GetAll(predicate).MapperToOutPut<TOutput>().ToList();
                count = list.Count;
            }
            else
                list = Repository.GetPage(input.pageindex, input.pagesize > 0 ? input.pagesize : 10, predicate, ref count).MapperToOutPut<TOutput>().ToList();

            return new Pages<List<TOutput>>(list, count);
        }

        public Pages<List<TOutput>> GetPageBySql(string sql, int pageindex = 0, int pagesize = 10)
        {
            List<TOutput> list;
            int count = 0;

            if (pageindex == 0 && pagesize == 0)
            {
                list = Repository.GetBySql(sql).MapperToOutPut<TOutput>().ToList();
                count = list.Count;
            }
            else
                list = Repository.GetBySql(sql, pageindex, pagesize > 0 ? pagesize : 10, ref count).MapperToOutPut<TOutput>().ToList();

            return new Pages<List<TOutput>>(list, count);
        }

        public Pages<List<TOutput>> GetPageBySql(string sql, Expression<Func<TEntity, bool>> predicate,
            int pageindex = 0, int pagesize = 10)
        {
            List<TOutput> list;

            int count = 0;

            if (pageindex == 0 && pagesize == 0)
            {
                list = Repository.GetBySql(sql, predicate).MapperToOutPut<TOutput>().ToList();
                count = list.Count;
            }
            else
                list = Repository.GetBySql(sql, predicate, pageindex, pagesize > 0 ? pagesize : 10, ref count).MapperToOutPut<TOutput>().ToList();

            return new Pages<List<TOutput>>(list, count);
        }

        #region Permission

        /// <summary>
        /// 获取父级
        /// </summary>
        /// <param name="parentid"></param>
        /// <returns></returns>
        public bool GetParent(string parentid)
        {
            var parent = Repository.Get(parentid);
            if (parent != null)
                return true;
            Error.Execute("父级不存在！");
            return false;
        }
        
        /// <summary>
        /// 父级Id赋值
        /// </summary>
        /// <returns></returns>
        public bool AddParentId(string parentid, string input_parentid, out string out_parentid)
        {
            out_parentid = "";

            //如果现有ParentId不为空
            if (!string.IsNullOrWhiteSpace(parentid))
            {
                //如果Dto的ParentId不为空
                if (!string.IsNullOrWhiteSpace(input_parentid))
                {
                    //检查父级时候存在
                    if (!GetParent(input_parentid))
                        return false;

                    //添加ParentId
                    out_parentid = input_parentid;
                }
                else  //删除现有ParentId
                    out_parentid = null;
            }
            else      //如果现有ParentId为空
            {
                //如果Dto的ParentId不为空
                if (!string.IsNullOrWhiteSpace(input_parentid))
                {
                    if (!GetParent(input_parentid))
                        return false;

                    //添加ParentId
                    out_parentid = input_parentid;
                }
            }

            return true;
        }

        #endregion 
    }
}