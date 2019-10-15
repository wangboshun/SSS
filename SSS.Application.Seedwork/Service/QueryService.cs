using AutoMapper;
using AutoMapper.QueryableExtensions;
using FluentValidation;
using Microsoft.Extensions.DependencyInjection;
using SSS.Domain.Seedwork.ErrorHandler;
using SSS.Domain.Seedwork.Model;
using SSS.Domain.Seedwork.Repository;
using SSS.Infrastructure.Util.Attribute;
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
        public readonly IMapper _mapper;

        public readonly IErrorHandler _error;

        public readonly IRepository<TEntity> _repository;

        public readonly IValidator<TInput> _validator;

        public QueryService(IMapper mapper, IRepository<TEntity> repository,
            IErrorHandler error, IValidator<TInput> validator)
        {
            _mapper = mapper;
            _repository = repository;
            _error = error;
            _validator = validator;
        }

        public TOutput Get(string id)
        {
            return _mapper.Map<TOutput>(_repository.Get(id));
        }
        public TOutput Get(Expression<Func<TEntity, bool>> predicate)
        {
            return _mapper.Map<TOutput>(_repository.Get(predicate));
        }
        public Pages<List<TOutput>> GetList(TInput input)
        {
            List<TOutput> list;
            int count = 0;

            if (input.pagesize == 0 && input.pagesize == 0)
            {
                list = _repository.GetAll().ProjectTo<TOutput>(_mapper.ConfigurationProvider).ToList();
                count = list.Count;
            }
            else
                list = _repository.GetPage(input.pageindex, input.pagesize, ref count).ProjectTo<TOutput>(_mapper.ConfigurationProvider).ToList();
            return new Pages<List<TOutput>>(list, count);
        }

        public Pages<List<TOutput>> GetList(Expression<Func<TEntity, bool>> predicate)
        {
            List<TOutput> list;
            list = _repository.GetAll(predicate).ProjectTo<TOutput>(_mapper.ConfigurationProvider).ToList();
            int count = list.Count;
            return new Pages<List<TOutput>>(list, count);
        }
    }
}
