﻿using SSS.Domain.Seedwork.Model;

using System;
using System.Collections.Generic;
using System.Data.Common;
using System.Linq;
using System.Linq.Expressions;

namespace SSS.Domain.Seedwork.Repository
{
    public interface IRepository<TEntity, TInput, TOutput> : IRepository<TEntity>
        where TEntity : Entity
        where TInput : InputDtoBase
        where TOutput : OutputDtoBase
    {
    }

    public interface IRepository<TEntity> : IDisposable
        where TEntity : Entity
    {
        bool Add(TEntity obj, bool save = false);

        bool AddList(List<TEntity> list, bool save = false);

        bool DeleteList(Expression<Func<TEntity, bool>> predicate, bool save = false, bool have_delete = false);

        int Execute(string sql, params DbParameter[] parameter);

        TEntity Get(string id, bool have_delete = false);

        TEntity Get(Expression<Func<TEntity, bool>> predicate, bool have_delete = false);

        IQueryable<TEntity> GetAll();

        IQueryable<TEntity> GetAll(Expression<Func<TEntity, bool>> predicate, bool have_delete = false);

        IQueryable<TEntity> GetBySql(string sql);

        IQueryable<TEntity> GetBySql(string sql, params DbParameter[] parameter);

        IQueryable<TEntity> GetBySql(string sql, int pageindex, int pagesize, ref int count);

        IQueryable<TEntity> GetBySql(string sql, Expression<Func<TEntity, bool>> predicate, bool have_delete = false);

        IQueryable<TEntity> GetBySql(string sql, Expression<Func<TEntity, bool>> predicate, int pageindex, int pagesize, ref int count, bool have_delete = false);

        IQueryable<TEntity> GetPage(int pageindex, int pagesize, ref int count);

        IQueryable<TEntity> GetPage(int pageindex, int pagesize, Expression<Func<TEntity, bool>> predicate, ref int count, bool have_delete = false);

        Pages<IEnumerable<TEntity>> GetPage(string sql, string field, int pageindex, int pagesize);

        bool Have(string id, bool have_delete = false);

        bool Have(Expression<Func<TEntity, bool>> predicate, bool have_delete = false);

        bool Remove(string id, bool save = false);

        bool Remove(Expression<Func<TEntity, bool>> predicate, bool save = false, bool have_delete = false);

        int SaveChanges();

        bool Update(TEntity obj, bool save = false);

        bool UpdateList(List<TEntity> list, bool save = false);
    }
}