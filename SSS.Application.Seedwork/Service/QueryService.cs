using System;
using System.Collections.Generic;
using System.Linq;
using System.Linq.Expressions;
using AutoMapper;
using AutoMapper.QueryableExtensions;
using FluentValidation;
using Microsoft.Extensions.DependencyInjection;
using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Domain.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;

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

        public Pages<List<TOutput>> GetList(TInput input, int pageindex = 0, int pagesize = 10)
        {
            List<TOutput> list;
            int count = 0;

            if (input.pageindex == 0 && input.pagesize == 0)
            {
                list = Repository.GetAll().ProjectTo<TOutput>(Mapper.ConfigurationProvider).ToList();
                count = list.Count;
            }
            else
            {
                list = Repository.GetPage(input.pageindex, input.pagesize > 0 ? input.pagesize : 10, ref count)
                    .ProjectTo<TOutput>(Mapper.ConfigurationProvider).ToList();
                count = list.Count;
            }

            return new Pages<List<TOutput>>(list, count);
        }

        public Pages<List<TOutput>> GetList(Expression<Func<TEntity, bool>> predicate, int pageindex = 0, int pagesize = 10)
        {
            List<TOutput> list;

            int count = 0;

            if (pageindex == 0 && pagesize == 0)
            {
                list = Repository.GetAll(predicate).ProjectTo<TOutput>(Mapper.ConfigurationProvider).ToList();
                count = list.Count;
            }
            else
            {
                list = Repository.GetPage(pageindex, pagesize > 0 ? pagesize : 10, predicate, ref count).ProjectTo<TOutput>(Mapper.ConfigurationProvider).ToList();
                count = list.Count;
            }

            return new Pages<List<TOutput>>(list, count);
        }
    }
}